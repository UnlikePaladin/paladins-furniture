package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.fabric;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricBasicDeskCabinetModel extends PFMFabricBakedModel {
    public FabricBasicDeskCabinetModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof BasicDeskCabinetBlock block) {
            Direction isFacing = state.getValue(BasicDeskCabinetBlock.FACING);

            BlockState rightState = world.getBlockState(pos.relative(isFacing.getCounterClockWise()));
            boolean right = block.canConnect(rightState) && rightState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.relative(isFacing.getClockWise()));
            boolean left = block.canConnect(leftState) && leftState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.relative(isFacing));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(isFacing.getOpposite()));
            int openOffset = state.getValue(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));
            if (block.canConnect(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // outer corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(world, pos, neighborFacing.getOpposite())) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        getTemplateBakedModels().get((4 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    }
                    else {
                        getTemplateBakedModels().get((5 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(world, pos, neighborFacing)) {
                    if (neighborFacing == isFacing.getCounterClockWise()) {
                        getTemplateBakedModels().get((6 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    } else {
                        getTemplateBakedModels().get((7 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else {
                middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
            }


            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            switch (isFacing) {
                case SOUTH:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 18, 19, 16, 17);
                        break;
                case NORTH:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 17, 16, 19, 18);
                        break;
                case EAST:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 19, 17, 18, 16);
                        break;
                default:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 16, 18, 17, 19);
                        break;
            }
            context.popTransform();
        }
    }

    private void legsDesk(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        if (!north && !east) {
            getTemplateBakedModels().get(northLeg).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
        if (!north && !west) {
            getTemplateBakedModels().get(southLeg).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
        if (!south && !west) {
            getTemplateBakedModels().get(eastLeg).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
        if (!south && !east) {
            getTemplateBakedModels().get(westLeg).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
    }


    private void middleDesk(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean left, boolean right, int openOffset) {
        if (left && right) {
            getTemplateBakedModels().get((3 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }  else if (left) {
            getTemplateBakedModels().get((1 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (right) {
            getTemplateBakedModels().get((2 + openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else {
            getTemplateBakedModels().get((openOffset)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        pushTextureTransform(emitter, ModelHelper.getOakPlankLogSprites(), getSpriteList(blockState));
        getTemplateBakedModels().get(0).emitItemQuads(emitter, randomSupplier);
        // legs
        getTemplateBakedModels().get(18).emitItemQuads(emitter, randomSupplier);

        getTemplateBakedModels().get(16).emitItemQuads(emitter, randomSupplier);

        getTemplateBakedModels().get(17).emitItemQuads(emitter, randomSupplier);

        getTemplateBakedModels().get(19).emitItemQuads(emitter, randomSupplier);
        emitter.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}