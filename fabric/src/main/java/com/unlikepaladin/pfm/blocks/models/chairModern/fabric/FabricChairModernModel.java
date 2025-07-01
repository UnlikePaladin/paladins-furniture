package com.unlikepaladin.pfm.blocks.models.chairModern.fabric;

import com.unlikepaladin.pfm.blocks.ModernChairBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
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

public class FabricChairModernModel extends PFMFabricBakedModel {
    public FabricChairModernModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> bakedModels) {
        super(settings, modelSettings, bakedModels);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ModernChairBlock) {
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            int tucked = state.get(ModernChairBlock.TUCKED) ? 1 : 0;
            getTemplateBakedModels().get(tucked).emitQuads(context, cullTest);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<Sprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().getFirst().emitQuads(context, anyPredicate);
        context.popTransform();
    }
}
