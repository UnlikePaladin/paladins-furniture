package com.unlikepaladin.pfm.blocks.models.modernDinnerTable.neoforge;

import com.unlikepaladin.pfm.blocks.ModernDinnerTableBlock;
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

public class NeoForgeModernDinnerTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeModernDinnerTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelList) {
        super(settings, modelSettings, modelList);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof ModernDinnerTableBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

        ModernDinnerTableBlock block = (ModernDinnerTableBlock) state.getBlock();
        Direction.Axis dir = state.get(ModernDinnerTableBlock.AXIS);
        boolean left = block.isTable(world, pos, dir, -1);
        boolean right = block.isTable(world, pos, dir, 1);

        baseQuads.add(getTemplateBakedModels().get(0));
        if (left && right) {
            secondaryQuads.add(getTemplateBakedModels().get(4));
        }
        if (!left && right) {
            secondaryQuads.add(getTemplateBakedModels().get(1));
        }
        if (!right && left) {
            secondaryQuads.add(getTemplateBakedModels().get(2));
        }
        if (!right && !left) {
            secondaryQuads.add(getTemplateBakedModels().get(3));
        }
        List<Sprite> spriteList = getSpriteList(state);
        List<BlockModelPart> quads = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getPartsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        parts.addAll(quads);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));
        // legs
        List<BakedQuad> secondaryQuads = new ArrayList<>(getTemplateBakedModels().get(3).getQuads(face));

        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}