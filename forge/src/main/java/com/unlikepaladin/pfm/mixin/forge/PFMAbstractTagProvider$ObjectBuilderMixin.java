package com.unlikepaladin.pfm.mixin.forge;

import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.TagBuilder;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractTagProvider.ObjectBuilder.class)
public interface PFMAbstractTagProvider$ObjectBuilderMixin {
    @Invoker("<init>")
    static <T> AbstractTagProvider.ObjectBuilder<T> newTagProvider(TagBuilder builder, Registry<T> registry, String id) {
        throw new AssertionError();
    }
}
