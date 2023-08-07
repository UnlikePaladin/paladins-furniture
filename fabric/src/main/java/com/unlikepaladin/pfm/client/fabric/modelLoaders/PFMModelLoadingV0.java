package com.unlikepaladin.pfm.client.fabric.modelLoaders;

import com.unlikepaladin.pfm.client.fabric.PFMExtraModelProvider;
import com.unlikepaladin.pfm.client.fabric.PFMModelProvider;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class PFMModelLoadingV0 {
    public static void registerV0Plugin() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider(new PFMExtraModelProvider());
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new PFMModelProvider());
    }
}
