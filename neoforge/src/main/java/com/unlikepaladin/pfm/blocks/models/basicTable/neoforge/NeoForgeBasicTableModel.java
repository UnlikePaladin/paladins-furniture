package com.unlikepaladin.pfm.blocks.models.basicTable.neoforge;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.blocks.models.neoforge.ModelBitSetProperty;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeBasicTableModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof BasicTableBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

        BasicTableBlock block = (BasicTableBlock) state.getBlock();
        boolean north = block.canConnect(world, state, pos.north(), pos);
        boolean east = block.canConnect(world, state, pos.east(), pos);
        boolean west = block.canConnect(world, state, pos.west(), pos);
        boolean south = block.canConnect(world, state, pos.south(), pos);
        boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
        boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
        boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
        boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);

        Direction.Axis dir = state.get(BasicTableBlock.AXIS);
        baseQuads.add(getTemplateBakedModels().get(0));
        if (!north && !south && !east && !west) {
            secondaryQuads.add(getTemplateBakedModels().get(8));
            secondaryQuads.add(getTemplateBakedModels().get(7));
        }
        if (dir == Direction.Axis.Z) {
            if (!north && !east)  {
                secondaryQuads.add(getTemplateBakedModels().get(1));
            }
            if (!north && !west)  {
                secondaryQuads.add(getTemplateBakedModels().get(2));
            }
            if (!south && !east)  {
                secondaryQuads.add(getTemplateBakedModels().get(3));
            }
            if (!south && !west)  {
                secondaryQuads.add(getTemplateBakedModels().get(4));
            }
            if (!north && south && !east && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(7));
            }
            if (north && !south && !east && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(8));
            }
            if (!north && east && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(5));
            }
            if (!south && !east && west) {
                secondaryQuads.add(getTemplateBakedModels().get(10));
            }
            if (!south && east && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(9));
            }
            if (!north && !east && west) {
                secondaryQuads.add(getTemplateBakedModels().get(6));
            }
            if (!north && east && west) {
                secondaryQuads.add(getTemplateBakedModels().get(12));
            }
            if (!south && east && west) {
                secondaryQuads.add(getTemplateBakedModels().get(11));
            }
            if (cornerNorthEast) {
                secondaryQuads.add(getTemplateBakedModels().get(13));
                secondaryQuads.add(getTemplateBakedModels().get(1));
            }
            if (cornerNorthWest) {
                secondaryQuads.add(getTemplateBakedModels().get(14));
                secondaryQuads.add(getTemplateBakedModels().get(2));
            }
            if (cornerSouthWest) {
                secondaryQuads.add(getTemplateBakedModels().get(16));
                secondaryQuads.add(getTemplateBakedModels().get(4));
            }
            if (cornerSouthEast) {
                secondaryQuads.add(getTemplateBakedModels().get(15));
                secondaryQuads.add(getTemplateBakedModels().get(3));
            }
        } else {
            if (!north && !east)  {
                secondaryQuads.add(getTemplateBakedModels().get(2));
            }
            if (!north && !west)  {
                secondaryQuads.add(getTemplateBakedModels().get(4));
            }
            if (!south && !east)  {
                secondaryQuads.add(getTemplateBakedModels().get(1));
            }
            if (!south && !west)  {
                secondaryQuads.add(getTemplateBakedModels().get(3));
            }
            if (!north && south && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(9));
            }
            if (north && !south && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(10));
            }
            if (!north && south && !east) {
                secondaryQuads.add(getTemplateBakedModels().get(5));
            }
            if (north && !south && !east) {
                secondaryQuads.add(getTemplateBakedModels().get(6));
            }

            if (!north && !south && !east) {
                secondaryQuads.add(getTemplateBakedModels().get(7));
            }
            if (!north && !south && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(8));
            }

            if (north && south && !east) {
                secondaryQuads.add(getTemplateBakedModels().get(12));
            }
            if (north && south && !west) {
                secondaryQuads.add(getTemplateBakedModels().get(11));
            }

            if (cornerNorthEast) {
                secondaryQuads.add(getTemplateBakedModels().get(14));
                secondaryQuads.add(getTemplateBakedModels().get(2));
            }
            if (cornerSouthEast) {
                secondaryQuads.add(getTemplateBakedModels().get(13));
                secondaryQuads.add(getTemplateBakedModels().get(1));
            }
            if (cornerNorthWest) {
                secondaryQuads.add(getTemplateBakedModels().get(16));
                secondaryQuads.add(getTemplateBakedModels().get(4));
            }
            if (cornerSouthWest) {
                secondaryQuads.add(getTemplateBakedModels().get(15));
                secondaryQuads.add(getTemplateBakedModels().get(3));
            }
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
        // in between pieces
        secondaryQuads.addAll(getTemplateBakedModels().get(8).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(7).getQuads(face));

        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}