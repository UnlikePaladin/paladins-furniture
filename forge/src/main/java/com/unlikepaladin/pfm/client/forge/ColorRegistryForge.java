package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorRegistryForge {
    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event){
        List<Block> sinks = new ArrayList<>();
        KitchenSinkBlock.streamStoneSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        KitchenSinkBlock.streamWoodSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        BasicSinkBlock.streamSinks().forEach(sinks::add);
        event.getBlockColors().registerColorProvider(addWaterColor(), sinks.toArray(new Block[0]));
        event.getBlockColors().registerColorProvider(addToiletColor(), PaladinFurnitureModBlocksItems.BASIC_TOILET);
        event.getBlockColors().registerColorProvider(addWaterColor(), PaladinFurnitureModBlocksItems.BASIC_BATHTUB);
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event){
        event.getItemColors().register((stack, index) -> index == 1 ?  0x3c44a9 : 0xFFFFFF, PaladinFurnitureModBlocksItems.BASIC_BATHTUB.asItem());
    }

    private static BlockColorProvider addToiletColor() {
        return (state, view, pos, index) -> state.get(BasicToiletBlock.TOILET_STATE) !=  ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230;
    }

    private static BlockColorProvider addWaterColor() {
        return (state, view, pos, index) -> index == 1 ? BiomeColors.getWaterColor(view, pos) : 0xFFFFFF;
    }

    public static void registerBlockRenderLayers() {
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.IRON_MICROWAVE, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.IRON_CHAIN, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GRAY_STOVE, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.WHITE_STOVE, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.IRON_STOVE, RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.XBOX_FRIDGE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, RenderLayer.getCutout());

        List<Block> ovens = new ArrayList<>();
        KitchenCounterOvenBlock.streamStoneCounterOvens().map(FurnitureBlock::getBlock).forEach(ovens::add);
        KitchenCounterOvenBlock.streamWoodCounterOvens().map(FurnitureBlock::getBlock).forEach(ovens::add);
        ovens.forEach(oven -> RenderLayers.setRenderLayer(oven, RenderLayer.getTranslucent()));
    }
}
