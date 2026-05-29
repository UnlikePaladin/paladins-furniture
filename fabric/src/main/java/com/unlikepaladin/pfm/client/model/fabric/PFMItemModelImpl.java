package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.List;
import java.util.function.Supplier;

public class PFMItemModelImpl {
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc() {
        return PFMFabricItemModel::new;
    }

    public static void emitItemModelQuads(ItemStackRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, RandomSource random) {
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
                ((PFMFabricBakedModel) model1).getItemDisplaySettings().applyToLayer(layerRenderState, context);

            ((PFMFabricBakedModel) model1).emitItemQuads(layerRenderState.emitter(), random);
        } else {
            List<BlockModelPart> parts;
            parts = model.collectParts(random);
            for (Direction direction : Direction.values()) {
                layerRenderState.prepareQuadList().addAll(parts.stream().flatMap(p -> p.getQuads(direction).stream()).toList());
            }
            layerRenderState.prepareQuadList().addAll(parts.stream().flatMap(p -> p.getQuads(null).stream()).toList());
        }
    }
}
