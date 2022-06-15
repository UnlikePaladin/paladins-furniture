package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.entity.model.ModelEmpty;
import com.unlikepaladin.pfm.entity.render.ChairEntityRenderer;
import com.unlikepaladin.pfm.entity.render.MicrowaveBlockEntityRenderer;
import com.unlikepaladin.pfm.entity.render.StovetopBlockEntityRenderer;
import com.unlikepaladin.pfm.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static com.unlikepaladin.pfm.client.EntityPaladinClient.MODEL_CUBE_LAYER;

@Environment(EnvType.CLIENT)
public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        // EntityPaladinClient.registerClientEntity();
        //  EntityRenderRegistry.registerRender();
        ColorRegistry.registerAll();
        EntityRendererRegistry.register(EntityRegistry.CHAIR, ChairEntityRenderer::new);
        BlockEntityRendererRegistry.register(PaladinFurnitureMod.MICROWAVE_BLOCK_ENTITY, MicrowaveBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(PaladinFurnitureMod.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, ModelEmpty::getTexturedModelData);

        ScreenRegistry.register(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.STOVE_SCREEN_HANDLER, StoveScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        ScreenRegistry.register(PaladinFurnitureMod.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(PaladinFurnitureMod.MICROWAVE_UPDATE_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean active = buf.readBoolean();
            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                if (Objects.nonNull(MinecraftClient.getInstance().currentScreen)) {
                    MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                    currentScreen.getScreenHandler().setActive(active);
                }
            });
        });
    }


}