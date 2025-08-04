package com.unlikepaladin.pfm.data;

import net.minecraft.registry.RegistryKey;

public interface PFMTag<T> {
    PFMTag<T> addTags(T... values);
    PFMTag<T> addKey(RegistryKey<T>... keys);
}
