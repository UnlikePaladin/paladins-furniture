package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleProviderRegistryForge {

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleIDs.WATER_DROP, BlockLeakParticle.FallingWaterFactory::new);
    }
}
