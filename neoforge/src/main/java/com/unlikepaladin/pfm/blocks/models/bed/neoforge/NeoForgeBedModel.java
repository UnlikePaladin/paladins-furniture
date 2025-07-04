package com.unlikepaladin.pfm.blocks.models.bed.neoforge;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
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

public class NeoForgeBedModel extends PFMNeoForgeBakedModel implements BedInterface {
    public NeoForgeBedModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView blockView, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof BedBlock))
            return;

        List<BlockModelPart> quads = new ArrayList<>();

        Direction dir = state.get(BedBlock.FACING);
        boolean isClassic = state.getBlock().getTranslationKey().contains("classic");
        boolean left = isBed(blockView, pos, dir.rotateYCounterclockwise(), dir, state, isClassic);
        boolean right = isBed(blockView, pos, dir.rotateYClockwise(), dir, state, isClassic);
        boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
        int classicOffset = isClassic ? 12 : 0;
        BedPart part = state.get(BedBlock.PART);

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
        List<Sprite> spriteList = getSpriteList(state);
        parts.addAll(getTexturedParts(quads, ModelHelper.getOakBedSprites(), spriteList));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        int classicOffset = blockState.getBlock().getTranslationKey().contains("classic") ? 12 : 0;
        List<Sprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner((getTemplateBakedModels().get((classicOffset+11))).getQuads(face), ModelHelper.getOakBedSprites(), spriteList);
    }
}
