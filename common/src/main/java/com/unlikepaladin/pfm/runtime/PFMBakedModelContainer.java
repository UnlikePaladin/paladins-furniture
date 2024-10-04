package com.unlikepaladin.pfm.runtime;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PFMBakedModelContainer {
    public Map<ModelBakeSettings, BakedModel> getBakedModels() {
        return bakedModels;
    }

    final Map<ModelBakeSettings, BakedModel> bakedModels = new ConcurrentHashMap<>();

    final Map<ModelBakeSettings, List<BakedModel>> cachedModelParts = new ConcurrentHashMap<>();

    public Map<ModelBakeSettings, List<BakedModel>> getCachedModelParts() {
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
