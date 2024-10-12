package com.unlikepaladin.pfm.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class EntityRenderIDs {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(Identifier.of("pfm", "cube"), "main");
    public static final EntityModelLayer BED_HEAD_LAYER = new EntityModelLayer(Identifier.of("pfm", "bed_head"), "main");
    public static final EntityModelLayer BED_FOOT_LAYER = new EntityModelLayer(Identifier.of("pfm", "bed_foot"), "main");

}
