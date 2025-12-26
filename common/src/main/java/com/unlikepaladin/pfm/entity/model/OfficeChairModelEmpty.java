package com.unlikepaladin.pfm.entity.model;


import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.entity.render.state.OfficeChairEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

import java.util.List;
import java.util.Map;

public class OfficeChairModelEmpty extends EntityModel<OfficeChairEntityRenderState> {

    public OfficeChairModelEmpty() {
        super(new ModelPart(List.of(), Map.of()));
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(0F, 0F, 0F, 0F, 0F, 0F), ModelTransform.origin(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(OfficeChairEntityRenderState state) {
        super.setAngles(state);
    }
}
