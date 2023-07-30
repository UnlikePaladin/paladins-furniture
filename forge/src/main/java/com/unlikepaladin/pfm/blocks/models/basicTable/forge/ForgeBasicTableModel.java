package com.unlikepaladin.pfm.blocks.models.basicTable.forge;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeBasicTableModel extends AbstractBakedModel {
    public ForgeBasicTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof BasicTableBlock) {
            BasicTableBlock block = (BasicTableBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);
            BitSet set = new BitSet();
            set.set(0, north);
            set.set(1, east);
            set.set(2, west);
            set.set(3, south);
            set.set(4, cornerNorthWest);
            set.set(5, cornerNorthEast);
            set.set(6, cornerSouthEast);
            set.set(7, cornerSouthWest);
            builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.getBlock() instanceof BasicTableBlock) {
            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            boolean cornerNorthWest = set.get(4);
            boolean cornerNorthEast = set.get(5);
            boolean cornerSouthEast = set.get(6);
            boolean cornerSouthWest = set.get(7);
            Direction.Axis dir = state.get(BasicTableBlock.AXIS);
            quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData, renderType));
            if (!north && !south && !east && !west) {
                quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData, renderType));
            }
            if (dir == Direction.Axis.Z) {
                if (!north && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && south && !east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData, renderType));
                }
                if (north && !south && !east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && !east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && !east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(12)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(11)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerNorthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(13)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerNorthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(14)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerSouthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(16)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerSouthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(15)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
                }
            } else {
                if (!north && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!south && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData, renderType));
                }
                if (north && !south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData, renderType));
                }
                if (north && !south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData, renderType));
                }

                if (!north && !south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!north && !south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData, renderType));
                }

                if (north && south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(12)).getQuads(state, side, rand, extraData, renderType));
                }
                if (north && south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(11)).getQuads(state, side, rand, extraData, renderType));
                }

                if (cornerNorthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(14)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerSouthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(13)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerNorthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(16)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));
                }
                if (cornerSouthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(15)).getQuads(state, side, rand, extraData, renderType));
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
                }
            }
        }
        return quads;
    }
}