package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
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

public class NeoForgeKitchenCabinetModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenCabinetModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof KitchenCabinetBlock))
            return;

        KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
        Direction direction = state.getValue(KitchenCabinetBlock.FACING);
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
        Direction direction3 = null;
        Direction direction2;
        boolean innerCorner = block.isCabinet(neighborStateOpposite) && (direction3 = neighborStateOpposite.getValue(KitchenCabinetBlock.FACING)).getAxis() != state.getValue(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3);
        BlockState blockState = world.getBlockState(pos.relative(direction));
        boolean isNeighborStateOppositeFacingDifferentDirection;
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            direction2 = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
        } else {
            isNeighborStateOppositeFacingDifferentDirection = false;
        }

        List<TextureAtlasSprite> spriteList = getSpriteList(state);

        if (neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        int openOffset = state.getValue(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
        if (block.isCabinet(blockState) && (direction2 = blockState.getValue(KitchenCabinetBlock.FACING)).getAxis() != state.getValue(KitchenCabinetBlock.FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
            if (direction2 == direction.getCounterClockWise()) {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(3 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
            else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(4 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        }
        else if (innerCorner) {
            if (direction3 == direction.getCounterClockWise()) {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(2 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            } else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(1 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        } else {
            parts.add(getQuadsWithTexture(getTemplateBakedModels().get(openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}