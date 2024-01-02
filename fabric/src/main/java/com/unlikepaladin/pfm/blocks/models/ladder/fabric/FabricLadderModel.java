package com.unlikepaladin.pfm.blocks.models.ladder.fabric;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.function.Supplier;

public class FabricLadderModel extends PFMFabricBakedModel {
    public FabricLadderModel(ModelBakeSettings settings, List<BakedModel> bakedModels) {
        super(settings, bakedModels);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            Sprite sprite = getSpriteList(state).get(0);
            pushTextureTransform(context, sprite);
            int offset = state.get(SimpleBunkLadderBlock.UP) ? 1 : 0;
            ((FabricBakedModel)getTemplateBakedModels().get(offset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Sprite sprite = getSpriteList(stack).get(0);
        pushTextureTransform(context, sprite);
        ((FabricBakedModel)getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }
}
