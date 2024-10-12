package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class GiveGuideBookCriterion extends AbstractCriterion<GiveGuideBookCriterion.Conditions> {
    public static final Identifier ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "give_book");

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions
            implements AbstractCriterion.Conditions {
        public static final Codec<GiveGuideBookCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(GiveGuideBookCriterion.Conditions::player)).apply(instance, GiveGuideBookCriterion.Conditions::new);
        });
        private final Optional<LootContextPredicate> player;

        public Conditions(Optional<LootContextPredicate> playerPredicate) {
            this.player = playerPredicate;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return player;
        }
    }
}
