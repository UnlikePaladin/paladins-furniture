package com.unlikepaladin.pfm.runtime.data.forge;

import com.unlikepaladin.pfm.mixin.forge.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagBuilder;

public class PFMTagProviderImpl {
    public static <T> IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, t -> registry.getResourceKey(t).get(), modID);
    }
}
