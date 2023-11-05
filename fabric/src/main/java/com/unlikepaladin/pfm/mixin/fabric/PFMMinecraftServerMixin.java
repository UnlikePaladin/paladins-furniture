package com.unlikepaladin.pfm.mixin.fabric;

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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

@Mixin(MinecraftServer.class)
public class PFMMinecraftServerMixin {

    @Inject(method = "method_29442", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void createReload(CallbackInfoReturnable<ImmutableList<ResourcePack>> cir) {
        PFMRuntimeResources.RESOURCE_PACK_LIST = new ArrayList<>(cir.getReturnValue());
        List<ResourcePack> resourcePacks = new ArrayList<>(cir.getReturnValue());
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("pfm-runtime-resources"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE));
        resourcePacks.add(new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunResourceGen(false); return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata));
        cir.setReturnValue(ImmutableList.copyOf(resourcePacks));
    }
}