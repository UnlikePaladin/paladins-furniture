package com.unlikepaladin.pfm.advancements.forge;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid="pfm", bus= Mod.EventBusSubscriber.Bus.MOD)
public class CriteriaRegistryForge {
    @SubscribeEvent
    public static void registerCriteria(RegisterEvent event) {
        event.register(RegistryKeys.CRITERION, (registerHelper) -> {
            PFMCriteria.GUIDE_BOOK_CRITERION = new GiveGuideBookCriterion();
            registerHelper.register(GiveGuideBookCriterion.ID, PFMCriteria.GUIDE_BOOK_CRITERION);
        });
    }
}