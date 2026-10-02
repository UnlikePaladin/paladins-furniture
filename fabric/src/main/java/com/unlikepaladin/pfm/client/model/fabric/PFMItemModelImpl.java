package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.client.model.BakedItemData;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
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
        return PFMFabricItemModel::new;
    }

    public static void emitItemModelQuads(ItemStackRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, RandomSource random) {
        Object current = model;
        int depth = 0;
        while (current != null && depth < 20) {
            depth++;
            if (current instanceof WrapperBlockStateModel wrapper) {
                current = ((PFMWrapperBlockstateModelAccessor) wrapper).pfm$getWrapped();
            } else if (current instanceof com.unlikepaladin.pfm.mixin.PFMBlockStateModelWrapperAccessor accessor) {
                current = accessor.pfm$getModel();
            } else if (current instanceof com.unlikepaladin.pfm.mixin.PFMCompositeBlockModelAccessor accessor) {
                current = accessor.pfm$getNormal();
            } else {
                break;
            }
        }
        BlockStateModel model1 = current instanceof BlockStateModel bsm ? bsm : model;

        if (model1 instanceof PFMFabricBakedModel fabricModel) {
            if (fabricModel.getItemDisplaySettings() != null)
                fabricModel.getItemDisplaySettings().applyToLayer(layerRenderState, context);

            fabricModel.emitItemQuads(layerRenderState.emitter(), random);
        } else {
            BakedItemData data = BakedItemData.getFallbackItemData(model, random);
            layerRenderState.setExtents(data.extents());
            layerRenderState.setQuads(data.itemQuads());
        }
    }
}
