package com.unlikepaladin.pfm.advancements.neoforge;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid="pfm", bus= Mod.EventBusSubscriber.Bus.MOD)
public class CriteriaRegistryNeoForge {
    @SubscribeEvent
    public static void registerCriteria(FMLCommonSetupEvent event) {
        PFMCriteria.GUIDE_BOOK_CRITERION = Criteria.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
    }
}