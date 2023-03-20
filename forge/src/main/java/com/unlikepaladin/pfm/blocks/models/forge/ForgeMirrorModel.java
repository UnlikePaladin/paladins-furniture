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
import net.minecraft.world.BlockView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ForgeMirrorModel extends BakedMirrorModel {
    public ForgeMirrorModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier, BakedModel> bakedModels) {
        super(frame, frameOverrides, glassTex, reflectTex, settings, bakedModels);
    }

    public static final ModelProperty<Boolean> up = new ModelProperty<>();
    public static final ModelProperty<Boolean> down = new ModelProperty<>();
    public static final ModelProperty<Boolean> left = new ModelProperty<>();
    public static final ModelProperty<Boolean> right = new ModelProperty<>();

    public static final ModelProperty<Boolean> cornerLeftUp = new ModelProperty<>();
    public static final ModelProperty<Boolean> cornerRightDown = new ModelProperty<>();
    public static final ModelProperty<Boolean> cornerLeftDown = new ModelProperty<>();
    public static final ModelProperty<Boolean> cornerRightUp = new ModelProperty<>();


    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_GLASS).getQuads(state, side, rand, extraData));
        if (state.getBlock() instanceof MirrorBlock && extraData.hasProperty(up) && extraData.hasProperty(down) && extraData.hasProperty(left) && extraData.hasProperty(right) && extraData.hasProperty(cornerLeftDown) && extraData.hasProperty(cornerLeftUp) && extraData.hasProperty(cornerRightUp) && extraData.hasProperty(cornerRightDown)) {
            if (!extraData.getData(down)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_BOTTOM).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(up)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_TOP).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(right)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(left)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT).getQuads(state, side, rand, extraData));
            }

            if (!extraData.getData(cornerLeftDown)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_BOTTOM_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(cornerRightDown)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_BOTTOM_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(cornerLeftUp)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_TOP_CORNER).getQuads(state, side, rand, extraData));
            }
            if (!extraData.getData(cornerRightUp)) {
                quads.addAll(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_TOP_CORNER).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.get(MirrorBlock.FACING);
            tileData.setData(up, block.canConnect(blockView.getBlockState(pos.up()), state));
            tileData.setData(down, block.canConnect(blockView.getBlockState(pos.down()), state));
            tileData.setData(left, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise())), state));
            tileData.setData(right, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise())), state));
            tileData.setData(cornerLeftUp, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).up()), state));
            tileData.setData(cornerRightDown, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).down()), state));
            tileData.setData(cornerLeftDown, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYClockwise()).down()), state));
            tileData.setData(cornerRightUp, block.canConnect(blockView.getBlockState(pos.offset(facing.rotateYCounterclockwise()).up()), state));
        }
        return tileData;
    }
}