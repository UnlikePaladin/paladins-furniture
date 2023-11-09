package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ModResourcePackCreator.class)
public class PFMModResourcePackCreatorMixin {
    @Final
    @Shadow
    private ResourceType type;

    @Inject(method = "register(Ljava/util/function/Consumer;Lnet/minecraft/resource/ResourcePackProfile$Factory;)V", at = @At("TAIL"))
    private void addPFMResources(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory, CallbackInfo ci) {
        if (type == ResourceType.CLIENT_RESOURCES) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("pfm-runtime-resources"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE));
            consumer.accept(factory.create("pfm-asset-resources", new LiteralText("PFM Assets"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        PFMRuntimeResources.prepareAndRunAssetGen(false); return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata)
                    , packResourceMetadata, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.PACK_SOURCE_NONE));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("pfm-runtime-data"), SharedConstants.getGameVersion().getPackVersion(PackType.DATA));
            consumer.accept(factory.create("pfm-data-resources", new LiteralText("PFM Data"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        PFMRuntimeResources.prepareAndRunDataGen(false); return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata)
                    , packResourceMetadata, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.PACK_SOURCE_NONE));
        }
    }
}
