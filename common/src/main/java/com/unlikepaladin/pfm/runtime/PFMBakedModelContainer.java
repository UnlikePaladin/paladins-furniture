package com.unlikepaladin.pfm.runtime;

import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PFMBakedModelContainer {
    public Map<ModelBakeSettings, BlockStateModel> getBakedModels() {
        return bakedModels;
    }

    final Map<ModelBakeSettings, BlockStateModel> bakedModels = new ConcurrentHashMap<>();

    final Map<ModelBakeSettings, List<BlockModelPart>> cachedModelParts = new ConcurrentHashMap<>();

    public Map<ModelBakeSettings, List<BlockModelPart>> getCachedModelParts() {
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
