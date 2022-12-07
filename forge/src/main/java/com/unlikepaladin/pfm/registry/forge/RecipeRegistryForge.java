package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureSerializer;
import com.unlikepaladin.pfm.recipes.forge.FurnitureRecipeSerializerForge;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistryForge {

   @SubscribeEvent
    public static void registerRecipeSerializers(RegisterEvent event){
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, recipeSerializerRegisterHelper -> {
            recipeSerializerRegisterHelper.register( new Identifier(PaladinFurnitureMod.MOD_ID, "freezing"), RecipeTypes.FREEZING_RECIPE_SERIALIZER = new CookingRecipeSerializer<>(FreezingRecipe::new, 200));
            recipeSerializerRegisterHelper.register( new Identifier(PaladinFurnitureMod.MOD_ID, "furniture"), RecipeTypes.FURNITURE_SERIALIZER = new FurnitureRecipeSerializerForge());
        });
    }


    @SubscribeEvent
    public static void registerRecipeTypes(RegisterEvent event){
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, recipeTypeRegisterHelper -> {
            recipeTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "freezing"), RecipeTypes.FREEZING_RECIPE = new RecipeType<FreezingRecipe>() {
                @Override
                public String toString() {return "freezing";}
            });
            recipeTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "furniture"), RecipeTypes.FURNITURE_RECIPE = new RecipeType<FurnitureRecipe>() {
                @Override
                public String toString() {return "furniture";}
            });
        });
    }



}
