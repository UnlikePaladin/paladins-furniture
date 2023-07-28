package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ReloadableResourceManagerImpl.class)
public class PFMReloadableResourceManagerImplMixin {
    private static int count = 0;
    @ModifyVariable(at = @At(value = "HEAD"), method = "reload", argsOnly = true)
    private List<ResourcePack> createReload(List<ResourcePack> packs) {
        List<ResourcePack> resourcePacks = new ArrayList<>(packs);
        if (PFMRuntimeResources.ready) {
            PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
            //PFMRuntimeResources.runAsyncResourceGen(); No async for anyone, too bad forge won't behave
            PFMRuntimeResources.prepareAndRunResourceGen(true);
            resourcePacks.add(PFMRuntimeResources.ASSETS_PACK);
        }
        return resourcePacks;
    }
}