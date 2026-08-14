package com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.neoforge;

import com.unlikepaladin.pfm.blocks.ClassicCoffeeTableBlock;
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
import net.minecraft.util.RandomSource;

import java.util.*;

public class NeoForgeClassicCoffeeTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeClassicCoffeeTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> parts) {
        super(settings, modelSettings, parts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof ClassicCoffeeTableBlock))
            return;

        List<BlockStateModelPart> baseQuads = new ArrayList<>();
        List<BlockStateModelPart> secondaryQuads = new ArrayList<>();

        ClassicCoffeeTableBlock block = (ClassicCoffeeTableBlock) state.getBlock();
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
        List<BlockStateModelPart> quads = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
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