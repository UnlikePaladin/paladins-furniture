package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackRepository.class)
public abstract class PFMResourcePackManagerMixin {
    @Shadow public abstract void addPackFinder(RepositorySource packFinder);

    @Inject(method = "<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V", at = @At("TAIL"))
    private void addPFMDataPack(RepositorySource[] args, CallbackInfo ci) {
        PackMetadataSection packResourceMetadata = new PackMetadataSection(new TextComponent("Runtime Generated Data for PFM"), SharedConstants.getCurrentVersion().getPackVersion());
        this.addPackFinder((profileAdder, factory) -> profileAdder.accept(Pack.create("PFM Data", true, () -> new PathPackRPWrapper(Suppliers.memoize(() -> {
            PFMRuntimeResources.prepareAndRunDataGen(false); return PFMRuntimeResources.DATA_PACK;
        }), packResourceMetadata), factory, Pack.Position.BOTTOM, PackSource.DEFAULT)));
    }
}
