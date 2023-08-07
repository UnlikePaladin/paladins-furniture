package com.unlikepaladin.pfm.data;

import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.RegistryKey;

public interface PFMTag<T> {
    PFMTag<T> add(T... values);
    PFMTag<T> addKey(RegistryKey<T>... keys);
}
