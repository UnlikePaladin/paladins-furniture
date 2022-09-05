package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.client.EntityRenderIDs;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ChairEntityRenderer extends MobEntityRenderer<ChairEntity, ModelEmpty> {
    private static final Identifier EMPTY_TEXTURE = new Identifier("minecraft:textures/block/stone.png");
    public ChairEntityRenderer(EntityRenderDispatcher context) {
        super(context, new ModelEmpty(), 0f);
    }
    public Identifier getTexture(ChairEntity entity) {
        return EMPTY_TEXTURE;
    }


}
