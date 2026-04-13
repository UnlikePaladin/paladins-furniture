package com.unlikepaladin.pfm.blocks.models.bed.forge;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
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

public class ForgeBedModel extends PFMForgeBakedModel implements BedInterface {
    public ForgeBedModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BedPart part = state.getValue(BedBlock.PART);
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            boolean bunk = data.get(2);
            boolean isClassic = state.getBlock().getDescriptionId().contains("classic");
            int classicOffset = isClassic ? 12 : 0;
            if (part == BedPart.HEAD) {
                quads.addAll(getTemplateBakedModels().get(classicOffset+3).getQuads(state, side, rand, extraData, renderType));
                if (!right){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+6).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+7).getQuads(state, side, rand, extraData, renderType));
                }
                if (bunk && !isClassic){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+10).getQuads(state, side, rand, extraData, renderType));
                }
            } else {
                quads.addAll(getTemplateBakedModels().get(classicOffset+2).getQuads(state, side, rand, extraData, renderType));
                if (!right){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+4).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+5).getQuads(state, side, rand, extraData, renderType));
                }
                if (!right && bunk){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+8).getQuads(state, side, rand, extraData, renderType));
                }
                if (!left && bunk){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+9).getQuads(state, side, rand, extraData, renderType));
                }
            }
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(quads, ModelHelper.getOakBedSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        int classicOffset = stack.getItem().getDescriptionId().contains("classic") ? 12 : 0;
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        return getQuadsWithTexture((getTemplateBakedModels().get((classicOffset+11))).getQuads(state, face, random), ModelHelper.getOakBedSprites(), spriteList);
    }

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof SimpleBedBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(blockView, pos, state, data);

            Direction dir = state.getValue(BedBlock.FACING);
            boolean isClassic = state.getBlock().getDescriptionId().contains("classic");
            boolean left = isBed(blockView, pos, dir.getCounterClockWise(), dir, state, isClassic);
            boolean right = isBed(blockView, pos, dir.getClockWise(), dir, state, isClassic);
            boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            set.set(2, bunk);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }
}
