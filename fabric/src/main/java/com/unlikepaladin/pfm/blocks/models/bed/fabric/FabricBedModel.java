package com.unlikepaladin.pfm.blocks.models.bed.fabric;

import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.bed.BakedBedModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricBedModel extends BakedBedModel implements FabricBakedModel {
    private final List<String> modelParts; 
    public FabricBedModel(Sprite frame, Sprite beddingTex, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, beddingTex, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof SimpleBedBlock) {
            Direction dir = state.get(BedBlock.FACING);
            boolean left = isBed(blockView, pos, dir.rotateYCounterclockwise(), dir, state);
            boolean right = isBed(blockView, pos, dir.rotateYClockwise(), dir, state);
            boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state);
            
            BedPart part = state.get(BedBlock.PART);
            if (part == BedPart.HEAD) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                if (!right){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(6))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(7))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (bunk){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(8))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
            } else {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(0))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                if (!right){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(5))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!right && bunk){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(9))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
                if (!left && bunk){
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(10))).emitBlockQuads(blockView, state, pos, randomSupplier, context);
                }
            }
        }
    }

    public boolean isBed(BlockRenderView world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock().getClass().isAssignableFrom(SimpleBedBlock.class) && state.getBlock() instanceof SimpleBedBlock)
        {
            if (state.get(BedBlock.PART) == originalState.get(BedBlock.PART)) {
                Direction sourceDirection = state.get(BedBlock.FACING);
                return sourceDirection.equals(bedDirection);
            }
        }
        return false;
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}
