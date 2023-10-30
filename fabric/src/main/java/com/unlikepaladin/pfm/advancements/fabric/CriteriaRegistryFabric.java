package com.unlikepaladin.pfm.advancements.fabric;

import com.unlikepaladin.pfm.advancements.GiveGuideBookCriterion;
import com.unlikepaladin.pfm.advancements.PFMCriteria;
import net.minecraft.advancement.criterion.Criteria;

public class CriteriaRegistryFabric {
    public static void registerCriteria() {
        PFMCriteria.GUIDE_BOOK_CRITERION = Criteria.register(GiveGuideBookCriterion.ID.toString(), new GiveGuideBookCriterion());
    }
}
