package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.forge;

import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.BakedKitchenDrawerModel;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ForgeKitchenWallDrawerModel extends BakedKitchenDrawerModel {
    public ForgeKitchenWallDrawerModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    public static ModelProperty<CounterConnections> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        if (state.getBlock() instanceof KitchenWallDrawerBlock) {
            KitchenWallDrawerBlock block = (KitchenWallDrawerBlock) state.getBlock();
            Direction direction = state.get(KitchenWallDrawerBlock.FACING);
            BlockState neighborStateFacing = world.getBlockState(pos.offset(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
            boolean isNeighborStateOppositeFacingDifferentDirection;
            if (neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                }
                isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction3);
            } else {
                isNeighborStateOppositeFacingDifferentDirection = false;
            }

            boolean isNeighborStateFacingDifferentDirection;
            if (neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
            } else {
                isNeighborStateFacingDifferentDirection = false;
            }
            BitSet set = new BitSet();
            set.set(0, isNeighborStateOppositeFacingDifferentDirection);
            set.set(1, isNeighborStateFacingDifferentDirection);
            builder.withInitial(CONNECTIONS, new CounterConnections(set)).withInitial(NEIGHBOR_FACING, neighborStateFacing).withInitial(NEIGHBOR_OPPOSITE, neighborStateOpposite);
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state.getBlock() instanceof KitchenWallDrawerBlock) {
            BitSet set = extraData.getData(CONNECTIONS).connections;
            Direction direction = state.get(KitchenWallDrawerBlock.FACING);
            KitchenWallDrawerBlock block = (KitchenWallDrawerBlock) state.getBlock();
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(1);
            boolean isNeighborStateFacingDifferentDirection = set.get(0);
            BlockState neighborStateFacing = extraData.getData(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.getData(NEIGHBOR_OPPOSITE);
            int openOffset = state.get(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (direction2 == direction.rotateYCounterclockwise()) {
                        return getBakedModels().get(modelParts.get(3 + openOffset)).getQuads(state, side, rand, extraData);
                    }
                    else {
                        return getBakedModels().get(modelParts.get(4 + openOffset)).getQuads(state, side, rand, extraData);
                    }
                } else {
                    return getBakedModels().get(modelParts.get(openOffset)).getQuads(state, side, rand, extraData);
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
                if (direction3.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                    if (direction3 == direction.rotateYCounterclockwise()) {
                        return getBakedModels().get(modelParts.get(2 + openOffset)).getQuads(state, side, rand, extraData);
                    } else {
                        return getBakedModels().get(modelParts.get(1 + openOffset)).getQuads(state, side, rand, extraData);
                    }
                } else {
                    return getBakedModels().get(modelParts.get(openOffset)).getQuads(state, side, rand, extraData);
                }
            }
            else {
                return getBakedModels().get(modelParts.get(openOffset)).getQuads(state, side, rand, extraData);
            }
        }
        return Collections.emptyList();
    }
}
class CounterConnections implements Predicate<CounterConnections>
{
    public CounterConnections(BitSet connections) {
        this.connections = connections;
    }

    protected BitSet connections;
    @Override
    public boolean test(CounterConnections tableConnections) {
        return connections.equals(tableConnections.connections);
    }
}