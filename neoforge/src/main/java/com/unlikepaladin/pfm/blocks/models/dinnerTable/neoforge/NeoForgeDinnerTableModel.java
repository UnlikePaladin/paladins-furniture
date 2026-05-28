package com.unlikepaladin.pfm.blocks.models.dinnerTable.neoforge;

import com.unlikepaladin.pfm.blocks.DinnerTableBlock;
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

public class NeoForgeDinnerTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeDinnerTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelList) {
        super(settings, modelSettings, modelList);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null ||!(state.getBlock() instanceof DinnerTableBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

        DinnerTableBlock block = (DinnerTableBlock) state.getBlock();
        Direction dir = state.getValue(DinnerTableBlock.FACING);
        boolean left = block.isTable(world, pos, dir.getCounterClockWise(), dir);
        boolean right = block.isTable(world, pos, dir.getClockWise(), dir);

        baseQuads.add(getTemplateBakedModels().get(0));
        if (!left) {
            int index = dir == Direction.NORTH || dir == Direction.WEST ? 1 : 2;
            secondaryQuads.add(getTemplateBakedModels().get(index));
        }
        if (!right) {
            int index = dir == Direction.NORTH || dir == Direction.WEST ? 2 : 1;
            secondaryQuads.add(getTemplateBakedModels().get(index));
        }
        if (!right && !left) {
            secondaryQuads.add(getTemplateBakedModels().get(3));
        }
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        List<BlockModelPart> quads = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getPartsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        parts.addAll(quads);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));
        // legs
        List<BakedQuad> secondaryQuads = new ArrayList<>(getTemplateBakedModels().get(3).getQuads(face));

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}