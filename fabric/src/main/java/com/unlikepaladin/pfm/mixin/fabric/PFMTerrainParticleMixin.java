package com.unlikepaladin.pfm.mixin.fabric;

import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainParticle.class)
public abstract class PFMTerrainParticleMixin extends SingleQuadParticle {
    protected PFMTerrainParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, d, e, f, textureAtlasSprite);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
    public void setCustomModelParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci){
        if (state != null) {
            BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
            if (model instanceof PFMBakedModelParticleExtension) {
                this.setSprite(((PFMBakedModelParticleExtension) model).pfm$getParticle(world, BlockPos.containing(x, y, z), state));
            }
        }
    }
}