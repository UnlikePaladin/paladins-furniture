package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class PFMMinecraftServerMixin {

    @Inject(method = "lambda$reloadResources$15", at = @At(value = "RETURN"), cancellable = true)
    private void createReload(CallbackInfoReturnable<ImmutableList<ResourcePack>> cir) {
        List<ResourcePack> resourcePacks = new ArrayList<>(cir.getReturnValue());
        if (PFMRuntimeResources.ready) {
            PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
            //PFMRuntimeResources.runAsyncResourceGen(); No async for anyone, too bad forge won't behave
            PFMRuntimeResources.prepareAndRunResourceGen(false);
            resourcePacks.add(PFMRuntimeResources.ASSETS_PACK);
        }
        cir.setReturnValue(ImmutableList.copyOf(resourcePacks));
    }
}
