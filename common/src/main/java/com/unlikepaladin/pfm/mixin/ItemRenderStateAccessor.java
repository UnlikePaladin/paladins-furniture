package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.class)
public interface ItemRenderStateAccessor {
    @Accessor
    ItemStackRenderState.LayerRenderState[] getLayers();

    @Accessor("activeLayerCount")
    int getLayerCount();
}
