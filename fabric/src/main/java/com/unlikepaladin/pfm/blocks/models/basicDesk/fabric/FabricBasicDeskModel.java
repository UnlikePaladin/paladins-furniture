package com.unlikepaladin.pfm.blocks.models.basicDesk.fabric;

import com.unlikepaladin.pfm.blocks.BasicDeskBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricBasicDeskModel extends PFMFabricBakedModel {
    public FabricBasicDeskModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof BasicDeskBlock block) {
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            pushTextureTransform(context, getSpriteList(state).get(0));
            getTemplateBakedModels().get(0).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(state).get(1));
            if (!north && !west) {
                getTemplateBakedModels().get(1).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!north && !east) {
                getTemplateBakedModels().get(2).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!south && !west) {
                getTemplateBakedModels().get(3).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!south && !east) {
                getTemplateBakedModels().get(4).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        pushTextureTransform(emitter, getSpriteList(blockState).get(0));
        getTemplateBakedModels().get(0).emitItemQuads(emitter, randomSupplier);
        emitter.popTransform();

        pushTextureTransform(emitter, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(1).emitItemQuads(emitter, randomSupplier);
        getTemplateBakedModels().get(2).emitItemQuads(emitter, randomSupplier);
        getTemplateBakedModels().get(3).emitItemQuads(emitter, randomSupplier);
        getTemplateBakedModels().get(4).emitItemQuads(emitter, randomSupplier);
        emitter.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}