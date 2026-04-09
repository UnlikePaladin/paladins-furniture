package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryNeoForge {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.PARTICLE_TYPE.key(), particleTypeRegisterHelper -> {
            particleTypeRegisterHelper.register(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP = new SimpleParticleType(false));
        });
    }
}
