package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricKitchenCabinetModel extends PFMFabricBakedModel {
    public FabricKitchenCabinetModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
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
                    getTemplateBakedModels().get((3 + openOffset)).emitQuads(context, cullTest);
                }
                else {
                    getTemplateBakedModels().get((4 + openOffset)).emitQuads(context, cullTest);
                }
            }
            else if (innerCorner) {
                if (direction3 == direction.getCounterClockWise()) {
                    getTemplateBakedModels().get((2 + openOffset)).emitQuads(context, cullTest);
                } else {
                    getTemplateBakedModels().get((1 + openOffset)).emitQuads(context, cullTest);
                }
            } else {
                getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((0)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}