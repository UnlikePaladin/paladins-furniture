package com.unlikepaladin.pfm.blocks.models.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.forge.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.mixin.PFMSpriteAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public abstract class PFMForgeBakedModel extends AbstractBakedModel implements PFMBakedModelGetQuadsExtension {
    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        return getQuads(state, face, random);
    }

    Map<Pair<ItemStack, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        Pair<ItemStack, Direction> directionPair = new Pair<>(stack, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(stack, state, face, random);
        cache.put(directionPair, quads);
        return quads;
    }

    public PFMForgeBakedModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();

    public static boolean forceReload;

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        tileData.setData(STATE, state);
        return tileData;
    }

    Map<Pair<Identifier, SpriteData>, List<BakedQuad>> separatedQuads =  Collections.synchronizedMap(new LinkedHashMap<Pair<Identifier, SpriteData>, List<BakedQuad>>(1024, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Pair<Identifier, SpriteData>, List<BakedQuad>> eldest) {
            return size() > 250; // Adjust based on your mod's needs
        }
    });

    public List<BakedQuad> getQuadsWithTexture(List<BakedQuad> quads, List<Sprite> toReplace, List<Sprite> replacements) {
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
            SpriteData sprite = new SpriteData(quad.func_187508_a());
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
        Map<Pair<Identifier, SpriteData>, List<BakedQuad>> snapshot = new HashMap<>(separatedQuads);
        for (Map.Entry<Pair<Identifier, SpriteData>, List<BakedQuad>> entry : snapshot.entrySet()) {
            Identifier keyId = entry.getKey().getFirst();
            int index = IntStream.range(0, toReplace.size())
                    .filter(i -> keyId.equals(toReplace.get(i).getId()))
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                SpriteData replacement = new SpriteData(replacements.get(index));
                transformedQuads.addAll(getQuadsWithTexture(entry.getValue().stream().filter(quads::contains).collect(Collectors.toList()), replacement));
            } else {
                transformedQuads.addAll(entry.getValue().stream().filter(quads::contains).collect(Collectors.toList()));
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

        // UV Element index
        int uvVertexIndx = findVertexElement(VertexFormatElement.Type.UV, 0);

        // I basically have to disable caching if Optifine is present, otherwise it breaks uvs
        quads.forEach(quad -> {
            Pair<SpriteData, BakedQuad> quadKey = new Pair<>(spriteData, quad);

            BakedQuad cachedQuad = quadToTransformedQuad.get(quadKey);
            if (quad.func_187508_a().getId().equals(spriteData.getId()) && cachedQuad == null) {
                quadToTransformedQuad.put(quadKey, quad);
                transformedQuads.add(quad);
            } else if (cachedQuad != null) {
                transformedQuads.add(cachedQuad);
            }
            else {
                Sprite sprite = spriteData.getSprite();

                int[] vertexData = new int[quad.getVertexData().length];
                System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
                float[][] uv = new float[4][2];
                for (int vertexIndx = 0; vertexIndx < 4; vertexIndx++) {
                    LightUtil.unpack(vertexData, uv[vertexIndx], VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, vertexIndx, uvVertexIndx);
                    Sprite originalSprite = quad.func_187508_a();
                    float frameU = ModelHelper.method_35804(originalSprite, uv[vertexIndx][0]);
                    float frameV = ModelHelper.method_35805(originalSprite, uv[vertexIndx][1]);
                    uv[vertexIndx][0] = sprite.getFrameU(frameU);
                    uv[vertexIndx][1] = sprite.getFrameV(frameV);
                    LightUtil.pack(uv[vertexIndx], vertexData, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, vertexIndx, uvVertexIndx);
                }
                BakedQuad transformedQuad = new BakedQuad(vertexData, quad.getColorIndex(), quad.getFace(), quad.func_187508_a(), quad.hasShade());
                quadToTransformedQuad.put(quadKey, transformedQuad);
                transformedQuads.add(transformedQuad);
            }
        });
        return transformedQuads;
    }

    private static final Map<Pair<VertexFormatElement.Type, Integer>, Integer> ELEMENT_INTEGER_MAP = new ConcurrentHashMap<>();
    public static int findVertexElement(VertexFormatElement.Type type, int index) {
        Pair<VertexFormatElement.Type, Integer> pairToFind = new Pair<>(type, index);
        if (ELEMENT_INTEGER_MAP.containsKey(pairToFind))
            return ELEMENT_INTEGER_MAP.get(pairToFind);

        int id = 0;
        for (VertexFormatElement element1 : VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getElements())
        {
            if (element1.getType() == type && element1.getTextureIndex() == index)
                break;
            id++;
        }
        ELEMENT_INTEGER_MAP.put(pairToFind, id);
        return id;
    }

    @Override
    public Sprite getParticleTexture(@NotNull IModelData data) {
        if (data.hasProperty(STATE) && data.getData(STATE) != null)
            return getSpriteList(data.getData(STATE)).get(0);
        return super.getParticleTexture(data);
    }

    @Override
    public Sprite getSprite() {
        return getTemplateBakedModels().get(0).getSprite();
    }

    public void appendProperties(ModelDataMap.Builder builder) {
        builder.withProperty(STATE);
    }

    @Override
    public List<Pair<BakedModel, RenderLayer>> getLayerModels(ItemStack itemStack, boolean fabulous) {
        BlockState state = itemStack.getItem() instanceof BlockItem ? ((BlockItem) itemStack.getItem()).getBlock().getDefaultState() : null;
        Map<Direction, List<BakedQuad>> map = new HashMap<>();
        Random random = new Random(1);
        for (Direction direction : Direction.values()) {
            map.put(direction, getQuadsCached(itemStack, state, direction, random));
        }
        map.put(null, getQuadsCached(itemStack, state, null, random));
        PFMCachingBakedModel cachingBakedModel = new PFMCachingBakedModel(map, getSprite());
        return Collections.singletonList(Pair.of(cachingBakedModel, RenderLayers.getItemLayer(itemStack, fabulous)));
    }

    @Override
    public boolean isLayered() {
        return true;
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
            this.x = ((PFMSpriteAccessor)sprite).pfm$getX();
            this.y = ((PFMSpriteAccessor)sprite).pfm$getY();
            this.id = sprite.getId();
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

    public static final class PFMCachingBakedModel implements BakedModel {
        private final Map<Direction, List<BakedQuad>> transformedQuads;
        private final Sprite particleSprite;

        public PFMCachingBakedModel(Map<Direction, List<BakedQuad>> transformedQuads, Sprite particleSprite) {
            this.transformedQuads = transformedQuads;
            this.particleSprite = particleSprite;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
            return transformedQuads.get(face);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean hasDepth() {
            return true;
        }

        @Override
        public boolean isSideLit() {
            return true;
        }

        @Override
        public boolean isBuiltin() {
            return false;
        }

        @Override
        public Sprite getSprite() {
            return particleSprite;
        }

        @Override
        public ModelOverrideList getOverrides() {
            return ModelOverrideList.EMPTY;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            PFMCachingBakedModel that = (PFMCachingBakedModel) obj;
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
