package com.unlikepaladin.pfm.compat.imm_ptl.forge.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

public class PFMMirrorEntityRenderer extends PortalEntityRenderer {
    public PFMMirrorEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Portal portal, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsRender())
            super.render(portal, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
}
