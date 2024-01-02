package com.unlikepaladin.pfm.compat.imm_ptl.neoforge.client;

import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.imm_ptl.neoforge.PFMImmersivePortalsImpl;

public class PFMImmersivePortalsClient implements PFMClientModCompatibility {
    private final PFMImmersivePortalsImpl parent;
    public PFMImmersivePortalsClient(PFMImmersivePortalsImpl parent) {
        this.parent = parent;
    }
    @Override
    public PFMModCompatibility getCompatiblity() {
        return parent;
    }

    @Override
    public void registerEntityRenderer() {
        //EntityRenderRegistry.registerEntityRender(PFMImmersivePortalsImpl.MIRROR, PFMMirrorEntityRenderer::new);
    }
}
