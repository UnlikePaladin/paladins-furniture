package com.unlikepaladin.pfm.client.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
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
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import com.unlikepaladin.pfm.client.ScreenRegistry;
import com.unlikepaladin.pfm.client.screens.*;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = "pfm", value = Dist.CLIENT)
public class PaladinFurnitureModClientNeoForge {

    private PaladinFurnitureModClientNeoForge() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ColorRegistryNeoForge.registerBlockRenderLayers();
        event.enqueueWork(PaladinFurnitureModClientNeoForge::registerScreens);
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
                () -> ((modContainer, arg) -> new PFMConfigScreen(arg.getMinecraft(), arg)));
    }

    @SubscribeEvent
    public static void registerSprites(FMLCommonSetupEvent event) {
        PFMSpriteRegistry.registerAdditionalSprites();
    }

    private static void registerScreens() {
        ScreenRegistry.registerScreens();
    }

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.registerCategory(PaladinFurnitureModClient.PFM_CATEGORY);
        event.register(PaladinFurnitureModClient.USE_TOILET_KEYBIND = registerKey("key.pfm.toiletUse", PaladinFurnitureModClient.PFM_CATEGORY, GLFW.GLFW_KEY_U));
    }

    public static KeyMapping registerKey(String name, KeyMapping.Category category, int keyCode) {
        return new KeyMapping(
                name, // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                keyCode, // The keycode of the key
                category // The translation key of the keybinding's category.
        );
    }

    public static Map<ResourceLocation, StandaloneModelKey<BlockStateModel>> modelKeyMap = new HashMap<>();
    @SubscribeEvent
    public static void registerExtraModels(ModelEvent.RegisterStandalone event) {
        provideExtraModels().forEach(identifier -> {
            StandaloneModelKey<BlockStateModel> modelKey = new StandaloneModelKey<>(identifier::toString);
            modelKeyMap.put(identifier, modelKey);
            event.register(modelKey, SimpleUnbakedStandaloneModel.blockStateModel(identifier));
        });
    }

    public static List<ResourceLocation> provideExtraModels() {
        List<ResourceLocation> out = new ArrayList<>();
        for (ResourceLocation id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedCoffeeBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedModernCoffeeTableModel.MODERN_COFFEE_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedClassicCoffeeTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskCabinetModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        out.addAll(UnbakedMirrorModel.ALL_MODEL_IDS);
        out.addAll(UnbakedIronFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFreezerModel.ALL_MODEL_IDS);
        out.addAll(UnbakedBasicLampModel.ALL_MODEL_IDS);
        out.add(ResourceLocation.parse("minecraft:block/cube_all"));
        out.add(ResourceLocation.parse("minecraft:block/block"));
        for (ResourceLocation id : OfficeChairEntityRenderer.MODEL_IDS) {
            out.add(id);
        }
        return out;
    }

}