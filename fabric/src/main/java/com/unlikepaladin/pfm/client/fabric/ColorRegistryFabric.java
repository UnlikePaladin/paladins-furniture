package com.unlikepaladin.pfm.client.fabric;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public class ColorRegistryFabric {
    public static void registerAll(){
        List<Block> sinks = new ArrayList<>();
        KitchenSinkBlock.streamStoneSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        KitchenSinkBlock.streamWoodSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        BasicSinkBlock.streamSinks().forEach(sinks::add);
        sinks.forEach(ColorRegistryFabric::addWaterColor);
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> state.get(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230, PaladinFurnitureModBlocksItems.BASIC_TOILET);
        ColorRegistryFabric.addWaterColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB);

        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.XBOX_FRIDGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_STOVE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_STOVE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.IRON_STOVE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.IRON_CHAIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.IRON_MICROWAVE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, RenderLayer.getCutout());
        List<Block> ovens = new ArrayList<>();
        KitchenCounterOvenBlock.streamStoneCounterOvens().map(FurnitureBlock::getBlock).forEach(ovens::add);
        KitchenCounterOvenBlock.streamWoodCounterOvens().map(FurnitureBlock::getBlock).forEach(ovens::add);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), ovens.toArray(new Block[0]));

        ColorProviderRegistry.ITEM.register((stack, index) -> index == 1 ?  0x3c44a9 : 0xFFFFFF, PaladinFurnitureModBlocksItems.BASIC_BATHTUB);
    }

    private static void addWaterColor(Block block) {
        ColorProviderRegistry.BLOCK.register((state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF, block);
    }
}
