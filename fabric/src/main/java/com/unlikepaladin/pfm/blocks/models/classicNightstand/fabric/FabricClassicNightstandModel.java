package com.unlikepaladin.pfm.blocks.models.classicNightstand.fabric;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricClassicNightstandModel extends PFMFabricBakedModel {
    public FabricClassicNightstandModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.get(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isStand(world, pos, dir.rotateYClockwise(), dir);
            int openIndexOffset = state.get(ClassicNightstandBlock.OPEN) ? 4 : 0;
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            if (left && right) {
                getTemplateBakedModels().get((openIndexOffset)).emitQuads(context, cullTest);
            } else if (!left && right) {
                getTemplateBakedModels().get((1+openIndexOffset)).emitQuads(context, cullTest);
            } else if (left) {
                getTemplateBakedModels().get((2+openIndexOffset)).emitQuads(context, cullTest);
            } else {
                getTemplateBakedModels().get((3+openIndexOffset)).emitQuads(context, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<Sprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((3)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
