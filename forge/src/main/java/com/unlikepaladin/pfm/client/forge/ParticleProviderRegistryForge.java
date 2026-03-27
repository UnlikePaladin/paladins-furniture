package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DripParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleProviderRegistryForge {

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleIDs.WATER_DROP, DripParticle.WaterFallProvider::new);
    }
}
