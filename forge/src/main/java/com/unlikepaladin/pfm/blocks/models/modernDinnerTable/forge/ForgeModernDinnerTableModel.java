package com.unlikepaladin.pfm.blocks.models.modernDinnerTable.forge;

import com.unlikepaladin.pfm.blocks.ModernDinnerTableBlock;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
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

import java.util.*;
import net.minecraft.util.RandomSource;

public class ForgeModernDinnerTableModel extends PFMForgeBakedModel {
    public ForgeModernDinnerTableModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelList) {
        super(settings, modelSettings, modelList);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof ModernDinnerTableBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            ModernDinnerTableBlock block = (ModernDinnerTableBlock) state.getBlock();
            Direction.Axis dir = state.getValue(ModernDinnerTableBlock.AXIS);
            boolean left = block.isTable(world, pos, dir, -1);
            boolean right = block.isTable(world, pos, dir, 1);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && state.getBlock() instanceof ModernDinnerTableBlock && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> baseQuads = new ArrayList<>();
            List<BlockModelPart> secondaryQuads = new ArrayList<>();

            BitSet set = extraData.get(CONNECTIONS).connections;
            boolean left = set.get(0);
            boolean right = set.get(1);
            baseQuads.add(getTemplateBakedModels().get(0));
            if (left && right) {
                secondaryQuads.add(getTemplateBakedModels().get(4));
            }
            if (!left && right) {
                secondaryQuads.add(getTemplateBakedModels().get(1));
            }
            if (!right && left) {
                secondaryQuads.add(getTemplateBakedModels().get(2));
            }
            if (!right && !left) {
                secondaryQuads.add(getTemplateBakedModels().get(3));
            }
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            List<BlockModelPart> quads = getPartsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
            quads.addAll(getPartsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
            dest.addAll(quads);
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));
        // legs
        List<BakedQuad> secondaryQuads = new ArrayList<>(getTemplateBakedModels().get(3).getQuads(face));

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}