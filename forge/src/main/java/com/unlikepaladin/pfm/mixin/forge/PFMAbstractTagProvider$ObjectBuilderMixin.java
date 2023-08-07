package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.data.server.tag.ValueLookupTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraftforge.common.extensions.IForgeIntrinsicHolderTagAppender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;
import java.util.stream.Stream;

public class PFMAbstractTagProvider$ObjectBuilderMixin {

    public static <T, V extends TagProvider.ProvidedTagBuilder<T>> V newTagProvider(TagBuilder arg, Function<T, RegistryKey<T>> function, String modId) {
        return null;
    }
}
