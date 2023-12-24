package com.unlikepaladin.pfm.blocks.models.chairClassic.fabric;

import com.unlikepaladin.pfm.blocks.ClassicChairBlock;
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
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FabricChairClassicModel extends PFMFabricBakedModel {
    public FabricChairClassicModel(ModelBakeSettings settings, List<BakedModel> bakedModels) {
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
        if (state.getBlock() instanceof ClassicChairBlock) {
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            int tucked = state.get(ClassicChairBlock.TUCKED) ? 1 : 0;
            ((FabricBakedModel) getTemplateBakedModels().get(tucked)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
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
}
