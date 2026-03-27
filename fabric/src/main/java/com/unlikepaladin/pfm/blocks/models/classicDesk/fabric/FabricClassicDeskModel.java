package com.unlikepaladin.pfm.blocks.models.classicDesk.fabric;

import com.mojang.datafixers.util.Function4;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricClassicDeskModel extends PFMFabricBakedModel {
    public FabricClassicDeskModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock) {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            boolean isCabinet = state.getBlock() instanceof ClassicDeskCabinetBlock;
            Function4<BlockGetter, BlockState, BlockPos, BlockPos, Boolean> canConnect = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;
            Function<BlockState, Boolean> canConnectSimple = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;

            boolean north = canConnect.apply(world, state, pos.north(), pos);
            boolean east = canConnect.apply(world, state, pos.east(), pos);
            boolean west = canConnect.apply(world, state, pos.west(), pos);
            boolean south = canConnect.apply(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !canConnect.apply(world, state, pos.north().west(), pos)
                    && (world.getBlockState(pos.north()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.north()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.west()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerNorthEast = north && east && !canConnect.apply(world, state, pos.north().east(), pos)
                    && (world.getBlockState(pos.north()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.north()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.east()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerSouthEast = south && east && !canConnect.apply(world, state, pos.south().east(), pos)
                    && (world.getBlockState(pos.south()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.south()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.east()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerSouthWest = south && west && !canConnect.apply(world, state, pos.south().west(), pos)
                    && (world.getBlockState(pos.south()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.south()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.west()).getValue(HorizontalDirectionalBlock.FACING) == dir);

            boolean hasCornerNorthWest = north && west && canConnect.apply(world, state, pos.north().west(), pos);
            boolean hasCornerNorthEast = north && east && canConnect.apply(world, state, pos.north().east(), pos);
            boolean hasCornerSouthEast = south && east && canConnect.apply(world, state, pos.south().east(), pos);
            boolean hasCornerSouthWest = south && west && canConnect.apply(world, state, pos.south().west(), pos);
            int openOffset = isCabinet ? state.getValue(ClassicDeskCabinetBlock.OPEN) ? 1 : 0 : 0;


            BlockState rightState = world.getBlockState(pos.relative(dir.getClockWise()));
            boolean right = canConnectSimple.apply(rightState);
            boolean rightCabinet = right && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.relative(dir.getCounterClockWise()));
            boolean left = canConnectSimple.apply(leftState);
            boolean leftCabinet = left && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.relative(dir.getOpposite()));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(dir));

            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();


            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));

            boolean wasOuterCorner = false;
            if (isCabinet) {
                if (canConnectSimple.apply(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // inner corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.relative(neighborFacing)))) {
                        if (neighborFacing == dir.getClockWise()) {
                            ((FabricBakedModel) getTemplateBakedModels().get((34 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                        }
                        else {
                            ((FabricBakedModel) getTemplateBakedModels().get((30 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                        }
                    } else {
                        middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset);
                    }
                }
                else if (canConnectSimple.apply(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // outer corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.relative(neighborFacing.getOpposite())))) {
                        if (neighborFacing == dir.getClockWise()) {
                            ((FabricBakedModel) getTemplateBakedModels().get((32 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                        } else {
                            ((FabricBakedModel) getTemplateBakedModels().get((28 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                        }
                        wasOuterCorner = true;
                    } else {
                        middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset);
                    }
                }
                else {
                    middleDesk(world, state, pos, randomSupplier, context, leftCabinet, rightCabinet, openOffset);
                }
            }

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (north && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !east  && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (north && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerNorthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !(hasCornerNorthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // corners
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else if (dir == Direction.SOUTH){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                   ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (north && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !west && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (north && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !east && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerSouthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !(hasCornerSouthEast))
                  ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // corners
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else if (dir == Direction.EAST){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (west && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !(hasCornerSouthEast))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);


                // corners
                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !wasOuterCorner) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !south && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);


                if (west && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !north && !wasOuterCorner)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !(hasCornerSouthWest))
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);


                // corners
                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(17)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            context.popTransform();
        }
    }

    private void middleDesk(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean left, boolean right, int openOffset) {
        if (left && right) {
            ((FabricBakedModel) getTemplateBakedModels().get((22 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }  else if (left) {
            ((FabricBakedModel) getTemplateBakedModels().get((26 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (right) {
            ((FabricBakedModel) getTemplateBakedModels().get((24 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get(20+(openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (stack.getItem() instanceof BlockItem) {
            int offset = ((BlockItem) stack.getItem()).getBlock() instanceof ClassicDeskCabinetBlock ? 1 : 0;
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(stack));
            // base
            ((FabricBakedModel) getTemplateBakedModels().get(18+offset)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public ItemTransforms getTransforms() {
        return getTemplateBakedModels().get(19).getTransforms();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}