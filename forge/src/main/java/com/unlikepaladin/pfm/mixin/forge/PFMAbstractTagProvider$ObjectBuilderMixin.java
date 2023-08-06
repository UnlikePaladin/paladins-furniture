package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.data.server.tag.AbstractTagProvider;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(ValueLookupTagProvider.ObjectBuilder.class)
public interface PFMAbstractTagProvider$ObjectBuilderMixin {
    @Invoker("<init>")
    static <T> ValueLookupTagProvider.ObjectBuilder<T> newTagProvider(TagBuilder arg, Function<T, RegistryKey<T>> function, String modId) {
        throw new AssertionError();
    }
}
