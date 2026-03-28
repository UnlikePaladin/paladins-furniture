package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagsProvider.TagAppender.class)
public interface PFMAbstractTagProvider$ObjectBuilderMixin {
    @Invoker("<init>")
    static <T> TagsProvider.TagAppender<T> newTagProvider(TagBuilder builder, Registry<T> registry, String id) {
        throw new AssertionError();
    }
}
