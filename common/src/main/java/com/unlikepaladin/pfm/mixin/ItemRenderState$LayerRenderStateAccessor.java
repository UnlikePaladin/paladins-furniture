package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface ItemRenderState$LayerRenderStateAccessor {
    @Accessor
    BakedModel getModel();

    @Accessor("tintLayers")
    int[] getTints();
}
