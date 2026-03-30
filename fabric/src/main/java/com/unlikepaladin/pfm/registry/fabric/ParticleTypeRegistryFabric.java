package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class ParticleTypeRegistryFabric {
    public static void registerParticleTypes() {
        ParticleIDs.WATER_DROP = FabricParticleTypes.simple();
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP);
    }
}
