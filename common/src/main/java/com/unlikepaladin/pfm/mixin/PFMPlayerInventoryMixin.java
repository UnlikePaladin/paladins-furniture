package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.menus.NewWorkbenchScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PFMPlayerInventoryMixin {
    @Final
    @Shadow
    public PlayerEntity player;
    @Inject(at = @At("HEAD"), method = "markDirty")
    private void updateWorkbenchScreenInventoryOnSetStack(CallbackInfo ci) {
        if (player.currentScreenHandler instanceof NewWorkbenchScreenHandler) {
            ((NewWorkbenchScreenHandler)player.currentScreenHandler).updateInput();
        }
    }
}