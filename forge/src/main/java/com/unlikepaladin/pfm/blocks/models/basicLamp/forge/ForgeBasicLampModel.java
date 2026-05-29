package com.unlikepaladin.pfm.blocks.models.basicLamp.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeBasicLampModel extends PFMForgeBakedModel {
    public ForgeBasicLampModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
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
        TextureAtlasSprite wood = ModelHelper.getSprite(new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse("minecraft:block/stripped_oak_log")));
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = ModelHelper.getSprite(new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.STRIPPED_LOG)));
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> quads = new ArrayList<>();
            int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;
            WoodVariant variant = extraData.get(VARIANT);
            BitSet set = extraData.get(CONNECTIONS).connections;
            if (set.get(0) && set.get(1)) {
                quads.add(getTemplateBakedModels().get(1));
            } else if (set.get(0)) {
                quads.add(getTemplateBakedModels().get(0));
            } else if (set.get(1))
            {
                quads.add(getTemplateBakedModels().get(3));
                quads.add(getTemplateBakedModels().get(5+onOffset));
                quads.add(getTemplateBakedModels().get(4));
            }
            else {
                quads.add(getTemplateBakedModels().get(4));
                quads.add(getTemplateBakedModels().get(2));
                quads.add(getTemplateBakedModels().get(5+onOffset));
            }
            dest.addAll(getTexturedParts(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant)));
        }
    }

    @Override
    public TextureAtlasSprite particleIcon(@NotNull ModelData data) {
        if (data != null && data.has(VARIANT)) {
            return getVariantStrippedLogSprite(data.get(VARIANT)).get(0);
        }
        return super.particleIcon(data);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<BakedQuad> quads = new ArrayList<>();
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (this.variant != null) {
            variant = (WoodVariant) this.variant;
        }
        quads.addAll(getTemplateBakedModels().get(4).getQuads(face));
        quads.addAll(getTemplateBakedModels().get(2).getQuads(face));
        quads.addAll(getTemplateBakedModels().get(5).getQuads(face));
        return getQuadsWithTextureInner(quads, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
    }

    protected Map<Pair<VariantBase<?>, Direction>, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<VariantBase<?>, Direction> directionPair = new Pair<>(variant, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(face, random);
        cache.put(directionPair, quads);
        return quads;
    }
}
