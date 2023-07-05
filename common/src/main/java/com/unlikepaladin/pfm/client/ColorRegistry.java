package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ColorRegistry {

    public static void registerBlockColors(){
        List<Block> sinks = new ArrayList<>();
        KitchenSinkBlock.streamStoneSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        KitchenSinkBlock.streamWoodSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        BasicSinkBlock.streamSinks().forEach(sinks::add);
        sinks.forEach(block -> registerBlockColor(block, addWaterColor()));
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_TOILET, addToiletColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB, addWaterColor());
    }

    public static void registerBlockRenderLayers() {
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.IRON_CHAIN, RenderLayer.getCutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, RenderLayer.getCutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.WHITE_MIRROR, RenderLayer.getCutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.GRAY_MIRROR, RenderLayer.getCutout());
    }

    public static void registerItemColors() {
        registerItemColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB.asItem(), (stack, index) -> index == 1 ?  0x3c44a9 : 0xFFFFFF);
    }

    @ExpectPlatform
    public static void registerItemColor(Item item, ItemColorProvider colorProvider) {
        throw new RuntimeException();
    }
    @ExpectPlatform
    public static void registerBlockColor(Block block, BlockColorProvider blockColorProvider){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerBlockToRenderLayer(Block block, RenderLayer renderLayer){
        throw new RuntimeException();
    }

    private static BlockColorProvider addToiletColor() {
        return (state, view, pos, index) -> state.get(BasicToiletBlock.TOILET_STATE) !=  ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230;
    }

    private static BlockColorProvider addWaterColor() {
        return (state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF;
    }
}
