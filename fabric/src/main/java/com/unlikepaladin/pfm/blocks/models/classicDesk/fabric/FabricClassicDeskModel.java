package com.unlikepaladin.pfm.blocks.models.classicDesk.fabric;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
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
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof ClassicDeskBlock) {
            Direction dir = state.get(ClassicDeskBlock.FACING);
            ClassicDeskBlock block = (ClassicDeskBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);

            boolean hasCornerNorthWest = north && west && block.canConnect(world, state, pos.north().west(), pos);
            boolean hasCornerNorthEast = north && east && block.canConnect(world, state, pos.north().east(), pos);
            boolean hasCornerSouthEast = south && east && block.canConnect(world, state, pos.south().east(), pos);
            boolean hasCornerSouthWest = south && west && block.canConnect(world, state, pos.south().west(), pos);


            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();


            pushTextureTransform(context, spriteList.get(1));

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (north && !east)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !east)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (north && !west)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !west)
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
                if (!east && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (north && !west)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !west)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (north && !east)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (south && !east)
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
                if (!east && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!south) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (west && !south)
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !south)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);

                if (west && !north)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !north)
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
                if (!east && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west && !north)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // east & west & north bits
                if (!south) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                // side connecting bits
                if (west && !south)
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !south)
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);


                if (west && !north)
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                if (east && !north)
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

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (stack.getItem() instanceof BlockItem) {
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(stack));
            // base
            ((FabricBakedModel) getTemplateBakedModels().get(18)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}