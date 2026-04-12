package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import java.util.function.Supplier;

public class FabricKitchenCabinetModel extends PFMFabricBakedModel {
    public FabricKitchenCabinetModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof KitchenCabinetBlock) {
            KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
            Direction direction = state.getValue(KitchenCabinetBlock.FACING);
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
            int openOffset = state.getValue(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);

            Direction direction3 = null;
            Direction direction2;
            boolean innerCorner = block.isCabinet(neighborStateOpposite) && (direction3 = neighborStateOpposite.getValue(KitchenCabinetBlock.FACING)).getAxis() != state.getValue(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3);
            BlockState blockState = world.getBlockState(pos.relative(direction));
            if (block.isCabinet(blockState) && (direction2 = blockState.getValue(KitchenCabinetBlock.FACING)).getAxis() != state.getValue(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    ((FabricBakedModel) getTemplateBakedModels().get((3 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
                else {
                    ((FabricBakedModel) getTemplateBakedModels().get((4 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            }
            else if (innerCorner) {
                if (direction3 == direction.getCounterClockWise()) {
                    ((FabricBakedModel) getTemplateBakedModels().get((2 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                } else {
                    ((FabricBakedModel) getTemplateBakedModels().get((1 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                }
            } else {
                ((FabricBakedModel) getTemplateBakedModels().get((openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
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