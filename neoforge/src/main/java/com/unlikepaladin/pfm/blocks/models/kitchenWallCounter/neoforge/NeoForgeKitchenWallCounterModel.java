package com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenWallCounterBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class NeoForgeKitchenWallCounterModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenWallCounterModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof KitchenWallCounterBlock))
            return;

        KitchenWallCounterBlock block = (KitchenWallCounterBlock) state.getBlock();
        Direction direction = state.get(KitchenWallCounterBlock.FACING);
        BlockState neighborStateFacing = world.getBlockState(pos.offset(direction));
        BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
        boolean isNeighborStateOppositeFacingDifferentDirection;
        if (neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
            }
            isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction3);
        } else {
            isNeighborStateOppositeFacingDifferentDirection = false;
        }

        boolean isNeighborStateFacingDifferentDirection;
        if (neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
            isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
        } else {
            isNeighborStateFacingDifferentDirection = false;
        }

        List<Sprite> spriteList = getSpriteList(state);

        if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
            Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
            if (direction2.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(3), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
                else {
                    parts.add(getQuadsWithTexture(getTemplateBakedModels().get(4), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
            } else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(0), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        }
        else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
            Direction direction3;
            if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
            }
            else {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
            }
            if (direction3.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                if (direction3 == direction.rotateYCounterclockwise()) {
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
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}