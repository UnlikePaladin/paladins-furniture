package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.CompositeBlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CompositeBlockModel.class)
public interface PFMCompositeBlockModelAccessor {
    @Accessor("normal")
    BlockModel pfm$getNormal();
}
