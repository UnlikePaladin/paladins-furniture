package com.unlikepaladin.pfm.client.neoforge;

import com.mojang.serialization.MapCodec;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

public class BlockStateModelRegistryNeoForge {

    @SubscribeEvent
    public static void registerBlockStateModels(RegisterBlockStateModels registerBlockStateModels) {
        registerBlockStateModels.registerModel(UnbakedMirrorModel.MIRROR_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedMirrorModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedBedModel.BED_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBedModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedBasicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedClassicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedLogTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLogTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedDinnerTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedDinnerTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedModernDinnerTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernDinnerTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicNightstandModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedChairModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedChairDinnerModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairDinnerModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedChairModernModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairModernModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedChairClassicModel.CHAIR_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedChairClassicModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedSimpleStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedSimpleStoolModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedClassicStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicStoolModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedModernStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernStoolModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedLogStoolModel.STOOL_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLogStoolModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenCounterModel.COUNTER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCounterModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenDrawerModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenDrawerModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenWallCounterModel.COUNTER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallCounterModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenWallDrawerModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallDrawerModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenCabinetModel.CABINET_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCabinetModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenCounterOvenModel.OVEN_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenCounterOvenModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenSinkModel.SINK_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenSinkModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedKitchenWallDrawerSmallModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedIronFridgeModel.IRON_FRIDGE_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedIronFridgeModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedFridgeModel.FRIDGE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedFridgeModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedFreezerModel.FREEZER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedFreezerModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedBasicLampModel.LAMP_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicLampModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedLadderModel.LADDER_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedLadderModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedCoffeeBasicTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedCoffeeBasicTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedModernCoffeeTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedModernCoffeeTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedClassicCoffeeTableModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedClassicCoffeeTableModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedBasicDeskModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicDeskModel.MAP_CODEC);
        registerBlockStateModels.registerModel(UnbakedBasicDeskCabinetModel.TABLE_MODEL_ID, (MapCodec<? extends CustomUnbakedBlockStateModel>) (Object) UnbakedBasicDeskCabinetModel.MAP_CODEC);
    }
}
