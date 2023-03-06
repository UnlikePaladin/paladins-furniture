package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.client.PFMSandwichableClient;
import com.unlikepaladin.pfm.registry.fabric.NetworkRegistryFabric;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PaladinFurnitureModClientFabric implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        ColorRegistryFabric.registerAll();
        NetworkRegistryFabric.registerClientPackets();

        PaladinFurnitureModClient.USE_TOILET_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pfm.toiletUse", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_U, // The keycode of the key
                "keybindings.category.pfm" // The translation key of the keybinding's category.
        ));
        EntityRenderRegistryFabric.registerRender();


        ScreenRegistry.register(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        ScreenRegistry.register(ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        ScreenRegistry.register(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, StoveScreen::new);
        ScreenRegistry.register(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        ScreenRegistry.register(ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
        ScreenRegistry.register(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, TrashcanScreen::new);
        ParticleProviderRegistryFabric.registerParticleFactories();

        if (FabricLoader.getInstance().isModLoaded("sandwichable") && FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
            PFMSandwichableClient.register();
        }

    }




}