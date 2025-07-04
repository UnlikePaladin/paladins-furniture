package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.neoforge;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
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

public class NeoForgeKitchenCabinetModel extends PFMNeoForgeBakedModel {
    public NeoForgeKitchenCabinetModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {


        if (state == null || !(state.getBlock() instanceof KitchenCabinetBlock))
            return;

        KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
        Direction direction = state.get(KitchenCabinetBlock.FACING);
        BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
        Direction direction3 = null;
        Direction direction2;
        boolean innerCorner = block.isCabinet(neighborStateOpposite) && (direction3 = neighborStateOpposite.get(KitchenCabinetBlock.FACING)).getAxis() != state.get(KitchenCabinetBlock.FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        boolean isNeighborStateOppositeFacingDifferentDirection;
        if (blockState.contains(Properties.HORIZONTAL_FACING)) {
            direction2 = blockState.get(Properties.HORIZONTAL_FACING);
            isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(state, world, pos, direction2.getOpposite());
        } else {
            isNeighborStateOppositeFacingDifferentDirection = false;
        }

        List<Sprite> spriteList = getSpriteList(state);

        if (neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
            direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
        }
        int openOffset = state.get(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
        if (block.isCabinet(blockState) && (direction2 = blockState.get(KitchenCabinetBlock.FACING)).getAxis() != state.get(KitchenCabinetBlock.FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
            if (direction2 == direction.rotateYCounterclockwise()) {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(3 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
            else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(4 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        }
        else if (innerCorner) {
            if (direction3 == direction.rotateYCounterclockwise()) {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(2 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            } else {
                parts.add(getQuadsWithTexture(getTemplateBakedModels().get(1 + openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        } else {
            parts.add(getQuadsWithTexture(getTemplateBakedModels().get(openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}