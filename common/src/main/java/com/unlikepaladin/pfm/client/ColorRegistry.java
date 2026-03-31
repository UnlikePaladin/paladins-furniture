package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ColorRegistry {
    public static void registerBlockColors(){
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_TOILET, addToiletColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB, addWaterColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_SINK, addWaterColor());
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_LAMP, (state, world, pos, tintIndex) -> {
            if (world != null)    {
                BlockEntity entity = world.getBlockEntity(pos);
                if (entity != null && tintIndex == 1) {
                    if (entity instanceof LampBlockEntity) {
                        DyeColor color = ((LampBlockEntity)entity).getPFMColor();
                        return color.getFireworkColor();
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
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.IRON_CHAIN, RenderType.cutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, RenderType.cutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.WHITE_MIRROR, RenderType.cutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.GRAY_MIRROR, RenderType.cutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.BASIC_LAMP, RenderType.cutout());
        registerBlockToRenderLayer(PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP, RenderType.cutout());
    }

    public static void registerItemColors() {
        registerItemColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB.asItem(), (stack, index) -> index == 1 ?  0x3c44a9 : 0xFFFFFF);
        registerItemColor(PaladinFurnitureModBlocksItems.BASIC_SINK.asItem(), (stack, index) -> index == 1 ?  0x3c44a9 : 0xFFFFFF);
        registerItemColor(PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM, (stack, tintIndex) -> {
            if (stack.hasTag() && tintIndex == 1) {
                return DyeColor.byName(stack.getTagElement("BlockEntityTag").getString("color"), DyeColor.WHITE).getMaterialColor().col;
            } else if (stack.hasTag() && tintIndex == 0) {
                WoodVariant variantBase = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(stack.getTagElement("BlockEntityTag").getString("variant")));
                if (getItemColor(variantBase.getLogBlock().asItem()) != null) {
                    return getItemColor(variantBase.getLogBlock().asItem()).getColor(stack, tintIndex);
                }
            }
            return 0xFFFFFF;
        });

        PaladinFurnitureMod.furnitureEntryMap.forEach((key, value) -> {
            value.getVariantToBlockMap().forEach((variantBase, block) -> {
                ItemColor itemColorProvider = getItemColor(variantBase.getBaseBlock().asItem());
                if (itemColorProvider != null) {
                    registerItemColor(block.asItem(), itemColorProvider);
                }
            });
            value.getVariantToBlockMapNonBase().forEach((variantBase, block) -> {
                ItemColor itemColorProvider = getItemColor(variantBase.getBaseBlock().asItem());
                if (itemColorProvider != null) {
                    registerItemColor(block.asItem(), itemColorProvider);
                }
            });
        });
    }

    @ExpectPlatform
    public static void registerItemColor(Item item, ItemColor colorProvider) {
        throw new RuntimeException();
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
    public static ItemColor getItemColor(Item item){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerBlockToRenderLayer(Block block, RenderType renderLayer){
        throw new RuntimeException();
    }

    private static BlockColor addToiletColor() {
        return (state, view, pos, index) -> view != null && state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? BiomeColors.getAverageWaterColor(view, pos) : state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? 0x3c44a9 : 0x534230;
    }

    private static BlockColor addWaterColor() {
        return (state, view, pos, index) -> view != null && index == 1 ? BiomeColors.getAverageWaterColor(view, pos) : index == 1 ? 0x3c44a9 : 0xFFFFFF;
    }
}
