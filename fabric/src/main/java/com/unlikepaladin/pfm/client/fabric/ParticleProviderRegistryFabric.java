package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.DripParticle;

public class ParticleProviderRegistryFabric {

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(ParticleIDs.WATER_DROP, provider -> {
            return (parameters, world, x, y, z, velocityX, velocityY, velocityZ, random) -> {
                return new DripParticle.WaterFallProvider(provider).createParticle
                (parameters, world, x, y, z, velocityX, velocityY, velocityZ, random);
            };
        });
    }
}
