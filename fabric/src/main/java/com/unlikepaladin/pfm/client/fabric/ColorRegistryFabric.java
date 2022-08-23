package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class ColorRegistryFabric {
    public static void registerAll(){

        addWaterColor(PaladinFurnitureModBlocksItems.OAK_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.SPRUCE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.BIRCH_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.ACACIA_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.JUNGLE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DARK_OAK_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.CRIMSON_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.WARPED_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_OAK_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_DARK_OAK_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_BIRCH_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_SPRUCE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_ACACIA_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_JUNGLE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_CRIMSON_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STRIPPED_WARPED_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.CONCRETE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DARK_CONCRETE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DARK_WOOD_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.LIGHT_WOOD_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.GRANITE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DIORITE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.NETHERITE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.CALCITE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.ANDESITE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.SMOOTH_STONE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.STONE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.BLACKSTONE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DEEPSLATE_KITCHEN_SINK);
        addWaterColor(PaladinFurnitureModBlocksItems.DEEPSLATE_TILE_KITCHEN_SINK);

        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> state.get(BasicToilet.TOILET_STATE) !=  ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230, PaladinFurnitureModBlocksItems.BASIC_TOILET);

        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.XBOX_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_STOVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_STOVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.IRON_CHAIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.IRON_MICROWAVE, RenderLayer.getTranslucent());

    }

    private static void addWaterColor(Block block) {
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, block);
    }
}
