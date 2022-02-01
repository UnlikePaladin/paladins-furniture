package com.unlikepaladin.pfm.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftDedicatedServer.class)
public class PlayerChairMixin {
   // @Inject(method = "", at = @At("TAIL"))
    private void setPlayerChair(CallbackInfo info) {
       // PlayerChairBlockEntity.setUserCache(userCache);
        //PlayerChairBlockEntity.setSessionService("minecraftSessionService");
        //PlayerChairBlockEntity.setExecutor(super);
    }
}
