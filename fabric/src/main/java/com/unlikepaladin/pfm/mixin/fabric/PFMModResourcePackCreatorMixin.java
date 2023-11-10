package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
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
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getPackVersion());
            consumer.accept(ResourcePackProfile.of("PFM Assets", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                PFMRuntimeResources.prepareAndRunAssetGen(false); return PFMRuntimeResources.ASSETS_PACK;
            }), packResourceMetadata), factory, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.field_25347));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getPackVersion());
            consumer.accept(ResourcePackProfile.of("PFM Data", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                PFMRuntimeResources.prepareAndRunDataGen(false); return PFMRuntimeResources.DATA_PACK;
            }), packResourceMetadata), factory, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.field_25347));
        }
    }
}
