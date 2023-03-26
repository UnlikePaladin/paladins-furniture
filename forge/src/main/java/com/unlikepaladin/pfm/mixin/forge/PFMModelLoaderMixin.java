package com.unlikepaladin.pfm.mixin.forge;


import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;

import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class PFMModelLoaderMixin {
    @Shadow
    @Final private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void pfm$addMirrorModel(Identifier resourceId, CallbackInfo ci) {
        if (ModelHelper.containsIdentifier(UnbakedMirrorModel.MIRROR_MODEL_IDS, resourceId)){
            UnbakedModel model =  new UnbakedMirrorModel(UnbakedMirrorModel.DEFAULT_TEXTURES[2], UnbakedMirrorModel.DEFAULT_TEXTURES[0], UnbakedMirrorModel.DEFAULT_TEXTURES[1]);
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        } else if (ModelHelper.containsIdentifier(UnbakedBedModel.BED_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            UnbakedModel model = new UnbakedBedModel(ModelHelper.getWoodType(resourceId), ModelHelper.getColor(resourceId), new ArrayList<>(), resourceId.getPath().contains("classic"));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedBasicTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            UnbakedModel model = new UnbakedBasicTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedClassicTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            UnbakedModel model = new UnbakedClassicTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedLogTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            boolean raw = resourceId.getPath().contains("raw");
            UnbakedModel model = new UnbakedLogTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId), raw);
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedDinnerTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            UnbakedModel model = new UnbakedDinnerTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
        else if (ModelHelper.containsIdentifier(UnbakedModernDinnerTableModel.TABLE_MODEL_IDS.toArray(new Identifier[0]), resourceId)){
            UnbakedModel model = new UnbakedModernDinnerTableModel(ModelHelper.getVariant(resourceId), new ArrayList<>(), ModelHelper.getBlockType(resourceId));
            this.unbakedModels.put(resourceId, model);
            this.modelsToBake.put(resourceId, model);
            ci.cancel();
        }
    }
}