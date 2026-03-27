package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.server.packs.ResourcePackFileNotFoundException;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
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
    public InputStream getRootResource(String fileName) throws IOException {
        if (PFMRuntimeResources.ready && fileName.equals("pack.png")) {
            return delegate.get().getRootResource(fileName);
        }
        return null;
    }

    @Override
    public InputStream getResource(PackType type, ResourceLocation id) throws IOException {
        if (PFMRuntimeResources.ready)
            return delegate.get().getResource(type, id);
        return null;
    }

    @Override
    public Collection<ResourceLocation> getResources(PackType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        if (PFMRuntimeResources.ready)
            return delegate.get().getResources(type, namespace, prefix, maxDepth, pathFilter);
        return new ArrayList<>();
    }

    @Override
    public boolean hasResource(PackType type, ResourceLocation id) {
        if (PFMRuntimeResources.ready)
            return delegate.get().hasResource(type, id);
        return false;
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
    public String getName() {
        return "PFM-Runtime-RP";
    }

    @Override
    public void close() {
        if (PFMRuntimeResources.ready)
            delegate.get().close();
    }
}
