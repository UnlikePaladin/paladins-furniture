package com.unlikepaladin.pfm.client.fabric.modelLoaders;

import com.unlikepaladin.pfm.client.fabric.PFMModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class PFMModelLoadingV1 {
    public static void registerV1Plugin() {
        ModelLoadingPlugin.register(new PFMModelLoadingPlugin());
    }
}
