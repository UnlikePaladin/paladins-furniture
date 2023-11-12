package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class PFMMinecraftServerMixin {

    @Inject(method = "lambda$reloadResources$15", at = @At(value = "RETURN"), remap = false)
    private void createReload(CallbackInfoReturnable<ImmutableList<ResourcePack>> cir) {
        List<ResourcePack> resourcePacks = new ArrayList<>(cir.getReturnValue());
        resourcePacks.removeIf(pack -> pack instanceof PathPackRPWrapper);
        PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
        PFMRuntimeResources.ready = true;
    }
}
