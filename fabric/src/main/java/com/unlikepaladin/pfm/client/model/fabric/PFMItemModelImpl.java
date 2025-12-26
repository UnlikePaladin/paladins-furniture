package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Supplier;

public class PFMItemModelImpl {
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<TintSource>, ItemModel> getItemModelFunc() {
        return PFMFabricItemModel::new;
    }

    public static void emitItemModelQuads(ItemRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, Random random) {
        BlockStateModel model1 = model;

        int ctr = 0;
        while (model1 instanceof WrapperBlockStateModel) {
            model1 = ((PFMWrapperBlockstateModelAccessor) model1).pfm$getWrapped();
            ctr++;
            if (ctr > 15)
                break;
        }

        if (model1 instanceof PFMFabricBakedModel) {
            if (((PFMFabricBakedModel) model1).getItemDisplaySettings() != null)
                ((PFMFabricBakedModel) model1).getItemDisplaySettings().addSettings(layerRenderState, context);

            ((PFMFabricBakedModel) model1).emitItemQuads(layerRenderState.emitter(), random);
        } else {
            List<BlockModelPart> parts;
            parts = model.getParts(random);
            for (Direction direction : Direction.values()) {
                layerRenderState.getQuads().addAll(parts.stream().flatMap(p -> p.getQuads(direction).stream()).toList());
            }
            layerRenderState.getQuads().addAll(parts.stream().flatMap(p -> p.getQuads(null).stream()).toList());
        }
    }
}
