package com.unlikepaladin.pfm.client.fabric;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PFMBakedModelParticleExtension {
    Sprite pfm$getParticle(BlockState state);

    Sprite pfm$getParticle(World world, BlockPos pos, BlockState state);
}
