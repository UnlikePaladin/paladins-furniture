package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class EntityPaladinClient {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(new Identifier("pfm", "cube"), "main");
    public static final EntityModelLayer MODEL_CHAIR_LAYER = new EntityModelLayer(new Identifier("pfm", "entity"), "player_chair");

    public static void registerClientEntity() {

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);

    }


    }

