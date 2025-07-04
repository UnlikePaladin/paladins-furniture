package com.unlikepaladin.pfm.blocks.models.mirror.neoforge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class NeoForgeMirrorModel extends PFMNeoForgeBakedModel {
    public NeoForgeMirrorModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;

    @Override
    public void collectParts(BlockRenderView blockView, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof MirrorBlock))
            return;

        MirrorBlock block = (MirrorBlock) state.getBlock();
        Direction facing = state.get(MirrorBlock.FACING);

        boolean zero = block.canConnect(blockView.getBlockState(pos.up()), state);
        boolean one = block.canConnect(blockView.getBlockState(pos.down()), state);
        boolean two = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise())), state);
        boolean three = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise())), state);

        boolean four = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).up()), state);
        boolean five = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).down()), state);
        boolean six = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).up()), state);
        boolean seven = block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).down()), state);

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
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}