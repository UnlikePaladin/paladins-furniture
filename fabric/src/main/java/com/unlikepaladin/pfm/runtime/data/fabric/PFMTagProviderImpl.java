package com.unlikepaladin.pfm.runtime.data.fabric;

import com.unlikepaladin.pfm.mixin.fabric.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.data.server.tag.AbstractTagProvider;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagBuilder;

public class PFMTagProviderImpl {
    public static <T> ValueLookupTagProvider.ObjectBuilder<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, t -> registry.getKey(t).get());
    }
}