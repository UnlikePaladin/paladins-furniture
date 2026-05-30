package com.unlikepaladin.pfm.entity.render;

import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity, EntityRenderState> {
    private static final Identifier EMPTY_TEXTURE = Identifier.parse("minecraft:textures/block/stone.png");
    public ChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
