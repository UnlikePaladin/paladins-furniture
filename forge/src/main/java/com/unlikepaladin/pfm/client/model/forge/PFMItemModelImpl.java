package com.unlikepaladin.pfm.client.model.forge;

import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.client.model.PFMBakedModelGetQuadsExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.registry.TriFunc;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PFMItemModelImpl {
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc() {
        return PFMItemModel::new;
    }

    public static void emitItemModelQuads(ItemStackRenderState.LayerRenderState layerRenderState, BlockStateModel model, ItemDisplayContext context, RandomSource random) {
        if (model instanceof PFMForgeBakedModel) {
            if (((PFMForgeBakedModel) model).getItemDisplaySettings() != null)
                ((PFMForgeBakedModel) model).getItemDisplaySettings().applyToLayer(layerRenderState, context);

            List<BakedQuad> quads = new ArrayList<>();
            long seed = 42L;
            for (Direction direction : Direction.values()) {
                random.setSeed(seed);
                quads.addAll(((PFMBakedModelGetQuadsExtension) model).getQuadsCached(direction, random));
            }

            random.setSeed(seed);
            quads.addAll(((PFMBakedModelGetQuadsExtension) model).getQuadsCached(null, random));
            layerRenderState.setExtents(() -> BlockModelWrapper.computeExtents(quads));
            layerRenderState.prepareQuadList().addAll(quads);
        } else {
            List<BlockModelPart> parts;
            parts = model.collectParts(random);
            List<BakedQuad> quads = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                quads.addAll(parts.stream().flatMap(p -> p.getQuads(direction).stream()).toList());
            }
            quads.addAll(parts.stream().flatMap(p -> p.getQuads(null).stream()).toList());
            layerRenderState.setExtents(() -> BlockModelWrapper.computeExtents(quads));
            layerRenderState.prepareQuadList().addAll(quads);
        }
    }
}
