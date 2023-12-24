package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.forge;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerSmallBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForgeKitchenWallDrawerSmallModel extends PFMForgeBakedModel {
    public ForgeKitchenWallDrawerSmallModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData.getData(OPEN) != null) {
            int openOffset = extraData.getData(OPEN) ? 1 : 0;
            List<BakedQuad> originalQuads = getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData);
            List<Sprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    public static ModelProperty<Boolean> OPEN = new ModelProperty<>();
    @Override
    public void appendProperties(ModelDataMap.Builder builder) {
        super.appendProperties(builder);
        builder.withProperty(OPEN);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof KitchenWallDrawerSmallBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);
            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);
            data.setData(OPEN, state.get(KitchenWallDrawerSmallBlock.OPEN));
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(stack);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(0).getQuads(state, face, random);
        return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
