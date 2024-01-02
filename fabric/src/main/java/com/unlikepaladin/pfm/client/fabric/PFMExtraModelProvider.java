package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.chair.UnbakedChairModel;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
import com.unlikepaladin.pfm.blocks.models.chairDinner.UnbakedChairDinnerModel;
import com.unlikepaladin.pfm.blocks.models.chairModern.UnbakedChairModernModel;
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
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
import com.unlikepaladin.pfm.blocks.models.kitchenSink.UnbakedKitchenSinkModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.ladder.UnbakedLadderModel;
import com.unlikepaladin.pfm.blocks.models.logStool.UnbakedLogStoolModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class PFMExtraModelProvider implements ExtraModelProvider {
    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
        for (Identifier id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (Identifier id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            out.accept(id);
        }
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(out::accept);
    }
}
