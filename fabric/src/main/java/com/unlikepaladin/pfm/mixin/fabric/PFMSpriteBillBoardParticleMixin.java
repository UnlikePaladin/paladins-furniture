package com.unlikepaladin.pfm.mixin.fabric;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BillboardParticle.class)
public interface PFMSpriteBillBoardParticleMixin {
    @Invoker("setSprite")
    @Intrinsic
    void setSprite(Sprite sprite);
}
