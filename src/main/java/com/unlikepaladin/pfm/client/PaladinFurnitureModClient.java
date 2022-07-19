package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.compat.sandwichable.client.PFMSandwichableClient;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.ChairEntityRenderer;
import com.unlikepaladin.pfm.entity.render.MicrowaveBlockEntityRenderer;
import com.unlikepaladin.pfm.entity.render.StovetopBlockEntityRenderer;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import com.unlikepaladin.pfm.registry.NetworkRegistry;
import com.unlikepaladin.pfm.registry.ScreenHandlersRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.unlikepaladin.pfm.client.EntityPaladinClient.MODEL_CUBE_LAYER;

@Environment(EnvType.CLIENT)
public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        // EntityPaladinClient.registerClientEntity();
      //  EntityRenderRegistry.registerRender();
        ColorRegistry.registerAll();
        NetworkRegistry.registerClientPackets();

        EntityRendererRegistry.INSTANCE.register(EntityRegistry.CHAIR, ChairEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityRegistry.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityRegistry.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);

        ScreenRegistry.register(ScreenHandlersRegistry.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        ScreenRegistry.register(ScreenHandlersRegistry.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        ScreenRegistry.register(ScreenHandlersRegistry.STOVE_SCREEN_HANDLER, StoveScreen::new);
        ScreenRegistry.register(ScreenHandlersRegistry.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        ScreenRegistry.register(ScreenHandlersRegistry.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);

        if (FabricLoader.getInstance().isModLoaded("sandwichable")) {
            PFMSandwichableClient.register();
        }


    }




}