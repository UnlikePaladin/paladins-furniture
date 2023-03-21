package com.unlikepaladin.pfm.blocks.models.bed;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BakedBedModel implements BakedModel{
    protected final ModelBakeSettings settings;
    private final Sprite frame;
    private final Map<String,BakedModel> bakedModels;
    public BakedBedModel(Sprite frame, ModelBakeSettings settings, Map<String, BakedModel> bakedModels) {
        this.frame = frame;
        this.settings = settings;
        this.bakedModels = bakedModels;
    }

    public Map<String, BakedModel> getBakedModels() {
        return bakedModels;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return frame;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    public boolean isBed(BlockRenderView world, BlockPos pos, Direction direction, Direction bedDirection, BlockState originalState, boolean isClassic)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if (isClassic) {
            if(state.getBlock().getClass().isAssignableFrom(ClassicBedBlock.class) && state.getBlock() instanceof ClassicBedBlock)
            {
                if (state.get(BedBlock.PART) == originalState.get(BedBlock.PART)) {
                    Direction sourceDirection = state.get(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
       }
        else {
            if(state.getBlock().getClass().isAssignableFrom(SimpleBedBlock.class) && state.getBlock() instanceof SimpleBedBlock)
            {
                if (state.get(BedBlock.PART) == originalState.get(BedBlock.PART)) {
                    Direction sourceDirection = state.get(BedBlock.FACING);
                    return sourceDirection.equals(bedDirection);
                }
            }
       }
        return false;
    }
}