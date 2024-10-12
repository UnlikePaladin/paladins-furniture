package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class ParticleTypeRegistryFabric {
    public static void registerParticleTypes() {
        ParticleIDs.WATER_DROP = FabricParticleTypes.simple();
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(PaladinFurnitureMod.MOD_ID, "shower_water"), ParticleIDs.WATER_DROP);
    }
}
