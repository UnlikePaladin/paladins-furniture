package com.unlikepaladin.pfm.compat.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FreezingDisplay implements Display {
    public static final CategoryIdentifier<FreezingDisplay> IDENTIFIER = CategoryIdentifier.of(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "freezing"));
    public static final DisplaySerializer<FreezingDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(FreezingDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(FreezingDisplay::getOutputEntries),
                    ResourceLocation.CODEC.optionalFieldOf("location").forGetter(FreezingDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("cookTime").forGetter(d -> d.cookTime),
                    Codec.FLOAT.fieldOf("xp").forGetter(d -> d.xp)
            ).apply(instance, FreezingDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    FreezingDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    FreezingDisplay::getOutputEntries,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    FreezingDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    d -> d.cookTime,
                    ByteBufCodecs.FLOAT,
                    d -> d.xp,
                    FreezingDisplay::new
            ));

    public List<EntryIngredient> input;
    public List<EntryIngredient> output;
    public int cookTime;
    private final float xp;
    public Optional<ResourceLocation> location;

    public FreezingDisplay(RecipeHolder<FreezingRecipe> recipe) {
        input = Collections.singletonList(EntryIngredients.ofIngredient(recipe.value().input()));
        output = Collections.singletonList(EntryIngredients.of(recipe.value().result()));
        cookTime = recipe.value().cookingTime();
        xp = recipe.value().experience();
        location = Optional.of(recipe.id().location());
    }

    public FreezingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, int cookTime, float xp) {
        this.input = inputs;
        this.output = outputs;
        this.cookTime = cookTime;
        this.xp = xp;
        this.location = location;
    }

    public FreezingDisplay(FreezingRecipe freezingRecipe) {
        this.input = Collections.singletonList(EntryIngredients.ofIngredient(freezingRecipe.input()));
        this.output = Collections.singletonList(EntryIngredients.of(freezingRecipe.result()));
        this.cookTime = freezingRecipe.cookingTime();
        this.xp = freezingRecipe.experience();
        this.location = Optional.empty();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }


    public float getXp() {
        return xp;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return location;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
