package com.unlikepaladin.pfm.blocks.models.classicStool.forge;

import com.unlikepaladin.pfm.blocks.ClassicStoolBlock;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForgeClassicStoolModel extends PFMForgeBakedModel {
    public ForgeClassicStoolModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    public static ModelProperty<Boolean> TUCKED = new ModelProperty<>();
    @Override
    public void appendProperties(ModelDataMap.Builder builder) {
        super.appendProperties(builder);
        builder.withProperty(TUCKED);
    }


    @Override
    public @NotNull IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof ClassicStoolBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);
            data.setData(TUCKED, state.get(ClassicStoolBlock.TUCKED));
            return data;
        }
        return tileData;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData != null && extraData.getData(TUCKED) != null) {
            int tucked = Boolean.TRUE.equals(extraData.getData(TUCKED)) ? 1 : 0;
            List<Sprite> spriteList = getSpriteList(state);
            List<BakedQuad> quads = getTemplateBakedModels().get(tucked).getQuads(state, side, rand, extraData);
            return getQuadsWithTexture(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(state);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(state, face, random);
        return getQuadsWithTexture(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
