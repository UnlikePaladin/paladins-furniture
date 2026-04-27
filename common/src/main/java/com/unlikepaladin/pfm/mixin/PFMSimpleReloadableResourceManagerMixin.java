package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = SimpleReloadableResourceManager.class)
public class PFMSimpleReloadableResourceManagerMixin {

    @Inject(at = @At(value = "HEAD"), method = "createReload")
    private void createReload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<PackResources> packs, CallbackInfoReturnable<ReloadInstance> cir) {
        PFMRuntimeResources.modelCacheMap.clear();
        ModelHelper.blockToTextureMap.clear();
        ModelHelper.GENERATED_TEXTURE_IDS.clear();
        if (ModelHelper.OAK_SPRITES_PLANKS_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_PLANKS_TO_REPLACE = null;
        if (ModelHelper.OAK_SPRITES_BED_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_BED_TO_REPLACE = null;
        if (ModelHelper.OAK_SPRITES_LOG_TOP_TO_REPLACE != null)
            ModelHelper.OAK_SPRITES_LOG_TOP_TO_REPLACE = null;
    }
}