package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PathPackRPWrapper implements PackResources {
    private final Supplier<PackResources> delegate;
    private final PackMetadataSection packResourceMetadata;

    public PathPackRPWrapper(Supplier<PackResources> delegate, PackMetadataSection packResourceMetadata) {
        this.delegate = delegate;
        this.packResourceMetadata = packResourceMetadata;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... segments) {
        if (PFMRuntimeResources.ready && Arrays.asList(segments).contains("pack.png")) {
            return delegate.get().getRootResource(segments);
        }
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
        if (PFMRuntimeResources.ready)
            return delegate.get().getResource(type, id);
        return () -> null;
    }

    @Override
    public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
        if (PFMRuntimeResources.ready)
            delegate.get().listResources(type, namespace, prefix, consumer);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        if (PFMRuntimeResources.ready)
            return delegate.get().getNamespaces(type);
        return Set.of("pfm");
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) throws IOException {
        if (metaReader.getMetadataSectionName().equals("pack")) {
            return (T) packResourceMetadata;
        }
        if (PFMRuntimeResources.ready)
            return delegate.get().getMetadataSection(metaReader);
        return null;
    }

    @Override
    public String packId() {
        return "PFM-Runtime-RP";
    }

    @Override
    public void close() {
        if (PFMRuntimeResources.ready)
            delegate.get().close();
    }
}
