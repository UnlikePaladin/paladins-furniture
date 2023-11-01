package com.unlikepaladin.pfm.blocks.models.classicNightstand.forge;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
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

public class ForgeClassicNightstandModel extends AbstractBakedModel {
    private final List<String> modelParts;

    public ForgeClassicNightstandModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> modelParts) {
        super(frame, settings, bakedModels);
        this.modelParts = modelParts;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            int openIndexOffset = state.get(ClassicNightstandBlock.OPEN) ? 4 : 0;
            if (left && right) {
                quads.addAll(getBakedModels().get(modelParts.get(openIndexOffset)).getQuads(state, side, rand, extraData, renderType));
            } else if (!left && right) {
                quads.addAll(getBakedModels().get(modelParts.get(1+openIndexOffset)).getQuads(state, side, rand, extraData, renderType));
            } else if (left) {
                quads.addAll(getBakedModels().get(modelParts.get(2+openIndexOffset)).getQuads(state, side, rand, extraData, renderType));
            } else {
                quads.addAll(getBakedModels().get(modelParts.get(3+openIndexOffset)).getQuads(state, side, rand, extraData, renderType));
            }
        }
        return quads;
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.get(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isStand(world, pos, dir.rotateYClockwise(), dir);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        }
        return builder.build();
    }
}
