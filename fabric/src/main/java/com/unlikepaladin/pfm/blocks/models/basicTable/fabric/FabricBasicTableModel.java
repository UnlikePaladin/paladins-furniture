package com.unlikepaladin.pfm.blocks.models.basicTable.fabric;

import com.unlikepaladin.pfm.blocks.BasicTableBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;

import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricBasicTableModel extends PFMFabricBakedModel {
    public FabricBasicTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof BasicTableBlock) {
            Direction.Axis dir = state.get(BasicTableBlock.AXIS);
            BasicTableBlock block = (BasicTableBlock) state.getBlock();
            boolean north = block.canConnect(world, state, pos.north(), pos);
            boolean east = block.canConnect(world, state, pos.east(), pos);
            boolean west = block.canConnect(world, state, pos.west(), pos);
            boolean south = block.canConnect(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !block.canConnect(world, state, pos.north().west(), pos);
            boolean cornerNorthEast = north && east && !block.canConnect(world, state, pos.north().east(), pos);
            boolean cornerSouthEast = south && east && !block.canConnect(world, state, pos.south().east(), pos);
            boolean cornerSouthWest = south && west && !block.canConnect(world, state, pos.south().west(), pos);

            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            getTemplateBakedModels().get(0).emitQuads(context, cullTest);
            context.popTransform();


            pushTextureTransform(context, spriteList.get(1));
            if (!north && !south && !east && !west) {
                getTemplateBakedModels().get(8).emitQuads(context, cullTest);
                getTemplateBakedModels().get(7).emitQuads(context, cullTest);
            }
            if (dir == Direction.Axis.Z) {
                if (!north && !east)  {
                    getTemplateBakedModels().get(1).emitQuads(context, cullTest);
                }
                if (!north && !west)  {
                    getTemplateBakedModels().get(2).emitQuads(context, cullTest);
                }
                if (!south && !east)  {
                    getTemplateBakedModels().get(3).emitQuads(context, cullTest);
                }
                if (!south && !west)  {
                    getTemplateBakedModels().get(4).emitQuads(context, cullTest);
                }
                if (!north && south && !east && !west) {
                    getTemplateBakedModels().get(7).emitQuads(context, cullTest);
                }
                if (north && !south && !east && !west) {
                    getTemplateBakedModels().get(8).emitQuads(context, cullTest);
                }
                if (!north && east && !west) {
                    getTemplateBakedModels().get(5).emitQuads(context, cullTest);
                }
                if (!south && !east && west) {
                    getTemplateBakedModels().get(10).emitQuads(context, cullTest);
                }
                if (!south && east && !west) {
                    getTemplateBakedModels().get(9).emitQuads(context, cullTest);
                }
                if (!north && !east && west) {
                    getTemplateBakedModels().get(6).emitQuads(context, cullTest);
                }
                if (!north && east && west) {
                    getTemplateBakedModels().get(12).emitQuads(context, cullTest);
                }
                if (!south && east && west) {
                    getTemplateBakedModels().get(11).emitQuads(context, cullTest);
                }
                if (cornerNorthEast) {
                    getTemplateBakedModels().get(13).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(1).emitQuads(context, cullTest);
                }
                if (cornerNorthWest) {
                    getTemplateBakedModels().get(14).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(2).emitQuads(context, cullTest);
                }
                if (cornerSouthWest) {
                    getTemplateBakedModels().get(16).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(4).emitQuads(context, cullTest);
                }
                if (cornerSouthEast) {
                    getTemplateBakedModels().get(15).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(3).emitQuads(context, cullTest);
                }
            } else {
                if (!north && !east)  {
                    getTemplateBakedModels().get(2).emitQuads(context, cullTest);
                }
                if (!north && !west)  {
                    getTemplateBakedModels().get(4).emitQuads(context, cullTest);
                }
                if (!south && !east)  {
                    getTemplateBakedModels().get(1).emitQuads(context, cullTest);
                }
                if (!south && !west)  {
                    getTemplateBakedModels().get(3).emitQuads(context, cullTest);
                }
                if (!north && south && !west) {
                    getTemplateBakedModels().get(9).emitQuads(context, cullTest);
                }
                if (north && !south && !west) {
                    getTemplateBakedModels().get(10).emitQuads(context, cullTest);
                }
                if (!north && south && !east) {
                    getTemplateBakedModels().get(5).emitQuads(context, cullTest);
                }
                if (north && !south && !east) {
                    getTemplateBakedModels().get(6).emitQuads(context, cullTest);
                }

                if (!north && !south && !east) {
                    getTemplateBakedModels().get(7).emitQuads(context, cullTest);
                }
                if (!north && !south && !west) {
                    getTemplateBakedModels().get(8).emitQuads(context, cullTest);
                }

                if (north && south && !east) {
                    getTemplateBakedModels().get(12).emitQuads(context, cullTest);
                }
                if (north && south && !west) {
                    getTemplateBakedModels().get(11).emitQuads(context, cullTest);
                }

                if (cornerNorthEast) {
                    getTemplateBakedModels().get(14).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(2).emitQuads(context, cullTest);
                }
                if (cornerSouthEast) {
                    getTemplateBakedModels().get(13).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(1).emitQuads(context, cullTest);
                }
                if (cornerNorthWest) {
                    getTemplateBakedModels().get(16).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(4).emitQuads(context, cullTest);
                }
                if (cornerSouthWest) {
                    getTemplateBakedModels().get(15).emitQuads(context, cullTest);
                    getTemplateBakedModels().get(3).emitQuads(context, cullTest);
                }
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        pushTextureTransform(context, getSpriteList(blockState).get(0));
        // base
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();

        pushTextureTransform(context, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(1).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(2).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(3).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(4).emitQuads(context, anyPredicate);
        // in between pieces
        getTemplateBakedModels().get(8).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(7).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}