package com.unlikepaladin.pfm.client.model.forge;

import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.TriFunc;
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
        return PFMItemModel::new;
    }

    public static void emitItemModelQuads(ItemRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, Random random) {
        if (model instanceof PFMForgeBakedModel) {
            if (((PFMForgeBakedModel) model).getItemDisplaySettings() != null)
                ((PFMForgeBakedModel) model).getItemDisplaySettings().addSettings(layerRenderState, context);

            long seed = 42L;
            for (Direction direction : Direction.values()) {
                random.setSeed(seed);
                layerRenderState.getQuads().addAll(((PFMBakedModelGetQuadsExtension) model).getQuadsCached(direction, random));
            }

            random.setSeed(seed);
            layerRenderState.getQuads().addAll(((PFMBakedModelGetQuadsExtension) model).getQuadsCached(null, random));
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
