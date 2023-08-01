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
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.ClientRegistry;
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
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory(
                        (client, parent) -> new PFMConfigScreen(client, parent)));
        PaladinFurnitureModClient.USE_TOILET_KEYBIND = registerKey("key.pfm.toiletUse", "keybindings.category.pfm", GLFW.GLFW_KEY_U);
    }

    private static void registerScreens() {
        ScreenRegistry.registerScreens();
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

    @SubscribeEvent
    public static void registerExtraModels(ModelRegistryEvent event) {
        UnbakedBedModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedBasicTableModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedClassicTableModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedLogTableModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedDinnerTableModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedModernDinnerTableModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenCounterModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenDrawerModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenWallCounterModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenWallDrawerModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenCounterOvenModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedKitchenCabinetModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedClassicNightstandModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(ForgeModelBakery::addSpecialModel);
    }
}