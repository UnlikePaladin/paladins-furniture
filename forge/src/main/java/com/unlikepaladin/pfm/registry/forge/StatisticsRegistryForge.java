package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatisticsRegistryForge {
    @SubscribeEvent
    public static void registerStatistics(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.STAT_TYPES,
                statRegisterHelper -> {
            StatisticsRegistry.registerStatistics();
        });
    }
}
