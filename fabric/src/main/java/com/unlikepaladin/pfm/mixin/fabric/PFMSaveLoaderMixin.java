package com.unlikepaladin.pfm.mixin.fabric;


import com.google.common.base.Suppliers;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.SaveLoading;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(SaveLoading.DataPacks.class)
public class PFMSaveLoaderMixin {

    @ModifyArg(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/LifecycledResourceManagerImpl;<init>(Lnet/minecraft/resource/ResourceType;Ljava/util/List;)V"), index = 1)
    private List<ResourcePack> createReload(List<ResourcePack> packs) {
        PFMRuntimeResources.RESOURCE_PACK_LIST = packs;
        List<ResourcePack> resourcePacks = new ArrayList<>(packs);
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("pfm-runtime-resources"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE));
        resourcePacks.add(new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunResourceGen(false);
            return PFMRuntimeResources.ASSETS_PACK;
        }), packResourceMetadata));
        return resourcePacks;
    }
}