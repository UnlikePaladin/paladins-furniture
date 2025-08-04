package com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.forge;

import com.unlikepaladin.pfm.blocks.BasicCoffeeTableBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeCoffeeBasicTableModel extends PFMForgeBakedModel {
    public ForgeCoffeeBasicTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof BasicCoffeeTableBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            BasicCoffeeTableBlock block = (BasicCoffeeTableBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);
            BitSet set = new BitSet();
            set.set(0, north);
            set.set(1, east);
            set.set(2, west);
            set.set(3, south);
            set.set(4, cornerNorthWest);
            set.set(5, cornerNorthEast);
            set.set(6, cornerSouthEast);
            set.set(7, cornerSouthWest);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public void collectParts(Random random, List<BlockModelPart> parts, ModelData extraData, @Nullable BlockRenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof BasicCoffeeTableBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> baseQuads = new ArrayList<>();
            List<BlockModelPart> secondaryQuads = new ArrayList<>();

            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean north = set.get(0);
            boolean east = set.get(1);
            boolean west = set.get(2);
            boolean south = set.get(3);
            boolean cornerNorthWest = set.get(4);
            boolean cornerNorthEast = set.get(5);
            boolean cornerSouthEast = set.get(6);
            boolean cornerSouthWest = set.get(7);
            Direction.Axis dir = state.get(BasicCoffeeTableBlock.AXIS);
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