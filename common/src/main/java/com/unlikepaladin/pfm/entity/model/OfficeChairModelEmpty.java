package com.unlikepaladin.pfm.entity.model;


import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.AgeableListModel;

public class OfficeChairModelEmpty extends AgeableListModel<OfficeChairEntity> {

    public OfficeChairModelEmpty() {
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
    public void setupAnim(OfficeChairEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
