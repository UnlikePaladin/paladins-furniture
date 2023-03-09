package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import com.unlikepaladin.pfm.registry.forge.ScreenHandlerRegistryForge;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PaladinFurnitureModClientForge {

    private PaladinFurnitureModClientForge() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        NetworkRegistryForge.registerPackets();
        ColorRegistryForge.registerBlockRenderLayers();
        event.enqueueWork(PaladinFurnitureModClientForge::registerScreens);
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory(
                        (client, parent) -> new PFMConfigScreen(client, parent)));

        PaladinFurnitureModClient.USE_TOILET_KEYBIND = registerKey("key.pfm.toiletUse", "keybindings.category.pfm", GLFW.GLFW_KEY_U);
    }

    private static void registerScreens() {
        HandledScreens.register(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, FreezerScreen::new);
        HandledScreens.register(ScreenHandlerIDs.WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
        HandledScreens.register(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, StoveScreen::new);
        HandledScreens.register(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, IronStoveScreen::new);
        HandledScreens.register(ScreenHandlerIDs.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
        HandledScreens.register(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, TrashcanScreen::new);
    }

    public static KeyBinding registerKey(String name, String category, int keyCode) {
        final var key = new KeyBinding(
                name, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                keyCode, // The keycode of the key
                category // The translation key of the keybinding's category.
        );
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
