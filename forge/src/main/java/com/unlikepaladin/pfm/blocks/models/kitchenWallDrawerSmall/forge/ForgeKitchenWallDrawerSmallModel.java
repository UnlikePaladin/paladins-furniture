package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.forge;

import com.unlikepaladin.pfm.blocks.KitchenWallDrawerSmallBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
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

import java.util.List;
import net.minecraft.util.RandomSource;

public class ForgeKitchenWallDrawerSmallModel extends PFMForgeBakedModel {
    public ForgeKitchenWallDrawerSmallModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable RenderType renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(OPEN) != null) {
            int openOffset = extraData.get(OPEN) ? 1 : 0;
            BlockModelPart originalQuads = getTemplateBakedModels().get(openOffset);
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            dest.add(getQuadsWithTexture(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    public static ModelProperty<Boolean> OPEN = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof KitchenWallDrawerSmallBlock) {
            ModelData.Builder builder = ModelData.builder();
            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            data = data.derive().with(OPEN, state.getValue(KitchenWallDrawerSmallBlock.OPEN)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
