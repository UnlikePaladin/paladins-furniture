package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.forge;

import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeKitchenWallDrawerModel extends PFMForgeBakedModel {
    public ForgeKitchenWallDrawerModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }
    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof KitchenWallDrawerBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            KitchenWallDrawerBlock block = (KitchenWallDrawerBlock) state.getBlock();
            Direction direction = state.getValue(KitchenWallDrawerBlock.FACING);
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
            BitSet set = new BitSet();
            set.set(0, isNeighborStateOppositeFacingDifferentDirection);
            set.set(1, isNeighborStateFacingDifferentDirection);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).with(NEIGHBOR_FACING, neighborStateFacing).with(NEIGHBOR_OPPOSITE, neighborStateOpposite).build();
            return data;
        }
        return tileData;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof KitchenWallDrawerBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BitSet set = extraData.get(CONNECTIONS).connections;
            Direction direction = state.getValue(KitchenWallDrawerBlock.FACING);
            KitchenWallDrawerBlock block = (KitchenWallDrawerBlock) state.getBlock();
            boolean isNeighborStateOppositeFacingDifferentDirection =  set.get(0);
            boolean isNeighborStateFacingDifferentDirection = set.get(1);
            BlockState neighborStateFacing = extraData.get(NEIGHBOR_FACING);
            BlockState neighborStateOpposite = extraData.get(NEIGHBOR_OPPOSITE);
            int openOffset = state.getValue(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);

            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                    if (direction2 == direction.getCounterClockWise()) {
                        dest.add(getQuadsWithTexture(getTemplateBakedModels().get(3+openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
                    }
                    else {
                        dest.add(getQuadsWithTexture(getTemplateBakedModels().get(4+openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
                    }
                } else {
                    dest.add(getQuadsWithTexture(getTemplateBakedModels().get(openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
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
                        dest.add(getQuadsWithTexture(getTemplateBakedModels().get(2+openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
                    } else {
                        dest.add(getQuadsWithTexture(getTemplateBakedModels().get(1+openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
                    }
                } else {
                    dest.add(getQuadsWithTexture(getTemplateBakedModels().get(openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
                }
            }
            else {
                dest.add(getQuadsWithTexture(getTemplateBakedModels().get(openOffset), ModelHelper.getOakPlankLogSprites(), spriteList));
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}