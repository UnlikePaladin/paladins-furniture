package com.unlikepaladin.pfm.blocks.models.ladder.forge;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ForgeLadderModel extends PFMForgeBakedModel {

    public ForgeLadderModel(ModelState settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer layer) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            int offset = state.getValue(SimpleBunkLadderBlock.UP) ? 1 : 0;
            TextureAtlasSprite sprite = getSpriteList(state).get(0);
            return getQuadsWithTexture(getTemplateBakedModels().get(offset).getQuads(state, side, rand, extraData, layer), new SpriteData(sprite));
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        return super.getModelData(world, pos, state, ModelData.builder().build());
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        TextureAtlasSprite sprite = getSpriteList(stack).get(0);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(state, face, random), new SpriteData(sprite));
    }
}
