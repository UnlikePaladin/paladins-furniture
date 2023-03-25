package com.unlikepaladin.pfm.blocks.models.classicTable.fabric;

import com.unlikepaladin.pfm.blocks.ClassicTableBlock;
import com.unlikepaladin.pfm.blocks.models.classicTable.BakedClassicTableModel;
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

public class FabricClassicTableModel extends BakedClassicTableModel implements FabricBakedModel {
    public FabricClassicTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
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
        if (state.getBlock() instanceof ClassicTableBlock) {
            ClassicTableBlock block = (ClassicTableBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            ((FabricBakedModel)getBakedModels().get(modelParts.get(0))).emitBlockQuads(world, state, pos, randomSupplier, context);
            if (!north && !east) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(1))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!north && !west) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(2))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!south && !west) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(3))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!south && !east) {
                ((FabricBakedModel)getBakedModels().get(modelParts.get(4))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

    }
}