package com.unlikepaladin.pfm.client.fabric;

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
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
import com.unlikepaladin.pfm.blocks.models.kitchenSink.UnbakedKitchenSinkModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.ladder.UnbakedLadderModel;
import com.unlikepaladin.pfm.blocks.models.logStool.UnbakedLogStoolModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import com.unlikepaladin.pfm.entity.render.OfficeChairEntityRenderer;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class PFMExtraModelProvider implements ExtraModelProvider {
    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<ResourceLocation> out) {
        for (ResourceLocation id : UnbakedBedModel.BED_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedClassicTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedLogTableModel.LOG_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedDinnerTableModel.DINNER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedModernDinnerTableModel.MODERN_DINNER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedChairModel.CHAIR_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedChairDinnerModel.CHAIR_DINNER_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedChairModernModel.CHAIR_MODERN_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedChairClassicModel.CHAIR_CLASSIC_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedSimpleStoolModel.SIMPLE_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedClassicStoolModel.CLASSIC_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedModernStoolModel.MODERN_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedLogStoolModel.LOG_STOOL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenCabinetModel.CABINET_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallCounterModel.COUNTER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenCounterOvenModel.OVEN_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenSinkModel.SINK_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedLadderModel.LADDER_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedCoffeeBasicTableModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedModernCoffeeTableModel.MODERN_COFFEE_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedClassicCoffeeTableModel.CLASSIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedBasicDeskCabinetModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        for (ResourceLocation id : UnbakedClassicDeskModel.BASIC_MODEL_PARTS_BASE) {
            out.accept(id);
        }
        UnbakedMirrorModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedIronFridgeModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedFridgeModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedFreezerModel.ALL_MODEL_IDS.forEach(out::accept);
        UnbakedBasicLampModel.ALL_MODEL_IDS.forEach(out::accept);
        out.accept(new ResourceLocation("minecraft:block/cube_all"));
        for (ResourceLocation id : OfficeChairEntityRenderer.MODEL_IDS) {
            out.accept(id);
        }
    }
}
