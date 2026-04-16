package com.unlikepaladin.pfm.blocks.models.chairDinner.fabric;

import com.unlikepaladin.pfm.blocks.DinnerChairBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricChairDinnerModel extends PFMFabricBakedModel {
    public FabricChairDinnerModel(ModelState settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof DinnerChairBlock) {
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(emitter, ModelHelper.getOakPlankLogSprites(), spriteList);
            int tucked = state.getValue(DinnerChairBlock.TUCKED) ? 1 : 0;

            getTemplateBakedModels().get(tucked).emitBlockQuads(emitter, blockView, state, pos, randomSupplier, cullTest);
            emitter.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get(0).emitItemQuads(context, randomSupplier);
        context.popTransform();
    }
}
