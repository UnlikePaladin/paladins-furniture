package com.unlikepaladin.pfm.blocks.models.basicDesk.neoforge;

import com.unlikepaladin.pfm.blocks.BasicDeskBlock;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBasicDeskModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicDeskModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> templateBakedModels) {
        super(settings, modelSettings, templateBakedModels);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof BasicDeskBlock))
            return;

        List<BlockModelPart> baseQuads = new ArrayList<>();
        List<BlockModelPart> secondaryQuads = new ArrayList<>();

        BasicDeskBlock block = (BasicDeskBlock) state.getBlock();

        boolean north = block.canConnect(world.getBlockState(pos.north()));
        boolean east = block.canConnect(world.getBlockState(pos.east()));
        boolean west = block.canConnect(world.getBlockState(pos.west()));
        boolean south = block.canConnect(world.getBlockState(pos.south()));

        baseQuads.add(getTemplateBakedModels().get(0));
        if (!north && !west) {
            secondaryQuads.add((getTemplateBakedModels().get(1)));
        }
        if (!north && !east) {
            secondaryQuads.add((getTemplateBakedModels().get(2)));
        }
        if (!south && !west) {
            secondaryQuads.add((getTemplateBakedModels().get(3)));
        }
        if (!south && !east) {
            secondaryQuads.add((getTemplateBakedModels().get(4)));
        }
        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        List<BlockModelPart> transformedParts = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        transformedParts.addAll(getPartsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));

        parts.addAll(transformedParts);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(1).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(2).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(3).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(4).getQuads(face));
        // in between pieces


        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}