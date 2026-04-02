package com.unlikepaladin.pfm.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class GiveGuideBookCriterion extends SimpleCriterionTrigger<GiveGuideBookCriterion.Conditions> {
    public static final ResourceLocation ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "give_book");

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public static class Conditions
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<GiveGuideBookCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(GiveGuideBookCriterion.Conditions::player)).apply(instance, GiveGuideBookCriterion.Conditions::new);
        });
        private final Optional<ContextAwarePredicate> player;

        public Conditions(Optional<ContextAwarePredicate> playerPredicate) {
            this.player = playerPredicate;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
