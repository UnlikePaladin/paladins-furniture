package com.unlikepaladin.pfm.blocks.models.ladder.fabric;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricLadderModel extends PFMFabricBakedModel {
    public FabricLadderModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> bakedModels) {
        super(settings, modelSettings, bakedModels);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            TextureAtlasSprite sprite = getSpriteList(state).get(0);
            pushTextureTransform(context, sprite);
            int offset = state.getValue(SimpleBunkLadderBlock.UP) ? 1 : 0;
            getTemplateBakedModels().get(offset).emitQuads(context, cullTest);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        TextureAtlasSprite sprite = getSpriteList(blockState).get(0);
        pushTextureTransform(context, sprite);
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();
    }
}
