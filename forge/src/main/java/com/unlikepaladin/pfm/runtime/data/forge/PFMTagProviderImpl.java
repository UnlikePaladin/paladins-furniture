package com.unlikepaladin.pfm.runtime.data.forge;

import com.unlikepaladin.pfm.mixin.forge.PFMAbstractTagProvider$ObjectBuilderMixin;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;

public class PFMTagProviderImpl {
    public static <T> TagsProvider.TagAppender<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, registry, modID);
    }
}
