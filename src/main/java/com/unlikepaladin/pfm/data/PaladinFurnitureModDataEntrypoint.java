package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTablesProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;

import java.util.function.Consumer;

public class PaladinFurnitureModDataEntrypoint implements DataGeneratorEntrypoint {
    public static final ConditionJsonProvider conditions = DefaultResourceConditions.anyModLoaded("sandwichable");
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
       // if (FabricLoader.getInstance().isModLoaded("sandwichable")) {
            dataGenerator.addProvider(SandwichableRecipeProvider::new);
            dataGenerator.addProvider(SandwichableLootTableProvider::new);
            dataGenerator.addProvider(SandwichableBlockTagProvider::new);
       // }
    }

    private static class SandwichableRecipeProvider extends FabricRecipesProvider {
        private SandwichableRecipeProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
            ShapedRecipeJsonFactory.create(PFMSandwichableRegistry.IRON_TOASTER, 1).input('A', Items.LEVER).input('B', Items.IRON_INGOT).input('D', Items.REDSTONE).pattern(" A ").pattern("BDB").pattern("BBB");
        }
    }

    private static class SandwichableLootTableProvider extends FabricBlockLootTablesProvider {
        private SandwichableLootTableProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateBlockLootTables() {
            this.addDrop(PFMSandwichableRegistry.IRON_TOASTER);
        }
    }

    private static class SandwichableBlockTagProvider extends FabricTagProvider.BlockTagProvider {
        private SandwichableBlockTagProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(PFMSandwichableRegistry.IRON_TOASTER);
        }
    }

}
