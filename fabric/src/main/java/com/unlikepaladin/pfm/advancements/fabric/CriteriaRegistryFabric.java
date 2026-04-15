package com.unlikepaladin.pfm.advancements.fabric;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancements.CriteriaTriggers;

public class CriteriaRegistryFabric {
    public static void registerCriteria() {
        PFMCriteria.GUIDE_BOOK_CRITERION = CriteriaTriggers.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
    }
}
