package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.SaveLoading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(SaveLoading.DataPacks.class)
public class PFMSaveLoaderMixin {

    @ModifyArg(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/LifecycledResourceManagerImpl;<init>(Lnet/minecraft/resource/ResourceType;Ljava/util/List;)V"), index = 1)
    private List<ResourcePack> createReload(List<ResourcePack> packs) {
        List<ResourcePack> resourcePacks = new ArrayList<>(packs);
        resourcePacks.removeIf(pack -> pack instanceof PathPackRPWrapper);
        PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
        PFMRuntimeResources.ready = true;
        return packs;
    }
}
