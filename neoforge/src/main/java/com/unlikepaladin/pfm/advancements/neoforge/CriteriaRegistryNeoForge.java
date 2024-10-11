package com.unlikepaladin.pfm.advancements.neoforge;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid="pfm", bus= EventBusSubscriber.Bus.MOD)
public class CriteriaRegistryNeoForge {
    @SubscribeEvent
    public static void registerCriteria(RegisterEvent event) {
        event.register(RegistryKeys.CRITERION, criterionRegisterHelper -> {
            PFMCriteria.GUIDE_BOOK_CRITERION = Criteria.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
        });
    }
}