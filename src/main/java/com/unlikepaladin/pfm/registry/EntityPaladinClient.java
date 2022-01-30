package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.model.ChairEntityModel;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.menus.FreezerScreen;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class EntityPaladinClient {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(new Identifier("pfm", "cube"), "main");
    public static final EntityModelLayer MODEL_CHAIR_LAYER = new EntityModelLayer(new Identifier("pfm", "entity"), "player_chair");

    public static void registerClientEntity() {

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_CHAIR_LAYER, ChairEntityModel::getTexturedModelData);

    }


    }

