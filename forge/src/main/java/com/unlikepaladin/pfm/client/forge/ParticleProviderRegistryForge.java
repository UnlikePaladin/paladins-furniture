package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DripParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", value = Dist.CLIENT)
public class ParticleProviderRegistryForge {

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleIDs.WATER_DROP, DripParticle.WaterFallProvider::new);
    }
}
