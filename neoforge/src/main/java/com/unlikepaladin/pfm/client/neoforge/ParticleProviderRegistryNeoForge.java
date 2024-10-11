package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.client.particle.BlockLeakParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleProviderRegistryNeoForge {

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSprite(ParticleIDs.WATER_DROP, BlockLeakParticle::createFallingWater);
    }
}
