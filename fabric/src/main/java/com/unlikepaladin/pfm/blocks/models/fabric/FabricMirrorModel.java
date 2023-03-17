package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.BakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.MirrorUnbakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockRenderView;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
//TODO: Clean up classes
//TODO: implement and register mirror model on Forge
//TODO: add IP compatibility on Forge
public class FabricMirrorModel extends BakedMirrorModel implements FabricBakedModel {
    public FabricMirrorModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier,BakedModel> bakedModels) {
        super(frame, frameOverrides, glassTex, reflectTex, settings, bakedModels);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
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

            context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_GLASS));
            if (!down) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_BOTTOM));
            }
            if (!up) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_TOP));
            }
            if (!right) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT));
            }
            if (!left) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT));
            }

            if (!cornerLeftDown) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_BOTTOM_CORNER));
            }
            if (!cornerRightDown) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_BOTTOM_CORNER));
            }
            if (!cornerLeftUp) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_LEFT_TOP_CORNER));
            }
            if (!cornerRightUp) {
                context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_RIGHT_TOP_CORNER));
            }
        }

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(getBakedModels().get(MirrorUnbakedModel.MODEL_MIRROR_GLASS));
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }
}
