package com.unlikepaladin.pfm.data;

import net.minecraft.resources.ResourceKey;

public interface PFMTag<T> {
    PFMTag<T> add(T... values);
    PFMTag<T> addKey(ResourceKey<T>... keys);
}
