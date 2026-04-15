package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.neoforge;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class NeoForgeBasicDeskCabinetModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicDeskCabinetModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof BasicDeskCabinetBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

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
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            data = data.derive().with(NEIGHBOR_FACING, neighborStateFacing).build();
            data = data.derive().with(NEIGHBOR_OPPOSITE, neighborStateOpposite).build();
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType renderLayer) {
        if (state != null && state.getBlock() instanceof BasicDeskCabinetBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BakedQuad> secondaryQuads = new ArrayList<>();
            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();

            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            Direction isFacing = state.getValue(BasicDeskCabinetBlock.FACING);

            switch (isFacing) {
                case SOUTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderLayer, north, south, east, west, 18, 19, 16, 17));
                    break;
                case NORTH:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderLayer, north, south, east, west, 17, 16, 19, 18));
                    break;
                case EAST:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderLayer, north, south, east, west, 19, 17, 18, 16));
                    break;
                default:
                    secondaryQuads.addAll(legsDesk(state, side, rand, extraData, renderLayer, north, south, east, west, 16, 18, 17, 19));
                    break;
            }

            boolean left = set.get(4);
            boolean right = set.get(5);
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(6);
            boolean isNeighborStateFacingDifferentDirection = set.get(7);
            BlockState neighborStateFacing = extraData.get(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.get(NEIGHBOR_OPPOSITE);
            int openOffset = state.getValue(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

            if (block.canConnect(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // outer corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((4 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                    }
                    else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((5 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderLayer, left, right, openOffset));
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        secondaryQuads.addAll(getTemplateBakedModels().get((6 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                    } else {
                        secondaryQuads.addAll(getTemplateBakedModels().get((7 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                    }
                } else {
                    secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderLayer, left, right, openOffset));
                }
            }
            else {
                secondaryQuads.addAll(middleDesk(state, side, rand, extraData, renderLayer, left, right, openOffset));
            }

            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(secondaryQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
       return Collections.emptyList();
    }

    private List<BakedQuad> legsDesk(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderLayer, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        List<BakedQuad> quads = new ArrayList<>();
        if (!north && !east) {
            quads.addAll(getTemplateBakedModels().get(northLeg).getQuads(state, side, rand, extraData, renderLayer));
        }
        if (!north && !west) {
            quads.addAll(getTemplateBakedModels().get(southLeg).getQuads(state, side, rand, extraData, renderLayer));
        }
        if (!south && !west) {
            quads.addAll(getTemplateBakedModels().get(eastLeg).getQuads(state, side, rand, extraData, renderLayer));
        }
        if (!south && !east) {
            quads.addAll(getTemplateBakedModels().get(westLeg).getQuads(state, side, rand, extraData, renderLayer));
        }
        return quads;
    }


    private List<BakedQuad> middleDesk(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderLayer, boolean left, boolean right, int openOffset) {
        if (left && right) {
            return getTemplateBakedModels().get((3 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        }  else if (left) {
            return getTemplateBakedModels().get((1 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        } else if (right) {
            return getTemplateBakedModels().get((2 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        } else {
            return getTemplateBakedModels().get((openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(null, face, random));

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(18).getQuads(null, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(16).getQuads(null, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(17).getQuads(null, face, random));
        secondaryQuads.addAll(getTemplateBakedModels().get(19).getQuads(null, face, random));
        // in between pieces


        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}