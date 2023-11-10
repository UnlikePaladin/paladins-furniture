package com.unlikepaladin.pfm.fabric;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class PaladinFurnitureModUpdateCheckerImpl {

    public static URL getUpdateURL() throws MalformedURLException {
        return new URL("https://github.com/UnlikePaladin/Paladins-Furniture-Update-Index/releases/latest/download/updateIndexFabric.json");
    }
}
