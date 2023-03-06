package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class ParticleProviderRegistryFabric {

    public static void registerParticleFactories() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(PaladinFurnitureMod.MOD_ID, "particle/shower_water"));
        }));

        ParticleFactoryRegistry.getInstance().register(ParticleIDs.WATER_DROP, BlockLeakParticle.FallingWaterFactory::new);
    }
}
