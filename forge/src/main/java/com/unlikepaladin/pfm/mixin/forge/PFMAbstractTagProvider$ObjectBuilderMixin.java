package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(IntrinsicHolderTagsProvider.IntrinsicTagAppender.class)
public interface PFMAbstractTagProvider$ObjectBuilderMixin {
    @Invoker("<init>")
    static <T> IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> newTagProvider(TagBuilder builder, Function<T, ResourceKey<T>> registry, String id) {
        throw new AssertionError();
    }
}
