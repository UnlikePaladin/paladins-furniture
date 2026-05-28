package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class PathPackRPWrapper implements PackResources {
    private final Supplier<PackResources> delegate;
    private final PackMetadataSection packResourceMetadata;
    private final PackLocationInfo resourcePackInfo;

    public PathPackRPWrapper(Supplier<PackResources> delegate, PackMetadataSection packResourceMetadata, PackLocationInfo packInfo) {
        this.delegate = delegate;
        this.packResourceMetadata = packResourceMetadata;
        this.resourcePackInfo = packInfo;
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
    public <T> T getMetadataSection(MetadataSectionType<T> metaReader) throws IOException {
        if (metaReader.name().equals("pack")) {
            return (T) packResourceMetadata;
        }
        if (PFMRuntimeResources.ready)
            return delegate.get().getMetadataSection(metaReader);
        return null;
    }

    public String packId() {
        return "PFM-Runtime-RP";
    }

    @Override
    public PackLocationInfo location() {
        return resourcePackInfo;
    }

    @Override
    public void close() {
        if (PFMRuntimeResources.ready)
            delegate.get().close();
    }
}
