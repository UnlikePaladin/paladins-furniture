package com.unlikepaladin.pfm.mixin.fabric;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SingleQuadParticle.class)
public interface PFMSpriteBillBoardParticleMixin {
    @Invoker("setSprite")
    @Intrinsic
    void setSprite(TextureAtlasSprite sprite);
}
