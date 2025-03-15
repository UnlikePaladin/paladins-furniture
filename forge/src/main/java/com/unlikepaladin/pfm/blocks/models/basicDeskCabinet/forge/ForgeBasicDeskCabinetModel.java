package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.forge;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class ForgeBasicDeskCabinetModel extends PFMForgeBakedModel {
    public ForgeBasicDeskCabinetModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof BasicDeskCabinetBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));

            Direction isFacing = state.get(BasicDeskCabinetBlock.FACING);
            BlockState neighborStateFacing = world.getBlockState(pos.offset(isFacing));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(isFacing.getOpposite()));

            boolean isNeighborStateOppositeFacingDifferentDirection;
            if (neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateOpposite.get(Properties.HORIZONTAL_FACING));
            } else {
                isNeighborStateOppositeFacingDifferentDirection = false;
            }

            boolean isNeighborStateFacingDifferentDirection;
            if (neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateFacing.get(Properties.HORIZONTAL_FACING).getOpposite());
            } else {
                isNeighborStateFacingDifferentDirection = false;
            }

            BlockState rightState = world.getBlockState(pos.offset(isFacing.rotateYCounterclockwise()));
            boolean right = block.canConnect(rightState) && rightState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.offset(isFacing.rotateYClockwise()));
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
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            data = data.derive().with(NEIGHBOR_FACING, neighborStateFacing).build();
            data = data.derive().with(NEIGHBOR_OPPOSITE, neighborStateOpposite).build();
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderType) {
        if (state != null && state.getBlock() instanceof BasicDeskCabinetBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BakedQuad> secondaryQuads = new ArrayList<>();
            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();

            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            Direction isFacing = state.get(BasicDeskCabinetBlock.FACING);

            switch (isFacing) {
                case SOUTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderType, north, south, east, west, 18, 19, 16, 17));
                    break;
                case NORTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderType, north, south, east, west, 17, 16, 19, 18));
                    break;
                case EAST:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderType, north, south, east, west, 19, 17, 18, 16));
                    break;
                default:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderType, north, south, east, west, 16, 18, 17, 19));
                    break;
            }

            boolean left = set.get(4);
            boolean right = set.get(5);
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(6);
            boolean isNeighborStateFacingDifferentDirection = set.get(7);
            BlockState neighborStateFacing = extraData.get(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.get(NEIGHBOR_OPPOSITE);
            int openOffset = state.get(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

            if (block.canConnect(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                // outer corner
                if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (neighborFacing == isFacing.rotateYCounterclockwise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((4 + openOffset)).getQuads(state, side, rand, extraData, renderType));
                    }
                    else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((5 + openOffset)).getQuads(state, side, rand, extraData, renderType));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderType, left, right, openOffset));
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                    if (neighborFacing == isFacing.rotateYCounterclockwise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((6 + openOffset)).getQuads(state, side, rand, extraData, renderType));
                    } else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((7 + openOffset)).getQuads(state, side, rand, extraData, renderType));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderType, left, right, openOffset));
                }
            }
            else {
                secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderType, left, right, openOffset));
            }

            List<Sprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(secondaryQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
       return Collections.emptyList();
    }

    private List<BakedQuad> legsDesk(BlockState state, Direction side, Random rand, ModelData extraData, RenderLayer renderType, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        List<BakedQuad> quads = new ArrayList<>();
        if (!north && !east) {
            quads.addAll(getTemplateBakedModels().get(northLeg).getQuads(state, side, rand, extraData, renderType));
        }
        if (!north && !west) {
            quads.addAll(getTemplateBakedModels().get(southLeg).getQuads(state, side, rand, extraData, renderType));
        }
        if (!south && !west) {
            quads.addAll(getTemplateBakedModels().get(eastLeg).getQuads(state, side, rand, extraData, renderType));
        }
        if (!south && !east) {
            quads.addAll(getTemplateBakedModels().get(westLeg).getQuads(state, side, rand, extraData, renderType));
        }
        return quads;
    }


    private List<BakedQuad> middleDesk(BlockState state, Direction side, Random rand, ModelData extraData, RenderLayer layer, boolean left, boolean right, int openOffset) {
        if (left && right) {
            return getTemplateBakedModels().get((3 + openOffset)).getQuads(state, side, rand, extraData, layer);
        }  else if (left) {
            return getTemplateBakedModels().get((1 + openOffset)).getQuads(state, side, rand, extraData, layer);
        } else if (right) {
            return getTemplateBakedModels().get((2 + openOffset)).getQuads(state, side, rand, extraData, layer);
        } else {
            return getTemplateBakedModels().get((openOffset)).getQuads(state, side, rand, extraData, layer);
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


        List<Sprite> spriteList = getSpriteList(stack);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}