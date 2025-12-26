package com.unlikepaladin.pfm.blocks.models.classicDesk.fabric;

import com.mojang.datafixers.util.Function4;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricClassicDeskModel extends PFMFabricBakedModel {
    public FabricClassicDeskModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock) {
            Direction dir = state.get(HorizontalFacingBlock.FACING);
            boolean isCabinet = state.getBlock() instanceof ClassicDeskCabinetBlock;
            Function4<BlockView, BlockState, BlockPos, BlockPos, Boolean> canConnect = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;
            Function<BlockState, Boolean> canConnectSimple = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;

            boolean north = canConnect.apply(world, state, pos.north(), pos);
            boolean east = canConnect.apply(world, state, pos.east(), pos);
            boolean west = canConnect.apply(world, state, pos.west(), pos);
            boolean south = canConnect.apply(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !canConnect.apply(world, state, pos.north().west(), pos)
                    && (world.getBlockState(pos.north()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.north()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.west()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerNorthEast = north && east && !canConnect.apply(world, state, pos.north().east(), pos)
                    && (world.getBlockState(pos.north()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.north()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.east()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerSouthEast = south && east && !canConnect.apply(world, state, pos.south().east(), pos)
                    && (world.getBlockState(pos.south()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.south()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.east()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerSouthWest = south && west && !canConnect.apply(world, state, pos.south().west(), pos)
                    && (world.getBlockState(pos.south()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.south()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.west()).get(HorizontalFacingBlock.FACING) == dir);

            boolean hasCornerNorthWest = north && west && canConnect.apply(world, state, pos.north().west(), pos);
            boolean hasCornerNorthEast = north && east && canConnect.apply(world, state, pos.north().east(), pos);
            boolean hasCornerSouthEast = south && east && canConnect.apply(world, state, pos.south().east(), pos);
            boolean hasCornerSouthWest = south && west && canConnect.apply(world, state, pos.south().west(), pos);
            int openOffset = isCabinet ? state.get(ClassicDeskCabinetBlock.OPEN) ? 1 : 0 : 0;


            BlockState rightState = world.getBlockState(pos.offset(dir.rotateYClockwise()));
            boolean right = canConnectSimple.apply(rightState);
            boolean rightCabinet = right && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.offset(dir.rotateYCounterclockwise()));
            boolean left = canConnectSimple.apply(leftState);
            boolean leftCabinet = left && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.offset(dir.getOpposite()));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(dir));

            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            context.popTransform();


            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));

            boolean wasOuterCorner = false;
            if (isCabinet) {
                if (canConnectSimple.apply(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                    // inner corner
                    if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.offset(neighborFacing)))) {
                        if (neighborFacing == dir.rotateYClockwise()) {
                            ((FabricBakedModel) getTemplateBakedModels().get((34 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                        }
                        else {
                            ((FabricBakedModel) getTemplateBakedModels().get((30 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                        }
                    } else {
                        middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset, cullTest);
                    }
                }
                else if (canConnectSimple.apply(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                    // outer corner
                    if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.offset(neighborFacing.getOpposite())))) {
                        if (neighborFacing == dir.rotateYClockwise()) {
                            ((FabricBakedModel) getTemplateBakedModels().get((32 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                        } else {
                            ((FabricBakedModel) getTemplateBakedModels().get((28 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                        }
                        wasOuterCorner = true;
                    } else {
                        middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset, cullTest);
                    }
                }
                else {
                    middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset, cullTest);
                }
            }

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // side connecting bits
                if (north && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !east  && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                if (north && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerNorthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !(hasCornerNorthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // corners
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
            } else if (dir == Direction.SOUTH){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                   ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // side connecting bits
                if (north && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                if (north && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerSouthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !(hasCornerSouthEast))
                  ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // corners
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
            } else if (dir == Direction.EAST){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                if (west && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !(hasCornerSouthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);


                // corners
                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
            } else {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);


                if (west && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (east && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                if (south && !(hasCornerSouthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);


                // corners
                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                }
            }
            context.popTransform();
        }
    }

    private void middleDesk(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, QuadEmitter context, boolean left, boolean right, int openOffset, Predicate<@Nullable  Direction> cullTest) {
        if (left && right) {
            ((FabricBakedModel) getTemplateBakedModels().get((22 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }  else if (left) {
            ((FabricBakedModel) getTemplateBakedModels().get((26 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (right) {
            ((FabricBakedModel) getTemplateBakedModels().get((24 + openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get(20+(openOffset))).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<Random> randomSupplier) {
        if (blockState == null) return;

        int offset = blockState.getBlock() instanceof ClassicDeskCabinetBlock ? 1 : 0;
        pushTextureTransform(emitter, ModelHelper.getOakPlankLogSprites(), getSpriteList(blockState));
        // base
        ((FabricBakedModel) getTemplateBakedModels().get(18+offset)).emitItemQuads(emitter, randomSupplier);
        emitter.popTransform();
    }

    @Override
    public ModelTransformation getTransformation() {
        return getTemplateBakedModels().get(19).getTransformation();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}