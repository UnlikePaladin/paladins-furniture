package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
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
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PFMModelLoadingPlugin implements ModelLoadingPlugin {
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.resolveModel().register(context -> loadModelResource(context.id()));
        pluginContext.addModels(provideExtraModels());
    }

    public @Nullable UnbakedModel loadModelResource(Identifier resourceId) {
        if (ModelHelper.containsIdentifier(UnbakedMirrorModel.MIRROR_MODEL_IDS, resourceId)){
            return new UnbakedMirrorModel(UnbakedMirrorModel.DEFAULT_TEXTURES[2], ModelHelper.getVanillaConcreteColor(resourceId), UnbakedMirrorModel.DEFAULT_TEXTURES[1], new ArrayList<>(), ModelHelper.getColor(resourceId));
        } else if (ModelHelper.containsIdentifier(UnbakedBedModel.BED_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedBedModel(ModelHelper.getWoodType(resourceId), ModelHelper.getColor(resourceId), new ArrayList<>(), resourceId.getPath().contains("classic"));
        }
        else if (ModelHelper.containsIdentifier(UnbakedBasicTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedBasicTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedClassicTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedClassicTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedClassicNightstandModel.NIGHSTAND_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedClassicNightstandModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedLogTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedLogTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId), resourceId.getPath().contains("raw"));
        }
        else if (ModelHelper.containsIdentifier(UnbakedDinnerTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedDinnerTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedModernDinnerTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedModernDinnerTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenCounterModel.COUNTER_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenCounterModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenDrawerModel.DRAWER_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenDrawerModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenWallCounterModel.COUNTER_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenWallCounterModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenWallDrawerModel.DRAWER_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenWallDrawerModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenCabinetModel.CABINET_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenCabinetModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedKitchenCounterOvenModel.OVEN_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedKitchenCounterOvenModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
        }
        else if (ModelHelper.containsIdentifier(UnbakedIronFridgeModel.IRON_FRIDGE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedIronFridgeModel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedFridgeModel.FRIDGE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedFridgeModel(resourceId);
        }
        else if (ModelHelper.containsIdentifier(UnbakedFreezerModel.FREEZER_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedFreezerModel(resourceId);
        }
        else if (ModelHelper.containsIdentifier(UnbakedBasicLampModel.LAMP_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            return new UnbakedBasicLampModel();
        }
        else
            return null;
    }
    
    public List<Identifier> provideExtraModels() {
        List<Identifier> out = new ArrayList<>();
        out.addAll(UnbakedBedModel.ALL_MODEL_IDS);
        out.addAll(UnbakedBasicTableModel.ALL_MODEL_IDS);
        out.addAll(UnbakedClassicTableModel.ALL_MODEL_IDS);
        out.addAll(UnbakedClassicNightstandModel.ALL_MODEL_IDS);
        out.addAll(UnbakedLogTableModel.ALL_MODEL_IDS);
        out.addAll(UnbakedDinnerTableModel.ALL_MODEL_IDS);
        out.addAll(UnbakedModernDinnerTableModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenCounterModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenDrawerModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenWallCounterModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenWallDrawerModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenCabinetModel.ALL_MODEL_IDS);
        out.addAll(UnbakedKitchenCounterOvenModel.ALL_MODEL_IDS);
        out.addAll(UnbakedMirrorModel.ALL_MODEL_IDS);
        out.addAll(UnbakedIronFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFridgeModel.ALL_MODEL_IDS);
        out.addAll(UnbakedFreezerModel.ALL_MODEL_IDS);
        out.addAll(UnbakedBasicLampModel.ALL_MODEL_IDS);
        return out;
    }
}
