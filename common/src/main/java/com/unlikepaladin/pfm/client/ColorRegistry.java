package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ColorRegistry {
    public static final Map<ItemLike, ItemLike> itemColorProviders = new HashMap<>();

    public static void registerBlockColors(){
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_TOILET, List.of(addToiletColor()));
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_BATHTUB, List.of(state -> 0xFFFFFF, addWaterColor()));
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_SINK, List.of(state -> 0xFFFFFF, addWaterColor()));
        registerBlockColor(PaladinFurnitureModBlocksItems.BASIC_LAMP, List.of(
            new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return 0xFFFFFF;
                }

                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
                    if (world != null && pos != null) {
                        BlockEntity entity = world.getBlockEntity(pos);
                        if (entity instanceof LampBlockEntity lamp) {
                            List<BlockTintSource> logBlockTintSources = getBlockTintSources(lamp.getVariant().getLogBlock());
                            if (logBlockTintSources != null && !logBlockTintSources.isEmpty()) {
                                return logBlockTintSources.get(0).colorInWorld(state, world, pos);
                            }
                        }
                    }
                    return 0xFFFFFF;
                }
            },
            new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return 0xFFFFFF;
                }

                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
                    if (world != null && pos != null) {
                        BlockEntity entity = world.getBlockEntity(pos);
                        if (entity instanceof LampBlockEntity lamp) {
                            DyeColor color = lamp.getPFMColor();
                            return color.getMapColor().col;
                        }
                    }
                    return 0xFFFFFF;
                }
            }
        ));

        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> {
            if (pfmModCompatibility.getClientModCompatiblity().isPresent()){
                pfmModCompatibility.getClientModCompatiblity().get().registerBlockColors();
            }
        });
        PaladinFurnitureMod.furnitureEntryMap.forEach((key, value) -> {
            if (BasicLampBlock.class.isAssignableFrom(key)) {
                return;
            }
            value.getVariantToBlockMap().forEach((variantBase, block) -> {
                Block baseBlock = variantBase.getBaseBlock();
                if (key.isAssignableFrom(KitchenSinkBlock.class)) {
                    registerBlockColor(block, List.of(lazyBlockTintSource(() -> getBlockTintSources(baseBlock), 0), addWaterColor()));
                } else {
                    registerBlockColor(block, List.of(lazyBlockTintSource(() -> getBlockTintSources(baseBlock), 0)));
                }
            });
            value.getVariantToBlockMapNonBase().forEach((variantBase, block) -> {
                Block baseBlock = variantBase.getBaseBlock();
                if (key.isAssignableFrom(KitchenSinkBlock.class)) {
                    registerBlockColor(block, List.of(lazyBlockTintSource(() -> getBlockTintSources(baseBlock), 0), addWaterColor()));
                } else {
                    registerBlockColor(block, List.of(lazyBlockTintSource(() -> getBlockTintSources(baseBlock), 0)));
                }
            });
        });
    }

    public static void registerBlockRenderLayers() {
        // In 26.1, block render layers are dynamically derived from texture quad transparency.
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
    public static void registerBlockColor(Block block, List<BlockTintSource> blockTintSources){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerBlockColor(Block block, BlockTintSource blockTintSource){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static List<BlockTintSource> getBlockTintSources(Block block){
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static void registerBlockToRenderLayer(Block block, ChunkSectionLayer renderLayer){
        throw new RuntimeException();
    }

    private static BlockTintSource addToiletColor() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY ? PFMFileUtil.adjustColor(0x3c44a9) : PFMFileUtil.adjustColor(0x534230);
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter view, BlockPos pos) {
                if (view != null && pos != null && state.getValue(BasicToiletBlock.TOILET_STATE) != ToiletState.DIRTY) {
                    return BiomeColors.getAverageWaterColor(view, pos);
                }
                return color(state);
            }
        };
    }

    private static BlockTintSource addWaterColor() {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return 0x3c44a9;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter view, BlockPos pos) {
                if (view != null && pos != null) {
                    return BiomeColors.getAverageWaterColor(view, pos);
                }
                return color(state);
            }
        };
    }

    private static BlockTintSource lazyBlockTintSource(Supplier<List<BlockTintSource>> sourcesSupplier, int index) {
        return new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                List<BlockTintSource> sources = sourcesSupplier.get();
                if (sources != null && index < sources.size()) {
                    return sources.get(index).color(state);
                }
                return 0xFFFFFF;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
                List<BlockTintSource> sources = sourcesSupplier.get();
                if (sources != null && index < sources.size()) {
                    return sources.get(index).colorInWorld(state, world, pos);
                }
                return 0xFFFFFF;
            }
        };
    }
}
