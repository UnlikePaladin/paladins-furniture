package com.unlikepaladin.pfm.mixin.fabric;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.render.model.BlockStateModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WrapperBlockStateModel.class)
public interface PFMWrapperBlockstateModelAccessor {
    @Accessor("wrapped")
    BlockStateModel pfm$getWrapped();
}
