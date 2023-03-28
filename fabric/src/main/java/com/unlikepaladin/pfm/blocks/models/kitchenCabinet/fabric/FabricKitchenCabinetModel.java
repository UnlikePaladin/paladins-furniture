package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.kitchenCabinet.BakedKitchenCabinetModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.State;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricKitchenCabinetModel extends BakedKitchenCabinetModel implements FabricBakedModel {
    public FabricKitchenCabinetModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
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
        if (state.getBlock() instanceof KitchenCabinetBlock) {
            KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
            Direction direction = state.get(KitchenCabinetBlock.FACING);
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
            int openOffset = state.get(KitchenWallDrawerBlock.OPEN) ? 5 : 0;

            Direction direction3 = null;
            Direction direction2;
            boolean innerCorner = block.isCabinet(neighborStateOpposite) && (direction3 = neighborStateOpposite.get(KitchenCabinetBlock.FACING)).getAxis() != state.get(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3);
            BlockState blockState = world.getBlockState(pos.offset(direction));
            if (block.isCabinet(blockState) && (direction2 = blockState.get(KitchenCabinetBlock.FACING)).getAxis() != state.get(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(3 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                else {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            else if (innerCorner) {
                if (direction3 == direction.rotateYCounterclockwise()) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(2 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                } else {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(1 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}