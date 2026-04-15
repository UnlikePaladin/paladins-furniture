package com.unlikepaladin.pfm.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;

/**
 * This interface is used to change the RenderType blocks are on.
 * Mostly used when Shaders are enabled to prevent rendering issues.
 **/
public interface DynamicRenderLayerInterface {
    @Environment(EnvType.CLIENT)
    RenderType getCustomRenderLayer();
}
