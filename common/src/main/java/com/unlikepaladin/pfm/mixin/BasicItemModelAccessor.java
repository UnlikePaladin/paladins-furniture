package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
@Mixin(CuboidItemModelWrapper.class)
public interface BasicItemModelAccessor {
    @Accessor("tints")
    List<ItemTintSource> getTints();
}
