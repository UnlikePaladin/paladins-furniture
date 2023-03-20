package com.unlikepaladin.pfm.blocks.models.forge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.BakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.MirrorUnbakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ForgeMirrorModel extends BakedMirrorModel {
    public ForgeMirrorModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier, BakedModel> bakedModels) {
        super(frame, frameOverrides, glassTex, reflectTex, settings, bakedModels);
    }

    public static ModelProperty<MirrorDirections> DIRECTIONS = new ModelProperty<>();


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_GLASS).getQuads(state, side, rand, extraData));
        if (state.getBlock() instanceof MirrorBlock) {
            BitSet connections = extraData.getData(DIRECTIONS).connections;
            if (!connections.get(0)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_TOP).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(1)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_BOTTOM).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(2)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(3)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(4)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_TOP_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(5)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_BOTTOM_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(6)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_TOP_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(7)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_BOTTOM_CORNER).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
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
            MirrorDirections mirrorDirections = new MirrorDirections(connections);
            builder.withInitial(DIRECTIONS, mirrorDirections);
        }
        return builder.build();
    }
}

class MirrorDirections implements Predicate<MirrorDirections>
{
    public MirrorDirections(BitSet connections) {
        this.connections = connections;
    }

    protected BitSet connections;
    @Override
    public boolean test(MirrorDirections mirrorDirections) {
        return connections.equals(mirrorDirections.connections);
    }
}