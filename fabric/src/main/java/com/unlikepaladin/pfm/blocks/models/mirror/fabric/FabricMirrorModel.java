package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import java.util.function.Supplier;

public class FabricMirrorModel extends PFMFabricBakedModel {
    public FabricMirrorModel(TextureAtlasSprite frame, TextureAtlasSprite glassTex, TextureAtlasSprite reflectTex, ModelState settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(settings, bakedModels.values().stream().toList());
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
    }
    protected final TextureAtlasSprite glassTex;
    protected final TextureAtlasSprite reflectTex;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.getValue(MirrorBlock.FACING);
            boolean above = block.canConnect(blockView.getBlockState(pos.above()), state);
            boolean down = block.canConnect(blockView.getBlockState(pos.below()), state);
            boolean left = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise())), state);
            boolean right = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise())), state);

            boolean cornerLeftUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).above()), state);
            boolean cornerRightDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).below()), state);
            boolean cornerLeftDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).below()), state);
            boolean cornerRightUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).above()), state);

            getTemplateBakedModels().get(0).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            if (!down) {
                getTemplateBakedModels().get(2).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            if (!above) {
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
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        getTemplateBakedModels().get(0).emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getParticleIcon();
    }
}
