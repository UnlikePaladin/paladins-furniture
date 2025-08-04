package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeMirrorModel extends PFMForgeBakedModel {
    public ForgeMirrorModel(ModelBakeSettings settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = MODEL_PARTS;
    }

    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> DIRECTIONS = new ModelProperty<>();

    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        List<BlockModelPart> quads = new ArrayList<>();
        quads.add(getTemplateBakedModels().get((0)));
        if (state != null && state.getBlock() instanceof MirrorBlock && extraData.get(DIRECTIONS) != null && extraData.get(DIRECTIONS).connections != null) {
            BitSet connections = extraData.get(DIRECTIONS).connections;
            if (!connections.get(0)) {
                quads.add(getTemplateBakedModels().get((1)));
            }
            if (!connections.get(1)) {
                quads.add(getTemplateBakedModels().get((2)));
            }
            if (!connections.get(2)) {
                quads.add(getTemplateBakedModels().get((4)));
            }
            if (!connections.get(3)) {
                quads.add(getTemplateBakedModels().get((3)));
            }
            if (!connections.get(4)) {
                quads.add(getTemplateBakedModels().get((6)));
            }
            if (!connections.get(5)) {
                quads.add(getTemplateBakedModels().get((8)));
            }
            if (!connections.get(6)) {
                quads.add(getTemplateBakedModels().get((5)));
            }
            if (!connections.get(7)) {
                quads.add(getTemplateBakedModels().get((7)));
            }
        }
        dest.addAll(quads);
    }

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.get(MirrorBlock.FACING);
            BitSet connections = new BitSet(8);
            connections.set(0, block.canConnect(blockView.getBlockState(pos.up()), state));
            connections.set(1, block.canConnect(blockView.getBlockState(pos.down()), state));
            connections.set(2, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise())), state));
            connections.set(3, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise())), state));

            connections.set(4, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).up()), state));
            connections.set(5, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).down()), state));
            connections.set(6, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).up()), state));
            connections.set(7, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).down()), state));
            ModelBitSetProperty mirrorDirections = new ModelBitSetProperty(connections);
            builder.with(DIRECTIONS, mirrorDirections);
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        return List.of();
    }
}