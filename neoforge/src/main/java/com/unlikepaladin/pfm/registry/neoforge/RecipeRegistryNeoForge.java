package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;


public class RecipeRegistryNeoForge {

    @SubscribeEvent
    public static void registerRecipeSerializers(RegisterEvent event) {
        event.register(BuiltInRegistries.RECIPE_SERIALIZER.key(), recipeSerializerRegisterHelper -> {
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.FREEZING_ID, RecipeTypes.FREEZING_RECIPE_SERIALIZER = new RecipeSerializer<>(AbstractCookingRecipe.cookingMapCodec(FreezingRecipe::new, 200), AbstractCookingRecipe.cookingStreamCodec(FreezingRecipe::new))
            );
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.SIMPLE_FURNITURE_ID, RecipeTypes.SIMPLE_FURNITURE_SERIALIZER = new RecipeSerializer<>(SimpleFurnitureRecipe.Serializer.CODEC, SimpleFurnitureRecipe.Serializer.PACKET_CODEC)
            );
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.DYNAMIC_FURNITURE_ID, RecipeTypes.DYNAMIC_FURNITURE_SERIALIZER = new RecipeSerializer<>(DynamicFurnitureRecipe.Serializer.CODEC, DynamicFurnitureRecipe.Serializer.PACKET_CODEC)
            );
            // Can't run resource gen until the recipe serializer has been registered or it dies because it needs the ID
            // PFMRuntimeResources.prepareAsyncResourceGen(); Had to disable async gen because Forge dies and I can't be bothered to figure out why, this is cursed enough as it is
        });
    }


    @SubscribeEvent
    public static void registerRecipeTypes(RegisterEvent event){
        event.register(BuiltInRegistries.RECIPE_TYPE.key(), recipeTypeRegisterHelper -> {
            recipeTypeRegisterHelper.register(RecipeTypes.FREEZING_ID, RecipeTypes.FREEZING_RECIPE = new RecipeType<>() {
                @Override
                public String toString() {
                    return RecipeTypes.FREEZING_ID.getPath();
                }
            });
            recipeTypeRegisterHelper.register(RecipeTypes.FURNITURE_ID, RecipeTypes.FURNITURE_RECIPE = new RecipeType<>() {
                @Override
                public String toString() {
                    return RecipeTypes.FURNITURE_ID.getPath();
                }
            });
        });
    }



}
