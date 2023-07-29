package com.unlikepaladin.pfm.runtime.data;

import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.runtime.PFMDataGen;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PFMMCMetaProvider {
    public void run() {
        try(BufferedWriter writer = IOUtils.buffer(new FileWriter(new File(PFMRuntimeResources.createDirIfNeeded(PFMRuntimeResources.getResourceDirectory()).toFile(), "pack.mcmeta")))) {
            writer.write("{\n");
            writer.write("  \"pack\":\n   {\n");
            writer.write("          \"pack_format\": ");
            writer.write(String.valueOf(SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE)));
            writer.write(",\n           \"description\" : \"Paladin's Furniture Runtime Resources and Data\"\n  }\n");
            writer.write("}");
        } catch (IOException e) {
            PFMDataGen.LOGGER.error("Writer exception: " + e);
            e.printStackTrace();
        }
    }
}
