package com.unlikepaladin.pfm.blocks.models.basicTable.fabric;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class FabricBasicTableModel extends AbstractBakedModel implements FabricBakedModel {
    public FabricBasicTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof BasicTableBlock) {
            Direction.Axis dir = state.get(BasicTableBlock.AXIS);
            BasicTableBlock block = (BasicTableBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);

            ((FabricBakedModel)getBakedModels().get(modelParts.get(0))).emitBlockQuads(world, state, pos, randomSupplier, context);
            if (!north && !south && !east && !west) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(8))).emitBlockQuads(world, state, pos, randomSupplier, context);
                ((FabricBakedModel)getBakedModels().get(modelParts.get(7))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (dir == Direction.Axis.Z) {
                if (!north && !east)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !west)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !east && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(7))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !east && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(8))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && east && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(5))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east && west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(10))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && east && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(9))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !east && west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(6))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && east && west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(12))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && east && west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(11))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthEast) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(13))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthWest) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(14))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthWest) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(16))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthEast) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(15))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else {
                if (!north && !east)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !west)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !east)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!south && !west)  {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(9))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(10))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && south && !east) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(5))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && !south && !east) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(6))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (!north && !south && !east) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(7))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (!north && !south && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(8))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (north && south && !east) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(12))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (north && south && !west) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(11))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }

                if (cornerNorthEast) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(14))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthEast) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(13))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerNorthWest) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(16))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                if (cornerSouthWest) {
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(15))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}