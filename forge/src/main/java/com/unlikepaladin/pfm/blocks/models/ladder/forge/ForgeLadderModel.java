package com.unlikepaladin.pfm.blocks.models.ladder.forge;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeLadderModel extends PFMForgeBakedModel {

    public ForgeLadderModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

        @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            int offset = state.get(SimpleBunkLadderBlock.UP) ? 1 : 0;
            Sprite sprite = getSpriteList(state).get(0);
            dest.add(getPartWithTexture(getTemplateBakedModels().get(offset), new SpriteData(sprite)));
        }
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        return super.getModelData(world, pos, state, ModelData.builder().build());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        Sprite sprite = getSpriteList(blockState).get(0);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(face), new SpriteData(sprite));
    }
}
