package com.unlikepaladin.pfm.mixin.forge;


import com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.UnbakedCoffeeBasicTableModel;
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
import com.unlikepaladin.pfm.blocks.models.ModelHelper;

import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Mixin(ModelLoader.class)
public abstract class PFMModelLoaderMixin {
    @Shadow
    @Final private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;


    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void pfm$loadModels(Identifier resourceId, CallbackInfo ci) {
        Identifier modifiedId = resourceId;
        if (resourceId instanceof ModelIdentifier && Objects.equals(((ModelIdentifier)resourceId).getVariant(), "inventory")) {
            modifiedId = new Identifier(resourceId.getNamespace(), "item/" + resourceId.getPath());
        }

        if (ModelHelper.containsIdentifier(UnbakedMirrorModel.MIRROR_MODEL_IDS, modifiedId)){
            UnbakedModel model =  new UnbakedMirrorModel(UnbakedMirrorModel.DEFAULT_TEXTURES[2], ModelHelper.getVanillaConcreteColor(resourceId), UnbakedMirrorModel.DEFAULT_TEXTURES[1], new ArrayList<>(), ModelHelper.getColor(resourceId));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        } else if (UnbakedBedModel.BED_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBedModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLogTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLogTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedDinnerTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedDinnerTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernDinnerTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernDinnerTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCounterModel.COUNTER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCounterModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenDrawerModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenDrawerModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallCounterModel.COUNTER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallCounterModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallDrawerModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallDrawerModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCabinetModel.CABINET_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCabinetModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicNightstandModel.NIGHSTAND_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicNightstandModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCounterOvenModel.OVEN_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCounterOvenModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenSinkModel.SINK_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenSinkModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallDrawerSmallModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedIronFridgeModel.IRON_FRIDGE_MODEL_IDS.toArray(new Identifier[0]), modifiedId)){
            UnbakedModel model = new UnbakedIronFridgeModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedFridgeModel.FRIDGE_MODEL_IDS.toArray(new Identifier[0]), modifiedId)){
            UnbakedModel model = new UnbakedFridgeModel(resourceId);
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedFreezerModel.FREEZER_MODEL_IDS.toArray(new Identifier[0]), modifiedId)){
            UnbakedModel model = new UnbakedFreezerModel(resourceId);
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicLampModel.LAMP_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicLampModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairModel.CHAIR_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairDinnerModel.CHAIR_DINNER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairDinnerModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairModernModel.CHAIR_MODERN_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairModernModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairClassicModel.CHAIR_CLASSIC_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairClassicModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedSimpleStoolModel.SIMPLE_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedSimpleStoolModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicStoolModel.CLASSIC_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicStoolModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernStoolModel.MODERN_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernStoolModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLogStoolModel.LOG_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLogStoolModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLadderModel.LADDER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLadderModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedCoffeeBasicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedCoffeeBasicTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernCoffeeTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernCoffeeTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicCoffeeTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicCoffeeTableModel();
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
    }
}