package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricMirrorModel extends PFMFabricBakedModel {
    public FabricMirrorModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.get(MirrorBlock.FACING);
            boolean up = block.canConnect(blockView.getBlockState(pos.up()), state);
            boolean down = block.canConnect(blockView.getBlockState(pos.down()), state);
            boolean left = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise())), state);
            boolean right = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise())), state);

            boolean cornerLeftUp = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).up()), state);
            boolean cornerRightDown = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).down()), state);
            boolean cornerLeftDown = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).down()), state);
            boolean cornerRightUp = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).up()), state);

            getTemplateBakedModels().get(0).emitQuads(context, cullTest);
            if (!down) {
                getTemplateBakedModels().get(2).emitQuads(context, cullTest);
            }
            if (!up) {
                getTemplateBakedModels().get(1).emitQuads(context, cullTest);
            }
            if (!right) {
                getTemplateBakedModels().get(3).emitQuads(context, cullTest);
            }
            if (!left) {
                getTemplateBakedModels().get(4).emitQuads(context, cullTest);
            }

            if (!cornerLeftDown) {
                getTemplateBakedModels().get(8).emitQuads(context, cullTest);
            }
            if (!cornerRightDown) {
                getTemplateBakedModels().get(7).emitQuads(context, cullTest);
            }
            if (!cornerLeftUp) {
                getTemplateBakedModels().get(6).emitQuads(context, cullTest);
            }
            if (!cornerRightUp) {
                getTemplateBakedModels().get(5).emitQuads(context, cullTest);
            }
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Random randomSupplier) {
        Predicate<Direction> anyPredicate = d -> false;
        getTemplateBakedModels().get(0).emitQuads(emitter, anyPredicate);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return particleSprite();
    }
}
