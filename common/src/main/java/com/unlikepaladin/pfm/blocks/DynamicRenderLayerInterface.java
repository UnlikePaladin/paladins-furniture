package com.unlikepaladin.pfm.blocks;

import net.minecraft.client.render.RenderLayer;

/**
 * This interface is used to change the RenderLayer blocks are on.
 * Mostly used when Shaders are enabled to prevent rendering issues.
 **/
public interface DynamicRenderLayerInterface {
    RenderLayer getCustomRenderLayer();
}
