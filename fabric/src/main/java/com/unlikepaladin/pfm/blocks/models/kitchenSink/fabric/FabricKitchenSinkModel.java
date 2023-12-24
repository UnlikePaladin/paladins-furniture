package com.unlikepaladin.pfm.blocks.models.kitchenSink.fabric;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FabricKitchenSinkModel extends PFMFabricBakedModel {
    public FabricKitchenSinkModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof KitchenSinkBlock) {
            int level = state.get(KitchenSinkBlock.LEVEL_4);
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            ((FabricBakedModel) getTemplateBakedModels().get(level)).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        List<Sprite> spriteList = getSpriteList(stack);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        ((FabricBakedModel) getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
