package com.unlikepaladin.pfm.client.fabric;

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
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

import java.util.*;

public class PFMModelLoadingPlugin implements ModelLoadingPlugin {

    public static Map<Identifier, ExtraModelKey<BlockStateModel>> modelKeyMap = new HashMap<>();
    @Override
    public void initialize(Context pluginContext) {
        PaladinFurnitureMod.GENERAL_LOGGER.info("Initializing PFM Model Loading Plugin");
        provideExtraModels().forEach(identifier -> {
            ExtraModelKey<BlockStateModel> modelKey = ExtraModelKey.create(identifier::toString);
            modelKeyMap.put(identifier, modelKey);
            pluginContext.addModel(modelKey, SimpleUnbakedExtraModel.blockStateModel(identifier));
        });
    }

    public static void registerCustomModels() {
        CustomUnbakedBlockStateModel.register(UnbakedMirrorModel.MIRROR_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedMirrorModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedBedModel.BED_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBedModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedBasicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedClassicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedLogTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLogTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedDinnerTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedDinnerTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedModernDinnerTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernDinnerTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicNightstandModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedChairModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedChairDinnerModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairDinnerModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedChairModernModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairModernModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedChairClassicModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairClassicModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedSimpleStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedSimpleStoolModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedClassicStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicStoolModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedModernStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernStoolModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedLogStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLogStoolModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenCounterModel.COUNTER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCounterModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenDrawerModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenDrawerModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenWallCounterModel.COUNTER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallCounterModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenWallDrawerModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallDrawerModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenCabinetModel.CABINET_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCabinetModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenCounterOvenModel.OVEN_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCounterOvenModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenSinkModel.SINK_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenSinkModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallDrawerSmallModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedIronFridgeModel.IRON_FRIDGE_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedIronFridgeModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedFridgeModel.FRIDGE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedFridgeModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedFreezerModel.FREEZER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedFreezerModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedBasicLampModel.LAMP_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicLampModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedLadderModel.LADDER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLadderModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedCoffeeBasicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedCoffeeBasicTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedModernCoffeeTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernCoffeeTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedClassicCoffeeTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicCoffeeTableModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedBasicDeskModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicDeskModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedBasicDeskCabinetModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicDeskCabinetModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedHerringboneModel.ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedHerringboneModel.MAP_CODEC);
        CustomUnbakedBlockStateModel.register(UnbakedClassicDeskModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicDeskModel.MAP_CODEC);
    }
    
    public List<Identifier> provideExtraModels() {
        List<Identifier> out = new ArrayList<>();
        for (Identifier id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedCoffeeBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedModernCoffeeTableModel.MODERN_COFFEE_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedClassicCoffeeTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedBasicDeskModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedBasicDeskCabinetModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        for (Identifier id : UnbakedClassicDeskModel.BASIC_MODEL_PARTS_BASE) {
            out.add(id);
        }
        out.addAll(UnbakedMirrorModel.ALL_MODEL_IDS);
        out.addAll(UnbakedIronFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFreezerModel.ALL_MODEL_IDS);
        out.addAll(UnbakedBasicLampModel.ALL_MODEL_IDS);
        out.add(Identifier.parse("minecraft:block/cube_all"));
        out.add(Identifier.parse("minecraft:block/block"));
        out.addAll(Arrays.asList(OfficeChairEntityRenderer.MODEL_IDS));
        PaladinFurnitureMod.GENERAL_LOGGER.info(OfficeChairEntityRenderer.MODEL_IDS);
        return out;
    }

}
