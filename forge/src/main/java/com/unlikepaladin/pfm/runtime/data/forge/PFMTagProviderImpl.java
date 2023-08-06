package com.unlikepaladin.pfm.runtime.data.forge;

import com.unlikepaladin.pfm.mixin.forge.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.data.server.tag.AbstractTagProvider;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.Registry;

public class PFMTagProviderImpl {
    public static <T> ValueLookupTagProvider.ObjectBuilder<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, t -> registry.getKey(t).get(), modID);
    }
}
