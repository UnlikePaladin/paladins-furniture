package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.compat.fabric.imm_ptl.client.PFMImmPtlRegistryClient;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.client.PFMSandwichableClient;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.networking.fabric.LeaveEventHandlerFabric;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.fabric.NetworkRegistryFabric;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

//TODO: Implement Baked Model for Beds and Tables and Kitchen Counters
//TODO: fix counters facing total opposite directions connecting
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

        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerModelProvider(new PFMExtraModelProvider());
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new PFMModelProvider());
        ParticleProviderRegistryFabric.registerParticleFactories();
        if (FabricLoader.getInstance().isModLoaded("sandwichable") && FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
            PFMSandwichableClient.register();
        }

        if (FabricLoader.getInstance().isModLoaded("imm_ptl_core")) {
            PFMImmPtlRegistryClient.register();
        }

        ClientPlayConnectionEvents.DISCONNECT.register(LeaveEventHandlerFabric::onServerLeave);
    }
}