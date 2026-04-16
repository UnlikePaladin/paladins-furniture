package com.unlikepaladin.pfm.blocks.models.classicNightstand.fabric;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricClassicNightstandModel extends PFMFabricBakedModel {
    public FabricClassicNightstandModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.getValue(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.getCounterClockWise(), dir);
            boolean right = block.isStand(world, pos, dir.getClockWise(), dir);
            int openIndexOffset = state.getValue(ClassicNightstandBlock.OPEN) ? 4 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            if (left && right) {
                getTemplateBakedModels().get((openIndexOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            } else if (!left && right) {
                getTemplateBakedModels().get((1+openIndexOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            } else if (left) {
                getTemplateBakedModels().get((2+openIndexOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            } else {
                getTemplateBakedModels().get((3+openIndexOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((3)).emitItemQuads(context, randomSupplier);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
