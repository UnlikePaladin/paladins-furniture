package com.unlikepaladin.pfm.entity.model;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;

public class ModelEmpty extends AnimalModel<ChairEntity> {
    private final ModelPart base;

    public ModelEmpty() {
        this.base = new ModelPart(this);
    }

    @Override

    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setAngles(ChairEntity entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

    }
}