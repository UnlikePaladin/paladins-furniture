package com.unlikepaladin.pfm.mixin.neoforge;


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
import com.unlikepaladin.pfm.blocks.models.ModelHelper;

import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Mixin(ModelBakery.class)
public abstract class PFMModelBakeryMixin {
    @Shadow
    @Final private Map<ResourceLocation, UnbakedModel> unbakedCache;

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> topLevelModels;


    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void pfm$loadModels(ResourceLocation resourceId, CallbackInfo ci) {
        ResourceLocation modifiedId = resourceId;
        if (resourceId instanceof ModelResourceLocation && Objects.requireNonNull(((ModelResourceLocation) resourceId).getVariant()).startsWith("inventory")) {
            modifiedId = new ResourceLocation(resourceId.getNamespace(), "item/" + resourceId.getPath());
        }

        if (ModelHelper.containsIdentifier(UnbakedMirrorModel.MIRROR_MODEL_IDS, modifiedId)){
            UnbakedModel model =  new UnbakedMirrorModel(UnbakedMirrorModel.DEFAULT_TEXTURES[2], ModelHelper.getVanillaConcreteColor(resourceId), UnbakedMirrorModel.DEFAULT_TEXTURES[1], new ArrayList<>(), ModelHelper.getColor(resourceId));
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        } else if (UnbakedBedModel.BED_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBedModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLogTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLogTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedDinnerTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedDinnerTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernDinnerTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernDinnerTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCounterModel.COUNTER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCounterModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenDrawerModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenDrawerModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallCounterModel.COUNTER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallCounterModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallDrawerModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallDrawerModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCabinetModel.CABINET_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCabinetModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicNightstandModel.NIGHSTAND_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicNightstandModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenCounterOvenModel.OVEN_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenCounterOvenModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenSinkModel.SINK_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenSinkModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedKitchenWallDrawerSmallModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedIronFridgeModel.IRON_FRIDGE_MODEL_IDS.toArray(new ResourceLocation[0]), modifiedId)){
            UnbakedModel model = new UnbakedIronFridgeModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedFridgeModel.FRIDGE_MODEL_IDS.toArray(new ResourceLocation[0]), modifiedId)){
            UnbakedModel model = new UnbakedFridgeModel(resourceId);
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedFreezerModel.FREEZER_MODEL_IDS.toArray(new ResourceLocation[0]), modifiedId)){
            UnbakedModel model = new UnbakedFreezerModel(resourceId);
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicLampModel.LAMP_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicLampModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairModel.CHAIR_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairDinnerModel.CHAIR_DINNER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairDinnerModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairModernModel.CHAIR_MODERN_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairModernModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedChairClassicModel.CHAIR_CLASSIC_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedChairClassicModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedSimpleStoolModel.SIMPLE_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedSimpleStoolModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicStoolModel.CLASSIC_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicStoolModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernStoolModel.MODERN_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernStoolModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLogStoolModel.LOG_STOOL_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLogStoolModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedLadderModel.LADDER_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedLadderModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedCoffeeBasicTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedCoffeeBasicTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedModernCoffeeTableModel.TABLE_MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedModernCoffeeTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicCoffeeTableModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicCoffeeTableModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicDeskModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicDeskModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedBasicDeskCabinetModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedBasicDeskCabinetModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedClassicDeskModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedClassicDeskModel();
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
        else if (UnbakedHerringboneModel.MODEL_IDS.contains(modifiedId)){
            UnbakedModel model = new UnbakedHerringboneModel(resourceId);
            this.unbakedCache.put(resourceId, model);
            this.topLevelModels.put(resourceId, model);
            ci.cancel();
        }
    }
}