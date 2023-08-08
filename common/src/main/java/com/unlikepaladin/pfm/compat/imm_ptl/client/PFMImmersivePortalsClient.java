package com.unlikepaladin.pfm.compat.imm_ptl.client;

import com.unlikepaladin.pfm.client.EntityRenderRegistry;
import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.imm_ptl.PFMImmersivePortals;

public class PFMImmersivePortalsClient implements PFMClientModCompatibility {
    private final PFMImmersivePortals parent;
    public PFMImmersivePortalsClient(PFMImmersivePortals parent) {
        this.parent = parent;
    }
    @Override
    public PFMModCompatibility getCompatiblity() {
        return parent;
    }

    @Override
    public void registerEntityRenderer() {
        //EntityRenderRegistry.registerEntityRender(PFMImmersivePortals.MIRROR, PortalEntityRenderer::new);
    }
}
