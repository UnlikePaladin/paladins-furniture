package com.unlikepaladin.pfm.blocks.models.bed.forge;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
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

public class ForgeBedModel extends AbstractBakedModel implements BedInterface {
    public ForgeBedModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(frame, settings, bakedModels);
        this.modelParts = MODEL_PARTS;
    }
    private final List<String> modelParts;

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BedPart part = state.get(BedBlock.PART);
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            boolean bunk = data.get(2);
            if (part == BedPart.HEAD) {
                quads.addAll(getBakedModels().get(modelParts.get(1)).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getBakedModels().get(modelParts.get(3)).getQuads(state, side, rand, extraData, renderType));
                if (!right){
                    quads.addAll(getBakedModels().get(modelParts.get(6)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left){
                    quads.addAll(getBakedModels().get(modelParts.get(7)).getQuads(state, side, rand, extraData, renderType));
                }
                if (bunk && !(state.getBlock() instanceof ClassicBedBlock)){
                    quads.addAll(getBakedModels().get(modelParts.get(10)).getQuads(state, side, rand, extraData, renderType));
                }
            } else {
                quads.addAll(getBakedModels().get(modelParts.get(0)).getQuads(state, side, rand, extraData, renderType));
                quads.addAll(getBakedModels().get(modelParts.get(2)).getQuads(state, side, rand, extraData, renderType));
                if (!right){
                    quads.addAll(getBakedModels().get(modelParts.get(4)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left){
                    quads.addAll(getBakedModels().get(modelParts.get(5)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!right && bunk){
                    quads.addAll(getBakedModels().get(modelParts.get(8)).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left && bunk){
                    quads.addAll(getBakedModels().get(modelParts.get(9)).getQuads(state, side, rand, extraData, renderType));
                }
            }
        }
        return quads;
    }


    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        ModelData.Builder builder = ModelData.builder();
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
            builder.with(CONNECTIONS, new ModelBitSetProperty(set));
        }
        return builder.build();
    }
}
