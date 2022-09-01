package com.unlikepaladin.pfm.client.forge;

import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.KitchenSink;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorRegistryForge {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
        List<Block> sinks = new ArrayList<>();
        KitchenSink.streamStoneSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        KitchenSink.streamWoodSinks().map(FurnitureBlock::getBlock).forEach(sinks::add);
        event.getBlockColors().registerColorProvider(addWaterColor(), sinks.toArray(new Block[0]));
        event.getBlockColors().registerColorProvider(addToiletColor(), PaladinFurnitureModBlocksItems.BASIC_TOILET);
    }

    private static BlockColorProvider addToiletColor() {
        return (state, view, pos, index) -> state.get(BasicToilet.TOILET_STATE) !=  ToiletState.DIRTY ? BiomeColors.getWaterColor(view, pos) : 0x534230;
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
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GRAY_STOVE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.WHITE_STOVE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.XBOX_FRIDGE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, RenderLayer.getCutout());
        RenderLayers.setRenderLayer(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, RenderLayer.getCutout());
    }
}
