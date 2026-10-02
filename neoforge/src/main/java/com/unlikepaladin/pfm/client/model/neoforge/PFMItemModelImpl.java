package com.unlikepaladin.pfm.client.model.neoforge;

import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.client.model.BakedItemData;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.List;
import java.util.function.Supplier;

public class PFMItemModelImpl {
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc() {
        return PFMItemModel::new;
    }

    public static void emitItemModelQuads(ItemStackRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, RandomSource random) {
        if (model instanceof PFMNeoForgeBakedModel pfmModel) {
            if (pfmModel.getItemDisplaySettings() != null)
                pfmModel.getItemDisplaySettings().applyToLayer(layerRenderState, context);

            BakedItemData data = pfmModel.getBakedItemData(random);
            layerRenderState.setExtents(data.extents());
            layerRenderState.setQuads(data.itemQuads());
        } else {
            BakedItemData data = BakedItemData.getFallbackItemData(model, random);
            layerRenderState.setExtents(data.extents());
            layerRenderState.setQuads(data.itemQuads());
        }
    }
}
