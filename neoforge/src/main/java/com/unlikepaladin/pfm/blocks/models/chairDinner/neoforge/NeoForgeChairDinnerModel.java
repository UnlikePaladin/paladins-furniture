package com.unlikepaladin.pfm.blocks.models.chairDinner.neoforge;

import com.unlikepaladin.pfm.blocks.DinnerChairBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.util.RandomSource;

public class NeoForgeChairDinnerModel extends PFMNeoForgeBakedModel {
    public NeoForgeChairDinnerModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {

        if (state == null || !(state.getBlock() instanceof DinnerChairBlock))
            return;

        int tucked = state.getValue(DinnerChairBlock.TUCKED) ? 1 : 0;
        List<TextureAtlasSprite> spriteList = getSpriteList(state);

        BlockStateModelPart part = getTemplateBakedModels().get(tucked);
        parts.add(getQuadsWithTexture(part, ModelHelper.getOakPlankLogSprites(), spriteList));
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
