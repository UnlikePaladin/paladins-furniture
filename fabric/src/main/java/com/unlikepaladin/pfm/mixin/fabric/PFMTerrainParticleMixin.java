package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainParticle.class)
public abstract class PFMTerrainParticleMixin extends TextureSheetParticle {
    protected PFMTerrainParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
    public void setCustomModelParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci){
        if (state != null) {
            BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
            if (model instanceof PFMBakedModelParticleExtension) {
                this.setSprite(((PFMBakedModelParticleExtension) model).pfm$getParticle(world, BlockPos.containing(x, y, z), state));
            }
        }
    }
}