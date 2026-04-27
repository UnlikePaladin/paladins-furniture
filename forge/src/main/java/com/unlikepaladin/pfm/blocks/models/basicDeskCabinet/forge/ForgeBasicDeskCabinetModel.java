package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.forge;

import com.unlikepaladin.pfm.blocks.BasicDeskBlock;
import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenDrawerBlock;
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
import java.util.function.Supplier;

public class ForgeBasicDeskCabinetModel extends PFMForgeBakedModel {
    public ForgeBasicDeskCabinetModel(ModelState settings, List<BakedModel> modelParts) {
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
        if (state.getBlock() instanceof BasicDeskCabinetBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));

            Direction isFacing = state.getValue(BasicDeskCabinetBlock.FACING);
            BlockState neighborStateFacing = world.getBlockState(pos.relative(isFacing));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(isFacing.getOpposite()));

            boolean isNeighborStateOppositeFacingDifferentDirection;
            if (neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING));
            } else {
                isNeighborStateOppositeFacingDifferentDirection = false;
            }

            boolean isNeighborStateFacingDifferentDirection;
            if (neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
            } else {
                isNeighborStateFacingDifferentDirection = false;
            }

            BlockState rightState = world.getBlockState(pos.relative(isFacing.getCounterClockWise()));
            boolean right = block.canConnect(rightState) && rightState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.relative(isFacing.getClockWise()));
            boolean left = block.canConnect(leftState) && leftState.getBlock() instanceof BasicDeskCabinetBlock;

            BitSet set = new BitSet();
            set.set(0, north);
            set.set(1, east);
            set.set(2, west);
            set.set(3, south);
            set.set(4, left);
            set.set(5, right);
            set.set(6, isNeighborStateOppositeFacingDifferentDirection);
            set.set(7, isNeighborStateFacingDifferentDirection);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            data.setData(NEIGHBOR_FACING, neighborStateFacing);
            data.setData(NEIGHBOR_OPPOSITE, neighborStateOpposite);
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && state.getBlock() instanceof BasicDeskCabinetBlock && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            List<BakedQuad> secondaryQuads = new ArrayList<>();
            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();

            BitSet set = extraData.getData(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            Direction isFacing = state.getValue(BasicDeskCabinetBlock.FACING);

            switch (isFacing) {
                case SOUTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, north, south, east, west, 18, 19, 16, 17));
                    break;
                case NORTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, north, south, east, west, 17, 16, 19, 18));
                    break;
                case EAST:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, north, south, east, west, 19, 17, 18, 16));
                    break;
                default:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, north, south, east, west, 16, 18, 17, 19));
                    break;
            }

            boolean left = set.get(4);
            boolean right = set.get(5);
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(6);
            boolean isNeighborStateFacingDifferentDirection = set.get(7);
            BlockState neighborStateFacing = extraData.getData(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.getData(NEIGHBOR_OPPOSITE);
            int openOffset = state.getValue(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

            if (block.canConnect(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // outer corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((4 + openOffset)).getQuads(state, side, rand, extraData));
                    }
                    else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((5 + openOffset)).getQuads(state, side, rand, extraData));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, left, right, openOffset));
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((6 + openOffset)).getQuads(state, side, rand, extraData));
                    } else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((7 + openOffset)).getQuads(state, side, rand, extraData));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, left, right, openOffset));
                }
            }
            else {
                secondaryQuads.addAll(middleDesk(state, side, rand, extraData, left, right, openOffset));
            }

            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(secondaryQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
       return Collections.emptyList();
    }

    private List<BakedQuad> legsDesk(BlockState state, Direction side, Random rand, IModelData extraData, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        List<BakedQuad> quads = new ArrayList<>();
        if (!north && !east) {
            quads.addAll(getTemplateBakedModels().get(northLeg).getQuads(state, side, rand, extraData));
        }
        if (!north && !west) {
            quads.addAll(getTemplateBakedModels().get(southLeg).getQuads(state, side, rand, extraData));
        }
        if (!south && !west) {
            quads.addAll(getTemplateBakedModels().get(eastLeg).getQuads(state, side, rand, extraData));
        }
        if (!south && !east) {
            quads.addAll(getTemplateBakedModels().get(westLeg).getQuads(state, side, rand, extraData));
        }
        return quads;
    }


    private List<BakedQuad> middleDesk(BlockState state, Direction side, Random rand, IModelData extraData, boolean left, boolean right, int openOffset) {
        if (left && right) {
            return getTemplateBakedModels().get((3 + openOffset)).getQuads(state, side, rand, extraData);
        }  else if (left) {
            return getTemplateBakedModels().get((1 + openOffset)).getQuads(state, side, rand, extraData);
        } else if (right) {
            return getTemplateBakedModels().get((2 + openOffset)).getQuads(state, side, rand, extraData);
        } else {
            return getTemplateBakedModels().get((openOffset)).getQuads(state, side, rand, extraData);
        }
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(state, face, random));

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(18).getQuads(state, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(16).getQuads(state, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(17).getQuads(state, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(19).getQuads(state, face, random));
        // in between pieces


        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}