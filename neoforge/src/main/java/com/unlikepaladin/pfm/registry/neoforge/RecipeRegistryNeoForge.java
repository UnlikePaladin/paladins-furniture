package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import com.unlikepaladin.pfm.recipes.neoforge.FurnitureSerializerNeoForge;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;


@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistryNeoForge {

    @SubscribeEvent
    public static void registerRecipeSerializers(RegisterEvent event) {
        event.register(BuiltInRegistries.RECIPE_SERIALIZER.key(), recipeSerializerRegisterHelper -> {
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.FREEZING_ID, RecipeTypes.FREEZING_RECIPE_SERIALIZER = new SimpleCookingSerializer<>(FreezingRecipe::new, 200)
            );
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.SIMPLE_FURNITURE_ID, RecipeTypes.SIMPLE_FURNITURE_SERIALIZER = new FurnitureSerializerNeoForge<>(new SimpleFurnitureRecipe.Serializer())
            );
            recipeSerializerRegisterHelper.register(
                    RecipeTypes.DYNAMIC_FURNITURE_ID, RecipeTypes.DYNAMIC_FURNITURE_SERIALIZER = new FurnitureSerializerNeoForge<>(new DynamicFurnitureRecipe.Serializer())
            );
            // Can't run resource gen until the recipe serializer has been registered or it dies because it needs the ID
            // PFMRuntimeResources.prepareAsyncResourceGen(); Had to disable async gen because Forge dies and I can't be bothered to figure out why, this is cursed enough as it is
            if (PaladinFurnitureMod.getModList().contains("cookingforblockheads"))
                PFMCookingForBlockHeadsCompat.initBlockConnectors();
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
