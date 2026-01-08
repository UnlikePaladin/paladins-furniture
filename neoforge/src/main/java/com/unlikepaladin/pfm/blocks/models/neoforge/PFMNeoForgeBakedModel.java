package com.unlikepaladin.pfm.blocks.models.neoforge;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import net.minecraft.util.math.random.Random;

public abstract class PFMNeoForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    protected Map<Pair<Pair<BlockState, VariantBase<?>>, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<Pair<BlockState, VariantBase<?>>, Direction> directionPair = new Pair<>(new Pair<>(blockState, variant), face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMNeoForgeBakedModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
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

    final Map<Pair<Identifier, SpriteData>, List<BakedQuad>> separatedQuads =  Collections.synchronizedMap(new LinkedHashMap<>(1024, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Pair<Identifier, SpriteData>, List<BakedQuad>> eldest) {
            return size() > 250; // Adjust based on your mod's needs
        }
    });

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
        Map<Pair<Identifier, SpriteData>, List<BakedQuad>> snapshot;
        synchronized (separatedQuads) {
            snapshot = new HashMap<>(separatedQuads);
        }

        for (Map.Entry<Pair<Identifier, SpriteData>, List<BakedQuad>> entry : snapshot.entrySet()) {
            Identifier keyId = entry.getKey().getFirst();
            int index = IntStream.range(0, toReplace.size())
                    .filter(i -> keyId.equals(toReplace.get(i).getContents().getId()))
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
                if (quad.sprite().getContents().getId().equals(spriteData.getId())) {
                    // Same sprite, return original quad
                    return quad;
                } else {
                    // Transform the quad
                    Sprite sprite = spriteData.getSprite();

                    long[] newUVs = new long[4];
                long[] ogUVs = {quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3()};
                    Sprite originalSprite = quad.sprite();
                    for (int i = 0; i < 4; i++) {
                        Vector2f unpacked = unpackUV(ogUVs[i]);

                        float frameU = ModelHelper.getFrameFromU(originalSprite, unpacked.x());
                        float frameV = ModelHelper.getFrameFromV(originalSprite, unpacked.y());
                        float newU = sprite.getFrameU(frameU);
                        float newV = sprite.getFrameV(frameV);
                        newUVs[i] = Vector2f.toLong(newU, newV);
                }

                    return new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                        newUVs[0], newUVs[1], newUVs[2], newUVs[3], quad.tintIndex(), quad.face(), quad.sprite(), quad.shade(), quad.lightEmission());
                }
            });

            transformedQuads.add(resultQuad);
        });
        return transformedQuads;
    }

    public static Vector2f unpackUV(long packed) {
        int ix = (int)(packed >>> 32);
        int iy = (int) packed;
        return new Vector2f(Float.intBitsToFloat(ix), Float.intBitsToFloat(iy));
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
    public Sprite particleIcon(BlockRenderView level, BlockPos pos, BlockState state) {
        if (state != null && getVariant(state) != null)
            return getSpriteList(state).get(0);
        return super.particleIcon(level, pos, state);
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
