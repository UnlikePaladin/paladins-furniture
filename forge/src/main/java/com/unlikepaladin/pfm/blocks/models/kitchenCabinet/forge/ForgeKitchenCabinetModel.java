package com.unlikepaladin.pfm.blocks.models.kitchenCabinet.forge;

import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeKitchenCabinetModel extends PFMForgeBakedModel {
    public ForgeKitchenCabinetModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_OPPOSITE = new ModelProperty<>();
    public static ModelProperty<BlockState> NEIGHBOR_FACING = new ModelProperty<>();
    @Override
    public void appendProperties(ModelDataMap.Builder builder) {
        super.appendProperties(builder);
        builder.withProperty(CONNECTIONS);
        builder.withProperty(NEIGHBOR_FACING);
        builder.withProperty(NEIGHBOR_OPPOSITE);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof KitchenCabinetBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
            Direction direction = state.get(KitchenCabinetBlock.FACING);
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
            Direction direction3;
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
            BitSet set = new BitSet();
            set.set(0, innerCorner);
            set.set(1, isNeighborStateOppositeFacingDifferentDirection);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            data.setData(NEIGHBOR_OPPOSITE, neighborStateOpposite);
            data.setData(NEIGHBOR_FACING, blockState);
            return data;
        }
        return tileData;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && state.getBlock() instanceof KitchenCabinetBlock && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            BitSet set = extraData.getData(CONNECTIONS).connections;
            KitchenCabinetBlock block = (KitchenCabinetBlock) state.getBlock();
            Direction direction = state.get(KitchenCabinetBlock.FACING);
            BlockState neighborStateOpposite = extraData.getData(NEIGHBOR_OPPOSITE);
            List<Sprite> spriteList = getSpriteList(state);

            Direction direction3 = null;
            if (neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
            }
            Direction direction2;
            BlockState blockState = extraData.getData(NEIGHBOR_FACING);
            int openOffset = state.get(KitchenWallDrawerBlock.OPEN) ? 5 : 0;
            boolean innerCorner = set.get(0);
            boolean isNeighborStateOppositeFacingDifferentDirection = set.get(1);
            if (block.isCabinet(blockState) && (direction2 = blockState.get(KitchenCabinetBlock.FACING)).getAxis() != state.get(KitchenCabinetBlock.FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                if (direction2 == direction.rotateYCounterclockwise()) {
                    return getQuadsWithTexture(getTemplateBakedModels().get(3 + openOffset).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                }
                else {
                    return getQuadsWithTexture(getTemplateBakedModels().get(4 + openOffset).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                }
            }
            else if (innerCorner) {
                if (direction3 == direction.rotateYCounterclockwise()) {
                    return getQuadsWithTexture(getTemplateBakedModels().get(2 + openOffset).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                } else {
                    return getQuadsWithTexture(getTemplateBakedModels().get(1 + openOffset).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
                }
            } else {
                return getQuadsWithTexture(getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData), ModelHelper.getOakPlankLogSprites(), spriteList);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(state);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(state, face, random), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}