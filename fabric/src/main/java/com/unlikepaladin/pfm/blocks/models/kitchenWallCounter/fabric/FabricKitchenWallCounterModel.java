package com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.fabric;

import com.unlikepaladin.pfm.blocks.KitchenWallCounterBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;
import java.util.function.Supplier;

public class FabricKitchenWallCounterModel extends AbstractBakedModel implements FabricBakedModel {
    public FabricKitchenWallCounterModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof KitchenWallCounterBlock) {
            KitchenWallCounterBlock block = (KitchenWallCounterBlock) state.getBlock();
            Direction direction = state.get(KitchenWallCounterBlock.FACING);
            BlockState neighborStateFacing = world.getBlockState(pos.offset(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                    if (direction2 == direction.rotateYCounterclockwise()) {
                        ((FabricBakedModel) getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                    else {
                        ((FabricBakedModel) getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    ((FabricBakedModel) getBakedModels().get(modelParts.get(0))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                }
                if (direction3.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3)) {
                    if (direction3 == direction.rotateYCounterclockwise()) {
                        ((FabricBakedModel) getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    } else {
                        ((FabricBakedModel) getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    ((FabricBakedModel) getBakedModels().get(modelParts.get(0))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            else {
                ((FabricBakedModel) getBakedModels().get(modelParts.get(0))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}