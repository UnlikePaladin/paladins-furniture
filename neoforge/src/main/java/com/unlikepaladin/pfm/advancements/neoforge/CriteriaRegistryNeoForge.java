package com.unlikepaladin.pfm.advancements.neoforge;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid="pfm")
public class CriteriaRegistryNeoForge {
    @SubscribeEvent
    public static void registerCriteria(RegisterEvent event) {
        event.register(Registries.TRIGGER_TYPE, criterionRegisterHelper -> {
            PFMCriteria.GUIDE_BOOK_CRITERION = CriteriaTriggers.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
        });
    }
}