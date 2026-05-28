package com.unlikepaladin.pfm.blocks.models.logStool.fabric;

import com.unlikepaladin.pfm.blocks.LogStoolBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricLogStoolModel extends PFMFabricBakedModel {
    public FabricLogStoolModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> bakedModels) {
        super(settings, modelSettings, bakedModels);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }


    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof LogStoolBlock) {
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakLogLogTopSprites(), spriteList);
            int tucked = state.getValue(LogStoolBlock.TUCKED) ? 1 : 0;
            getTemplateBakedModels().get(tucked).emitQuads(context, cullTest);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakLogLogTopSprites(), spriteList);
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();
    }
}
