package com.unlikepaladin.pfm.blocks.models.chairDinner.neoforge;

import com.unlikepaladin.pfm.blocks.DinnerChairBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.util.math.random.Random;

public class NeoForgeChairDinnerModel extends PFMNeoForgeBakedModel {
    public NeoForgeChairDinnerModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(BlockRenderView level, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {

        if (state == null || !(state.getBlock() instanceof DinnerChairBlock))
            return;

        int tucked = state.get(DinnerChairBlock.TUCKED) ? 1 : 0;
        List<Sprite> spriteList = getSpriteList(state);

        BlockModelPart part = getTemplateBakedModels().get(tucked);
        parts.add(getQuadsWithTexture(part, ModelHelper.getOakPlankLogSprites(), spriteList));
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
