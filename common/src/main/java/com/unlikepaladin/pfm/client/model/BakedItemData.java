package com.unlikepaladin.pfm.client.model;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public record BakedItemData(ItemQuads itemQuads, Supplier<Vector3fc[]> extents) {
    private static final Map<BlockStateModel, BakedItemData> FALLBACK_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    public static BakedItemData of(List<BakedQuad> quads) {
        Vector3fc[] extents = CuboidItemModelWrapper.computeExtents(quads);
        return new BakedItemData(ItemQuads.split(quads), () -> extents);
    }

    public static BakedItemData getFallbackItemData(BlockStateModel model, RandomSource random) {
        return FALLBACK_CACHE.computeIfAbsent(model, m -> {
            List<BlockStateModelPart> parts = new ArrayList<>();
            m.collectParts(random, parts);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockStateModelPart part : parts) {
                for (Direction direction : Direction.values()) {
                    quads.addAll(part.getQuads(direction));
                }
                quads.addAll(part.getQuads(null));
            }
            return of(quads);
        });
    }
}
