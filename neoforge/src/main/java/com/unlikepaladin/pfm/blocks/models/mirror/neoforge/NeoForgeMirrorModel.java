package com.unlikepaladin.pfm.blocks.models.mirror.neoforge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeMirrorModel extends PFMNeoForgeBakedModel {
    public NeoForgeMirrorModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;

    @Override
    public void collectParts(BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof MirrorBlock))
            return;

        MirrorBlock block = (MirrorBlock) state.getBlock();
        Direction facing = state.getValue(MirrorBlock.FACING);

        boolean zero = block.canConnect(blockView.getBlockState(pos.above()), state);
        boolean one = block.canConnect(blockView.getBlockState(pos.below()), state);
        boolean two = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise())), state);
        boolean three = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise())), state);

        boolean four = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).above()), state);
        boolean five = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).below()), state);
        boolean six = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).above()), state);
        boolean seven = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).below()), state);

        List<BlockModelPart> quads = new ArrayList<>();
        quads.add(getTemplateBakedModels().get((0)));

        if (!zero) {
            quads.add(getTemplateBakedModels().get((1)));
        }
        if (!one) {
            quads.add(getTemplateBakedModels().get((2)));
        }
        if (!two) {
            quads.add(getTemplateBakedModels().get((4)));
        }
        if (!three) {
            quads.add(getTemplateBakedModels().get((3)));
        }
        if (!four) {
            quads.add(getTemplateBakedModels().get((6)));
        }
        if (!five) {
            quads.add(getTemplateBakedModels().get((8)));
        }
        if (!six) {
            quads.add(getTemplateBakedModels().get((5)));
        }
        if (!seven) {
            quads.add(getTemplateBakedModels().get((7)));
        }

        parts.addAll(quads);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}