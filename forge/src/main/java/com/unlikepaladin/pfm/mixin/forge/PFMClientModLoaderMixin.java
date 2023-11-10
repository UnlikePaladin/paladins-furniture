package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import net.minecraftforge.fml.client.ClientModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientModLoader.class)
public class PFMClientModLoaderMixin {
    @Inject(method = "begin", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/DataPackSettings;addModPacks(Ljava/util/List;)V", shift = At.Shift.BEFORE))
    private static void addPFMClientPack(MinecraftClient minecraft, ResourcePackManager defaultResourcePacks, ReloadableResourceManager mcResourceManager, ClientBuiltinResourcePackProvider metadataSerializer, CallbackInfo ci) {
        PackResourceMetadata packResourceMetadata = new PackResourceMetadata(new LiteralText("Runtime Generated Assets for PFM"), SharedConstants.getGameVersion().getPackVersion());
        defaultResourcePacks.addPackFinder((profileAdder, factory) -> profileAdder.accept(ResourcePackProfile.of("PFM Assets", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunAssetGen(false); return PFMRuntimeResources.ASSETS_PACK;
        }), packResourceMetadata), factory, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.field_25347)));
    }
}
