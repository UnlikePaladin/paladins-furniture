package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class PFMInventoryMixin {
    @Final
    @Shadow
    public Player player;
    @Inject(at = @At("HEAD"), method = "setChanged")
    private void updateWorkbenchScreenInventoryOnSetStack(CallbackInfo ci) {
        if (player.containerMenu instanceof WorkbenchScreenHandler) {
            ((WorkbenchScreenHandler)player.containerMenu).updateInput();
        }
    }
}