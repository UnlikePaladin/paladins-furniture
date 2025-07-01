package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;

import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricKitchenCounterOvenModel extends PFMFabricBakedModel {
    public FabricKitchenCounterOvenModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            boolean up = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.up()).getBlock());
            boolean down = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.down()).getBlock());
            int openOffset = state.get(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
            if (up || down) {
                getTemplateBakedModels().get((1 + openOffset)).emitQuads(context, cullTest);
            } else {
                getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<Sprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((0)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
