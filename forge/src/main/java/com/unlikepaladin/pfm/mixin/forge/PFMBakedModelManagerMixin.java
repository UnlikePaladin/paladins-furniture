package com.unlikepaladin.pfm.mixin.forge;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.forge.PFMExtraModelsForge;
import com.unlikepaladin.pfm.client.forge.PaladinFurnitureModClientForge;
import com.unlikepaladin.pfm.ducks.forge.PFMBakedModelManagerExtensions;
import com.unlikepaladin.pfm.ducks.forge.PFModelBakerBakedExtensions;
import net.minecraft.client.item.ItemAssetsLoader;
import net.minecraft.client.render.model.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
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

@Mixin(BakedModelManager.class)
public abstract class PFMBakedModelManagerMixin implements PFMBakedModelManagerExtensions {

    @Shadow
    public abstract BlockStateModel getMissingModel();

    @Unique
    @Nullable
    private Map<Identifier, BlockStateModel> pfm$extraModels;

    @Inject(method = "collect", at = @At(value = "INVOKE", target = "net/minecraft/client/render/model/ReferencedModelsCollector.collectModels()Ljava/util/Map;"))
    private static void resolveExtraModels(
            Map<Identifier, UnbakedModel> modelMap, BlockStatesLoader.LoadedModels stateDefinition, ItemAssetsLoader.Result result, CallbackInfoReturnable<?> cir,
            @Local ReferencedModelsCollector collector
    ) {
        PFMExtraModelsForge.unbakedModels.forEach((id, block) -> collector.resolve(block));
    }

    @Inject(method = "reload", at = @At("HEAD"))
    private void onHeadReload(ResourceReloader.Store arg, Executor executor, ResourceReloader.Synchronizer arg2, Executor executor2, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        List<Identifier> ids = new ArrayList<>();
        PaladinFurnitureModClientForge.registerExtraModels(ids::add);
        PFMExtraModelsForge.registerExtraModels(ids);
    }

    @Inject(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onModelBake(Lnet/minecraft/client/render/model/BakedModelManager;Lnet/minecraft/client/render/model/ModelBaker;)V", ordinal = 0))
    private void onUpload(CallbackInfo ci, @Local ModelBaker.BakedModels bakedModels) {
        pfm$extraModels = ((PFModelBakerBakedExtensions) (Object) bakedModels).pfm_getExtraModels();
    }

    @Override
    public BlockStateModel pfm_getModel(Identifier id) {
        if (pfm$extraModels == null) {
            return getMissingModel();
        }
        return pfm$extraModels.getOrDefault(id, getMissingModel());
    }
}
