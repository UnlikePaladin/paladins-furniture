package com.unlikepaladin.pfm.advancements;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions
            implements AbstractCriterion.Conditions {
        public static final Codec<GiveGuideBookCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(GiveGuideBookCriterion.Conditions::player)).apply(instance, GiveGuideBookCriterion.Conditions::new);
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
