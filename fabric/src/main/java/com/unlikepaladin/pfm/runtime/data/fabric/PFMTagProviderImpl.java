package com.unlikepaladin.pfm.runtime.data.fabric;

import com.unlikepaladin.pfm.mixin.fabric.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.TagBuilder;
import net.minecraft.util.registry.Registry;

public class PFMTagProviderImpl {
    public static <T> AbstractTagProvider.ObjectBuilder<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, registry);
    }
}
