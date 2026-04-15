package com.unlikepaladin.pfm.runtime.data.neoforge;

import com.unlikepaladin.pfm.data.PFMTag;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

import java.util.function.Function;
import java.util.stream.Stream;

public class PFMTagProviderImpl {

    public static <T> PFMTag<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return new ObjectBuilder<>(builder, t -> registry.getResourceKey(t).get(), modID);
    }

    public static class ObjectBuilder<T> extends TagsProvider.TagAppender<T>  implements PFMTag<T> {
        private final Function<T, ResourceKey<T>> valueToKey;

        ObjectBuilder(TagBuilder arg, Function<T, ResourceKey<T>> function, String modId) {
            super(arg, modId);
            this.valueToKey = function;
        }

        public ObjectBuilder<T> addTag(TagKey<T> arg) {
            super.addTag(arg);
            return this;
        }

        public final ObjectBuilder<T> add(T value) {
            this.add(this.valueToKey.apply(value));
            return this;
        }

        @SafeVarargs
        public final ObjectBuilder<T> add(T... values) {
            Stream.of(values).map(this.valueToKey).forEach(this::add);
            return this;
        }

        @Override
        public PFMTag<T> addKey(ResourceKey<T>... keys) {
            for (ResourceKey<T> key : keys){
                super.add(key);
            }
            return this;
        }

        public final ResourceKey<T> getKey(T value) {
            return this.valueToKey.apply(value);
        }
    }
}
