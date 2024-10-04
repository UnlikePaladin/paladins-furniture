package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.PathPackRPWrapper;
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

    @ModifyVariable(at = @At(value = "HEAD"), method = "reload", argsOnly = true)
    private List<ResourcePack> createReload(List<ResourcePack> packs) {
        PFMRuntimeResources.modelCacheMap.clear();
        return packs;
    }
}