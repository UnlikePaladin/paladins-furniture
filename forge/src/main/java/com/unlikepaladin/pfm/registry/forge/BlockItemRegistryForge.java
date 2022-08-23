package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockItemRegistryForge {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                //registerFurniture("working_table", PaladinFurnitureModBlocksItems.WORKING_TABLE, true),
                registerFurniture("oak_chair", PaladinFurnitureModBlocksItems.OAK_CHAIR, true),
                registerFurniture("birch_chair", PaladinFurnitureModBlocksItems.BIRCH_CHAIR, true),
                registerFurniture("spruce_chair", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR, true),
                registerFurniture("acacia_chair", PaladinFurnitureModBlocksItems.ACACIA_CHAIR, true),
                registerFurniture("jungle_chair", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR, true),
                registerFurniture("dark_oak_chair", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR, true),
                registerFurniture("warped_chair", PaladinFurnitureModBlocksItems.WARPED_CHAIR, true),
                registerFurniture("crimson_chair", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR, true),
                registerFurniture("stripped_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR, true),
                registerFurniture("stripped_birch_chair", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR, true),
                registerFurniture("stripped_spruce_chair", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR, true),
                registerFurniture("stripped_acacia_chair", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR, true),
                registerFurniture("stripped_jungle_chair", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR, true),
                registerFurniture("stripped_dark_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR, true),
                registerFurniture("stripped_warped_chair", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR, true),
                registerFurniture("stripped_crimson_chair", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR, true),
                registerFurniture("quartz_chair", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR, true),
                registerFurniture("netherite_chair", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR, true),
                registerFurniture("light_wood_chair", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR, true),
                registerFurniture("dark_wood_chair", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR, true),
                registerFurniture("granite_chair", PaladinFurnitureModBlocksItems.GRANITE_CHAIR, true),
                registerFurniture("calcite_chair", PaladinFurnitureModBlocksItems.CALCITE_CHAIR, true),
                registerFurniture("andesite_chair", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR, true),
                registerFurniture("diorite_chair", PaladinFurnitureModBlocksItems.DIORITE_CHAIR, true),
                registerFurniture("stone_chair", PaladinFurnitureModBlocksItems.STONE_CHAIR, true),
                registerFurniture("blackstone_chair", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR, true),
                registerFurniture("deepslate_chair", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR, true)
        );
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                //registerItem("working_table", PaladinFurnitureModBlocksItems.WORKING_TABLE),
                registerItem("oak_chair", PaladinFurnitureModBlocksItems.OAK_CHAIR),
                registerItem("birch_chair", PaladinFurnitureModBlocksItems.BIRCH_CHAIR),
                registerItem("spruce_chair", PaladinFurnitureModBlocksItems.SPRUCE_CHAIR),
                registerItem("acacia_chair", PaladinFurnitureModBlocksItems.ACACIA_CHAIR),
                registerItem("jungle_chair", PaladinFurnitureModBlocksItems.JUNGLE_CHAIR),
                registerItem("dark_oak_chair", PaladinFurnitureModBlocksItems.DARK_OAK_CHAIR),
                registerItem("warped_chair", PaladinFurnitureModBlocksItems.WARPED_CHAIR),
                registerItem("crimson_chair", PaladinFurnitureModBlocksItems.CRIMSON_CHAIR),
                registerItem("stripped_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_OAK_CHAIR),
                registerItem("stripped_birch_chair", PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_CHAIR),
                registerItem("stripped_spruce_chair", PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_CHAIR),
                registerItem("stripped_acacia_chair", PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_CHAIR),
                registerItem("stripped_jungle_chair", PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_CHAIR),
                registerItem("stripped_dark_oak_chair", PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_CHAIR),
                registerItem("stripped_warped_chair", PaladinFurnitureModBlocksItems.STRIPPED_WARPED_CHAIR),
                registerItem("stripped_crimson_chair", PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_CHAIR),
                registerItem("quartz_chair", PaladinFurnitureModBlocksItems.QUARTZ_CHAIR),
                registerItem("netherite_chair", PaladinFurnitureModBlocksItems.NETHERITE_CHAIR),
                registerItem("light_wood_chair", PaladinFurnitureModBlocksItems.LIGHT_WOOD_CHAIR),
                registerItem("dark_wood_chair", PaladinFurnitureModBlocksItems.DARK_WOOD_CHAIR),
                registerItem("granite_chair", PaladinFurnitureModBlocksItems.GRANITE_CHAIR),
                registerItem("calcite_chair", PaladinFurnitureModBlocksItems.CALCITE_CHAIR),
                registerItem("andesite_chair", PaladinFurnitureModBlocksItems.ANDESITE_CHAIR),
                registerItem("diorite_chair", PaladinFurnitureModBlocksItems.DIORITE_CHAIR),
                registerItem("stone_chair", PaladinFurnitureModBlocksItems.STONE_CHAIR),
                registerItem("blackstone_chair", PaladinFurnitureModBlocksItems.BLACKSTONE_CHAIR),
                registerItem("deepslate_chair", PaladinFurnitureModBlocksItems.DEEPSLATE_CHAIR)
        );
    }



    public static Block registerFurniture(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        }
        return registerBlock(blockName, block, false);
    }

    public static Block registerFurniture(String blockName, Block block, int count) {
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        return registerBlock(blockName, block, false);
    }

    public static Block registerBlock(String blockName, Block block, boolean registerItem) {
        if (registerItem) {
            PaladinFurnitureModBlocksItems.BLOCKS.add(block);
            //registerItem(blockName, new BlockItem(block, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        }
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            /*FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistry.INSTANCE.add(block, 300);*/
        }
        return block.setRegistryName(blockName);
    }

    public static Item registerItem(String itemName, Block block) {
        return new BlockItem(block, new Item.Settings().group(PaladinFurnitureMod.FURNITURE_GROUP)).setRegistryName(itemName);
    }

}
