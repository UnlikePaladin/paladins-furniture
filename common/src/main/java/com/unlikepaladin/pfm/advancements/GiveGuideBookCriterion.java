package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GiveGuideBookCriterion extends AbstractCriterion<GiveGuideBookCriterion.Conditions> {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "give_book");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public GiveGuideBookCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new GiveGuideBookCriterion.Conditions(extended);
    }

    public void trigger(ServerPlayerEntity player) {
        this.test(player, conditions -> true);
    }

    public static class Conditions
            extends AbstractCriterionConditions {
        public Conditions(EntityPredicate.Extended player) {
            super(ID, player);
        }

        @Override
        protected EntityPredicate.Extended getPlayerPredicate() {
            return super.getPlayerPredicate();
        }
    }
}
