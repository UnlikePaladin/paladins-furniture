package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
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

    @Inject(method = "register", at = @At("TAIL"))
    private void addPFMResources(Consumer<ResourcePackProfile> consumer, CallbackInfo ci) {
        if (type == ResourceType.CLIENT_RESOURCES) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES));
            ResourcePackProfile.PackFactory packFactory = name -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                PFMRuntimeResources.prepareAndRunAssetGen(false); return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata);
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES), FeatureFlags.DEFAULT_ENABLED_FEATURES);
            consumer.accept(ResourcePackProfile.of("pfm-asset-resources", Text.literal("PFM Assets"), true,  packFactory, metadata, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.SERVER_DATA));
            ResourcePackProfile.PackFactory packFactory = name -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                PFMRuntimeResources.prepareAndRunDataGen(false); return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata);
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.SERVER_DATA), FeatureFlags.DEFAULT_ENABLED_FEATURES);
            consumer.accept(ResourcePackProfile.of("pfm-data-resources", Text.literal("PFM Data"), true,  packFactory, metadata, ResourceType.SERVER_DATA, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        }
    }
}
