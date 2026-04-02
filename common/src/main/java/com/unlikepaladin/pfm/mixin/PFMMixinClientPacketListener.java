package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class PFMMixinClientPacketListener {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void pfm$showModUpdateMessage(ClientboundLoginPacket par1, CallbackInfo ci) {
        if (this.minecraft.player == null) {
            return;
        }

        PaladinFurnitureMod.getUpdateChecker().getUpdateMessage().ifPresent(msg ->
                this.minecraft.player.displayClientMessage(msg, false));
    }
}
