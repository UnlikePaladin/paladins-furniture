package com.unlikepaladin.pfm.client.forge;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
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
import com.unlikepaladin.pfm.blocks.models.herringbone.UnbakedHerringboneModel;
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
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PaladinFurnitureModClientForge {

    private PaladinFurnitureModClientForge() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        PFMSpriteRegistry.registerAdditionalSprites();
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

    public static void registerCustomModels() {
        PaladinFurnitureMod.GENERAL_LOGGER.info("Registering Custom Models");
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedMirrorModel.MIRROR_ID, UnbakedMirrorModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedBedModel.BED_MODEL_ID, UnbakedBedModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedBasicTableModel.TABLE_MODEL_ID, UnbakedBasicTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedClassicTableModel.TABLE_MODEL_ID, UnbakedClassicTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedLogTableModel.TABLE_MODEL_ID, UnbakedLogTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedDinnerTableModel.TABLE_MODEL_ID, UnbakedDinnerTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedModernDinnerTableModel.TABLE_MODEL_ID, UnbakedModernDinnerTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_ID, UnbakedClassicNightstandModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedChairModel.CHAIR_MODEL_ID, UnbakedChairModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedChairDinnerModel.CHAIR_MODEL_ID, UnbakedChairDinnerModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedChairModernModel.CHAIR_MODEL_ID, UnbakedChairModernModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedChairClassicModel.CHAIR_MODEL_ID, UnbakedChairClassicModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedSimpleStoolModel.STOOL_MODEL_ID, UnbakedSimpleStoolModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedClassicStoolModel.STOOL_MODEL_ID, UnbakedClassicStoolModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedModernStoolModel.STOOL_MODEL_ID, UnbakedModernStoolModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedLogStoolModel.STOOL_MODEL_ID, UnbakedLogStoolModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenCounterModel.COUNTER_MODEL_ID, UnbakedKitchenCounterModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenDrawerModel.DRAWER_MODEL_ID, UnbakedKitchenDrawerModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenWallCounterModel.COUNTER_MODEL_ID, UnbakedKitchenWallCounterModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenWallDrawerModel.DRAWER_MODEL_ID, UnbakedKitchenWallDrawerModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenCabinetModel.CABINET_MODEL_ID, UnbakedKitchenCabinetModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenCounterOvenModel.OVEN_MODEL_ID, UnbakedKitchenCounterOvenModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenSinkModel.SINK_MODEL_ID, UnbakedKitchenSinkModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_ID, UnbakedKitchenWallDrawerSmallModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedIronFridgeModel.IRON_FRIDGE_ID, UnbakedIronFridgeModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedFridgeModel.FRIDGE_MODEL_ID, UnbakedFridgeModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedFreezerModel.FREEZER_MODEL_ID, UnbakedFreezerModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedBasicLampModel.LAMP_MODEL_ID, UnbakedBasicLampModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedLadderModel.LADDER_MODEL_ID, UnbakedLadderModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedCoffeeBasicTableModel.TABLE_MODEL_ID, UnbakedCoffeeBasicTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedModernCoffeeTableModel.TABLE_MODEL_ID, UnbakedModernCoffeeTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedClassicCoffeeTableModel.TABLE_MODEL_ID, UnbakedClassicCoffeeTableModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedBasicDeskModel.TABLE_MODEL_ID, UnbakedBasicDeskModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedBasicDeskCabinetModel.TABLE_MODEL_ID, UnbakedBasicDeskCabinetModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedClassicDeskModel.TABLE_MODEL_ID, UnbakedClassicDeskModel.MAP_CODEC);
        PFMUnbakedBlockStateModelRegistryFabricAPI.register(UnbakedHerringboneModel.ID, UnbakedHerringboneModel.MAP_CODEC);
    }

    public static void registerExtraModels(Consumer<Identifier> event) {
        for (Identifier id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedCoffeeBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedModernCoffeeTableModel.MODERN_COFFEE_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedClassicCoffeeTableModel.CLASSIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedBasicDeskModel.BASIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedBasicDeskCabinetModel.BASIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        for (Identifier id : UnbakedClassicDeskModel.BASIC_MODEL_PARTS_BASE) {
            event.accept(id);
        }
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(event);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(event);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(event);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(event);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(event);
        event.accept(Identifier.of("minecraft:block/cube_all"));
        for (Identifier id : OfficeChairEntityRenderer.MODEL_IDS) {
            event.accept(id);
        }
    }
}