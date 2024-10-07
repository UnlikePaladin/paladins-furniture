package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;
import java.util.function.Supplier;

public class FabricMirrorModel extends PFMFabricBakedModel {
    public FabricMirrorModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(settings, bakedModels.values().stream().toList());
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
    }
    protected final Sprite glassTex;
    protected final Sprite reflectTex;

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

            getTemplateBakedModels().get(0).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            if (!down) {
                getTemplateBakedModels().get(2).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!up) {
                getTemplateBakedModels().get(1).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!right) {
                getTemplateBakedModels().get(3).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!left) {
                getTemplateBakedModels().get(4).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }

            if (!cornerLeftDown) {
                getTemplateBakedModels().get(8).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!cornerRightDown) {
                getTemplateBakedModels().get(7).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!cornerLeftUp) {
                getTemplateBakedModels().get(6).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!cornerRightUp) {
                getTemplateBakedModels().get(5).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        getTemplateBakedModels().get(0).emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getParticleSprite();
    }
}
