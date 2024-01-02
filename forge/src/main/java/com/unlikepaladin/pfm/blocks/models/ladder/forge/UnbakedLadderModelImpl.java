package com.unlikepaladin.pfm.blocks.models.ladder.forge;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;

import java.util.List;

public class UnbakedLadderModelImpl {
    public static BakedModel getBakedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        return new ForgeLadderModel(settings, modelParts);
    }
}
