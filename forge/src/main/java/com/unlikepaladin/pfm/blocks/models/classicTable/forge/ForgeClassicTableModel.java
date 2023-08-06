package com.unlikepaladin.pfm.blocks.models.classicTable.forge;

import com.unlikepaladin.pfm.blocks.ClassicTableBlock;
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

public class ForgeClassicTableModel extends AbstractBakedModel {
    public ForgeClassicTableModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;
    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof ClassicTableBlock) {
            ClassicTableBlock block = (ClassicTableBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            BitSet set = new BitSet();
            set.set(0, north);
            set.set(1, east);
            set.set(2, west);
            set.set(3, south);
            builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        }
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.getBlock() instanceof ClassicTableBlock) {
            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData, renderType));
            if (!north && !east) {
                quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
            }
            if (!north && !west) {
                quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
            }
            if (!south && !west) {
                quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
            }
            if (!south && !east) {
                quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));            }
        }
        return quads;
    }
}