package com.unlikepaladin.pfm.blocks.models.bed.neoforge;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NeoForgeBedModel extends PFMNeoForgeBakedModel implements BedInterface {
    public NeoForgeBedModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof BedBlock))
            return;

        List<BlockModelPart> quads = new ArrayList<>();

        Direction dir = state.getValue(BedBlock.FACING);
        boolean isClassic = state.getBlock().getDescriptionId().contains("classic");
        boolean left = isBed(blockView, pos, dir.getCounterClockWise(), dir, state, isClassic);
        boolean right = isBed(blockView, pos, dir.getClockWise(), dir, state, isClassic);
        boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
        int classicOffset = isClassic ? 12 : 0;
        BedPart part = state.getValue(BedBlock.PART);

        if (part == BedPart.HEAD) {
            quads.add(getTemplateBakedModels().get(classicOffset+3));
            if (!right){
                quads.add(getTemplateBakedModels().get(classicOffset+6));
            }
            if (!left){
                quads.add(getTemplateBakedModels().get(classicOffset+7));
            }
            if (bunk && !isClassic){
                quads.add(getTemplateBakedModels().get(classicOffset+10));
            }
        } else {
            quads.add(getTemplateBakedModels().get(classicOffset+2));
            if (!right){
                quads.add(getTemplateBakedModels().get(classicOffset+4));
            }
            if (!left){
                quads.add(getTemplateBakedModels().get(classicOffset+5));
            }
            if (!right && bunk){
                quads.add(getTemplateBakedModels().get(classicOffset+8));
            }
            if (!left && bunk){
                quads.add(getTemplateBakedModels().get(classicOffset+9));
            }
        }
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        parts.addAll(getTexturedParts(quads, ModelHelper.getOakBedSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        int classicOffset = blockState.getBlock().getDescriptionId().contains("classic") ? 12 : 0;
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner((getTemplateBakedModels().get((classicOffset+11))).getQuads(face), ModelHelper.getOakBedSprites(), spriteList);
    }
}
