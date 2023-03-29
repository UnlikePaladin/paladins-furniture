package com.unlikepaladin.pfm.blocks.models.modernDinnerTable.forge;

import com.unlikepaladin.pfm.blocks.ModernDinnerTableBlock;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.BakedModernDinnerTableModel;
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

public class ForgeModernDinnerTableModel extends BakedModernDinnerTableModel {
    public ForgeModernDinnerTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    public static ModelProperty<TableConnections> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        if (state.getBlock() instanceof ModernDinnerTableBlock) {
            ModernDinnerTableBlock block = (ModernDinnerTableBlock) state.getBlock();
            Direction.Axis dir = state.get(ModernDinnerTableBlock.AXIS);
            boolean left = block.isTable(world, pos, dir, -1);
            boolean right = block.isTable(world, pos, dir, 1);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            builder.withInitial(CONNECTIONS, new TableConnections(set));
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.getBlock() instanceof ModernDinnerTableBlock) {
            BitSet set = extraData.getData(CONNECTIONS).connections;
            boolean left = set.get(0);
            boolean right = set.get(1);
            if (left && right) {
                quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData));
            }
            if (!left && right) {
                quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
            }
            if (!right && left) {
                quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
            }
            if (!right && !left) {
                quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
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