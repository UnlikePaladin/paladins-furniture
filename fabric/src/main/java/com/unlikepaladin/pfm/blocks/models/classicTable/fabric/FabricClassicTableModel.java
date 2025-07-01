package com.unlikepaladin.pfm.blocks.models.classicTable.fabric;

import com.unlikepaladin.pfm.blocks.ClassicTableBlock;
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

public class FabricClassicTableModel extends PFMFabricBakedModel {
    public FabricClassicTableModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ClassicTableBlock) {
            ClassicTableBlock block = (ClassicTableBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            pushTextureTransform(context, getSpriteList(state).get(0));
            getTemplateBakedModels().get(0).emitQuads(context, cullTest);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(state).get(1));
            if (!north && !east) {
                getTemplateBakedModels().get(1).emitQuads(context, cullTest);
            }
            if (!north && !west) {
                getTemplateBakedModels().get(2).emitQuads(context, cullTest);
            }
            if (!south && !west) {
                getTemplateBakedModels().get(3).emitQuads(context, cullTest);
            }
            if (!south && !east) {
                getTemplateBakedModels().get(4).emitQuads(context, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        pushTextureTransform(context, getSpriteList(blockState).get(0));
        getTemplateBakedModels().get(0).emitQuads(context, anyPredicate);
        context.popTransform();

        pushTextureTransform(context, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(1).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(2).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(3).emitQuads(context, anyPredicate);
        getTemplateBakedModels().get(4).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}