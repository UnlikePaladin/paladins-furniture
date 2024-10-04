package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.fabric.PaladinFurnitureModFabric;
import com.unlikepaladin.pfm.networking.fabric.LeaveEventHandlerFabric;
import com.unlikepaladin.pfm.registry.fabric.NetworkRegistryFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
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
        PaladinFurnitureMod.isClient = true;
        PaladinFurnitureModFabric.registerLateEntries();
        PaladinFurnitureModFabric.replaceHomePOIStates();
        ColorRegistryFabric.registerAll();
        NetworkRegistryFabric.registerClientPackets();

        PaladinFurnitureModClient.USE_TOILET_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pfm.toiletUse", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_U, // The keycode of the key
                "keybindings.category.pfm" // The translation key of the keybinding's category.
        ));
        EntityRenderRegistryFabric.registerRender();

        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerModelProvider(new PFMExtraModelProvider());
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new PFMModelProvider());
        ParticleProviderRegistryFabric.registerParticleFactories();
        ClientPlayConnectionEvents.DISCONNECT.register(LeaveEventHandlerFabric::onServerLeave);
    }
}