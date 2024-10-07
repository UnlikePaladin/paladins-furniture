package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.resource.*;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ModResourcePackCreator.class)
public class PFMModResourcePackCreatorMixin {
    @Final
    @Shadow
    private ResourceType type;

    @Inject(method = "register", at = @At("TAIL"))
    private void addPFMResources(Consumer<ResourcePackProfile> consumer, CallbackInfo ci) {
        if (type == ResourceType.CLIENT_RESOURCES) {
            AbstractBakedModel.reloading = true;
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES), Optional.empty());
            ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(String name) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata);
                }

                @Override
                public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                    return this.open(name);
                }
            };
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Assets for PFM"), ResourcePackCompatibility.COMPATIBLE, FeatureFlags.DEFAULT_ENABLED_FEATURES, List.of());
            consumer.accept(ResourcePackProfile.of("pfm-asset-resources", Text.literal("PFM Assets"), true,  packFactory, metadata, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.SERVER_DATA), Optional.empty());
            ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(String name) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata);
                }

                @Override
                public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                    return this.open(name);
                }
            };
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Data for PFM"), ResourcePackCompatibility.COMPATIBLE, FeatureFlags.DEFAULT_ENABLED_FEATURES, List.of());
            consumer.accept(ResourcePackProfile.of("pfm-data-resources", Text.literal("PFM Data"), true,  packFactory, metadata, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        }
    }
}
