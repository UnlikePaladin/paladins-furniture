package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity, EntityRenderState> {
    private static final ResourceLocation EMPTY_TEXTURE = ResourceLocation.parse("minecraft:textures/block/stone.png");
    public ChairEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
