package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MixinDedicatedServer {
    @Shadow
    static Logger LOGGER;

    @Inject(method = "setupServer", at = @At("TAIL"))
    private void pfm$showUpdateMessage(CallbackInfoReturnable<Boolean> cir) {
        PaladinFurnitureMod.getUpdateChecker().getUpdateMessageServer().ifPresent(msg ->
                LOGGER.info(msg)
        );
    }
}
