package com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.neoforge;

import com.unlikepaladin.pfm.blocks.ClassicCoffeeTableBlock;
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
import net.minecraft.util.math.random.Random;

import java.util.*;

public class NeoForgeClassicCoffeeTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeClassicCoffeeTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> parts) {
        super(settings, modelSettings, parts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof ClassicCoffeeTableBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

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

        List<Sprite> spriteList = getSpriteList(state);
        List<BlockModelPart> quads = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getPartsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));

        parts.addAll(quads);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(1).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(2).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(3).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(4).getQuads(face));

        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}