package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryForge {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(ParticleIDs.WATER_DROP = (DefaultParticleType) new DefaultParticleType(false).setRegistryName(new Identifier(PaladinFurnitureMod.MOD_ID, "shower_water")));
    }
}
