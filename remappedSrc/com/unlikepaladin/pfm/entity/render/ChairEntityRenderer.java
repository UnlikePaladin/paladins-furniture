package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.client.EntityPaladinClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ChairEntityRenderer extends MobEntityRenderer<ChairEntity, ModelEmpty> {
    private static final Identifier EMPTY_TEXTURE = new Identifier("minecraft:textures/block/stone.png");
    public ChairEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ModelEmpty(context.getPart(EntityPaladinClient.MODEL_CUBE_LAYER)), 0.5f);
    }
    public Identifier getTexture(ChairEntity entity) {
        return EMPTY_TEXTURE;
    }


}
