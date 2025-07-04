package com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.neoforge;

import com.unlikepaladin.pfm.blocks.BasicCoffeeTableBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeCoffeeBasicTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeCoffeeBasicTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof BasicCoffeeTableBlock))
            return;
        List<BlockModelPart> baseParts = new ArrayList<>();
        List<BlockModelPart> secondarParts = new ArrayList<>();
        BasicCoffeeTableBlock block = (BasicCoffeeTableBlock) state.getBlock();
        boolean north = block.canConnect(world, state, pos.north(), pos);
        boolean east = block.canConnect(world, state, pos.east(), pos);
        boolean west = block.canConnect(world, state, pos.west(), pos);
        boolean south = block.canConnect(world, state, pos.south(), pos);
        boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
        boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
        boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
        boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);
        Direction.Axis dir = state.get(BasicCoffeeTableBlock.AXIS);

        baseParts.add(getTemplateBakedModels().get(0));
        if (!north && !south && !east && !west) {
            secondarParts.add(getTemplateBakedModels().get(8));
            secondarParts.add(getTemplateBakedModels().get(7));
        }
        if (dir == Direction.Axis.Z) {
            if (!north && !east)  {
                secondarParts.add(getTemplateBakedModels().get(1));
            }
            if (!north && !west)  {
                secondarParts.add(getTemplateBakedModels().get(2));
            }
            if (!south && !east)  {
                secondarParts.add(getTemplateBakedModels().get(3));
            }
            if (!south && !west)  {
                secondarParts.add(getTemplateBakedModels().get(4));
            }
            if (!north && south && !east && !west) {
                secondarParts.add(getTemplateBakedModels().get(7));
            }
            if (north && !south && !east && !west) {
                secondarParts.add(getTemplateBakedModels().get(8));
            }
            if (!north && east && !west) {
                secondarParts.add(getTemplateBakedModels().get(5));
            }
            if (!south && !east && west) {
                secondarParts.add(getTemplateBakedModels().get(10));
            }
            if (!south && east && !west) {
                secondarParts.add(getTemplateBakedModels().get(9));
            }
            if (!north && !east && west) {
                secondarParts.add(getTemplateBakedModels().get(6));
            }
            if (!north && east && west) {
                secondarParts.add(getTemplateBakedModels().get(12));
            }
            if (!south && east && west) {
                secondarParts.add(getTemplateBakedModels().get(11));
            }
            if (cornerNorthEast) {
                secondarParts.add(getTemplateBakedModels().get(13));
                secondarParts.add(getTemplateBakedModels().get(1));
            }
            if (cornerNorthWest) {
                secondarParts.add(getTemplateBakedModels().get(14));
                secondarParts.add(getTemplateBakedModels().get(2));
            }
            if (cornerSouthWest) {
                secondarParts.add(getTemplateBakedModels().get(16));
                secondarParts.add(getTemplateBakedModels().get(4));
            }
            if (cornerSouthEast) {
                secondarParts.add(getTemplateBakedModels().get(15));
                secondarParts.add(getTemplateBakedModels().get(3));
            }
        } else {
            if (!north && !east)  {
                secondarParts.add(getTemplateBakedModels().get(2));
            }
            if (!north && !west)  {
                secondarParts.add(getTemplateBakedModels().get(4));
            }
            if (!south && !east)  {
                secondarParts.add(getTemplateBakedModels().get(1));
            }
            if (!south && !west)  {
                secondarParts.add(getTemplateBakedModels().get(3));
            }
            if (!north && south && !west) {
                secondarParts.add(getTemplateBakedModels().get(9));
            }
            if (north && !south && !west) {
                secondarParts.add(getTemplateBakedModels().get(10));
            }
            if (!north && south && !east) {
                secondarParts.add(getTemplateBakedModels().get(5));
            }
            if (north && !south && !east) {
                secondarParts.add(getTemplateBakedModels().get(6));
            }

            if (!north && !south && !east) {
                secondarParts.add(getTemplateBakedModels().get(7));
            }
            if (!north && !south && !west) {
                secondarParts.add(getTemplateBakedModels().get(8));
            }

            if (north && south && !east) {
                secondarParts.add(getTemplateBakedModels().get(12));
            }
            if (north && south && !west) {
                secondarParts.add(getTemplateBakedModels().get(11));
            }

            if (cornerNorthEast) {
                secondarParts.add(getTemplateBakedModels().get(14));
                secondarParts.add(getTemplateBakedModels().get(2));
            }
            if (cornerSouthEast) {
                secondarParts.add(getTemplateBakedModels().get(13));
                secondarParts.add(getTemplateBakedModels().get(1));
            }
            if (cornerNorthWest) {
                secondarParts.add(getTemplateBakedModels().get(16));
                secondarParts.add(getTemplateBakedModels().get(4));
            }
            if (cornerSouthWest) {
                secondarParts.add(getTemplateBakedModels().get(15));
                secondarParts.add(getTemplateBakedModels().get(3));
            }
        }
        List<Sprite> spriteList = getSpriteList(state);
        List<BlockModelPart> quads = getPartsWithTexture(baseParts, new SpriteData(spriteList.get(0)));
        quads.addAll(getPartsWithTexture(secondarParts, new SpriteData(spriteList.get(1))));
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
        // in between pieces
        secondaryQuads.addAll(getTemplateBakedModels().get(8).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(7).getQuads(face));

        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}