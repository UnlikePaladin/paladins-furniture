package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.utilities.Version;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.registry.VersionedIdentifier;
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
                public ResourcePack open(ResourcePackInfo info) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.getAssetsPack(info);}), packResourceMetadata, info);
                }

                @Override
                public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                    return open(info);
                }
            };
            consumer.accept(ResourcePackProfile.create(new ResourcePackInfo("pfm-asset-resources", Text.literal("PFM Assets"), ResourcePackSource.NONE, Optional.of(new VersionedIdentifier(PaladinFurnitureMod.MOD_ID, "pfm_assets", Version.getCurrentVersion()))), packFactory, ResourceType.CLIENT_RESOURCES, new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.BOTTOM, false)));
        } else if (type == ResourceType.SERVER_DATA) {
            PackResourceMetadata packResourceMetadata = new PackResourceMetadata(Text.literal("Runtime Generated Data for PFM"), SharedConstants.getGameVersion().getResourceVersion(ResourceType.SERVER_DATA), Optional.empty());
            ResourcePackProfile.PackFactory packFactory = new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(ResourcePackInfo name) {
                    return new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.getDataPack(name);}), packResourceMetadata, name);
                }

                @Override
                public ResourcePack openWithOverlays(ResourcePackInfo name, ResourcePackProfile.Metadata metadata) {
                    return this.open(name);
                }
            };
            consumer.accept(ResourcePackProfile.create(new ResourcePackInfo("pfm-data-resources", Text.literal("PFM Data"), ResourcePackSource.NONE, Optional.of(new VersionedIdentifier(PaladinFurnitureMod.MOD_ID, "pfm_data", Version.getCurrentVersion()))),  packFactory, ResourceType.SERVER_DATA, new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.BOTTOM, false)));
        }
    }
}
