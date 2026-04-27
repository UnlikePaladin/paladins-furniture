package com.unlikepaladin.pfm.blocks.models.mirror.forge;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeMirrorModel extends PFMForgeBakedModel {
    public ForgeMirrorModel(TextureAtlasSprite frame, TextureAtlasSprite glassTex, TextureAtlasSprite reflectTex, ModelState settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(settings, new ArrayList<>(bakedModels.values()));
        this.modelParts = MODEL_PARTS;
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
    }
    protected final TextureAtlasSprite glassTex;
    protected final TextureAtlasSprite reflectTex;

    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> DIRECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>(getTemplateBakedModels().get((0)).getQuads(state, side, rand, extraData));
        if (state != null && state.getBlock() instanceof MirrorBlock && extraData.getData(DIRECTIONS) != null && extraData.getData(DIRECTIONS).connections != null) {
            BitSet connections = extraData.getData(DIRECTIONS).connections;
            if (!connections.get(0)) {
                quads.addAll(getTemplateBakedModels().get((1)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(1)) {
                quads.addAll(getTemplateBakedModels().get((2)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(2)) {
                quads.addAll(getTemplateBakedModels().get((4)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(3)) {
                quads.addAll(getTemplateBakedModels().get((3)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(4)) {
                quads.addAll(getTemplateBakedModels().get((6)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(5)) {
                quads.addAll(getTemplateBakedModels().get((8)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(6)) {
                quads.addAll(getTemplateBakedModels().get((5)).getQuads(state, side, rand, extraData));
            }
            if (!connections.get(7)) {
                quads.addAll(getTemplateBakedModels().get((7)).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
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
            builder.withInitial(DIRECTIONS, mirrorDirections);
        }
        return builder.build();
    }
}