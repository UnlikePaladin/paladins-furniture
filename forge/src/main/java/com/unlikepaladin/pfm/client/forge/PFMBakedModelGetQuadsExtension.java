package com.unlikepaladin.pfm.client.forge;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.util.math.random.Random;

public interface PFMBakedModelGetQuadsExtension {
    List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random);

    Map<Pair<ItemStack, Direction>, List<BakedQuad>> cache = new HashMap<>();
    default List<BakedQuad> getQuadsCached(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        Pair<ItemStack, Direction> directionPair = new Pair<>(stack, face);
        if (cache.containsKey(directionPair))
            return cache.get(directionPair);

        List<BakedQuad> quads = getQuads(stack, state, face, random);
        cache.put(directionPair, quads);
        return quads;
    }
}
