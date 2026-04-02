package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class GiveGuideBookCriterion extends SimpleCriterionTrigger<GiveGuideBookCriterion.Conditions> {
    public static final ResourceLocation ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "give_book");

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new GiveGuideBookCriterion.Conditions(playerPredicate);
    }

    public static class Conditions
            extends AbstractCriterionTriggerInstance {
        public Conditions(Optional<ContextAwarePredicate> playerPredicate) {
            super(playerPredicate);
        }
    }
}
