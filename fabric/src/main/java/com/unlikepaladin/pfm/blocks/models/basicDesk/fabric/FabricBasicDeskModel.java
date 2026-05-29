package com.unlikepaladin.pfm.blocks.models.basicDesk.fabric;

import com.unlikepaladin.pfm.blocks.BasicDeskBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricBasicDeskModel extends PFMFabricBakedModel {
    public FabricBasicDeskModel(ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        super(settings, itemSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof BasicDeskBlock block) {
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            pushTextureTransform(context, getSpriteList(state).get(0));
            getTemplateBakedModels().get(0).emitQuads(context, cullTest);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(state).get(1));
            if (!north && !west) {
                getTemplateBakedModels().get(1).emitQuads(context, cullTest);
            }
            if (!north && !east) {
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
    public void emitItemQuads(QuadEmitter emitter, RandomSource randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;

        pushTextureTransform(emitter, getSpriteList(blockState).get(0));
        getTemplateBakedModels().get(0).emitQuads(emitter, anyPredicate);
        emitter.popTransform();

        pushTextureTransform(emitter, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(1).emitQuads(emitter, anyPredicate);
        getTemplateBakedModels().get(2).emitQuads(emitter, anyPredicate);
        getTemplateBakedModels().get(3).emitQuads(emitter, anyPredicate);
        getTemplateBakedModels().get(4).emitQuads(emitter, anyPredicate);
        emitter.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}