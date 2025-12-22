package com.unlikepaladin.pfm.entity.model;


import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;

public class OfficeChairModelEmpty extends AnimalModel<OfficeChairEntity> {

    public OfficeChairModelEmpty() {
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
    public void setAngles(OfficeChairEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
