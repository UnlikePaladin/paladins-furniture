package com.unlikepaladin.pfm.client.neoforge;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.client.particle.BlockLeakParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleProviderRegistryNeoForge {

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSprite(ParticleIDs.WATER_DROP, BlockLeakParticle::createFallingWater);
    }
}
