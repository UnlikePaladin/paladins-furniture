package com.unlikepaladin.pfm.compat.imm_ptl.fabric.client;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.render.PortalEntityRenderer;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;

public class PFMMirrorEntityRenderer extends PortalEntityRenderer {

    public PFMMirrorEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
    }

    @Override
    public void render(Portal portal, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsRender())
            super.render(portal, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
}
