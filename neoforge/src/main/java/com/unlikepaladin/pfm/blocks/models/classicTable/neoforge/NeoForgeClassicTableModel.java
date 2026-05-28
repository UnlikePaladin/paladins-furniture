package com.unlikepaladin.pfm.blocks.models.classicTable.neoforge;

import com.unlikepaladin.pfm.blocks.ClassicTableBlock;
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

public class NeoForgeClassicTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeClassicTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> parts) {
        super(settings, modelSettings, parts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof ClassicTableBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

        ClassicTableBlock block = (ClassicTableBlock) state.getBlock();
        boolean north = block.canConnect(world.getBlockState(pos.north()));
        boolean east = block.canConnect(world.getBlockState(pos.east()));
        boolean west = block.canConnect(world.getBlockState(pos.west()));
        boolean south = block.canConnect(world.getBlockState(pos.south()));

        baseQuads.add(getTemplateBakedModels().get(0));
        if (!north && !east) {
            secondaryQuads.add(getTemplateBakedModels().get(1));
        }
        if (!north && !west) {
            secondaryQuads.add(getTemplateBakedModels().get(2));
        }
        if (!south && !west) {
            secondaryQuads.add(getTemplateBakedModels().get(3));
        }
        if (!south && !east) {
            secondaryQuads.add(getTemplateBakedModels().get(4));
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

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(1).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(2).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(3).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(4).getQuads(face));

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}