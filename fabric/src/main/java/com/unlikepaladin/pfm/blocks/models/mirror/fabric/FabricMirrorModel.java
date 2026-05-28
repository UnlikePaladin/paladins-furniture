package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricMirrorModel extends PFMFabricBakedModel {
    public FabricMirrorModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.getValue(MirrorBlock.FACING);
            boolean above = block.canConnect(blockView.getBlockState(pos.above()), state);
            boolean down = block.canConnect(blockView.getBlockState(pos.below()), state);
            boolean left = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise())), state);
            boolean right = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise())), state);

            boolean cornerLeftUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).above()), state);
            boolean cornerRightDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).below()), state);
            boolean cornerLeftDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).below()), state);
            boolean cornerRightUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).above()), state);

            getTemplateBakedModels().get(0).emitQuads(context, cullTest);
            if (!down) {
                getTemplateBakedModels().get(2).emitQuads(context, cullTest);
            }
            if (!above) {
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
    public void emitItemQuads(QuadEmitter emitter, RandomSource randomSupplier) {
        Predicate<Direction> anyPredicate = d -> false;
        getTemplateBakedModels().get(0).emitQuads(emitter, anyPredicate);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return particleIcon();
    }
}
