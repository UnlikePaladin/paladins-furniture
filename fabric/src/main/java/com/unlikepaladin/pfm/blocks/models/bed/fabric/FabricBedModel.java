package com.unlikepaladin.pfm.blocks.models.bed.fabric;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
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
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
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
                getTemplateBakedModels().get((classicOffset+3)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                if (!right){
                    getTemplateBakedModels().get((classicOffset+6)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
                if (!left){
                    getTemplateBakedModels().get((classicOffset+7)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
                if (bunk && !(state.getBlock() instanceof ClassicBedBlock)){
                    getTemplateBakedModels().get((classicOffset+10)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
            } else {
                getTemplateBakedModels().get((classicOffset+2)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                if (!right){
                    getTemplateBakedModels().get((classicOffset+4)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
                if (!left){
                    getTemplateBakedModels().get((classicOffset+5)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
                if (!right && bunk){
                    getTemplateBakedModels().get((classicOffset+8)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
                if (!left && bunk){
                    getTemplateBakedModels().get((classicOffset+9)).emitBlockQuads(context, blockView, state, pos, randomSupplier, cullTest);
                }
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakBedSprites(), spriteList);
        int classicOffset = blockState.getBlock().getDescriptionId().contains("classic") ? 12 : 0;
        getTemplateBakedModels().get((classicOffset+11)).emitItemQuads(context, randomSupplier);
        context.popTransform();

    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}
