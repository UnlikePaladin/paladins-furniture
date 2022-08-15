package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.compat.sandwichable.client.PFMSandwichableClient;
import com.unlikepaladin.pfm.registry.NetworkRegistry;
import com.unlikepaladin.pfm.registry.ScreenHandlersRegistry;
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
public class PaladinFurnitureModClient implements ClientModInitializer {
    public static final Logger CLIENT_LOGGER = LogManager.getLogger();
    public static KeyBinding USE_TOILET_KEYBIND;

    @Override
    public void onInitializeClient() {
        // EntityPaladinClient.registerClientEntity();
      //  EntityRenderRegistry.registerRender();
        ColorRegistry.registerAll();
        NetworkRegistry.registerClientPackets();

        USE_TOILET_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pfm.toiletUse", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_U, // The keycode of the key
                "keybindings.category.pfm" // The translation key of the keybinding's category.
        ));
        EntityRenderRegistry.registerRender();


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