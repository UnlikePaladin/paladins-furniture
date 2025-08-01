package com.unlikepaladin.pfm.blocks.models.classicNightstand.forge;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.ModelBitSetProperty;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.util.math.random.Random;

public class ForgeClassicNightstandModel extends PFMForgeBakedModel {

    public ForgeClassicNightstandModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData extraData, @Nullable RenderLayer renderType) {
        BlockState state = extraData.get(STATE);
        if (state != null && extraData.get(CONNECTIONS) != null && extraData.get(CONNECTIONS).connections != null) {
            List<BlockModelPart> originalQuads = new ArrayList<>();
            BitSet data = extraData.get(CONNECTIONS).connections;
            boolean left = data.get(0);
            boolean right = data.get(1);
            int openIndexOffset = state.get(ClassicNightstandBlock.OPEN) ? 4 : 0;
            if (left && right) {
                originalQuads.add(getTemplateBakedModels().get(openIndexOffset));
            } else if (!left && right) {
                originalQuads.add(getTemplateBakedModels().get(1+openIndexOffset));
            } else if (left) {
                originalQuads.add(getTemplateBakedModels().get(2+openIndexOffset));
            } else {
                originalQuads.add(getTemplateBakedModels().get(3+openIndexOffset));
            }
            List<Sprite> spriteList = getSpriteList(state);
            dest.addAll(getTexturedParts(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    public static ModelProperty<ModelBitSetProperty> CONNECTIONS = new ModelProperty<>();

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof ClassicNightstandBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            ClassicNightstandBlock block = (ClassicNightstandBlock) state.getBlock();
            Direction dir = state.get(ClassicNightstandBlock.FACING);
            boolean left = block.isStand(world, pos, dir.rotateYCounterclockwise(), dir);
            boolean right = block.isStand(world, pos, dir.rotateYClockwise(), dir);
            BitSet set = new BitSet();
            set.set(0, left);
            set.set(1, right);
            data = data.derive().with(CONNECTIONS, new ModelBitSetProperty(set)).build();
            return data;
        }
        return tileData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        List<Sprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> originalQuads = getTemplateBakedModels().get(3).getQuads(face);
        return getQuadsWithTextureInner(originalQuads, ModelHelper.getOakPlankLogSprites(), spriteList);
    }
}
