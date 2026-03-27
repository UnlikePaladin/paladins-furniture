package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.forge;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeKitchenCounterOvenModel extends PFMForgeBakedModel {
    public ForgeKitchenCounterOvenModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(CONNECTIONS);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && state.getBlock() instanceof KitchenCounterOvenBlock && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            List<BakedQuad> quads = new ArrayList<>();
            BitSet data = extraData.getData(CONNECTIONS).connections;
            int openOffset = state.getValue(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            if (data.get(0) || data.get(1)) {
                quads.addAll(getTemplateBakedModels().get(1 + openOffset).getQuads(state, side, rand, extraData));
            } else {
                quads.addAll(getTemplateBakedModels().get(openOffset).getQuads(state, side, rand, extraData));
            }
            return getQuadsWithTexture(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            BitSet set = new BitSet();
            set.set(0, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.above()).getBlock()));
            set.set(1, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.below()).getBlock()));
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(state, face, random), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
