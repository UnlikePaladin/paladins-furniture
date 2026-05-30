package com.unlikepaladin.pfm.runtime.data.forge;

import com.unlikepaladin.pfm.data.PFMTag;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class PFMTagProviderImpl {
    public static <T> PFMTag<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return new ObjectBuilder<T>(builder, t -> registry.getResourceKey(t).get(), modID);
    }

    public static class ObjectBuilder<T> implements PFMTag<T>, TagAppender<ResourceKey<T>, T> {
        private final Function<T, ResourceKey<T>> valueToKey;
        private final TagBuilder tagBuilder;
        ObjectBuilder(TagBuilder arg, Function<T, ResourceKey<T>> function, String modId) {
            super();
            this.valueToKey = function;
            this.tagBuilder = arg;
        }


        @SafeVarargs
        @Override
        public final PFMTag<T> addTags(T... values) {
            Stream.of(values).map(this.valueToKey).forEach(this::add);
            return this;
        }

        @Override
        public PFMTag<T> addKey(ResourceKey<T>... keys) {
            for (ResourceKey<T> key : keys){
                tagBuilder.addElement(key.identifier());
            }
            return this;
        }

        @Override
        public TagAppender<ResourceKey<T>, T> add(ResourceKey<T> value) {
            tagBuilder.addElement(value.identifier());
            return this;
        }

        @Override
        public TagAppender<ResourceKey<T>, T> add(ResourceKey<T>... values) {
            return TagAppender.super.add(values);
        }

        @Override
        public TagAppender<ResourceKey<T>, T> addAll(Collection<ResourceKey<T>> collection) {
            return TagAppender.super.addAll(collection);
        }

        @Override
        public TagAppender<ResourceKey<T>, T> addOptional(ResourceKey<T> value) {
            tagBuilder.addOptionalElement(value.identifier());
            return this;
        }

        @Override
        public TagAppender<ResourceKey<T>, T> addTag(TagKey tag) {
            this.tagBuilder.addTag(tag.location());
            return this;
        }

        @Override
        public TagAppender<ResourceKey<T>, T> addOptionalTag(TagKey tag) {
            this.tagBuilder.addOptionalTag(tag.location());
            return this;
        }

        public final ResourceKey<T> getKey(T value) {
            return this.valueToKey.apply(value);
        }

        @Override
        public TagAppender<ResourceKey<T>, T> remove(ResourceKey<T> resourceKey) {
            this.remove(resourceKey.identifier());
            return this;
        }
    }
}