package com.unlikepaladin.pfm.mixin.forge;

import com.google.common.base.Suppliers;
import com.unlikepaladin.pfm.client.PathPackRPWrapper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = SimpleReloadableResourceManager.class)
public class PFMSimpleReloadableResourceManagerMixin {

    @ModifyVariable(at = @At(value = "HEAD"), method = "createReload", argsOnly = true)
    private List<PackResources> createReload(List<PackResources> packs) {
        List<PackResources> resourcePacks = new ArrayList<>(packs);
        resourcePacks.removeIf(pack -> pack instanceof PathPackRPWrapper);
        PFMRuntimeResources.RESOURCE_PACK_LIST = resourcePacks;
        PFMRuntimeResources.ready = true;
        return packs;
    }
}