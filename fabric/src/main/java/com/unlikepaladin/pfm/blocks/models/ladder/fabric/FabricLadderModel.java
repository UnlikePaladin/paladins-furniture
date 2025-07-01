package com.unlikepaladin.pfm.blocks.models.ladder.fabric;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricLadderModel extends PFMFabricBakedModel {
    public FabricLadderModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> bakedModels) {
        super(settings, modelSettings, bakedModels);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            Sprite sprite = getSpriteList(state).get(0);
            pushTextureTransform(context, sprite);
            int offset = state.get(SimpleBunkLadderBlock.UP) ? 1 : 0;
            getTemplateBakedModels().get(offset).emitQuads(context, cullTest);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        Sprite sprite = getSpriteList(blockState).get(0);
        pushTextureTransform(context, sprite);
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();
    }
}
