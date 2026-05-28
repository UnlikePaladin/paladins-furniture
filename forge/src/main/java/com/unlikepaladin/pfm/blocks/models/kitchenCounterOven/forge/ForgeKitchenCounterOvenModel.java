package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.forge;

import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeKitchenCounterOvenModel extends PFMForgeBakedModel {
    public ForgeKitchenCounterOvenModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }


    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable RenderType renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof KitchenCounterOvenBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> quads = new ArrayList<>();
            BitSet data = extraData.get(CONNECTIONS).connections;
            int openOffset = state.getValue(KitchenCounterOvenBlock.OPEN) ? 2 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            if (data.get(0) || data.get(1)) {
                quads.add(getTemplateBakedModels().get(1 + openOffset));
            } else {
                quads.add(getTemplateBakedModels().get(openOffset));
            }
            dest.addAll(getTexturedParts(quads, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            BitSet set = new BitSet();
            set.set(0, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.above()).getBlock()));
            set.set(1, KitchenCounterOvenBlock.connectsVertical(world.getBlockState(pos.below()).getBlock()));
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner(getTemplateBakedModels().get(0).getQuads(face), ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
