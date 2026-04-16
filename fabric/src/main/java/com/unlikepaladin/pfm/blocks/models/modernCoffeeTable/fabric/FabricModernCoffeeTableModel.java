package com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.fabric;

import com.unlikepaladin.pfm.blocks.ModernCoffeeTableBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricModernCoffeeTableModel extends PFMFabricBakedModel {
    public FabricModernCoffeeTableModel(ModelState settings, List<BakedModel> modelList) {
        super(settings, modelList);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ModernCoffeeTableBlock) {
            ModernCoffeeTableBlock block = (ModernCoffeeTableBlock) state.getBlock();
            Direction.Axis dir = state.getValue(ModernCoffeeTableBlock.AXIS);
            boolean left = block.isTable(world, pos, dir, -1);
            boolean right = block.isTable(world, pos, dir, 1);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            getTemplateBakedModels().get((0)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            context.popTransform();

            pushTextureTransform(context, spriteList.get(1));
            if (left && right) {
                getTemplateBakedModels().get(4).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!left && right) {
                getTemplateBakedModels().get((1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!right && left) {
                getTemplateBakedModels().get((2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            if (!right && !left) {
                getTemplateBakedModels().get((3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        pushTextureTransform(context, getSpriteList(blockState).get(0));
        // base
        getTemplateBakedModels().get(0).emitItemQuads(context, randomSupplier);
        context.popTransform();

        pushTextureTransform(context, getSpriteList(blockState).get(1));
        // legs
        getTemplateBakedModels().get(3).emitItemQuads(context, randomSupplier);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}