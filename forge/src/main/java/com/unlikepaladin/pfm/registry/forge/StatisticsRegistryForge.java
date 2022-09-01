package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.util.registry.Registry;
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
                    Registry.register(Registry.CUSTOM_STAT, "fridge_opened", Statistics.FRIDGE_OPENED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.FRIDGE_OPENED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "sink_filled", Statistics.SINK_FILLED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.SINK_FILLED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "use_sink", Statistics.USE_SINK);
                            Stats.CUSTOM.getOrCreateStat(Statistics.USE_SINK, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "freezer_opened", Statistics.FREEZER_OPENED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.FREEZER_OPENED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "drawer_searched", Statistics.DRAWER_SEARCHED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.DRAWER_SEARCHED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "cabinet_searched", Statistics.CABINET_SEARCHED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.CABINET_SEARCHED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "stove_opened", Statistics.STOVE_OPENED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.STOVE_OPENED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "stovetop_used", Statistics.STOVETOP_USED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.STOVETOP_USED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "plate_used", Statistics.PLATE_USED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.PLATE_USED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "microwave_used", Statistics.MICROWAVE_USED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.MICROWAVE_USED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "chair_used", Statistics.CHAIR_USED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.CHAIR_USED, StatFormatter.DEFAULT);

                            Registry.register(Registry.CUSTOM_STAT, "toilet_used", Statistics.TOILET_USED);
                            Stats.CUSTOM.getOrCreateStat(Statistics.TOILET_USED, StatFormatter.DEFAULT);
                }
        );
    }
}
