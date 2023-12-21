package com.unlikepaladin.pfm.client.fabric;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;

public interface PFMBakedModelParticleExtension {
    Sprite pfm$getParticle(BlockState state);
}
