package com.unlikepaladin.pfm.compat.imm_ptl.client;

import com.unlikepaladin.pfm.client.EntityRenderRegistry;
import com.unlikepaladin.pfm.compat.imm_ptl.PFMImmPtlRegistry;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

public class PFMImmPtlRegistryClient {
    public static void register(){
        EntityRenderRegistry.registerEntityRender(PFMImmPtlRegistry.MIRROR, PortalEntityRenderer::new);
    }
}
