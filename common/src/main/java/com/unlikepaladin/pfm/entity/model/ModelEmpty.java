package com.unlikepaladin.pfm.entity.model;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;

public class ModelEmpty extends AgeableListModel<ChairEntity> {
    private final ModelPart base;

    public ModelEmpty() {
        this.base = new ModelPart(this);
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
}