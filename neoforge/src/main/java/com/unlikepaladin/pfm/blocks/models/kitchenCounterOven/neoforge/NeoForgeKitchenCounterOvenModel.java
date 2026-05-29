package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeKitchenCounterOvenModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenCounterOvenModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof KitchenCounterOvenBlock))
            return;

        boolean up = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.above()).getBlock());
        boolean down = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.below()).getBlock());
        int openOffset = state.getValue(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
        List<BlockModelPart> ogParts = new ArrayList<>();
        List<TextureAtlasSprite> spriteList = getSpriteList(state);

        if (up || down) {
            ogParts.add(getTemplateBakedModels().get(1 + openOffset));
        } else {
            ogParts.add(getTemplateBakedModels().get(openOffset));
        }
        parts.addAll(getTexturedParts(ogParts, ModelHelper.getOakPlankLogSprites(), spriteList));
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
