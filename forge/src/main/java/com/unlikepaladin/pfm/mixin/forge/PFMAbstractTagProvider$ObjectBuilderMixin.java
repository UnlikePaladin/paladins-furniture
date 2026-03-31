package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;
import java.util.stream.Stream;

public class PFMAbstractTagProvider$ObjectBuilderMixin {

    public static <T, V extends TagProvider.ProvidedTagBuilder<T>> V newTagProvider(TagBuilder arg, Function<T, RegistryKey<T>> function, String modId) {
        return null;
    }
}
