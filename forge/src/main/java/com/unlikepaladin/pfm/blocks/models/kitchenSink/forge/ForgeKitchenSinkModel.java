package com.unlikepaladin.pfm.blocks.models.kitchenSink.forge;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeKitchenSinkModel extends PFMForgeBakedModel {
    public ForgeKitchenSinkModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData.getData(LEVEL) != null) {
            int level = extraData.getData(LEVEL);
            List<BakedQuad> originalQuads = getTemplateBakedModels().get(level).getQuads(state, side, rand, extraData);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    public static ModelProperty<Integer> LEVEL = new ModelProperty<>();
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(LEVEL);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof KitchenSinkBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);
            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);
            data.setData(LEVEL, state.getValue(KitchenSinkBlock.LEVEL_4));
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(0).getQuads(state, face, random);
        return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
