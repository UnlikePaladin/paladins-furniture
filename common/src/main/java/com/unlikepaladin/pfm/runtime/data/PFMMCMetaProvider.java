package com.unlikepaladin.pfm.runtime.data;

import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.data.DataWriter;
import net.minecraft.resource.ResourceType;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class PFMMCMetaProvider extends PFMProvider {
    private final ResourceType packType;
    private final String description;

    public PFMMCMetaProvider(PFMGenerator parent, ResourceType packType, String description) {
        super(parent);
        this.packType = packType;
        this.description = description;
    }

    public CompletableFuture<?> run(ResourceType type, String description) {
        try(BufferedWriter writer = IOUtils.buffer(new FileWriter(new File(PFMRuntimeResources.createDirIfNeeded(getParent().getOutput()).toFile(), "pack.mcmeta")))) {
            writer.write("{\n");
            writer.write("  \"pack\":\n   {\n");
            writer.write("          \"pack_format\": ");
            writer.write(String.valueOf(SharedConstants.getGameVersion().getResourceVersion(type)));
            writer.write(",\n           \"description\" : \"" + description + "\"\n  }\n");
            writer.write("}");
        } catch (IOException e) {
            getParent().getLogger().error("Writer exception: " + e);
            e.printStackTrace();
        }
        return CompletableFuture.allOf();
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        run(packType, description);
        return CompletableFuture.allOf();
    }

    public String getName() {
        return "PFM Meta Provider";
    }
}
