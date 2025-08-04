package com.unlikepaladin.pfm.runtime.data.neoforge;

import com.unlikepaladin.pfm.data.PFMTag;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagKey;

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
        public ProvidedTagBuilder<RegistryKey<T>, T> addOptional(RegistryKey<T> value) {
            return null;
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
        public ProvidedTagBuilder<RegistryKey<T>, T> add(TagEntry arg) {
            tagBuilder.add(arg);
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> replace(boolean bl) {
            tagBuilder.replace(bl);
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> remove(RegistryKey<T> object) {
            tagBuilder.removeElement(object.getValue());
            return this;
        }

        @Override
        public ProvidedTagBuilder<RegistryKey<T>, T> remove(TagKey<T> arg) {
            tagBuilder.removeTag(arg.id());
            return this;
        }
    }
}