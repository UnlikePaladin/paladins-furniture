package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeMirrorModel extends PFMForgeBakedModel {
    public ForgeMirrorModel(ModelState settings, Map<String, BlockModelPart> bakedModels, List<String> MODEL_PARTS) {
        super(settings, null, bakedModels.values().stream().toList());
        this.modelParts = MODEL_PARTS;
    }

    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> DIRECTIONS = new ModelProperty<>();

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable RenderType renderType) {
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
    public ModelData getModelData(@NotNull BlockAndTintGetter blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.getValue(MirrorBlock.FACING);
            BitSet connections = new BitSet(8);
            connections.set(0, block.canConnect(blockView.getBlockState(pos.above()), state));
            connections.set(1, block.canConnect(blockView.getBlockState(pos.below()), state));
            connections.set(2, block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise())), state));
            connections.set(3, block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise())), state));

            connections.set(4, block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).above()), state));
            connections.set(5, block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).below()), state));
            connections.set(6, block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).above()), state));
            connections.set(7, block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).below()), state));
            ModelBitSetProperty mirrorDirections = new ModelBitSetProperty(connections);
            builder.with(DIRECTIONS, mirrorDirections);
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        return List.of();
    }
}