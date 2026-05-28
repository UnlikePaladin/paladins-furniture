package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.PackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldLoader.PackConfig.class)
public class PFMSaveLoaderMixin {

    @ModifyArg(method = "createResourceManager", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/MultiPackResourceManager;<init>(Lnet/minecraft/server/packs/PackType;Ljava/util/List;)V"), index = 1)
    private List<PackResources> createReload(List<PackResources> packs) {
        List<PackResources> resourcePacks = new ArrayList<>(packs);
        resourcePacks.removeIf(pack -> pack instanceof PathPackRPWrapper);
        PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
        PFMRuntimeResources.ready = true;
        return packs;
    }
}
