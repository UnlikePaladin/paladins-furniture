package com.unlikepaladin.pfm.entity.model;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Mob;

public class ModelEmpty extends AgeableListModel<ChairEntity> {
    private final ModelPart base;

    public ModelEmpty(ModelPart modelPart) {
        this.base = modelPart.getChild(PartNames.CUBE);
    }


    @Override

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setupAnim(ChairEntity entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(PartNames.CUBE, CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, 0F, 0F, 0F, 0F), PartPose.offset(0F, 0F, 0F));
        return LayerDefinition.create(modelData, 16, 16);
    }

}