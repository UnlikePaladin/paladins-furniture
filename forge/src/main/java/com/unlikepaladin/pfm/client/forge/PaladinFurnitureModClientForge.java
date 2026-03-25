package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.UnbakedCoffeeBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.basicDesk.UnbakedBasicDeskModel;
import com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.UnbakedBasicDeskCabinetModel;
import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.chair.UnbakedChairModel;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
import com.unlikepaladin.pfm.blocks.models.chairDinner.UnbakedChairDinnerModel;
import com.unlikepaladin.pfm.blocks.models.chairModern.UnbakedChairModernModel;
import com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.UnbakedClassicCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.classicDesk.UnbakedClassicDeskModel;
import com.unlikepaladin.pfm.blocks.models.classicNightstand.UnbakedClassicNightstandModel;
import com.unlikepaladin.pfm.blocks.models.classicStool.UnbakedClassicStoolModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFreezerModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFridgeModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedIronFridgeModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCabinet.UnbakedKitchenCabinetModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounter.UnbakedKitchenCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.UnbakedKitchenCounterOvenModel;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.UnbakedKitchenDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenSink.UnbakedKitchenSinkModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
import com.unlikepaladin.pfm.blocks.models.ladder.UnbakedLadderModel;
import com.unlikepaladin.pfm.blocks.models.logStool.UnbakedLogStoolModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PaladinFurnitureModClientForge {

    private PaladinFurnitureModClientForge() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
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

    public static KeyMapping registerKey(String name, String category, int keyCode) {
        final var key = new KeyMapping(
                name, // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                keyCode, // The keycode of the key
                category // The translation key of the keybinding's category.
        );
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @SubscribeEvent
    public static void registerExtraModels(ModelRegistryEvent event) {
        for (ResourceLocation id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedCoffeeBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedModernCoffeeTableModel.MODERN_COFFEE_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedClassicCoffeeTableModel.CLASSIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskModel.BASIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskCabinetModel.BASIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        for (ResourceLocation id : UnbakedClassicDeskModel.BASIC_MODEL_PARTS_BASE) {
            ModelLoader.addSpecialModel(id);
        }
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(ModelLoader::addSpecialModel);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(ModelLoader::addSpecialModel);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(ModelLoader::addSpecialModel);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(ModelLoader::addSpecialModel);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(ModelLoader::addSpecialModel);
        ModelLoader.addSpecialModel(new ResourceLocation("minecraft:block/cube_all"));
        for (ResourceLocation id : OfficeChairEntityRenderer.MODEL_IDS) {
            ModelLoader.addSpecialModel(id);
        }
    }
}