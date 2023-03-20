package com.unlikepaladin.pfm.compat.forge.imm_ptl.client;

import com.unlikepaladin.pfm.client.EntityRenderRegistry;
import com.unlikepaladin.pfm.compat.forge.imm_ptl.PFMImmPtlRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

public class PFMImmPtlRegistryClient {
    public static void register(){
        EntityRenderRegistry.registerEntityRender(PFMImmPtlRegistry.MIRROR, PortalEntityRenderer::new);
    }
}
