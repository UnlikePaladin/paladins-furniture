package com.unlikepaladin.pfm.blocks.models.kitchenCounter.forge;

import com.unlikepaladin.pfm.blocks.KitchenCounterBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeKitchenCounterModel extends PFMForgeBakedModel {
    public ForgeKitchenCounterModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(CONNECTIONS);
        builder.withProperty(NEIGHBOR_FACING);
        builder.withProperty(NEIGHBOR_OPPOSITE);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof KitchenCounterBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            KitchenCounterBlock block = (KitchenCounterBlock) state.getBlock();
            Direction direction = state.getValue(KitchenCounterBlock.FACING);
            boolean right = block.canConnect(world, pos, direction.getCounterClockWise());
            boolean left = block.canConnect(world, pos, direction.getClockWise());
            BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
            boolean isNeighborStateOppositeFacingDifferentDirection;
            if (neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                }
                isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction3);
            } else {
                isNeighborStateOppositeFacingDifferentDirection = false;
            }

            boolean isNeighborStateFacingDifferentDirection;
            if (neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
            } else {
                isNeighborStateFacingDifferentDirection = false;
            }
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            set.set(2, isNeighborStateOppositeFacingDifferentDirection);
            set.set(3, isNeighborStateFacingDifferentDirection);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            data.setData(NEIGHBOR_FACING, neighborStateFacing);
            data.setData(NEIGHBOR_OPPOSITE, neighborStateOpposite);
            return data;
        }
        return tileData;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && state.getBlock() instanceof KitchenCounterBlock && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            BitSet set = extraData.getData(CONNECTIONS).connections;
            Direction direction = state.getValue(KitchenCounterBlock.FACING);
            KitchenCounterBlock block = (KitchenCounterBlock) state.getBlock();
            boolean left = set.get(0);
            boolean right = set.get(1);
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(2);
            boolean isNeighborStateFacingDifferentDirection = set.get(3);
            BlockState neighborStateFacing = extraData.getData(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.getData(NEIGHBOR_OPPOSITE);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (direction2 == direction.getCounterClockWise()) {
                        return getQuadsWithTexture(getTemplateBakedModels().get(5).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                    }
                    else {
                        return getQuadsWithTexture(getTemplateBakedModels().get(6).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                    }
                } else {
                    return getQuadsWithTexture(getMiddleQuads(state, side, rand, extraData, left, right), ModelHelper.getOakPlankLogSprites(), spriteList);
                }
            }
            else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                }
                if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                    if (direction3 == direction.getCounterClockWise()) {
                        return getQuadsWithTexture(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                    } else {
                        return getQuadsWithTexture(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                    }
                } else {
                    return getQuadsWithTexture(getMiddleQuads(state, side, rand, extraData, left, right), ModelHelper.getOakPlankLogSprites(), spriteList);
                }
            }
            else {
                return getQuadsWithTexture(getMiddleQuads(state, side, rand, extraData, left, right), ModelHelper.getOakPlankLogSprites(), spriteList);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(state, face, random), ModelHelper.getOakPlankLogSprites(), spriteList);
    }

    private List<BakedQuad> getMiddleQuads(BlockState state, Direction side, Random rand, IModelData extraData, boolean left, boolean right) {
        if (left && right) {
            return getTemplateBakedModels().get(0).getQuads(state, side, rand, extraData);
        } else if (left) {
            return getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData);
        } else if (right) {
            return getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData);
        } else {
            return getTemplateBakedModels().get(0).getQuads(state, side, rand, extraData);
        }
    }
}