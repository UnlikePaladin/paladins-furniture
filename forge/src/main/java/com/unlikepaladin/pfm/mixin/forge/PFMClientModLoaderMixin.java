package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.fml.client.ClientModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientModLoader.class)
public class PFMClientModLoaderMixin {
    @Inject(method = "begin", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/DataPackConfig;addModPacks(Ljava/util/List;)V", shift = At.Shift.BEFORE))
    private static void addPFMClientPack(Minecraft minecraft, PackRepository defaultResourcePacks, ReloadableResourceManager mcResourceManager, ClientPackSource metadataSerializer, CallbackInfo ci) {
        PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Assets for PFM"), SharedConstants.getCurrentVersion().getPackVersion());
        defaultResourcePacks.addPackFinder((profileAdder, factory) -> profileAdder.accept(Pack.create("PFM Assets", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunAssetGen(false); return PFMRuntimeResources.ASSETS_PACK;
        }), packResourceMetadata), factory, Pack.Position.BOTTOM, PackSource.DEFAULT)));
    }
}
