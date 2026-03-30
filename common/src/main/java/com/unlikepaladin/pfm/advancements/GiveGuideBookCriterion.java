package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

public class GiveGuideBookCriterion extends SimpleCriterionTrigger<GiveGuideBookCriterion.Conditions> {
    public static final ResourceLocation ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "give_book");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public GiveGuideBookCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite extended, DeserializationContext advancementEntityPredicateDeserializer) {
        return new GiveGuideBookCriterion.Conditions(extended);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    public static class Conditions
            extends AbstractCriterionTriggerInstance {
        public Conditions(EntityPredicate.Composite player) {
            super(ID, player);
        }
    }
}
