package com.unlikepaladin.pfm.blocks.models.basicLamp.neoforge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.neoforge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeBasicLampModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicLampModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<WoodVariant> VARIANT = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof BasicLampBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            WoodVariant variant = WoodVariantRegistry.OAK;
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof LampBlockEntity) {
                variant = ((LampBlockEntity) entity).getVariant();
            }
            BitSet set = new BitSet();
            set.set(0, world.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock);
            set.set(1, world.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).with(VARIANT, variant).build();
            return data;
        }
        return tileData;
    }

    static List<TextureAtlasSprite> oakSprite = new ArrayList<>();
    static List<TextureAtlasSprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        TextureAtlasSprite wood = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse("minecraft:block/stripped_oak_log")).sprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BakedQuad> quads = new ArrayList<>();
            int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.get(VARIANT);
            TextureAtlasSprite sprite = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
            BitSet set = extraData.get(CONNECTIONS).connections;
            if (set.get(0) && set.get(1)) {
                quads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData, renderType));
            } else if (set.get(0)) {
                quads.addAll(getTemplateBakedModels().get(0).getQuads(state, side, rand, extraData, renderType));
            } else if (set.get(1))
            {
                quads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getTemplateBakedModels().get(5+onOffset).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderType));
            }
            else {
                quads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getTemplateBakedModels().get(5+onOffset).getQuads(state, side, rand, extraData, renderType));
            }
            return getQuadsWithTexture(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        }
        return Collections.emptyList();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        if (data != null && data.has(VARIANT)) {
            return getVariantStrippedLogSprite(data.get(VARIANT)).get(0);
        }
        return super.getParticleIcon(data);
    }

    @Override
    public ItemTransforms getTransforms() {
        return getTemplateBakedModels().get(2).getTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<BakedQuad> quads = new ArrayList<>();
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (this.variant != null) {
            variant = (WoodVariant) this.variant;
        }
        quads.addAll(getTemplateBakedModels().get(4).getQuads(null, face, random));
        quads.addAll(getTemplateBakedModels().get(2).getQuads(null, face, random));
        quads.addAll(getTemplateBakedModels().get(5).getQuads(null, face, random));
        return getQuadsWithTexture(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
    }

    protected Map<Pair<VariantBase<?>, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<VariantBase<?>, Direction> directionPair = new Pair<>(variant, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }
}
