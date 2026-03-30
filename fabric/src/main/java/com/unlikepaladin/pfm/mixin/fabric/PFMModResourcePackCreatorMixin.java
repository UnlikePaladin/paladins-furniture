package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.mojang.bridge.game.PackType;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ModResourcePackCreator.class)
public class PFMModResourcePackCreatorMixin {

    @Shadow
    @Final
    private net.minecraft.server.packs.PackType type;

    @Inject(method = "register", at = @At("TAIL"))
    private void addPFMResources(Consumer<ResourcePackProfile> consumer, CallbackInfo ci) {
        if (type == ResourceType.CLIENT_RESOURCES) {
            AbstractBakedModel.reloading = true;
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE));
            ResourcePackProfile.PackFactory packFactory = name -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata);
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getPackVersion(PackType.RESOURCE), FeatureFlags.DEFAULT_ENABLED_FEATURES);
            consumer.accept(ResourcePackProfile.of("pfm-asset-resources", Text.literal("PFM Assets"), true,  packFactory, metadata, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getPackVersion(PackType.DATA));
            ResourcePackProfile.PackFactory packFactory = name -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata);
            ResourcePackProfile.Metadata metadata = new ResourcePackProfile.Metadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getPackVersion(PackType.DATA), FeatureFlags.DEFAULT_ENABLED_FEATURES);
            consumer.accept(ResourcePackProfile.of("pfm-data-resources", Text.literal("PFM Data"), true,  packFactory, metadata, ResourceType.SERVER_DATA, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.NONE));
        }
    }
}
