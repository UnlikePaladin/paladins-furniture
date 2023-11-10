package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackManager.class)
public abstract class PFMResourcePackManagerMixin {
    @Shadow public abstract void addPackFinder(ResourcePackProvider packFinder);

    @Inject(method = "<init>([Lnet/minecraft/resource/ResourcePackProvider;)V", at = @At("TAIL"))
    private void addPFMDataPack(ResourcePackProvider[] args, CallbackInfo ci) {
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getPackVersion());
        this.addPackFinder((profileAdder, factory) -> profileAdder.accept(ResourcePackProfile.of("PFM Data", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunDataGen(false); return PFMRuntimeResources.DATA_PACK;
        }), packResourceMetadata), factory, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.field_25347)));
    }
}
