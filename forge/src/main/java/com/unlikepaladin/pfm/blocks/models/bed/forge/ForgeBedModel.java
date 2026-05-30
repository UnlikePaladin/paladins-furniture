package com.unlikepaladin.pfm.blocks.models.bed.forge;

import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.bed.BedInterface;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    public ForgeBedModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData extraData, @Nullable ChunkSectionLayer renderType) {
        BlockState state = extraData.get(STATE);
        List<BlockModelPart> quads = new ArrayList<>();
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            BedPart part = state.getValue(BedBlock.PART);
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            boolean bunk = data.get(2);
            boolean isClassic = state.getBlock().getDescriptionId().contains("classic");
            int classicOffset = isClassic ? 12 : 0;
            if (part == BedPart.HEAD) {
                quads.add(getTemplateBakedModels().get(classicOffset+3));
                if (!right){
                    quads.add(getTemplateBakedModels().get(classicOffset+6));
                }
                if (!left){
                    quads.add(getTemplateBakedModels().get(classicOffset+7));
                }
                if (bunk && !isClassic){
                    quads.add(getTemplateBakedModels().get(classicOffset+10));
                }
            } else {
                quads.add(getTemplateBakedModels().get(classicOffset+2));
                if (!right){
                    quads.add(getTemplateBakedModels().get(classicOffset+4));
                }
                if (!left){
                    quads.add(getTemplateBakedModels().get(classicOffset+5));
                }
                if (!right && bunk){
                    quads.add(getTemplateBakedModels().get(classicOffset+8));
                }
                if (!left && bunk){
                    quads.add(getTemplateBakedModels().get(classicOffset+9));
                }
            }
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            dest.addAll(getTexturedParts(quads, ModelHelper.getOakBedSprites(), spriteList));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        int classicOffset = blockState.getBlock().getDescriptionId().contains("classic") ? 12 : 0;
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        return getQuadsWithTextureInner((getTemplateBakedModels().get((classicOffset+11))).getQuads(face), ModelHelper.getOakBedSprites(), spriteList);
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
}
