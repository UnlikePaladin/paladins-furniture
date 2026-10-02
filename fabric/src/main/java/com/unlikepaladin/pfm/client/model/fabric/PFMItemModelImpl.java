package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.ArrayList;
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

        if (model1 instanceof PFMFabricBakedModel) {
            if (((PFMFabricBakedModel) model1).getItemDisplaySettings() != null)
                ((PFMFabricBakedModel) model1).getItemDisplaySettings().applyToLayer(layerRenderState, context);

            ((PFMFabricBakedModel) model1).emitItemQuads(layerRenderState.emitter(), random);
        } else {
            List<BlockStateModelPart> parts = new ArrayList<>();
            model.collectParts(random, parts);
            for (Direction direction : Direction.values()) {
                layerRenderState.setQuads(ItemQuads.split(parts.stream().flatMap(p -> p.getQuads(direction).stream()).toList()));
            }
            layerRenderState.setQuads(ItemQuads.split(parts.stream().flatMap(p -> p.getQuads(null).stream()).toList()));
        }
    }
}
