package com.unlikepaladin.pfm.blocks.models.ladder.fabric;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.function.Supplier;

public class FabricLadderModel extends PFMFabricBakedModel {
    public FabricLadderModel(ModelState settings, List<BakedModel> bakedModels) {
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
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            TextureAtlasSprite sprite = getSpriteList(state).get(0);
            pushTextureTransform(context, sprite);
            int offset = state.getValue(SimpleBunkLadderBlock.UP) ? 1 : 0;
            ((FabricBakedModel)getTemplateBakedModels().get(offset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        TextureAtlasSprite sprite = getSpriteList(stack).get(0);
        pushTextureTransform(context, sprite);
        ((FabricBakedModel)getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }
}
