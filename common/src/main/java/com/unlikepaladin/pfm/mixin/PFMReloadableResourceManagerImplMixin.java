package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ReloadableResourceManagerImpl.class)
public class PFMReloadableResourceManagerImplMixin {

    @Inject(at = @At(value = "HEAD"), method = "reload")
    private void createReload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir) {
        PFMRuntimeResources.modelCacheMap.clear();
        ModelHelper.blockToTextureMap.clear();
        if (ModelHelper.OAK_SPRITES_PLANKS_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_PLANKS_TO_REPLACE = null;
        if (ModelHelper.OAK_SPRITES_BED_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_BED_TO_REPLACE = null;
        if (ModelHelper.OAK_SPRITES_LOG_TOP_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_LOG_TOP_TO_REPLACE = null;
    }
}