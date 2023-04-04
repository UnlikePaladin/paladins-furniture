package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.forge.FurnitureSerializerForge;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistryForge {

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(
                (RecipeTypes.FREEZING_RECIPE_SERIALIZER = new CookingRecipeSerializer<>(FreezingRecipe::new, 200)).setRegistryName(new Identifier(PaladinFurnitureMod.MOD_ID, "freezing"))
        );
        event.getRegistry().register(
                (RecipeTypes.FURNITURE_SERIALIZER = new FurnitureSerializerForge()).setRegistryName(new Identifier(PaladinFurnitureMod.MOD_ID, "furniture"))
        );
    }


    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        RecipeTypes.FREEZING_RECIPE = Registry.register(Registry.RECIPE_TYPE, PaladinFurnitureMod.MOD_ID + ":freezing",  new RecipeType<FreezingRecipe>() {
            @Override
            public String toString() {return "freezing";}
        });
        RecipeTypes.FURNITURE_RECIPE = Registry.register(Registry.RECIPE_TYPE, RecipeTypes.FURNITURE_ID,  new RecipeType<FurnitureRecipe>() {
            @Override
            public String toString() {return "furniture";}
        });
    }



}
