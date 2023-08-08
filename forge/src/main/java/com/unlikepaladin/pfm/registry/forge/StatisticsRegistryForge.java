package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.stat.StatType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatisticsRegistryForge {
    @SubscribeEvent
    public static void registerStatistics(RegistryEvent.Register<StatType<?>> event) {
        StatisticsRegistry.registerStatistics();
    }
}
