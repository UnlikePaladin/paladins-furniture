package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.resources.model.BakedModel;

import java.util.List;

public class PFMItemModelImpl {
    public static TriFunc<BakedModel, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc() {
        return PFMFabricItemModel::new;
    }
}
