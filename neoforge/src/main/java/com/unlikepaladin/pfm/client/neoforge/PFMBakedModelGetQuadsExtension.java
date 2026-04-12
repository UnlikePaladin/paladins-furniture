package com.unlikepaladin.pfm.client.neoforge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.RandomSource;

public interface PFMBakedModelGetQuadsExtension {
    List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random);

    List<BakedQuad> getQuadsCached(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random);
}
