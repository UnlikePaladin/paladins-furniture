package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class GiveGuideBookCriterion extends AbstractCriterion<GiveGuideBookCriterion.Conditions> {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "give_book");

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new GiveGuideBookCriterion.Conditions(playerPredicate);
    }

    public static class Conditions
            extends AbstractCriterionConditions {
        public Conditions(Optional<LootContextPredicate> playerPredicate) {
            super(playerPredicate);
        }
    }
}
