package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface ItemRenderState$LayerRenderStateAccessor {
    @Accessor("tintLayers")
    int[] getTints();
}
