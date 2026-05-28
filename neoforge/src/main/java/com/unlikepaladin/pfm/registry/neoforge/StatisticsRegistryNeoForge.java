package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

public class StatisticsRegistryNeoForge {
    @SubscribeEvent
    public static void registerStatistics(RegisterEvent event) {
        event.register(BuiltInRegistries.STAT_TYPE.key(),
                statRegisterHelper -> {
            StatisticsRegistry.registerStatistics();
        });
    }
}
