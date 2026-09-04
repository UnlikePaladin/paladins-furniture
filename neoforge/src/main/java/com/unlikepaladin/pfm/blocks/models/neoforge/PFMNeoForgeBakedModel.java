package com.unlikepaladin.pfm.blocks.models.neoforge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import net.minecraft.util.RandomSource;

public abstract class PFMNeoForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    protected Map<Pair<Pair<BlockState, VariantBase<?>>, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<Pair<BlockState, VariantBase<?>>, Direction> directionPair = new Pair<>(new Pair<>(blockState, variant), face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMNeoForgeBakedModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    public BlockStateModelPart getQuadsWithTexture(List<BakedQuad> quads, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacements) {
        List<BakedQuad> quadList = getQuadsWithTextureInner(quads, toReplace, replacements);
        return new BlockStateModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return quadList;
            }

            @Override
            public boolean useAmbientOcclusion() {
                return true;
            }

            @Override
            public Material.Baked particleMaterial() {
                return new Material.Baked(replacements.getFirst(), false);
            }

            @Override
            public @BakedQuad.MaterialFlags int materialFlags() {
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockStateModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockStateModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleMaterial().equals(((BlockStateModelPart) obj).particleMaterial()) && useAmbientOcclusion() == ((BlockStateModelPart) obj).useAmbientOcclusion();
            }
        };
    }

    public BlockStateModelPart getQuadsWithTexture(BlockStateModelPart modelPart, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacements) {
        return new BlockStateModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return getQuadsWithTextureInner(modelPart.getQuads(side), toReplace, replacements);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return true;
            }

            @Override
            public Material.Baked particleMaterial() {
                return new Material.Baked(replacements.getFirst(), false);
            }

            @Override
            public @BakedQuad.MaterialFlags int materialFlags() {
                return modelPart.materialFlags();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockStateModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockStateModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleMaterial().equals(((BlockStateModelPart) obj).particleMaterial()) && useAmbientOcclusion() == ((BlockStateModelPart) obj).useAmbientOcclusion();
            }
        };
    }

    public List<BlockStateModelPart> getTexturedParts(List<BlockStateModelPart> quads, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacements) {
        List<BlockStateModelPart> modelParts = new ArrayList<>();
        for (BlockStateModelPart quad : quads) {
            modelParts.add(new BlockStateModelPart() {
                @Override
                public List<BakedQuad> getQuads(@Nullable Direction side) {
                    return getQuadsWithTextureInner(quad.getQuads(side), toReplace, replacements);
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return quad.useAmbientOcclusion();
                }

                @Override
                public Material.Baked particleMaterial() {
                    return quad.particleMaterial();
                }

                @Override
                public @BakedQuad.MaterialFlags int materialFlags() {
                    return quad.materialFlags();
                }

                @Override
                public boolean equals(Object obj) {
                    if (!(obj instanceof BlockStateModelPart))
                        return false;

                    for (Direction direction : Direction.values()) {
                        if (this.getQuads(direction) != ((BlockStateModelPart) obj).getQuads(direction))
                            return false;
                    }
                    return particleMaterial().equals(((BlockStateModelPart) obj).particleMaterial()) && useAmbientOcclusion() == ((BlockStateModelPart) obj).useAmbientOcclusion();
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

    public List<BakedQuad> getQuadsWithTextureInner(List<BakedQuad> quads, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacements) {
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
            SpriteData sprite = new SpriteData(quad.materialInfo().sprite());
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

    public List<BlockStateModelPart> getPartsWithTexture(List<BlockStateModelPart> parts, SpriteData spriteData) {
        List<BlockStateModelPart> partsWithTexture = new ArrayList<>();
        for (BlockStateModelPart part : parts) {
            partsWithTexture.add(getPartWithTexture(part, spriteData));
        }
        return partsWithTexture;
    }


    Map<Pair<SpriteData, BlockStateModelPart>, BlockStateModelPart> partToTransformedPart = new ConcurrentHashMap<>();
    public BlockStateModelPart getPartWithTexture(BlockStateModelPart ogPart, SpriteData spriteData) {
        Pair<SpriteData, BlockStateModelPart> pair = new Pair<>(spriteData, ogPart);

        if (partToTransformedPart.containsKey(pair)) {
            return partToTransformedPart.get(pair);
        }

        BlockStateModelPart part = new BlockStateModelPart() {
            @Override
            public List<BakedQuad> getQuads(@Nullable Direction side) {
                return getQuadsWithTexture(ogPart.getQuads(side), spriteData);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return ogPart.useAmbientOcclusion();
            }

            @Override
            public Material.Baked particleMaterial() {
                return ogPart.particleMaterial();
            }

            @Override
            public @BakedQuad.MaterialFlags int materialFlags() {
                return ogPart.materialFlags();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof BlockStateModelPart))
                    return false;

                for (Direction direction : Direction.values()) {
                    if (this.getQuads(direction) != ((BlockStateModelPart) obj).getQuads(direction))
                        return false;
                }
                return particleMaterial().equals(((BlockStateModelPart) obj).particleMaterial()) && useAmbientOcclusion() == ((BlockStateModelPart) obj).useAmbientOcclusion();
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
                if (quad.materialInfo().sprite().contents().name().equals(spriteData.getId())) {
                    // Same sprite, return original quad
                    return quad;
                } else {
                    // Transform the quad
                    TextureAtlasSprite sprite = spriteData.getSprite();

                    long[] newUVs = new long[4];
                    long[] ogUVs = {quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3()};
                    TextureAtlasSprite originalSprite = quad.materialInfo().sprite();
                    for (int i = 0; i < 4; i++) {
                        UVPair unpacked = unpackUV(ogUVs[i]);

                        float frameU = ModelHelper.getFrameFromU(originalSprite, unpacked.u());
                        float frameV = ModelHelper.getFrameFromV(originalSprite, unpacked.v());
                        float newU = sprite.getU(frameU);
                        float newV = sprite.getV(frameV);
                        newUVs[i] = UVPair.pack(newU, newV);
                    }

                    return new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                        newUVs[0], newUVs[1], newUVs[2], newUVs[3], quad.direction(), BakedQuad.MaterialInfo.of(new Material.Baked(quad.materialInfo().sprite(), false), quad.materialInfo().sprite().transparency(), quad.materialInfo().tintIndex(), quad.materialInfo().shade(), quad.materialInfo().lightEmission(), quad.materialInfo().ambientOcclusion()));
                }
            });

            transformedQuads.add(resultQuad);
        });
        return transformedQuads;
    }

    public static UVPair unpackUV(long packed) {
        int ix = (int)(packed >>> 32);
        int iy = (int) packed;
        return new UVPair(Float.intBitsToFloat(ix), Float.intBitsToFloat(iy));
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
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (state != null && getVariant(state) != null)
            return new Material.Baked(getSpriteList(state).get(0), false);
        return super.particleMaterial(level, pos, state);
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
    public Material.Baked particleMaterial() {
        return getTemplateBakedModels().getFirst().particleMaterial();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return getTemplateBakedModels().getFirst().materialFlags();
    }

    public static class SpriteData {
        float minU, maxU, minV, maxV;
        int x, y;
        Identifier id;
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

        public Identifier getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minU, maxU, minV, maxV, x, y, id);
        }
    }

}
