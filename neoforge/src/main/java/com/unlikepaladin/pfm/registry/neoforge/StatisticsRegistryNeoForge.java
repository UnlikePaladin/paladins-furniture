package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatisticsRegistryNeoForge {
    @SubscribeEvent
    public static void registerStatistics(RegisterEvent event) {
        event.register(Registries.STAT_TYPE.getKey(),
                statRegisterHelper -> {
            StatisticsRegistry.registerStatistics();
        });
    }
}
