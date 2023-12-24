package com.unlikepaladin.pfm.client.forge;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public interface PFMBakedModelGetQuadsExtension {
    List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random);
}
