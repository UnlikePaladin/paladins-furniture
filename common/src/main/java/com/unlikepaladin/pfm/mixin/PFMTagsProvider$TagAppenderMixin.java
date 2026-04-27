package com.unlikepaladin.pfm.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagsProvider.TagAppender.class)
public interface PFMTagsProvider$TagAppenderMixin {
    @Invoker("<init>")
    static <T> TagsProvider.TagAppender<T> newTagProvider(Tag.Builder builder, Registry<T> registry, String source) {
        throw new AssertionError();
    }
}
