package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.data.DataCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;


@Mixin(value = DataCache.CachedData.class)
public class PFMDataCache$CachedDataMixin {
    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),cancellable = true, remap = true)
    public void stopCacheWarning(Path root, Path dataProviderPath, String description, CallbackInfo ci) {
        if (PFMRuntimeResources.isAnyGeneratorRunning())
            ci.cancel();
    }
}