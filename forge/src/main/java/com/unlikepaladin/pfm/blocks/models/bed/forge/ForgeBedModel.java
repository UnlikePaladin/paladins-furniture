package com.unlikepaladin.pfm.blocks.models.bed.forge;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.bed.BakedBedModel;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
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

public class ForgeBedModel extends BakedBedModel {
    public ForgeBedModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;

    public static ModelProperty<BedConnections> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        BedPart part = state.get(BedBlock.PART);
        BitSet data = extraData.getData(CONNECTIONS).connections;
        boolean left = data.get(0);
        boolean right = data.get(1);
        boolean bunk = data.get(2);
        if (part == BedPart.HEAD) {
            quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData));
            quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData));
            if (!right){
                quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData));
            }
            if (!left){
                quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData));
            }
            if (bunk && !(state.getBlock() instanceof ClassicBedBlock)){
                quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData));
            }
        } else {
            quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData));
            quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData));
            if (!right){
                quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData));
            }
            if (!left){
                quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData));
            }
            if (!right && bunk){
                quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData));
            }
            if (!left && bunk){
                quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData));
            }
        }
        return quads;
    }


    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        if (state.getBlock() instanceof SimpleBedBlock) {
            Direction dir = state.get(BedBlock.FACING);
            boolean isClassic = state.getBlock().getTranslationKey().contains("classic");
            boolean left = isBed(blockView, pos, dir.rotateYCounterclockwise(), dir, state, isClassic);
            boolean right = isBed(blockView, pos, dir.rotateYClockwise(), dir, state, isClassic);
            boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            set.set(2, bunk);
            builder.withInitial(CONNECTIONS, new BedConnections(set));
        }
        return builder.build();
    }
}

class BedConnections implements Predicate<BedConnections>
{
    public BedConnections(BitSet connections) {
        this.connections = connections;
    }

    protected BitSet connections;
    @Override
    public boolean test(BedConnections mirrorDirections) {
        return connections.equals(mirrorDirections.connections);
    }
}
