package com.unlikepaladin.pfm.blocks.models.neoforge;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.neoforge.PFMBakedModelGetQuadsExtension;
import net.minecraft.client.renderer.block.model.BakedOverrides;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import net.minecraft.util.RandomSource;

public abstract class PFMNeoForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return getQuads(blockState, face, random);
    }

    protected Map<Pair<BlockState, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMNeoForgeBakedModel(ModelState settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        return tileData.derive().with(STATE, state).build();
    }

    final Map<Pair<ResourceLocation, SpriteData>, List<BakedQuad>> separatedQuads =  Collections.synchronizedMap(new LinkedHashMap<>(1024, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Pair<ResourceLocation, SpriteData>, List<BakedQuad>> eldest) {
            return size() > 250; // Adjust based on your mod's needs
        }
    });

    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacements) {
        if (quads == null)
            return Collections.emptyList();

        if (replacements == null || toReplace == null) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Replacement list was null, skipping transformation");
            return quads;
        } else if (toReplace.size() != replacements.size()) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Replacement list was not the same size, skipping transformation, expected {} sprites, got {}", toReplace.size(), replacements.size());
            PaladinFurnitureMod.GENERAL_LOGGER.debug(toReplace);
            PaladinFurnitureMod.GENERAL_LOGGER.debug(replacements);
            return quads;
        }
        if (toReplace.equals(replacements))
            return quads;

        for (BakedQuad quad : quads) {
            SpriteData sprite = new SpriteData(quad.getSprite());
            Pair<ResourceLocation, SpriteData> pair = new Pair<>(sprite.getId(), sprite);

            separatedQuads.compute(pair, (key, existingList) -> {
                if (existingList == null) {
                    List<BakedQuad> newList = new ArrayList<>();
                    newList.add(quad);
                    return newList;
                } else if (!existingList.contains(quad)) {
                    List<BakedQuad> newList = new ArrayList<>(existingList);
                    newList.add(quad);
                    return newList;
                }
                return existingList;
            });
        }

        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());

        // Synchronize the snapshot creation, otherwise embeddium explodes
        Map<Pair<ResourceLocation, SpriteData>, List<BakedQuad>> snapshot;
        synchronized (separatedQuads) {
            snapshot = new HashMap<>(separatedQuads);
        }

        for (Map.Entry<Pair<ResourceLocation, SpriteData>, List<BakedQuad>> entry : snapshot.entrySet()) {
            ResourceLocation keyId = entry.getKey().getFirst();
            int index = IntStream.range(0, toReplace.size())
                    .filter(i -> keyId.equals(toReplace.get(i).contents().name()))
                    .findFirst()
                    .orElse(-1);

            if (index != -1 && index < toReplace.size()) {
                SpriteData replacement = new SpriteData(replacements.get(index));
                transformedQuads.addAll(getQuadsWithTexture(entry.getValue().stream().filter(quads::contains).toList(), replacement));
            } else {
                transformedQuads.addAll(entry.getValue().stream().filter(quads::contains).toList());
            }
        }
        return transformedQuads;
    }

    Map<Pair<SpriteData, BakedQuad>, BakedQuad> quadToTransformedQuad = Collections.synchronizedMap(new LinkedHashMap<Pair<SpriteData, BakedQuad>, BakedQuad>(1024, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Pair<SpriteData, BakedQuad>, BakedQuad> eldest) {
            return size() > 10_000; // Adjust based on your mod's needs
        }
    });

    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, SpriteData spriteData) {
        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());

        // I basically have to disable caching if Optifine is present, otherwise it breaks uvs
        quads.forEach(quad -> {
            Pair<SpriteData, BakedQuad> quadKey = new Pair<>(spriteData, quad);

            // Use computeIfAbsent for atomic check-and-put operation
            BakedQuad resultQuad = quadToTransformedQuad.computeIfAbsent(quadKey, key -> {
                if (quad.getSprite().contents().name().equals(spriteData.getId())) {
                    // Same sprite, return original quad
                    return quad;
                } else {
                    // Transform the quad
                    TextureAtlasSprite sprite = spriteData.getSprite();

                    int[] vertexData = new int[quad.getVertices().length];
                    System.arraycopy(quad.getVertices(), 0, vertexData, 0, vertexData.length);
                    float[][] uv = new float[4][2];
                    for (int vertexIndx = 0; vertexIndx < 4; vertexIndx++) {
                        unpackUV(vertexData, uv[vertexIndx], vertexIndx);
                        TextureAtlasSprite originalSprite = quad.getSprite();
                        float frameU = originalSprite.getUOffset(uv[vertexIndx][0]);
                        float frameV = originalSprite.getVOffset(uv[vertexIndx][1]);
                        uv[vertexIndx][0] = sprite.getU(frameU);
                        uv[vertexIndx][1] = sprite.getV(frameV);
                        packUV(uv[vertexIndx], vertexData, vertexIndx);
                    }
                    return new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), sprite, quad.isShade(), quad.getLightEmission());
                }
            });

            transformedQuads.add(resultQuad);
        });
        return transformedQuads;
    }

    public static void unpackUV(int[] vertexData, float[] uv, int vertexIndx)
    {
        int offset = vertexIndx * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        uv[0] = Float.intBitsToFloat(vertexData[offset]);
        uv[1] = Float.intBitsToFloat(vertexData[offset + 1]);
    }

    public static void packUV(float[] uv, int[] vertexData, int vertexIndx)
    {
        int offset = vertexIndx * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        vertexData[offset] = Float.floatToRawIntBits(uv[0]);
        vertexData[offset+1] = Float.floatToRawIntBits(uv[1]);
    }


    private static final Map<Pair<VertexFormatElement.Type, Integer>, Integer> ELEMENT_INTEGER_MAP = new ConcurrentHashMap<>();
    public static int findVertexElement(VertexFormatElement.Type type, int index) {
        Pair<VertexFormatElement.Type, Integer> pairToFind = new Pair<>(type, index);
        if (ELEMENT_INTEGER_MAP.containsKey(pairToFind))
            return ELEMENT_INTEGER_MAP.get(pairToFind);

        int id = 0;
        for (VertexFormatElement element1 : DefaultVertexFormat.BLOCK.getElements())
        {
            if (element1.type() == type && element1.index() == index)
                break;
            id++;
        }
        ELEMENT_INTEGER_MAP.put(pairToFind, id);
        return id;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        if (data.has(STATE) && data.get(STATE) != null)
            return getSpriteList(data.get(STATE)).get(0);
        return super.getParticleIcon(data);
    }

    @Override
    public void setBlockStateProperty(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void setVariant(VariantBase<?> variant) {
        this.variant = variant;
    }

    @Override
    public BlockState getBlockStateProperty() {
        return blockState;
    }

    @Override
    public VariantBase<?> getVariant() {
        return variant;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return getTemplateBakedModels().get(0).getParticleIcon();
    }

    public static class SpriteData {
        float minU, maxU, minV, maxV;
        int x, y;
        ResourceLocation id;
        TextureAtlasSprite sprite;

        public SpriteData(TextureAtlasSprite sprite) {
            this.sprite = sprite;
            this.minU = sprite.getU0();
            this.maxU = sprite.getU1();
            this.minV = sprite.getV0();
            this.maxV = sprite.getV1();
            this.x = sprite.getX();
            this.y = sprite.getY();
            this.id = sprite.contents().name();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof SpriteData && ((SpriteData) obj).id == id && ((SpriteData) obj).minV == minV && ((SpriteData) obj).maxV == maxV && ((SpriteData) obj).minU == minU && ((SpriteData) obj).maxU == maxU && ((SpriteData) obj).x == x && ((SpriteData) obj).y == y;
        }

        public TextureAtlasSprite getSprite() {
            return sprite;
        }

        public ResourceLocation getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minU, maxU, minV, maxV, x, y, id);
        }
    }

}
