package com.unlikepaladin.pfm.blocks.models.kitchenDrawer.fabric;

import com.unlikepaladin.pfm.blocks.KitchenDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;
import java.util.function.Supplier;

public class FabricKitchenDrawerModel extends PFMFabricBakedModel {
    public FabricKitchenDrawerModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof KitchenDrawerBlock) {
            KitchenDrawerBlock block = (KitchenDrawerBlock) state.getBlock();
            Direction direction = state.getValue(KitchenDrawerBlock.FACING);
            boolean right = block.canConnect(world, pos, direction.getCounterClockWise());
            boolean left = block.canConnect(world, pos, direction.getClockWise());
            BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
            int openOffset = state.getValue(KitchenDrawerBlock.OPEN) ? 7 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);

            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                // outer corner
                if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                    if (direction2 == direction.getCounterClockWise()) {
                        ((FabricBakedModel) getTemplateBakedModels().get((5 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                    else {
                        ((FabricBakedModel) getTemplateBakedModels().get((6 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, left, right, openOffset);
                }
            }
            else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                }
                if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3)) {
                    // inner corner
                    if (direction3 == direction.getCounterClockWise()) {
                        ((FabricBakedModel) getTemplateBakedModels().get((4 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    } else {
                        ((FabricBakedModel) getTemplateBakedModels().get((3 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, left, right, openOffset);
                }
            }
            else {
                middleCounter(world, state, pos, randomSupplier, context, left, right, openOffset);
            }
            context.popTransform();
        }
    }

    private void middleCounter(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean left, boolean right, int openOffset) {
        if (left && right) {
            ((FabricBakedModel) getTemplateBakedModels().get((openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (left) {
            ((FabricBakedModel) getTemplateBakedModels().get((1 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (right) {
            ((FabricBakedModel) getTemplateBakedModels().get((2 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get((openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        ((FabricBakedModel) getTemplateBakedModels().get((0))).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}