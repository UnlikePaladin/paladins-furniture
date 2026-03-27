package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleTypeRegistryForge {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(ParticleIDs.WATER_DROP = (SimpleParticleType) new SimpleParticleType(false).setRegistryName(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "shower_water")));
    }
}
