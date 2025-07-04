package com.unlikepaladin.pfm.mixin.neoforge;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PFMUnbakedBlockStateModel.class)
public interface PFMUnbakedBlockStateModelMixin extends CustomUnbakedBlockStateModel {
    @Override
    default @NotNull MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
        return (MapCodec<? extends CustomUnbakedBlockStateModel>) ((PFMUnbakedBlockStateModel)this).getCodec();
    }
}
