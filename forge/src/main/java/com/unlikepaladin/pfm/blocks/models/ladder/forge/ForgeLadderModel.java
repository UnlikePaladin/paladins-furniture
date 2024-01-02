package com.unlikepaladin.pfm.blocks.models.ladder.forge;

import com.unlikepaladin.pfm.blocks.SimpleBunkLadderBlock;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForgeLadderModel extends PFMForgeBakedModel {

    public ForgeLadderModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null && state.getBlock() instanceof SimpleBunkLadderBlock) {
            int offset = state.get(SimpleBunkLadderBlock.UP) ? 1 : 0;
            Sprite sprite = getSpriteList(state).get(0);
            return getQuadsWithTexture(getTemplateBakedModels().get(offset).getQuads(state, side, rand, extraData), sprite);
        }
        return Collections.emptyList();
    }


    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        Sprite sprite = getSpriteList(stack).get(0);
        return getQuadsWithTexture(getTemplateBakedModels().get(0).getQuads(state, face, random), sprite);
    }
}
