package com.unlikepaladin.pfm.blocks.models.classicNightstand.neoforge;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
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

public class NeoForgeClassicNightstandModel extends PFMNeoForgeBakedModel {

    public NeoForgeClassicNightstandModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof ClassicNightstandBlock))
            return;

        List<BlockModelPart> originalQuads = new ArrayList<>();

        ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
        Direction dir = state.getValue(ClassicNightstandBlock.FACING);
        boolean left = block.isStand(world, pos, dir.getCounterClockWise(), dir);
        boolean right = block.isStand(world, pos, dir.getClockWise(), dir);

        int openIndexOffset = state.getValue(ClassicNightstandBlock.OPEN) ? 4 : 0;
        if (left && right) {
            originalQuads.add(getTemplateBakedModels().get(openIndexOffset));
        } else if (!left && right) {
            originalQuads.add(getTemplateBakedModels().get(1+openIndexOffset));
        } else if (left) {
            originalQuads.add(getTemplateBakedModels().get(2+openIndexOffset));
        } else {
            originalQuads.add(getTemplateBakedModels().get(3+openIndexOffset));
        }
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        parts.addAll(getTexturedParts(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(3).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
