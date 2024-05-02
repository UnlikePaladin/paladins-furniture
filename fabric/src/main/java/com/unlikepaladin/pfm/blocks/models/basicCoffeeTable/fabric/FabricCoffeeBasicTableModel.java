package com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.fabric;

import com.unlikepaladin.pfm.blocks.BasicCoffeeTableBlock;
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

public class FabricCoffeeBasicTableModel extends PFMFabricBakedModel {
    public FabricCoffeeBasicTableModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof BasicCoffeeTableBlock) {
            Direction.Axis dir = state.get(BasicCoffeeTableBlock.AXIS);
            BasicCoffeeTableBlock block = (BasicCoffeeTableBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);

            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();


            pushTextureTransform(context, spriteList.get(1));
            if (!north && !south && !east && !west) {
                ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (dir == Direction.Axis.Z) {
                if (!north && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !east && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !east && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && east && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east && west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && east && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !east && west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && east && west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && east && west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else {
                if (!north && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(9)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(10)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(5)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(6)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (!north && !south && !east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(7)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !south && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(8)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (north && south && !east) {
                    ((FabricBakedModel) getTemplateBakedModels().get(12)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && south && !west) {
                    ((FabricBakedModel) getTemplateBakedModels().get(11)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(14)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthEast) {
                    ((FabricBakedModel) getTemplateBakedModels().get(13)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(16)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthWest) {
                    ((FabricBakedModel) getTemplateBakedModels().get(15)).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (stack.getItem() instanceof BlockItem) {
            pushTextureTransform(context, getSpriteList(stack).get(0));
            // base
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(stack).get(1));
            // legs
            ((FabricBakedModel) getTemplateBakedModels().get(1)).emitItemQuads(stack, randomSupplier, context);
            ((FabricBakedModel) getTemplateBakedModels().get(2)).emitItemQuads(stack, randomSupplier, context);
            ((FabricBakedModel) getTemplateBakedModels().get(3)).emitItemQuads(stack, randomSupplier, context);
            ((FabricBakedModel) getTemplateBakedModels().get(4)).emitItemQuads(stack, randomSupplier, context);
            // in between pieces
            ((FabricBakedModel) getTemplateBakedModels().get(8)).emitItemQuads(stack, randomSupplier, context);
            ((FabricBakedModel) getTemplateBakedModels().get(7)).emitItemQuads(stack, randomSupplier, context);
            context.popTransform();
        }
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}