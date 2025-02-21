package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.BakedModel;

import java.util.List;

public class PFMItemModelImpl {
    public static TriFunc<BakedModel, SpecialModelRenderer<?>, List<TintSource>, ItemModel> getItemModelFunc() {
        return PFMFabricItemModel::new;
    }
}
