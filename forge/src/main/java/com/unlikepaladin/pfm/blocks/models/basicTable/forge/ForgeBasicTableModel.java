package com.unlikepaladin.pfm.blocks.models.basicTable.forge;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.models.basicTable.BakedBasicTableModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

//TODO: remove state properties and add outline back to the actual table block
public class ForgeBasicTableModel extends BakedBasicTableModel {
    public ForgeBasicTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    public static ModelProperty<TableConnections> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        if (state.getBlock() instanceof BasicTableBlock) {
            Direction.Axis dir = state.get(BasicTableBlock.AXIS);
            BasicTableBlock block = (BasicTableBlock) state.getBlock();
            boolean north = false;
            boolean east = false;
            boolean west = false;
            boolean south = false;
            if (block.canConnect(world.getBlockState(pos.north())) && world.getBlockState(pos.north()).get(BasicTableBlock.AXIS) == dir) {
                north = block.canConnect(world.getBlockState(pos.north()));
            }
            if (block.canConnect(world.getBlockState(pos.east())) && world.getBlockState(pos.east()).get(BasicTableBlock.AXIS) == dir) {
                east =  block.canConnect(world.getBlockState(pos.east()));
            }

            if (block.canConnect(world.getBlockState(pos.west())) && world.getBlockState(pos.west()).get(BasicTableBlock.AXIS) == dir) {
                west =  block.canConnect(world.getBlockState(pos.west()));
            }
            if (block.canConnect(world.getBlockState(pos.south())) && world.getBlockState(pos.south()).get(BasicTableBlock.AXIS) == dir) {
                south =  block.canConnect(world.getBlockState(pos.south()));
            }
            boolean cornerNorthWest = north && west && !block.canConnect(world.getBlockState(pos.north().west()));
            boolean cornerNorthEast = north && east && !block.canConnect(world.getBlockState(pos.north().east()));
            boolean cornerSouthEast = south && east && !block.canConnect(world.getBlockState(pos.south().east()));
            boolean cornerSouthWest = south && west && !block.canConnect(world.getBlockState(pos.south().west()));

            BitSet set = new BitSet();
            set.set(0, north);
            set.set(1, east);
            set.set(2, west);
            set.set(3, south);
            set.set(4, cornerNorthWest);
            set.set(5, cornerNorthEast);
            set.set(6, cornerSouthEast);
            set.set(7, cornerSouthWest);
            builder.withInitial(CONNECTIONS, new TableConnections(set));
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.getBlock() instanceof BasicTableBlock) {
            BitSet set = extraData.getData(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            boolean cornerNorthWest = set.get(4);
            boolean cornerNorthEast = set.get(5);
            boolean cornerSouthEast = set.get(6);
            boolean cornerSouthWest = set.get(7);
            Direction.Axis dir = state.get(BasicTableBlock.AXIS);
            quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData));
            if (!north && !south && !east && !west) {
                quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData));
                quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData));
            }
            if (dir == Direction.Axis.Z) {
                if (!north && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
                }
                if (!north && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
                }
                if (!south && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
                }
                if (!south && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData));
                }
                if (!north && south && !east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData));
                }
                if (north && !south && !east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData));
                }
                if (!north && east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData));
                }
                if (!south && !east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData));
                }
                if (!south && east && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData));
                }
                if (!north && !east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData));
                }
                if (!north && east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(12)).getQuads(state, side, rand, extraData));
                }
                if (!south && east && west) {
                    quads.addAll(getBakedModels().get(modelParts.get(11)).getQuads(state, side, rand, extraData));
                }
                if (cornerNorthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(13)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
                }
                if (cornerNorthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(14)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
                }
                if (cornerSouthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(16)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData));
                }
                if (cornerSouthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(15)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
                }
            } else {
                if (!north && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
                }
                if (!north && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData));
                }
                if (!south && !east)  {
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
                }
                if (!south && !west)  {
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
                }
                if (!north && south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData));
                }
                if (north && !south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData));
                }
                if (!north && south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData));
                }
                if (north && !south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData));
                }

                if (!north && !south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData));
                }
                if (!north && !south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData));
                }

                if (north && south && !east) {
                    quads.addAll(getBakedModels().get(modelParts.get(12)).getQuads(state, side, rand, extraData));
                }
                if (north && south && !west) {
                    quads.addAll(getBakedModels().get(modelParts.get(11)).getQuads(state, side, rand, extraData));
                }

                if (cornerNorthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(14)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
                }
                if (cornerSouthEast) {
                    quads.addAll(getBakedModels().get(modelParts.get(13)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
                }
                if (cornerNorthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(16)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData));
                }
                if (cornerSouthWest) {
                    quads.addAll(getBakedModels().get(modelParts.get(15)).getQuads(state, side, rand, extraData));
                    quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
                }
            }
        }
        return quads;
    }
}
class TableConnections implements Predicate<TableConnections>
{
    public TableConnections(BitSet connections) {
        this.connections = connections;
    }

    protected BitSet connections;
    @Override
    public boolean test(TableConnections tableConnections) {
        return connections.equals(tableConnections.connections);
    }
}

