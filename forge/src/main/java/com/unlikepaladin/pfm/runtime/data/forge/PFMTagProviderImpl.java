package com.unlikepaladin.pfm.runtime.data.forge;

import com.unlikepaladin.pfm.data.PFMTag;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class PFMTagProviderImpl {
    public static <T> PFMTag<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        return new ObjectBuilder<T>(builder, t -> registry.getKey(t).get(), modID);
    }

    public static class ObjectBuilder<T> implements PFMTag<T>, ProvidedTagBuilder<RegistryKey<T>, T> {
        private final Function<T, RegistryKey<T>> valueToKey;
        private final TagBuilder tagBuilder;
        ObjectBuilder(TagBuilder arg, Function<T, RegistryKey<T>> function, String modId) {
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
        public PFMTag<T> addKey(RegistryKey<T>... keys) {
            for (RegistryKey<T> key : keys){
                tagBuilder.add(key.getValue());
            }
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> add(RegistryKey<T> value) {
            tagBuilder.add(value.getValue());
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> add(RegistryKey<T>... values) {
            return ProvidedTagBuilder.super.add(values);
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> add(Collection<RegistryKey<T>> values) {
            return ProvidedTagBuilder.super.add(values);
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> addOptional(RegistryKey<T> value) {
            tagBuilder.addOptional(value.getValue());
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> addTag(TagKey tag) {
            this.tagBuilder.addTag(tag.id());
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> addOptionalTag(TagKey tag) {
            this.tagBuilder.addOptionalTag(tag.id());
            return this;
        }

        public final RegistryKey<T> getKey(T value) {
            return this.valueToKey.apply(value);
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> remove(RegistryKey<T> resourceKey) {
            this.remove(resourceKey.getValue());
            return this;
        }
    }
}