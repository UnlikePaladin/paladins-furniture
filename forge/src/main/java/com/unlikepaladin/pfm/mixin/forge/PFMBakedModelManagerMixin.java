package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.forge.PFMExtraModelsForge;
import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelManager.class)
public abstract class PFMBakedModelManagerMixin implements PFMBakedModelManagerExtensions {
    @Shadow
    public abstract BlockStateModel getMissingBlockStateModel();

    @Unique
    @Nullable
    private Map<ResourceLocation, BlockStateModel> pfm$extraModels;

    @Inject(method = "discoverModelDependencies", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelDiscovery;resolve()Ljava/util/Map;"))
    private static void resolveExtraModels(
            Map<ResourceLocation, UnbakedModel> modelMap, BlockStateModelLoader.LoadedModels stateDefinition, ClientItemInfoLoader.LoadedClientInfos result, CallbackInfoReturnable<?> cir,
            @Local ModelDiscovery collector
    ) {
        PFMExtraModelsForge.unbakedModels.forEach((id, block) -> collector.addRoot(block));
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void onHeadReload(PreparableReloadListener.SharedState arg, Executor executor, PreparableReloadListener.PreparationBarrier arg2, Executor executor2, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        List<ResourceLocation> ids = new ArrayList<>();
        PaladinFurnitureModClientForge.registerExtraModels(ids::add);
        PFMExtraModelsForge.registerExtraModels(ids);
    }

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;replaceCache(Ljava/util/Map;)V", ordinal = 0))
    private void onUpload(CallbackInfo ci, @Local ModelBakery.BakingResult bakedModels) {
        pfm$extraModels = ((PFModelBakerBakedExtensions) (Object) bakedModels).pfm_getExtraModels();
    }

    @Override
    public BlockStateModel pfm_getModel(ResourceLocation id) {
        if (pfm$extraModels == null) {
            return getMissingBlockStateModel();
        }
        return pfm$extraModels.getOrDefault(id, getMissingBlockStateModel());
    }
}
