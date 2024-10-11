package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryNeoForge {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.PARTICLE_TYPE.getKey(), particleTypeRegisterHelper -> {
            particleTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP = new SimpleParticleType(false));
        });
    }
}
