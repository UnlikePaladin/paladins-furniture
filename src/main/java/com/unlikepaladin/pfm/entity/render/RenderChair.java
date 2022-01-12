package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.entity.EntityChair;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.registry.EntityPaladinClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderChair extends MobEntityRenderer<EntityChair, ModelEmpty> {
    private static final Identifier EMPTY_TEXTURE = new Identifier("minecraft:textures/block/stone.png");

    public RenderChair(EntityRendererFactory.Context context) {
        super(context, new ModelEmpty(context.getPart(EntityPaladinClient.MODEL_CUBE_LAYER)), 0.5f);
    }


    public Identifier getTexture(EntityChair entity) {
        return EMPTY_TEXTURE;
    }


}
