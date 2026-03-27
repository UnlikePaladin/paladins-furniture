package com.unlikepaladin.pfm.blocks.models.bed.fabric;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricBedModel extends PFMFabricBakedModel implements BedInterface {
    public FabricBedModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof SimpleBedBlock) {
            Direction dir = state.getValue(BedBlock.FACING);
            boolean isClassic = state.getBlock().getDescriptionId().contains("classic");
            boolean left = isBed(blockView, pos, dir.getCounterClockWise(), dir, state, isClassic);
            boolean right = isBed(blockView, pos, dir.getClockWise(), dir, state, isClassic);
            boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
            int classicOffset = isClassic ? 12 : 0;
            BedPart part = state.getValue(BedBlock.PART);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakBedSprites(), spriteList);
            if (part == BedPart.HEAD) {
                ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+3))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                if (!right){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+6))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+7))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (bunk && !(state.getBlock() instanceof ClassicBedBlock)){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+10))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
            } else {
                ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+2))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                if (!right){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+4))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+5))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!right && bunk){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+8))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left && bunk){
                    ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+9))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
            }
            context.popTransform();
        }
    }


    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        pushTextureTransform(context, ModelHelper.getOakBedSprites(), spriteList);
        int classicOffset = stack.getDescriptionId().contains("classic") ? 12 : 0;
        ((FabricBakedModel) getTemplateBakedModels().get((classicOffset+11))).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
