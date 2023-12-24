package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrackParticle.class)
public abstract class PFMCrackParticleMixin extends SpriteBillboardParticle {
    protected PFMCrackParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    public void setCustomModelParticle(ClientWorld world, double x, double y, double z, ItemStack stack, CallbackInfo ci){
        if (stack.getItem() instanceof BlockItem) {
            BlockState defaultState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
            BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(defaultState);
            if (model instanceof PFMBakedModelParticleExtension) {
                this.setSprite(((PFMBakedModelParticleExtension) model).pfm$getParticle(world, new BlockPos(x, y, z), defaultState));
            }
        }
    }
}
