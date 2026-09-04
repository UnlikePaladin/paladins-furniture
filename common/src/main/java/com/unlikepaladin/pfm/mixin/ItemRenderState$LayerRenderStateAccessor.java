package com.unlikepaladin.pfm.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface ItemRenderState$LayerRenderStateAccessor {
    @Accessor("tintLayers")
    IntList getTints();
}
