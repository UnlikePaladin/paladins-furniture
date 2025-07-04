package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
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

public class NeoForgeKitchenCounterOvenModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenCounterOvenModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof KitchenCounterOvenBlock))
            return;

        boolean up = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.up()).getBlock());
        boolean down = KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.down()).getBlock());
        int openOffset = state.get(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
        List<BlockModelPart> ogParts = new ArrayList<>();
        List<Sprite> spriteList = getSpriteList(state);

        if (up || down) {
            ogParts.add(getTemplateBakedModels().get(1 + openOffset));
        } else {
            ogParts.add(getTemplateBakedModels().get(openOffset));
        }
        parts.addAll(getTexturedParts(ogParts, ModelHelper.getOakPlankLogSprites(), spriteList));
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
