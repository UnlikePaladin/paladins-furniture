package com.unlikepaladin.pfm.advancements.forge;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid="pfm", bus= Mod.EventBusSubscriber.Bus.MOD)
public class CriteriaRegistryForge {
    @SubscribeEvent
    public static void registerCriteria(FMLCommonSetupEvent event) {
        PFMCriteria.GUIDE_BOOK_CRITERION = Criteria.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
    }
}