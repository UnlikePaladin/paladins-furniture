package com.unlikepaladin.pfm.compat.imm_ptl.client;

import qouteall.imm_ptl.core.render.PortalEntityRenderer;
import com.unlikepaladin.pfm.compat.imm_ptl.PFMImmPtlRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PFMImmPtlRegistryClient {

    public static void register(){
        EntityRendererRegistry.register(PFMImmPtlRegistry.MIRROR, PortalEntityRenderer::new);
    }
}
