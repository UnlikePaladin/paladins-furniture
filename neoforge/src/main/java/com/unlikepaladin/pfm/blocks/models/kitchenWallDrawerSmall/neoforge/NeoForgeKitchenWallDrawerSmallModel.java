package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenWallDrawerSmallBlock;
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

public class NeoForgeKitchenWallDrawerSmallModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenWallDrawerSmallModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView level, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {

        if (state == null || !(state.getBlock() instanceof KitchenWallDrawerSmallBlock))
            return;

        int openOffset = state.get(KitchenWallDrawerSmallBlock.OPEN) ? 1 : 0;

        BlockModelPart originalQuads = getTemplateBakedModels().get(openOffset);
        List<Sprite> spriteList = getSpriteList(state);
        parts.add(getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
