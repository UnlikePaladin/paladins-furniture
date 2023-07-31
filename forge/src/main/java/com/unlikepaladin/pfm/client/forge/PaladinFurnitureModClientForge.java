package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.classicNightstand.UnbakedClassicNightstandModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFreezerModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFridgeModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedIronFridgeModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCabinet.UnbakedKitchenCabinetModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounter.UnbakedKitchenCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.UnbakedKitchenCounterOvenModel;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.UnbakedKitchenDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> new PFMConfigScreen(client, parent)));
    }

    private static void registerScreens() {
        ScreenRegistry.registerScreens();
    }

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(PaladinFurnitureModClient.USE_TOILET_KEYBIND = registerKey("key.pfm.toiletUse", "keybindings.category.pfm", GLFW.GLFW_KEY_U));
    }

    public static KeyBinding registerKey(String name, String category, int keyCode) {
        return new KeyBinding(
                name, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                keyCode, // The keycode of the key
                category // The translation key of the keybinding's category.
        );
    }

    @SubscribeEvent
    public static void registerExtraModels(ModelEvent.RegisterAdditional event) {
        UnbakedBedModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedBasicTableModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedClassicTableModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedLogTableModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedDinnerTableModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedModernDinnerTableModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenCounterModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenDrawerModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenWallCounterModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenWallDrawerModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenCounterOvenModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedKitchenCabinetModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedClassicNightstandModel.ALL_MODEL_IDS.forEach(event::register);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(event::register);
    }
}