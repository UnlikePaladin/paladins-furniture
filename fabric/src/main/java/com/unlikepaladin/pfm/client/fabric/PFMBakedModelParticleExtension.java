package com.unlikepaladin.pfm.client.fabric;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface PFMBakedModelParticleExtension {
    TextureAtlasSprite pfm$getParticle(BlockState state);

    TextureAtlasSprite pfm$getParticle(Level world, BlockPos pos, BlockState state);
}
