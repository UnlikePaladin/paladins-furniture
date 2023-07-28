package com.unlikepaladin.pfm.advancements.fabric;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class CriteriaRegistryFabric {
    public static void registerCriteria() {
        PFMCriteria.GUIDE_BOOK_CRITERION = CriterionRegistry.register(new GiveGuideBookCriterion());
    }
}
