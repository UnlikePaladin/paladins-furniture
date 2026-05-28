package com.unlikepaladin.pfm.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.BlockItem;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BreakingItemParticle.Provider.class)
public class PFMCrackParticle$ItemFactoryMixin {
    @ModifyReturnValue(method = "createParticle(Lnet/minecraft/core/particles/ItemParticleOption;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
    public Particle modifyParticle(Particle original, ItemParticleOption itemStackParticleEffect, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        if (itemStackParticleEffect.getItem().getItem() instanceof BlockItem) {
            BlockState defaultState = ((BlockItem)itemStackParticleEffect.getItem().getItem()).getBlock().defaultBlockState();
            BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(defaultState);
            if (model instanceof PFMBakedModelParticleExtension) {
                ((PFMSpriteBillBoardParticleMixin)original).setSprite(((PFMBakedModelParticleExtension) model).pfm$getParticle(clientWorld, BlockPos.containing(d, e, f), defaultState));
            }
        }
        return original;
    }
}
