package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
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
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
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

            getTemplateBakedModels().get(0).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            if (!down) {
                getTemplateBakedModels().get(2).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!above) {
                getTemplateBakedModels().get(1).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!right) {
                getTemplateBakedModels().get(3).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!left) {
                getTemplateBakedModels().get(4).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }

            if (!cornerLeftDown) {
                getTemplateBakedModels().get(8).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!cornerRightDown) {
                getTemplateBakedModels().get(7).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!cornerLeftUp) {
                getTemplateBakedModels().get(6).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
            if (!cornerRightUp) {
                getTemplateBakedModels().get(5).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
            }
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<RandomSource> randomSupplier) {
        getTemplateBakedModels().get(0).emitItemQuads(emitter, randomSupplier);
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
