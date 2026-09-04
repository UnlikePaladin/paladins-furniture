package com.unlikepaladin.pfm.blocks.models.ladder.forge;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeLadderModel extends PFMForgeBakedModel {

    public ForgeLadderModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> dest, ModelData extraData) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            int offset = state.getValue(SimpleBunkLadderBlock.UP) ? 1 : 0;
            TextureAtlasSprite sprite = getSpriteList(state).get(0);
            dest.add(getPartWithTexture(getTemplateBakedModels().get(offset), new SpriteData(sprite)));
        }
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        return super.getModelData(world, pos, state, ModelData.builder().build());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        TextureAtlasSprite sprite = getSpriteList(blockState).get(0);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(face), new SpriteData(sprite));
    }
}
