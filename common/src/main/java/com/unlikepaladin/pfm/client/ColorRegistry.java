package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Map;

public class ColorRegistry {
    public static final Map<ItemLike, ItemLike> itemColorProviders = new HashMap<>();

    public static void registerBlockColors(){
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_TOILET, addToiletColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB, addWaterColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_SINK, addWaterColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_LAMP, (state, world, pos, tintIndex) -> {
            if (world != null) {
                BlockEntity entity = world.getBlockEntity(pos);
                if (entity != null && tintIndex == 1) {
                    if (entity instanceof LampBlockEntity) {
                        DyeColor color = ((LampBlockEntity)entity).getPFMColor();
                        return color.getMapColor().col;
                    }
                } else if (entity != null && tintIndex == 0) {
                    if (entity instanceof LampBlockEntity && getBlockColor(((LampBlockEntity)entity).getVariant().getLogBlock()) != null) {
                        return getBlockColor(((LampBlockEntity)entity).getVariant().getLogBlock()).getColor(state, world, pos, tintIndex);
                    }
                }
            }
            return 0xFFFFFF;
        });
        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> {
            if (pfmModCompatibility.getClientModCompatiblity().isPresent()){
                pfmModCompatibility.getClientModCompatiblity().get().registerBlockColors();
            }
        });
        PaladinFurnitureMod.furnitureEntryMap.forEach((key, value) -> {
            value.getVariantToBlockMap().forEach((variantBase, block) -> {
                BlockColor blockColorProvider = getBlockColor(variantBase.getBaseBlock());
                if (key.isAssignableFrom(KitchenSinkBlock.class)) {
                    registerBlockColor(block, ((state, world, pos, tintIndex) -> {
                        if (tintIndex == 1) {
                            return addWaterColor().getColor(state, world, pos, tintIndex);
                        } else if (blockColorProvider == null) {
                            return 0xFFFFFFF;
                        }
                        return blockColorProvider.getColor(state, world, pos, tintIndex);
                    }));
                } else {
                    if (blockColorProvider != null) {
                        registerBlockColor(block, blockColorProvider);
                    }
                }
            });
            value.getVariantToBlockMapNonBase().forEach((variantBase, block) -> {
                BlockColor blockColorProvider = getBlockColor(variantBase.getBaseBlock());
                if (key.isAssignableFrom(KitchenSinkBlock.class)) {
                    registerBlockColor(block, ((state, world, pos, tintIndex) -> {
                        if (tintIndex == 1) {
                            return addWaterColor().getColor(state, world, pos, tintIndex);
                        } else if (blockColorProvider == null) {
                            return 0xFFFFFFF;
                        }
                        return blockColorProvider.getColor(state, world, pos, tintIndex);
                    }));
                } else {
                    if (blockColorProvider != null) {
                        registerBlockColor(block, blockColorProvider);
                    }
                }
            });
        });
    }

    public static void registerBlockRenderLayers() {
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.IRON_CHAIN, ChunkSectionLayer.CUTOUT);
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, ChunkSectionLayer.CUTOUT);
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.WHITE_MIRROR, ChunkSectionLayer.CUTOUT);
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.GRAY_MIRROR, ChunkSectionLayer.CUTOUT);
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.BASIC_LAMP, ChunkSectionLayer.CUTOUT);
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP, ChunkSectionLayer.CUTOUT);
    }

    public static void registerItemColors() {
        PaladinFurnitureMod.furnitureEntryMap.forEach((key, value) -> {
            value.getVariantToBlockMap().forEach((variantBase, block) -> {
                itemColorProviders.put(block, variantBase.getBaseBlock());
            });
            value.getVariantToBlockMapNonBase().forEach((variantBase, block) -> {
                itemColorProviders.put(block, variantBase.getBaseBlock());
            });
        });
    }

    @ExpectPlatform
    public static void registerBlockColor(Block block, BlockColor blockColorProvider){
        throw new RuntimeException();
    }
    @ExpectPlatform
    public static BlockColor getBlockColor(Block block){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerBlockToRenderLayer(Block block, ChunkSectionLayer renderLayer){
        throw new RuntimeException();
    }

    private static BlockColor addToiletColor() {
        return (state, view, pos, index) -> view != null && state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? BiomeColors.getAverageWaterColor(view, pos) : state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? 0x3c44a9 : 0x534230;
    }

    private static BlockColor addWaterColor() {
        return (state, view, pos, index) -> view != null && index == 1 ? BiomeColors.getAverageWaterColor(view, pos) : index == 1 ? 0x3c44a9 : 0xFFFFFF;
    }
}
