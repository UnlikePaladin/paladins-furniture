package com.unlikepaladin.pfm.mixin;

import net.minecraft.block.Block;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractTagProvider.ObjectBuilder.class)
public interface PFMAbstractTagProvider$ObjectBuilderMixin {
    @Invoker("<init>")
    static <T> AbstractTagProvider.ObjectBuilder<T> newTagProvider(Tag.Builder builder, Registry<T> registry, String source) {
        throw new AssertionError();
    }
}
