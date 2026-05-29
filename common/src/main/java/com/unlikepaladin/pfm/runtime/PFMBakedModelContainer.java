package com.unlikepaladin.pfm.runtime;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PFMBakedModelContainer {
    public Map<ModelState, BlockStateModel> getBakedModels() {
        return bakedModels;
    }

    final Map<ModelState, BlockStateModel> bakedModels = new ConcurrentHashMap<>();

    final Map<ModelState, List<BlockModelPart>> cachedModelParts = new ConcurrentHashMap<>();

    public Map<ModelState, List<BlockModelPart>> getCachedModelParts() {
        return cachedModelParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PFMBakedModelContainer that)) return false;
        return Objects.equals(bakedModels, that.bakedModels) && Objects.equals(cachedModelParts, that.cachedModelParts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bakedModels, cachedModelParts);
    }
}
