package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryNeoForge {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.PARTICLE_TYPE.getKey(), particleTypeRegisterHelper -> {
            particleTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP = new DefaultParticleType(false));
        });
    }
}
