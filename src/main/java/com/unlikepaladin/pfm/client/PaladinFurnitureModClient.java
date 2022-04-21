package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.RenderChair;
import com.unlikepaladin.pfm.menus.FreezerScreen;
import com.unlikepaladin.pfm.menus.IronStoveScreen;
import com.unlikepaladin.pfm.menus.MicrowaveScreen;
import com.unlikepaladin.pfm.menus.StoveScreen;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.unlikepaladin.pfm.client.EntityPaladinClient.MODEL_CUBE_LAYER;


public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();
    @Override
    public void onInitializeClient() {

       // EntityPaladinClient.registerClientEntity();
      //  EntityRenderRegistry.registerRender();
        ColorRegistry.registerAll();
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, RenderChair::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);

        ScreenRegistry.register(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.STOVE_SCREEN_HANDLER, StoveScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);

    }


    }

