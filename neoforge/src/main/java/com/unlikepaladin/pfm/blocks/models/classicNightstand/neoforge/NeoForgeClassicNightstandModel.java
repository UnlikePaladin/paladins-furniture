package com.unlikepaladin.pfm.blocks.models.classicNightstand.neoforge;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
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

import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeClassicNightstandModel extends PFMNeoForgeBakedModel {

    public NeoForgeClassicNightstandModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof ClassicNightstandBlock))
            return;

        List<BlockModelPart> originalQuads = new ArrayList<>();

        ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
        Direction dir = state.get(ClassicNightstandBlock.FACING);
        boolean left = block.isStand(world, pos, dir.rotateYCounterclockwise(), dir);
        boolean right = block.isStand(world, pos, dir.rotateYClockwise(), dir);

        int openIndexOffset = state.get(ClassicNightstandBlock.OPEN) ? 4 : 0;
        if (left && right) {
            originalQuads.add(getTemplateBakedModels().get(openIndexOffset));
        } else if (!left && right) {
            originalQuads.add(getTemplateBakedModels().get(1+openIndexOffset));
        } else if (left) {
            originalQuads.add(getTemplateBakedModels().get(2+openIndexOffset));
        } else {
            originalQuads.add(getTemplateBakedModels().get(3+openIndexOffset));
        }
        List<Sprite> spriteList = getSpriteList(state);
        parts.addAll(getTexturedParts(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(3).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
