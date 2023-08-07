package com.unlikepaladin.pfm.runtime.data;

import com.unlikepaladin.pfm.runtime.PFMDataGen;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.resource.ResourceType;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class PFMMCMetaProvider implements DataProvider {
    public CompletableFuture<?> run(DataWriter dataWriter) {
        try(BufferedWriter writer = IOUtils.buffer(new FileWriter(new File(PFMRuntimeResources.createDirIfNeeded(PFMRuntimeResources.getResourceDirectory()).toFile(), "pack.mcmeta")))) {
            writer.write("{\n");
            writer.write("  \"pack\":\n   {\n");
            writer.write("          \"pack_format\": ");
            writer.write(String.valueOf(SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES)));
            writer.write(",\n           \"description\" : \"Paladin's Furniture Runtime Resources and Data\"\n  }\n");
            writer.write("}");
        } catch (IOException e) {
            PFMDataGen.LOGGER.error("Writer exception: " + e);
            e.printStackTrace();
        }
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "PFM Meta Provider";
    }
}
