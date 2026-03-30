package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreakingItemParticle.class)
public abstract class PFMBreakingItemParticleMixin extends TextureSheetParticle {
    protected PFMBreakingItemParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    public void setCustomModelParticle(ClientLevel world, double x, double y, double z, ItemStack stack, CallbackInfo ci){
        if (stack.getItem() instanceof BlockItem) {
            BlockState defaultState = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
            BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(defaultState);
            if (model instanceof PFMBakedModelParticleExtension) {
                this.setSprite(((PFMBakedModelParticleExtension) model).pfm$getParticle(world, new BlockPos(x, y, z), defaultState));
            }
        }
    }
}
