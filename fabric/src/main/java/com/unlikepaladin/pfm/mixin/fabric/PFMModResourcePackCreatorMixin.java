package com.unlikepaladin.pfm.mixin.fabric;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.network.chat.TextComponent;
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
    private PackType type;

    @Inject(method = "loadPacks", at = @At("TAIL"))
    private void addPFMResources(Consumer<Pack> consumer, Pack.PackConstructor factory, CallbackInfo ci) {
        if (type == PackType.CLIENT_RESOURCES) {
            AbstractBakedModel.reloading = true;
            PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Assets for PFM"), SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.RESOURCE));
            consumer.accept(factory.create("pfm-asset-resources", new TextComponent("PFM Assets"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.areAssetsRunning())
                            PFMRuntimeResources.prepareAndRunAssetGen(false);
                        return PFMRuntimeResources.ASSETS_PACK;}), packResourceMetadata)
                    , packResourceMetadata, Pack.Position.BOTTOM, PackSource.DEFAULT));
        } else if (type == PackType.SERVER_DATA) {
            PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Data for PFM"), SharedConstants.getCurrentVersion().getPackVersion(com.mojang.bridge.game.PackType.DATA));
            consumer.accept(factory.create("pfm-data-resources", new TextComponent("PFM Data"), true,
                    () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
                        if (!PFMDataGenerator.isDataRunning())
                            PFMRuntimeResources.prepareAndRunDataGen(false);
                        return PFMRuntimeResources.DATA_PACK;}), packResourceMetadata)
                    , packResourceMetadata, Pack.Position.BOTTOM, PackSource.DEFAULT));
        }
    }
}
