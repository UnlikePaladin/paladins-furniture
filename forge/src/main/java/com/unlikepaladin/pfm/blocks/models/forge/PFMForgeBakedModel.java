package com.unlikepaladin.pfm.blocks.models.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.forge.PFMBakedModelGetQuadsExtension;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import net.minecraft.util.RandomSource;

public abstract class PFMForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension {
    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        return getQuads(state, face, random);
    }

    protected Map<Pair<ItemStack, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        Pair<ItemStack, Direction> directionPair = new Pair<>(stack, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(stack, state, face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMForgeBakedModel(ModelState settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();

    public static boolean forceReload;

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

            if (index != -1  && index < toReplace.size()) {
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
                    return new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), sprite, quad.isShade());
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


    private static final Map<Pair<VertexFormatElement.Usage, Integer>, Integer> ELEMENT_INTEGER_MAP = new ConcurrentHashMap<>();
    public static int findVertexElement(VertexFormatElement.Usage type, int index) {
        Pair<VertexFormatElement.Usage, Integer> pairToFind = new Pair<>(type, index);
        if (ELEMENT_INTEGER_MAP.containsKey(pairToFind))
            return ELEMENT_INTEGER_MAP.get(pairToFind);

        int id = 0;
        for (VertexFormatElement element1 : DefaultVertexFormat.BLOCK.getElements())
        {
            if (element1.getUsage() == type && element1.getIndex() == index)
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
    public TextureAtlasSprite getParticleIcon() {
        return getTemplateBakedModels().get(0).getParticleIcon();
    }

    public ModelData.Builder getPropertiesForItem(ItemStack stack, ModelData data)  {
        BlockState state = stack.getItem() instanceof BlockItem ? ((BlockItem) stack.getItem()).getBlock().defaultBlockState() : null;
        if (state != null) {
            return data.derive().with(STATE,  state);
        }
        return data.derive();
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        BlockState state = itemStack.getItem() instanceof BlockItem ? ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState() : null;
        Map<Direction, List<BakedQuad>> map = new HashMap<>();
        RandomSource random = RandomSource.createNewThreadLocalInstance();
        for (Direction direction : Direction.values()) {
            map.put(direction, getQuadsCached(itemStack, state, direction, random));
        }
        map.put(null, getQuadsCached(itemStack, state, null, random));
        TextureAtlasSprite particle;
        if (itemStack.getItem() instanceof BlockItem) {
            ModelData data = ModelData.builder().build();
            data = getPropertiesForItem(itemStack, data).build();
            particle = getParticleIcon(data);
        } else {
            particle = getParticleIcon();
        }
        PFMCachingBakedModel cachingBakedModel = new PFMCachingBakedModel(map, particle);
        return List.of(cachingBakedModel);
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

    public static final class PFMCachingBakedModel implements BakedModel {
        private final Map<Direction, List<BakedQuad>> transformedQuads;
        private final TextureAtlasSprite particleSprite;

        public PFMCachingBakedModel(Map<Direction, List<BakedQuad>> transformedQuads, TextureAtlasSprite particleSprite) {
            this.transformedQuads = transformedQuads;
            this.particleSprite = particleSprite;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random) {
            return transformedQuads.get(face);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean usesBlockLight() {
            return true;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return particleSprite;
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (PFMCachingBakedModel) obj;
            return Objects.equals(this.transformedQuads, that.transformedQuads) &&
                    Objects.equals(this.particleSprite, that.particleSprite);
        }

        @Override
        public int hashCode() {
            return Objects.hash(transformedQuads, particleSprite);
        }

        @Override
        public String toString() {
            return "PFMCachingBakedModel[" +
                    "transformedQuads=" + transformedQuads + ", " +
                    "particleSprite=" + particleSprite + ']';
        }

    }
}
