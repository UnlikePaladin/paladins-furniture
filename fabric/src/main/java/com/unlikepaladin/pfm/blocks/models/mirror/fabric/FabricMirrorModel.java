package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
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
import java.util.Random;
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

            context.fallbackConsumer().accept(getTemplateBakedModels().get((0)));
            if (!down) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((2)));
            }
            if (!up) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((1)));
            }
            if (!right) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((3)));
            }
            if (!left) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((4)));
            }

            if (!cornerLeftDown) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((8)));
            }
            if (!cornerRightDown) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((7)));
            }
            if (!cornerLeftUp) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((6)));
            }
            if (!cornerRightUp) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((5)));
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(getTemplateBakedModels().get(0));
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
