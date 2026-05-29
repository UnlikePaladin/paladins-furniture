package com.unlikepaladin.pfm.blocks.models.modernStool.forge;

import com.unlikepaladin.pfm.blocks.ModernStoolBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
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

import java.util.Collections;
import java.util.List;
import net.minecraft.util.RandomSource;

public class ForgeModernStoolModel extends PFMForgeBakedModel {
    public ForgeModernStoolModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    public static ModelProperty<Boolean> TUCKED = new ModelProperty<>();


    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof ModernStoolBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            data = data.derive().with(TUCKED, state.getValue(ModernStoolBlock.TUCKED)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(TUCKED) != null) {
            int tucked = Boolean.TRUE.equals(extraData.get(TUCKED)) ? 1 : 0;
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            BlockModelPart part = getTemplateBakedModels().get(tucked);
            dest.add(getQuadsWithTexture(part, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getTemplateBakedModels().get(0).getQuads(face);
        return getQuadsWithTextureInner(quads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
