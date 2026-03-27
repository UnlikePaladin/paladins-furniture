package com.unlikepaladin.pfm.blocks.models.simpleStool.forge;

import com.unlikepaladin.pfm.blocks.SimpleStoolBlock;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForgeSimpleStoolModel extends PFMForgeBakedModel {
    public ForgeSimpleStoolModel(ModelState settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    public static ModelProperty<Boolean> TUCKED = new ModelProperty<>();
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(TUCKED);
    }


    @Override
    public @NotNull IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof SimpleStoolBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);
            data.setData(TUCKED, state.getValue(SimpleStoolBlock.TUCKED));
            return data;
        }
        return tileData;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData != null && extraData.getData(TUCKED) != null) {
            int tucked = Boolean.TRUE.equals(extraData.getData(TUCKED)) ? 1 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            List<BakedQuad> quads = getTemplateBakedModels().get(tucked).getQuads(state, side, rand, extraData);
            return getQuadsWithTexture(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(state, face, random);
        return getQuadsWithTexture(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
