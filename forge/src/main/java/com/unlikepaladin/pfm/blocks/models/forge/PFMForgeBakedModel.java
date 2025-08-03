package com.unlikepaladin.pfm.blocks.models.forge;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import net.minecraft.util.math.random.Random;

public abstract class PFMForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    Map<Pair<BlockState, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMForgeBakedModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();

    public static boolean forceReload;

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        return tileData.derive().with(STATE, state).build();
    }

    public BlockModelPart getQuadsWithTexture(List<BakedQuad> quads, List<Sprite> toReplace, List<Sprite> replacements) {
        List<BakedQuad> quadList = getQuadsWithTextureInner(quads, toReplace, replacements);
        return new BlockModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return quadList;
            }

            @Override
            public boolean useAmbientOcclusion() {
                return true;
            }

            @Override
            public Sprite particleSprite() {
                return replacements.getFirst();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleSprite().equals(((BlockModelPart) obj).particleSprite()) && useAmbientOcclusion() == ((BlockModelPart) obj).useAmbientOcclusion();
            }
        };
    }

    public BlockModelPart getQuadsWithTexture(BlockModelPart modelPart, List<Sprite> toReplace, List<Sprite> replacements) {
        return new BlockModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return getQuadsWithTextureInner(modelPart.getQuads(side), toReplace, replacements);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return true;
            }

            @Override
            public Sprite particleSprite() {
                return replacements.getFirst();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleSprite().equals(((BlockModelPart) obj).particleSprite()) && useAmbientOcclusion() == ((BlockModelPart) obj).useAmbientOcclusion();
            }
        };
    }

    public List<BlockModelPart> getTexturedParts(List<BlockModelPart> quads, List<Sprite> toReplace, List<Sprite> replacements) {
        List<BlockModelPart> modelParts = new ArrayList<>();
        for (BlockModelPart quad : quads) {
            modelParts.add(new BlockModelPart() {
                @Override
                public List<BakedQuad> getQuads(@Nullable Direction side) {
                    return getQuadsWithTextureInner(quad.getQuads(side), toReplace, replacements);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return quad.useAmbientOcclusion();
                }

                @Override
                public Sprite particleSprite() {
                    return quad.particleSprite();
                }


                @Override
                public boolean equals(Object obj) {
                    if (!(obj instanceof BlockModelPart))
                        return false;

                    for (Direction direction : Direction.values()) {
                        if (this.getQuads(direction) != ((BlockModelPart) obj).getQuads(direction))
                            return false;
                    }
                    return particleSprite().equals(((BlockModelPart) obj).particleSprite()) && useAmbientOcclusion() == ((BlockModelPart) obj).useAmbientOcclusion();
                }
            });
        }
        return modelParts;
    }

    Map<Pair<Identifier, SpriteData>, List<BakedQuad>> separatedQuads = new ConcurrentHashMap<>();
    public List<BakedQuad> getQuadsWithTextureInner(List<BakedQuad> quads, List<Sprite> toReplace, List<Sprite> replacements) {
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
            SpriteData sprite = new SpriteData(quad.sprite());
            Pair<Identifier, SpriteData> pair = new Pair<>(sprite.getId(), sprite);
            if (separatedQuads.containsKey(pair)) {
                if (!separatedQuads.get(pair).contains(quad)) {
                    List<BakedQuad> newQuadList = new ArrayList<>(separatedQuads.getOrDefault(pair, new ArrayList<>()));
                    newQuadList.add(quad);
                    separatedQuads.put(pair, newQuadList);
                }
                continue;
            } else if (!separatedQuads.isEmpty()) {
                AtomicReference<Pair<Identifier, SpriteData>> del = new AtomicReference<>(null);
                separatedQuads.keySet().forEach(identifierSpriteDataPair ->  {
                    if (identifierSpriteDataPair != null && sprite.getId().equals(identifierSpriteDataPair.getFirst())){
                        del.set(identifierSpriteDataPair);
                    }
                });
                if (del.get() != null)
                    separatedQuads.remove(del.get());
            }
            List<BakedQuad> list = new ArrayList<>();
            list.add(quad);
            separatedQuads.put(pair, list);
        }

        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());
        for (Map.Entry<Pair<Identifier, SpriteData>, List<BakedQuad>> entry : separatedQuads.entrySet()) {
            Identifier keyId = entry.getKey().getFirst();
            int index = IntStream.range(0, toReplace.size())
                    .filter(i -> keyId.equals(toReplace.get(i).getContents().getId()))
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                SpriteData replacement = new SpriteData(replacements.get(index));
                transformedQuads.addAll(getQuadsWithTexture(entry.getValue().stream().filter(quads::contains).toList(), replacement));
            } else {
                transformedQuads.addAll(entry.getValue().stream().filter(quads::contains).toList());
            }
        }
        return transformedQuads;
    }

    public List<BlockModelPart> getPartsWithTexture(List<BlockModelPart> parts, SpriteData spriteData) {
        List<BlockModelPart> partsWithTexture = new ArrayList<>();
        for (BlockModelPart part : parts) {
            partsWithTexture.add(getPartWithTexture(part, spriteData));
        }
        return partsWithTexture;
    }


    Map<Pair<SpriteData, BlockModelPart>, BlockModelPart> partToTransformedPart = new ConcurrentHashMap<>();
    public BlockModelPart getPartWithTexture(BlockModelPart ogPart, SpriteData spriteData) {
        Pair<SpriteData, BlockModelPart> pair = new Pair<>(spriteData, ogPart);

        if (partToTransformedPart.containsKey(pair)) {
            return partToTransformedPart.get(pair);
        }

        BlockModelPart part = new BlockModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return getQuadsWithTexture(ogPart.getQuads(side), spriteData);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return ogPart.useAmbientOcclusion();
            }

            @Override
            public Sprite particleSprite() {
                return ogPart.particleSprite();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleSprite().equals(((BlockModelPart) obj).particleSprite()) && useAmbientOcclusion() == ((BlockModelPart) obj).useAmbientOcclusion();
            }
        };
        partToTransformedPart.put(pair, part);
        return part;
    }

    Map<Pair<SpriteData, BakedQuad>, BakedQuad> quadToTransformedQuad = new ConcurrentHashMap<>();
    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, SpriteData spriteData) {
        List<BakedQuad> transformedQuads = new ArrayList<>(quads.size());

        // I basically have to disable caching if Optifine is present, otherwise it breaks uvs
        quads.forEach(quad -> {
            Pair<SpriteData, BakedQuad> quadKey = new Pair<>(spriteData, quad);

            if (quad.sprite().getContents().getId() == spriteData.getId() && !quadToTransformedQuad.containsKey(quadKey)) {
                quadToTransformedQuad.put(quadKey, quad);
                transformedQuads.add(quad);
            }
            else if (quadToTransformedQuad.containsKey(quadKey)) {
                transformedQuads.add(quadToTransformedQuad.get(quadKey));
            }
            else {
                Sprite sprite = spriteData.getSprite();

                int[] vertexData = new int[quad.vertexData().length];
                System.arraycopy(quad.vertexData(), 0, vertexData, 0, vertexData.length);
                float[][] uv = new float[4][2];
                for (int vertexIndx = 0; vertexIndx < 4; vertexIndx++) {
                    unpackUV(vertexData, uv[vertexIndx], vertexIndx);
                    Sprite originalSprite = quad.sprite();
                    float frameU = originalSprite.getFrameFromU(uv[vertexIndx][0]);
                    float frameV = originalSprite.getFrameFromV(uv[vertexIndx][1]);
                    uv[vertexIndx][0] = sprite.getFrameU(frameU);
                    uv[vertexIndx][1] = sprite.getFrameV(frameV);
                    packUV(uv[vertexIndx], vertexData, vertexIndx);
                }
                BakedQuad transformedQuad = new BakedQuad(vertexData, quad.tintIndex(), quad.face(), quad.sprite(), quad.shade(), quad.lightEmission());
                quadToTransformedQuad.put(quadKey, transformedQuad);
                transformedQuads.add(transformedQuad);
            }
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
        for (VertexFormatElement element1 : VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getElements())
        {
            if (element1.type() == type && element1.index() == index)
                break;
            id++;
        }
        ELEMENT_INTEGER_MAP.put(pairToFind, id);
        return id;
    }

    @Override
    public Sprite particleIcon(ModelData data) {
        if (data != null && data.has(STATE) && data.get(STATE) != null && getVariant(data.get(STATE)) != null)
            return getSpriteList(data.get(STATE)).get(0);
        return super.particleIcon(data);
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
    public Sprite particleSprite() {
        return getTemplateBakedModels().get(0).particleSprite();
    }

    public static class SpriteData {
        float minU, maxU, minV, maxV;
        int x, y;
        Identifier id;
        Sprite sprite;

        public SpriteData(Sprite sprite) {
            this.sprite = sprite;
            this.minU = sprite.getMinU();
            this.maxU = sprite.getMaxU();
            this.minV = sprite.getMinV();
            this.maxV = sprite.getMaxV();
            this.x = sprite.getX();
            this.y = sprite.getY();
            this.id = sprite.getContents().getId();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof SpriteData && ((SpriteData) obj).id == id && ((SpriteData) obj).minV == minV && ((SpriteData) obj).maxV == maxV && ((SpriteData) obj).minU == minU && ((SpriteData) obj).maxU == maxU && ((SpriteData) obj).x == x && ((SpriteData) obj).y == y;
        }

        public Sprite getSprite() {
            return sprite;
        }

        public Identifier getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minU, maxU, minV, maxV, x, y, id);
        }
    }
}
