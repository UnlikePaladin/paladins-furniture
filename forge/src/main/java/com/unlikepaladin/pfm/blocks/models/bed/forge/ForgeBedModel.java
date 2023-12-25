package com.unlikepaladin.pfm.blocks.models.bed.forge;

import com.unlikepaladin.pfm.blocks.ClassicBedBlock;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ForgeBedModel extends PFMForgeBakedModel implements BedInterface {
    public ForgeBedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();
    @Override
    public void appendProperties(ModelDataMap.Builder builder) {
        super.appendProperties(builder);
        builder.withProperty(CONNECTIONS);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state != null && extraData.getData(CONNECTIONS) != null && extraData.getData(CONNECTIONS).connections != null) {
            BedPart part = state.get(BedBlock.PART);
            BitSet data = extraData.getData(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            boolean bunk = data.get(2);
            boolean isClassic = state.getBlock().getTranslationKey().contains("classic");
            int classicOffset = isClassic ? 12 : 0;
            if (part == BedPart.HEAD) {
                quads.addAll(getTemplateBakedModels().get(classicOffset+3).getQuads(state, side, rand, extraData));
                if (!right){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+6).getQuads(state, side, rand, extraData));
                }
                if (!left){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+7).getQuads(state, side, rand, extraData));
                }
                if (bunk && !isClassic){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+10).getQuads(state, side, rand, extraData));
                }
            } else {
                quads.addAll(getTemplateBakedModels().get(classicOffset+2).getQuads(state, side, rand, extraData));
                if (!right){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+4).getQuads(state, side, rand, extraData));
                }
                if (!left){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+5).getQuads(state, side, rand, extraData));
                }
                if (!right && bunk){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+8).getQuads(state, side, rand, extraData));
                }
                if (!left && bunk){
                    quads.addAll(getTemplateBakedModels().get(classicOffset+9).getQuads(state, side, rand, extraData));
                }
            }
            List<Sprite> spriteList = getSpriteList(state);
            return getQuadsWithTexture(quads, ModelHelper.getOakBedSprites(), spriteList);
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        int classicOffset = stack.getTranslationKey().contains("classic") ? 12 : 0;
        List<Sprite> spriteList = getSpriteList(stack);
        return getQuadsWithTexture((getTemplateBakedModels().get((classicOffset+11))).getQuads(state, face, random), ModelHelper.getOakBedSprites(), spriteList);
    }

    @NotNull
    @Override
    public IModelData getModelData(@NotNull BlockRenderView blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state.getBlock() instanceof SimpleBedBlock) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);

            IModelData data = builder.build();
            super.getModelData(blockView, pos, state, data);

            Direction dir = state.get(BedBlock.FACING);
            boolean isClassic = state.getBlock().getTranslationKey().contains("classic");
            boolean left = isBed(blockView, pos, dir.rotateYCounterclockwise(), dir, state, isClassic);
            boolean right = isBed(blockView, pos, dir.rotateYClockwise(), dir, state, isClassic);
            boolean bunk = isBed(blockView, pos, Direction.DOWN, dir, state, isClassic);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            set.set(2, bunk);
            data.setData(CONNECTIONS, new ModelBitSetProperty(set));
            return data;
        }
        return tileData;
    }
}
