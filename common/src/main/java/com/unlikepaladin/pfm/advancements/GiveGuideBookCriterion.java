package com.unlikepaladin.pfm.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class GiveGuideBookCriterion extends SimpleCriterionTrigger<GiveGuideBookCriterion.Conditions> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "give_book");

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<GiveGuideBookCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(GiveGuideBookCriterion.Conditions::player)
            ).apply(instance, GiveGuideBookCriterion.Conditions::new)
        );
    }
}
