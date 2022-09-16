package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPacketListener {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void pfm$showUpdateMessage(GameJoinS2CPacket par1, CallbackInfo ci) {
        if (this.client.player == null) {
            return;
        }

        PaladinFurnitureMod.getUpdateChecker().getUpdateMessage().ifPresent(msg ->
                this.client.player.sendMessage(msg, false));
    }
}
