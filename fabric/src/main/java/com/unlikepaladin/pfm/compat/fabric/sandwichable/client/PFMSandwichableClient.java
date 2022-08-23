package com.unlikepaladin.pfm.compat.fabric.sandwichable.client;

import com.unlikepaladin.pfm.compat.fabric.sandwichable.PFMSandwichableRegistry;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.client.render.PFMToasterBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class PFMSandwichableClient {

    public static void register() {
        BlockEntityRendererRegistry.register(PFMSandwichableRegistry.IRON_TOASTER_BLOCKENTITY, PFMToasterBlockEntityRenderer::new);
    }
}
