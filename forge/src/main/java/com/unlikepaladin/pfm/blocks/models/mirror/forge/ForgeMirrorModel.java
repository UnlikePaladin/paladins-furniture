package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeMirrorModel extends AbstractBakedModel {
    public ForgeMirrorModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
    }
    protected final Sprite glassTex;
    protected final Sprite reflectTex;

    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> DIRECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderLayer) {
        List<BakedQuad> quads = new ArrayList<>(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData, renderLayer));
        if (state != null && state.getBlock() instanceof MirrorBlock && extraData.get(DIRECTIONS) != null && extraData.get(DIRECTIONS).connections != null) {
            BitSet connections = extraData.get(DIRECTIONS).connections;
            if (!connections.get(0)) {
                quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(1)) {
                quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(2)) {
                quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(3)) {
                quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(4)) {
                quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(5)) {
                quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(6)) {
                quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData, renderLayer));
            }
            if (!connections.get(7)) {
                quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData, renderLayer));
            }
        }
        return quads;
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
}