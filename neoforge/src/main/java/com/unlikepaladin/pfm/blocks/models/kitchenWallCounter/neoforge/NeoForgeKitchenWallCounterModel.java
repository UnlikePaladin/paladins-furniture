package com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenWallCounterBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class NeoForgeKitchenWallCounterModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenWallCounterModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof KitchenWallCounterBlock))
            return;

        KitchenWallCounterBlock block = (KitchenWallCounterBlock) state.getBlock();
        Direction direction = state.getValue(KitchenWallCounterBlock.FACING);
        BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
        boolean isNeighborStateOppositeFacingDifferentDirection;
        if (neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
            isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction3);
        } else {
            isNeighborStateOppositeFacingDifferentDirection = false;
        }

        boolean isNeighborStateFacingDifferentDirection;
        if (neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
        } else {
            isNeighborStateFacingDifferentDirection = false;
        }

        List<TextureAtlasSprite> spriteList = getSpriteList(state);

        if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                if (direction2 == direction.getCounterClockWise()) {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(3), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
                else {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(4), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
            } else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(0), ModelHelper.getOakPlankLogSprites(), spriteList));
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
            if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                if (direction3 == direction.getCounterClockWise()) {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(2), ModelHelper.getOakPlankLogSprites(), spriteList));
                } else {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(1), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
            } else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(0), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        }
        else {
            parts.add(getQuadsWithTexture(getTemplateBakedModels().get(0), ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}