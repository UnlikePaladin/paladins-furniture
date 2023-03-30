package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class PFMMixinDedicatedServer {
    @Inject(method = "setupServer", at = @At("TAIL"))
    private void pfm$showUpdateMessage(CallbackInfoReturnable<Boolean> cir) {
        PaladinFurnitureMod.getUpdateChecker().getUpdateMessageServer().ifPresent(msg ->
                PaladinFurnitureMod.GENERAL_LOGGER.info(msg.getString())
        );
    }
}
