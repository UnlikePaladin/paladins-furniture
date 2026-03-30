package com.unlikepaladin.pfm.runtime.data.fabric;

import com.unlikepaladin.pfm.mixin.fabric.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;

public class PFMTagProviderImpl {
    public static <T> IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, t -> registry.getKey(t).get());
    }
}