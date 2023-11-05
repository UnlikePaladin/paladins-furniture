package com.unlikepaladin.pfm.client;

import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PathPackRPWrapper implements ResourcePack {
    private final Supplier<ResourcePack> delegate;
    private final PackResourceMetadata packResourceMetadata;

    public PathPackRPWrapper(Supplier<ResourcePack> delegate, PackResourceMetadata packResourceMetadata) {
        this.delegate = delegate;
        this.packResourceMetadata = packResourceMetadata;
    }

    @Nullable
    @Override
    public InputSupplier<InputStream> openRoot(String... segments) {
        return null;
    }

    @Override
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return delegate.get().open(type, id);
    }

    @Override
    public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
        delegate.get().findResources(type, namespace, prefix, consumer);
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return delegate.get().getNamespaces(type);
    }

    @Nullable
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) {
        if (metaReader.getKey().equals("pack")) {
            return (T) packResourceMetadata;
        }
        return null;
    }

    @Override
    public String getName() {
        return "PFM-Runtime-RP";
    }

    @Override
    public void close() {
        delegate.get().close();
    }
}
