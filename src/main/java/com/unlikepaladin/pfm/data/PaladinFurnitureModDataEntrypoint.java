package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tag.BlockTags;

public class PaladinFurnitureModDataEntrypoint implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
            dataGenerator.addProvider(PFMBlockTagProvider::new);
    }

    private static class PFMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
        private PFMBlockTagProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                    .add(BlockItemRegistry.QUARTZ_CHAIR)
                    .add(BlockItemRegistry.NETHERITE_CHAIR)
                    .add(BlockItemRegistry.FROGGY_CHAIR)
                    .add(BlockItemRegistry.FROGGY_CHAIR_PINK)
                    .add(BlockItemRegistry.FROGGY_CHAIR_BLUE)
                    .add(BlockItemRegistry.FROGGY_CHAIR_LIGHT_BLUE)
                    .add(BlockItemRegistry.FROGGY_CHAIR_ORANGE)
                    .add(BlockItemRegistry.FROGGY_CHAIR_YELLOW)
                    .add(BlockItemRegistry.WHITE_FREEZER)
                    .add(BlockItemRegistry.WHITE_FRIDGE)
                    .add(BlockItemRegistry.IRON_FREEZER)
                    .add(BlockItemRegistry.IRON_FRIDGE)
                    .add(BlockItemRegistry.XBOX_FRIDGE)
                    .add(BlockItemRegistry.SIMPLE_STOVE)
                    .add(BlockItemRegistry.IRON_STOVE)
                    .add(BlockItemRegistry.MICROWAVE)
                    .add(BlockItemRegistry.RAW_CONCRETE)
                    .add(BlockItemRegistry.IRON_CHAIN)
                    .add(BlockItemRegistry.FROGGY_CHAIR_LIGHT_BLUE)
                    .add(BlockItemRegistry.GRAY_MODERN_PENDANT)
                    .add(BlockItemRegistry.WHITE_MODERN_PENDANT)
                    .add(BlockItemRegistry.GLASS_MODERN_PENDANT)
                    .add(BlockItemRegistry.SIMPLE_LIGHT)
                    .add(BlockItemRegistry.LIGHT_SWITCH)
                    .add(BlockItemRegistry.CONCRETE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.CONCRETE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.CONCRETE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.CONCRETE_KITCHEN_SINK)
                    .add(BlockItemRegistry.CONCRETE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.DARK_CONCRETE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.DARK_CONCRETE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.DARK_CONCRETE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.DARK_CONCRETE_KITCHEN_SINK)
                    .add(BlockItemRegistry.DARK_CONCRETE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.DARK_WOOD_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.DARK_WOOD_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.DARK_WOOD_KITCHEN_CABINET)
                    .add(BlockItemRegistry.DARK_WOOD_KITCHEN_SINK)
                    .add(BlockItemRegistry.DARK_WOOD_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.LIGHT_WOOD_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.LIGHT_WOOD_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.LIGHT_WOOD_KITCHEN_CABINET)
                    .add(BlockItemRegistry.LIGHT_WOOD_KITCHEN_SINK)
                    .add(BlockItemRegistry.LIGHT_WOOD_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.GRANITE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.GRANITE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.GRANITE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.GRANITE_KITCHEN_SINK)
                    .add(BlockItemRegistry.GRANITE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.CALCITE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.CALCITE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.CALCITE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.CALCITE_KITCHEN_SINK)
                    .add(BlockItemRegistry.CALCITE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.ANDESITE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.ANDESITE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.ANDESITE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.ANDESITE_KITCHEN_SINK)
                    .add(BlockItemRegistry.ANDESITE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.NETHERITE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.NETHERITE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.NETHERITE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.NETHERITE_KITCHEN_SINK)
                    .add(BlockItemRegistry.NETHERITE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.DIORITE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.DIORITE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.DIORITE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.DIORITE_KITCHEN_SINK)
                    .add(BlockItemRegistry.DIORITE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.STONE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.STONE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.STONE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.STONE_KITCHEN_SINK)
                    .add(BlockItemRegistry.STONE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.BLACKSTONE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.BLACKSTONE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.BLACKSTONE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.BLACKSTONE_KITCHEN_SINK)
                    .add(BlockItemRegistry.BLACKSTONE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.DEEPSLATE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.DEEPSLATE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.DEEPSLATE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.DEEPSLATE_KITCHEN_SINK)
                    .add(BlockItemRegistry.DEEPSLATE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_COUNTER)
                    .add(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_DRAWER)
                    .add(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_CABINET)
                    .add(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_SINK)
                    .add(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_COUNTER_OVEN)
                    .add(BlockItemRegistry.KITCHEN_STOVETOP);
            }

        }
}
