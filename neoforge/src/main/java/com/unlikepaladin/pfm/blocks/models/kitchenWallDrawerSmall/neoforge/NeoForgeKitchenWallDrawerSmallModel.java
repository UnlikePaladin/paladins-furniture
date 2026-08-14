package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenWallDrawerSmallBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.util.RandomSource;

public class NeoForgeKitchenWallDrawerSmallModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenWallDrawerSmallModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof KitchenWallDrawerSmallBlock))
            return;

        int openOffset = state.getValue(KitchenWallDrawerSmallBlock.OPEN) ? 1 : 0;

        BlockStateModelPart originalQuads = getTemplateBakedModels().get(openOffset);
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        parts.add(getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
