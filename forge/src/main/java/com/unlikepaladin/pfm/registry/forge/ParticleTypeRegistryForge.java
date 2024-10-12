package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryForge {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.PARTICLE_TYPES, particleTypeRegisterHelper -> {
            particleTypeRegisterHelper.register(Identifier.of(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP = new SimpleParticleType(false));
        });
    }
}
