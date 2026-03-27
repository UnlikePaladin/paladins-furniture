package com.unlikepaladin.pfm.blocks.models.classicNightstand.forge;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
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

public class ForgeClassicNightstandModel extends PFMForgeBakedModel {

    public ForgeClassicNightstandModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            List<BakedQuad> originalQuads = new ArrayList<>();
            BitSet data = extraData.getData(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            int openIndexOffset = state.getValue(ClassicNightstandBlock.OPEN) ? 4 : 0;
            if (left && right) {
                originalQuads.addAll(getTemplateBakedModels().get(openIndexOffset).getQuads(state, side, rand, extraData));
            } else if (!left && right) {
                originalQuads.addAll(getTemplateBakedModels().get(1+openIndexOffset).getQuads(state, side, rand, extraData));
            } else if (left) {
                originalQuads.addAll(getTemplateBakedModels().get(2+openIndexOffset).getQuads(state, side, rand, extraData));
            } else {
                originalQuads.addAll(getTemplateBakedModels().get(3+openIndexOffset).getQuads(state, side, rand, extraData));
            }
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @Override
    public void createBlockStateDefinition(ModelDataMap.Builder builder) {
        super.createBlockStateDefinition(builder);
        builder.withProperty(CONNECTIONS);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            createBlockStateDefinition(builder);

            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);

            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.getValue(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.getCounterClockWise(), dir);
            boolean right = block.isStand(world, pos, dir.getClockWise(), dir);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(3).getQuads(state, face, random);
        return getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
