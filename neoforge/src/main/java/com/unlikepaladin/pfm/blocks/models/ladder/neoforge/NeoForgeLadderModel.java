package com.unlikepaladin.pfm.blocks.models.ladder.neoforge;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NeoForgeLadderModel extends PFMNeoForgeBakedModel {

    public NeoForgeLadderModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(BlockRenderView level, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {

        if (state == null || !(state.getBlock() instanceof SimpleBunkLadderBlock))
            return;

        int offset = state.get(SimpleBunkLadderBlock.UP) ? 1 : 0;
        Sprite sprite = getSpriteList(state).get(0);
        parts.add(getPartWithTexture(getTemplateBakedModels().get(offset), new SpriteData(sprite)));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        Sprite sprite = getSpriteList(blockState).get(0);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(face), new SpriteData(sprite));
    }
}
