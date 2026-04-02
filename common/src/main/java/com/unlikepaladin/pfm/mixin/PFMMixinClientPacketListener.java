package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class PFMMixinClientPacketListener extends ClientCommonPacketListenerImpl {

    protected PFMMixinClientPacketListener(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void pfm$showModUpdateMessage(ClientboundLoginPacket par1, CallbackInfo ci) {
        if (this.minecraft.player == null) {
            return;
        }

        PaladinFurnitureMod.getUpdateChecker().getUpdateMessage().ifPresent(msg ->
                this.minecraft.player.displayClientMessage(msg, false));
    }
}
