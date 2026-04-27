package com.unlikepaladin.pfm.blocks.models.mirror.fabric;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FabricMirrorModel extends PFMFabricBakedModel {
    public FabricMirrorModel(TextureAtlasSprite frame, TextureAtlasSprite glassTex, TextureAtlasSprite reflectTex, ModelState settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        super(settings, new ArrayList<>(bakedModels.values()));
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
    }
    protected final TextureAtlasSprite glassTex;
    protected final TextureAtlasSprite reflectTex;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof MirrorBlock) {
            MirrorBlock block = (MirrorBlock) state.getBlock();
            Direction facing = state.getValue(MirrorBlock.FACING);
            boolean above = block.canConnect(blockView.getBlockState(pos.above()), state);
            boolean down = block.canConnect(blockView.getBlockState(pos.below()), state);
            boolean left = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise())), state);
            boolean right = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise())), state);

            boolean cornerLeftUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).above()), state);
            boolean cornerRightDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).below()), state);
            boolean cornerLeftDown = block.canConnect(blockView.getBlockState(pos.relative(facing.getClockWise()).below()), state);
            boolean cornerRightUp = block.canConnect(blockView.getBlockState(pos.relative(facing.getCounterClockWise()).above()), state);

            context.fallbackConsumer().accept(getTemplateBakedModels().get((0)));
            if (!down) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((2)));
            }
            if (!above) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((1)));
            }
            if (!right) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((3)));
            }
            if (!left) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((4)));
            }

            if (!cornerLeftDown) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((8)));
            }
            if (!cornerRightDown) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((7)));
            }
            if (!cornerLeftUp) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((6)));
            }
            if (!cornerRightUp) {
                context.fallbackConsumer().accept(getTemplateBakedModels().get((5)));
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(getTemplateBakedModels().get(0));
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getParticleIcon();
    }
}
