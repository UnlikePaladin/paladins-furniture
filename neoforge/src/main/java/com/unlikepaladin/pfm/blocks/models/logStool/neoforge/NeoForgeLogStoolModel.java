package com.unlikepaladin.pfm.blocks.models.logStool.neoforge;

import com.unlikepaladin.pfm.blocks.LogStoolBlock;
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

public class NeoForgeLogStoolModel extends PFMNeoForgeBakedModel {
    public NeoForgeLogStoolModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof LogStoolBlock))
            return;

        int tucked = state.getValue(LogStoolBlock.TUCKED) ? 1 : 0;
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        BlockStateModelPart quads = getTemplateBakedModels().get(tucked);
        parts.add(getQuadsWithTexture(quads, ModelHelper.getOakLogLogTopSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(quads, ModelHelper.getOakLogLogTopSprites(), spriteList);
    }
}
