package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.blocks.ArmChairDyeable;
import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ClassicChairDyeable;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class ColorRegistry {
    public static void registerAll(){
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ClassicChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.CHAIR_CLASSIC_WOOL);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ArmChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.ARM_CHAIR_STANDARD);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(ClassicChairDyeable.COLORID).getFireworkColor(), BlockItemRegistry.SOFA_SIMPLE);

        addWaterColor(BlockItemRegistry.OAK_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.SPRUCE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.BIRCH_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.ACACIA_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.JUNGLE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DARK_OAK_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.CRIMSON_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.WARPED_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_OAK_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_DARK_OAK_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_BIRCH_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_SPRUCE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_ACACIA_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_JUNGLE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_CRIMSON_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STRIPPED_WARPED_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.CONCRETE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DARK_CONCRETE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DARK_WOOD_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.LIGHT_WOOD_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.GRANITE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DIORITE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.NETHERITE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.CALCITE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.ANDESITE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.SMOOTH_STONE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.STONE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.BLACKSTONE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DEEPSLATE_KITCHEN_SINK);
        addWaterColor(BlockItemRegistry.DEEPSLATE_TILE_KITCHEN_SINK);

        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> state.get(BasicToilet.TOILET_STATE) !=  ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230, BlockItemRegistry.BASIC_TOILET);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.WHITE_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.XBOX_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.WHITE_STOVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.IRON_CHAIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.GRAY_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.WHITE_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.GLASS_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockItemRegistry.IRON_MICROWAVE, RenderLayer.getTranslucent());

    }

    private static void addWaterColor(Block block) {
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, block);
    }
}
