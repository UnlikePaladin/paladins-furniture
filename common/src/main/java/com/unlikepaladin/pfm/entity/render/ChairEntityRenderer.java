package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChairEntityRenderer extends MobRenderer<ChairEntity, ModelEmpty> {
    private static final ResourceLocation EMPTY_TEXTURE = ResourceLocation.parse("minecraft:textures/block/stone.png");
    public ChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelEmpty(context.bakeLayer(EntityRenderIDs.MODEL_CUBE_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChairEntity entity) {
        return EMPTY_TEXTURE;
    }


}
