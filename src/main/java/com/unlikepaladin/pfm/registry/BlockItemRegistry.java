package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.items.DyeKit;
import com.unlikepaladin.pfm.items.FurnitureGuideBook;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class BlockItemRegistry {
    private static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block OAK_CHAIR = new BasicChair(FabricBlockSettings.of(Material.WOOD).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.OAK_TAN));
    public static final Block BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR).mapColor(MapColor.PALE_YELLOW));
    public static final Block SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR).mapColor(MapColor.SPRUCE_BROWN));
    public static final Block ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR).mapColor(MapColor.ORANGE));
    public static final Block JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR).mapColor(MapColor.DIRT_BROWN));
    public static final Block DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR).mapColor(MapColor.BROWN));
    public static final Block CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.DULL_PINK));
    public static final Block WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR).mapColor(MapColor.DARK_AQUA));
    public static final Block STRIPPED_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block QUARTZ_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));
    public static final Block NETHERITE_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR = new BasicChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    //Dinner Chairs
    public static final Block OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block QUARTZ_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    //Froggy Chairs
    public static final Block FROGGY_CHAIR = new FroggyChair(FabricBlockSettings.of(Material.METAL).strength(9.0f).resistance(8.0f).nonOpaque().requiresTool().mapColor(MapColor.GREEN));
    public static final Block FROGGY_CHAIR_PINK = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR).mapColor(MapColor.PINK));
    public static final Block FROGGY_CHAIR_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR).mapColor(MapColor.BLUE));
    public static final Block FROGGY_CHAIR_LIGHT_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR).mapColor(MapColor.LIGHT_BLUE));
    public static final Block FROGGY_CHAIR_ORANGE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR).mapColor(MapColor.ORANGE));
    public static final Block FROGGY_CHAIR_YELLOW = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR).mapColor(MapColor.YELLOW));

    //Classic Chair
    public static final Block OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block CHAIR_CLASSIC_WOOL = new ClassicChairDyeable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block QUARTZ_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block NETHERITE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(NETHERITE_CHAIR));

    public static final Block LIGHT_WOOD_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    //Modern Chair
    public static final Block OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block QUARTZ_CHAIR_MODERN = new ModernChair(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));


    //Arm Chairs
    public static final Block ARM_CHAIR_STANDARD = new ArmChairDyeable(FabricBlockSettings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block ARM_CHAIR_LEATHER = new ArmChair(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block SOFA_SIMPLE = new SimpleSofa(FabricBlockSettings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));

    //tables
    public static final Block OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_BASIC_TABLE = new BasicTable(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_BASIC_TABLE = new BasicTable(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));


    public static final Block OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_NATURAL_TABLE = new LogTable(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_NATURAL_TABLE = new LogTable(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_NATURAL_TABLE = new LogTable(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block OAK_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_MODERN_DINNER_TABLE = new ModernDinnerTable(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block JUNGLE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block ACACIA_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block DARK_OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block JUNGLE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block ACACIA_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block DARK_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block JUNGLE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block ACACIA_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block WHITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));
    public static final Block GRAY_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));
    public static final Block DARK_WOOD_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(DARK_OAK_MODERN_STOOL));
    public static final Block GRAY_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(DARK_OAK_MODERN_STOOL));
    public static final Block LIGHT_GRAY_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(DARK_OAK_MODERN_STOOL));
    public static final Block LIGHT_WOOD_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_MODERN_STOOL));

    public static final Block QUARTZ_MODERN_STOOL = new ModernStool(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block GRANITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block JUNGLE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block ACACIA_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block DARK_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));

    public static final Block OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(WARPED_CHAIR));

    public static final Block QUARTZ_DINNER_TABLE = new DinnerTable(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_DINNER_TABLE= new DinnerTable(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));


    public static final Block OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.of(Material.WOOD).strength(2.0f).resistance(3.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.OAK_TAN));
    public static final Block OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));

    public static final Block BIRCH_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER).mapColor(MapColor.PALE_YELLOW));
    public static final Block BIRCH_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block BIRCH_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));

    public static final Block SPRUCE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER).mapColor(MapColor.SPRUCE_BROWN));
    public static final Block SPRUCE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block SPRUCE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));

    public static final Block JUNGLE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER).mapColor(MapColor.DIRT_BROWN));
    public static final Block JUNGLE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block JUNGLE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block ACACIA_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER).mapColor(MapColor.ORANGE));
    public static final Block ACACIA_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block ACACIA_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));

    public static final Block DARK_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER).mapColor(MapColor.BROWN));
    public static final Block DARK_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));

    public static final Block CRIMSON_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0f).resistance(3.0f).nonOpaque().sounds(BlockSoundGroup.NETHER_STEM).mapColor(MapColor.DULL_PINK));
    public static final Block CRIMSON_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CRIMSON_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));

    public static final Block WARPED_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER).mapColor(MapColor.DARK_AQUA));
    public static final Block WARPED_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block WARPED_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));

    public static final Block STRIPPED_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));

    public static final Block STRIPPED_BIRCH_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_BIRCH_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(BIRCH_KITCHEN_COUNTER));

    public static final Block STRIPPED_SPRUCE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_SPRUCE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(SPRUCE_KITCHEN_COUNTER));

    public static final Block STRIPPED_JUNGLE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(JUNGLE_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_JUNGLE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(JUNGLE_KITCHEN_COUNTER));

    public static final Block STRIPPED_ACACIA_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_ACACIA_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(ACACIA_KITCHEN_COUNTER));

    public static final Block STRIPPED_DARK_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_DARK_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));

    public static final Block STRIPPED_CRIMSON_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_CRIMSON_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(CRIMSON_KITCHEN_COUNTER));

    public static final Block STRIPPED_WARPED_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_WARPED_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(WARPED_KITCHEN_COUNTER));

    public static final Block WHITE_FREEZER = new Freezer(FabricBlockSettings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE).mapColor(MapColor.WHITE),() -> BlockItemRegistry.WHITE_FRIDGE);
    public static final Block WHITE_FRIDGE = new Fridge(FabricBlockSettings.copyOf(WHITE_FREEZER).nonOpaque(), () -> BlockItemRegistry.WHITE_FREEZER);
    public static final Block IRON_FREEZER = new IronFreezer(FabricBlockSettings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.METAL).mapColor(MapColor.IRON_GRAY),() -> BlockItemRegistry.IRON_FRIDGE);
    public static final Block IRON_FRIDGE = new IronFridge(FabricBlockSettings.copyOf(IRON_FREEZER).nonOpaque(), () -> BlockItemRegistry.IRON_FREEZER);

    public static final Block XBOX_FRIDGE = new XboxFridge(FabricBlockSettings.copyOf(WHITE_FREEZER).nonOpaque().mapColor(MapColor.BLACK), null);

    public static final Block WHITE_STOVE = new Stove(FabricBlockSettings.copyOf(WHITE_FREEZER));
    public static final Block IRON_STOVE = new IronStove(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
    public static final Block IRON_MICROWAVE = new Microwave(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));

    public static final Block OAK_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block ACACIA_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block JUNGLE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block QUARTZ_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_NIGHTSTAND= new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_NIGHTSTAND = new ClassicNightstand(FabricBlockSettings.copyOf(Blocks.BLACKSTONE));


    public static final Block OAK_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block OAK_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block OAK_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block OAK_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block OAK_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block OAK_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block OAK_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block OAK_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block OAK_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block OAK_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block OAK_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block OAK_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block OAK_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block OAK_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block OAK_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block OAK_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block SPRUCE_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block SPRUCE_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block SPRUCE_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block SPRUCE_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block SPRUCE_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block SPRUCE_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block SPRUCE_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block SPRUCE_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block SPRUCE_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block SPRUCE_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block SPRUCE_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block SPRUCE_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block SPRUCE_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block SPRUCE_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block SPRUCE_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block SPRUCE_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block DARK_OAK_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block DARK_OAK_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block DARK_OAK_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block DARK_OAK_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block DARK_OAK_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block DARK_OAK_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block DARK_OAK_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block DARK_OAK_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block DARK_OAK_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block DARK_OAK_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block DARK_OAK_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block DARK_OAK_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block DARK_OAK_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block DARK_OAK_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block DARK_OAK_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block DARK_OAK_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block BIRCH_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block BIRCH_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block BIRCH_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block BIRCH_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block BIRCH_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block BIRCH_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block BIRCH_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block BIRCH_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block BIRCH_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block BIRCH_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block BIRCH_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block BIRCH_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block BIRCH_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block BIRCH_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block BIRCH_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block BIRCH_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block ACACIA_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block ACACIA_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block ACACIA_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block ACACIA_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block ACACIA_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block ACACIA_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block ACACIA_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block ACACIA_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block ACACIA_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block ACACIA_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block ACACIA_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block ACACIA_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block ACACIA_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block ACACIA_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block ACACIA_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block ACACIA_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block JUNGLE_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block JUNGLE_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block JUNGLE_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block JUNGLE_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block JUNGLE_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block JUNGLE_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block JUNGLE_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block JUNGLE_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block JUNGLE_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block JUNGLE_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block JUNGLE_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block JUNGLE_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block JUNGLE_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block JUNGLE_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block JUNGLE_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block JUNGLE_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block WARPED_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block WARPED_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block WARPED_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block WARPED_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block WARPED_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block WARPED_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block WARPED_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block WARPED_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block WARPED_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block WARPED_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block WARPED_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block WARPED_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block WARPED_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block WARPED_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block WARPED_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block WARPED_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block CRIMSON_RED_SIMPLE_BED = new SimpleBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block CRIMSON_ORANGE_SIMPLE_BED = new SimpleBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block CRIMSON_YELLOW_SIMPLE_BED = new SimpleBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block CRIMSON_GREEN_SIMPLE_BED = new SimpleBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block CRIMSON_LIME_SIMPLE_BED = new SimpleBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block CRIMSON_CYAN_SIMPLE_BED = new SimpleBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block CRIMSON_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block CRIMSON_LIGHT_BLUE_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block CRIMSON_LIGHT_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block CRIMSON_MAGENTA_SIMPLE_BED = new SimpleBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block CRIMSON_PINK_SIMPLE_BED = new SimpleBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block CRIMSON_PURPLE_SIMPLE_BED = new SimpleBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block CRIMSON_WHITE_SIMPLE_BED = new SimpleBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block CRIMSON_BROWN_SIMPLE_BED = new SimpleBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block CRIMSON_GRAY_SIMPLE_BED = new SimpleBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block CRIMSON_BLACK_SIMPLE_BED = new SimpleBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));


    public static final Block OAK_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block OAK_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block OAK_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block OAK_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block OAK_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block OAK_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block OAK_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block OAK_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block OAK_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block OAK_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block OAK_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block OAK_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block OAK_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block OAK_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block OAK_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block OAK_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block SPRUCE_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block SPRUCE_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block SPRUCE_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block SPRUCE_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block SPRUCE_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block SPRUCE_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block SPRUCE_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block SPRUCE_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block SPRUCE_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block SPRUCE_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block SPRUCE_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block SPRUCE_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block SPRUCE_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block SPRUCE_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block SPRUCE_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block SPRUCE_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block DARK_OAK_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block DARK_OAK_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block DARK_OAK_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block DARK_OAK_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block DARK_OAK_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block DARK_OAK_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block DARK_OAK_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block DARK_OAK_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block DARK_OAK_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block DARK_OAK_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block DARK_OAK_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block DARK_OAK_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block DARK_OAK_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block DARK_OAK_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block DARK_OAK_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block DARK_OAK_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block BIRCH_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block BIRCH_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block BIRCH_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block BIRCH_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block BIRCH_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block BIRCH_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block BIRCH_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block BIRCH_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block BIRCH_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block BIRCH_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block BIRCH_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block BIRCH_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block BIRCH_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block BIRCH_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block BIRCH_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block BIRCH_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block ACACIA_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block ACACIA_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block ACACIA_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block ACACIA_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block ACACIA_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block ACACIA_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block ACACIA_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block ACACIA_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block ACACIA_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block ACACIA_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block ACACIA_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block ACACIA_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block ACACIA_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block ACACIA_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block ACACIA_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block ACACIA_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block JUNGLE_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block JUNGLE_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block JUNGLE_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block JUNGLE_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block JUNGLE_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block JUNGLE_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block JUNGLE_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block JUNGLE_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block JUNGLE_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block JUNGLE_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block JUNGLE_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block JUNGLE_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block JUNGLE_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block JUNGLE_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block JUNGLE_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block JUNGLE_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block WARPED_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block WARPED_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block WARPED_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block WARPED_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block WARPED_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block WARPED_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block WARPED_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block WARPED_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block WARPED_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block WARPED_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block WARPED_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block WARPED_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block WARPED_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block WARPED_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block WARPED_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block WARPED_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Block CRIMSON_RED_CLASSIC_BED = new ClassicBed(DyeColor.RED, FabricBlockSettings.copyOf(Blocks.RED_BED));
    public static final Block CRIMSON_ORANGE_CLASSIC_BED = new ClassicBed(DyeColor.ORANGE, FabricBlockSettings.copyOf(Blocks.ORANGE_BED));
    public static final Block CRIMSON_YELLOW_CLASSIC_BED = new ClassicBed(DyeColor.YELLOW, FabricBlockSettings.copyOf(Blocks.YELLOW_BED));
    public static final Block CRIMSON_GREEN_CLASSIC_BED = new ClassicBed(DyeColor.GREEN, FabricBlockSettings.copyOf(Blocks.GREEN_BED));
    public static final Block CRIMSON_LIME_CLASSIC_BED = new ClassicBed(DyeColor.LIME, FabricBlockSettings.copyOf(Blocks.LIME_BED));
    public static final Block CRIMSON_CYAN_CLASSIC_BED = new ClassicBed(DyeColor.CYAN, FabricBlockSettings.copyOf(Blocks.CYAN_BED));
    public static final Block CRIMSON_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.BLUE, FabricBlockSettings.copyOf(Blocks.BLUE_BED));
    public static final Block CRIMSON_LIGHT_BLUE_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_BLUE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_BED));
    public static final Block CRIMSON_LIGHT_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.LIGHT_GRAY, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_BED));
    public static final Block CRIMSON_MAGENTA_CLASSIC_BED = new ClassicBed(DyeColor.MAGENTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_BED));
    public static final Block CRIMSON_PINK_CLASSIC_BED = new ClassicBed(DyeColor.PINK, FabricBlockSettings.copyOf(Blocks.PINK_BED));
    public static final Block CRIMSON_PURPLE_CLASSIC_BED = new ClassicBed(DyeColor.PURPLE, FabricBlockSettings.copyOf(Blocks.PURPLE_BED));
    public static final Block CRIMSON_WHITE_CLASSIC_BED = new ClassicBed(DyeColor.WHITE, FabricBlockSettings.copyOf(Blocks.WHITE_BED));
    public static final Block CRIMSON_BROWN_CLASSIC_BED = new ClassicBed(DyeColor.BROWN, FabricBlockSettings.copyOf(Blocks.BROWN_BED));
    public static final Block CRIMSON_GRAY_CLASSIC_BED = new ClassicBed(DyeColor.GRAY, FabricBlockSettings.copyOf(Blocks.GRAY_BED));
    public static final Block CRIMSON_BLACK_CLASSIC_BED = new ClassicBed(DyeColor.BLACK, FabricBlockSettings.copyOf(Blocks.BLACK_BED));

    public static final Item DYE_KIT_YELLOW = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.YELLOW);
    public static final Item DYE_KIT_BLUE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BLUE);
    public static final Item DYE_KIT_WHITE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.WHITE);
    public static final Item DYE_KIT_PINK = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.PINK);
    public static final Item DYE_KIT_PURPLE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.PURPLE);
    public static final Item DYE_KIT_GREEN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.GREEN);
    public static final Item DYE_KIT_LIGHT_BLUE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIGHT_BLUE);
    public static final Item DYE_KIT_LIGHT_GRAY = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIGHT_GRAY);
    public static final Item DYE_KIT_LIME = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.LIME);
    public static final Item DYE_KIT_ORANGE = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.ORANGE);
    public static final Item DYE_KIT_BLACK = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BLACK);
    public static final Item DYE_KIT_BROWN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.BROWN);
    public static final Item DYE_KIT_MAGENTA = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.MAGENTA);
    public static final Item DYE_KIT_RED = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.RED);
    public static final Item DYE_KIT_CYAN = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.CYAN);
    public static final Item DYE_KIT_GRAY = new DyeKit(new FabricItemSettings().group(PaladinFurnitureMod.DYE_KITS).maxCount(16), DyeColor.GRAY);

    public static final Block ACACIA_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.ACACIA_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block SPRUCE_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_OAK_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.DARK_OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block JUNGLE_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.JUNGLE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block BIRCH_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block WARPED_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block CRIMSON_HERRINGBONE_PLANKS = new HerringbonePlanks(FabricBlockSettings.copyOf(Blocks.CRIMSON_PLANKS).sounds(BlockSoundGroup.WOOD));


    public static final Block RAW_CONCRETE = new Block(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE).sounds(BlockSoundGroup.STONE));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE_POWDER).sounds(BlockSoundGroup.SAND));
    public static final Block LEATHER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).mapColor(MapColor.ORANGE));

    public static final Block IRON_CHAIN = new ChainBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.METAL));
    public static final Block GRAY_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.GRAY));
    public static final Block WHITE_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.WHITE));
    public static final Block GLASS_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.OFF_WHITE));
    public static final Block SIMPLE_LIGHT = new SimpleLight(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.LIGHT_GRAY));

    public static final Block LIGHT_SWITCH = new LightSwitch(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).sounds(BlockSoundGroup.STONE).nonOpaque().mapColor(MapColor.WHITE));
    public static final BlockItem LIGHT_SWITCH_ITEM = new LightSwitchItem(LIGHT_SWITCH, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP));
    public static final Item FURNITURE_BOOK = new FurnitureGuideBook(new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP).rarity(Rarity.RARE).maxCount(1));

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT)? litLevel : 0;
    }
    public static final Block CONCRETE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(RAW_CONCRETE).mapColor(DyeColor.LIGHT_GRAY));
    public static final Block CONCRETE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(CONCRETE_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CONCRETE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(CONCRETE_KITCHEN_COUNTER));

    public static final Block DARK_CONCRETE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(RAW_CONCRETE).mapColor(MapColor.GRAY));
    public static final Block DARK_CONCRETE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(DARK_CONCRETE_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_CONCRETE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(DARK_CONCRETE_KITCHEN_COUNTER));

    public static final Block LIGHT_WOOD_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block LIGHT_WOOD_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(OAK_KITCHEN_COUNTER));

    public static final Block DARK_WOOD_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(DARK_OAK_KITCHEN_COUNTER),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_WOOD_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(OAK_CHAIR));

    public static final Block GRANITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block GRANITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.CALCITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CALCITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.CALCITE));

    public static final Block NETHERITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block NETHERITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));

    public static final Block ANDESITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block ANDESITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DIORITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));

    public static final Block SMOOTH_STONE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block SMOOTH_STONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));


    public static final Block STONE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));

    public static final Block STONE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block STONE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block STONE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.STONE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.STONE));

    public static final Block DEEPSLATE_TILE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DEEPSLATE_TILE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));

    public static final Block BLACKSTONE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block BLACKSTONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));

    public static final Block KITCHEN_STOVETOP = new KitchenStovetop(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));

    public static final Block DEEPSLATE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.DEEPSLATE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DEEPSLATE_KITCHEN_COUNTER_OVEN = new KitchenCounterOven(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block WORKING_TABLE = new WorkingTable(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE).sounds(BlockSoundGroup.WOOD));
    public static final Block BASIC_PLATE = new Plate(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));
    public static final Block BASIC_CUTLERY = new Cutlery(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));

    public static final Block OAK_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block SPRUCE_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block BIRCH_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block DARK_OAK_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block ACACIA_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block JUNGLE_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block CRIMSON_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));
    public static final Block WARPED_SIMPLE_BUNK_LADDER = new SimpleBunkLadder(FabricBlockSettings.copyOf(Blocks.LADDER));

    public static Stream<Block> streamBlocks() {
        return BLOCKS.stream();
    }

    public static void registerFurniture(String blockName, Block block, boolean registerItem) {
        registerBlock(blockName, block, false);
        if (registerItem) {
            BLOCKS.add(block);
            registerItem(blockName, new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
        }
    }
    public static Block[] getBeds() {
        return new Block[] {OAK_RED_SIMPLE_BED, OAK_ORANGE_SIMPLE_BED, OAK_YELLOW_SIMPLE_BED, OAK_GREEN_SIMPLE_BED, OAK_LIME_SIMPLE_BED, OAK_CYAN_SIMPLE_BED, OAK_BLUE_SIMPLE_BED, OAK_LIGHT_BLUE_SIMPLE_BED, OAK_LIGHT_GRAY_SIMPLE_BED, OAK_GRAY_SIMPLE_BED, OAK_BLACK_SIMPLE_BED, OAK_PURPLE_SIMPLE_BED, OAK_MAGENTA_SIMPLE_BED, OAK_PINK_SIMPLE_BED, OAK_BROWN_SIMPLE_BED, OAK_WHITE_SIMPLE_BED, SPRUCE_RED_SIMPLE_BED, SPRUCE_ORANGE_SIMPLE_BED, SPRUCE_YELLOW_SIMPLE_BED, SPRUCE_GREEN_SIMPLE_BED, SPRUCE_LIME_SIMPLE_BED, SPRUCE_CYAN_SIMPLE_BED, SPRUCE_BLUE_SIMPLE_BED, SPRUCE_LIGHT_BLUE_SIMPLE_BED, SPRUCE_LIGHT_GRAY_SIMPLE_BED, SPRUCE_GRAY_SIMPLE_BED, SPRUCE_BLACK_SIMPLE_BED, SPRUCE_PURPLE_SIMPLE_BED, SPRUCE_MAGENTA_SIMPLE_BED, SPRUCE_PINK_SIMPLE_BED, SPRUCE_BROWN_SIMPLE_BED, SPRUCE_WHITE_SIMPLE_BED, BIRCH_RED_SIMPLE_BED, BIRCH_ORANGE_SIMPLE_BED, BIRCH_YELLOW_SIMPLE_BED, BIRCH_GREEN_SIMPLE_BED, BIRCH_LIME_SIMPLE_BED, BIRCH_CYAN_SIMPLE_BED, BIRCH_BLUE_SIMPLE_BED, BIRCH_LIGHT_BLUE_SIMPLE_BED, BIRCH_LIGHT_GRAY_SIMPLE_BED, BIRCH_GRAY_SIMPLE_BED, BIRCH_BLACK_SIMPLE_BED, BIRCH_PURPLE_SIMPLE_BED, BIRCH_MAGENTA_SIMPLE_BED, BIRCH_PINK_SIMPLE_BED, BIRCH_BROWN_SIMPLE_BED, BIRCH_WHITE_SIMPLE_BED,JUNGLE_RED_SIMPLE_BED, JUNGLE_ORANGE_SIMPLE_BED, JUNGLE_YELLOW_SIMPLE_BED, JUNGLE_GREEN_SIMPLE_BED, JUNGLE_LIME_SIMPLE_BED, JUNGLE_CYAN_SIMPLE_BED, JUNGLE_BLUE_SIMPLE_BED, JUNGLE_LIGHT_BLUE_SIMPLE_BED, JUNGLE_LIGHT_GRAY_SIMPLE_BED, JUNGLE_GRAY_SIMPLE_BED, JUNGLE_BLACK_SIMPLE_BED, JUNGLE_PURPLE_SIMPLE_BED, JUNGLE_MAGENTA_SIMPLE_BED, JUNGLE_PINK_SIMPLE_BED, JUNGLE_BROWN_SIMPLE_BED, JUNGLE_WHITE_SIMPLE_BED, ACACIA_RED_SIMPLE_BED, ACACIA_ORANGE_SIMPLE_BED, ACACIA_YELLOW_SIMPLE_BED, ACACIA_GREEN_SIMPLE_BED, ACACIA_LIME_SIMPLE_BED, ACACIA_CYAN_SIMPLE_BED, ACACIA_BLUE_SIMPLE_BED, ACACIA_LIGHT_BLUE_SIMPLE_BED, ACACIA_LIGHT_GRAY_SIMPLE_BED, ACACIA_GRAY_SIMPLE_BED, ACACIA_BLACK_SIMPLE_BED, ACACIA_PURPLE_SIMPLE_BED, ACACIA_MAGENTA_SIMPLE_BED, ACACIA_PINK_SIMPLE_BED, ACACIA_BROWN_SIMPLE_BED, ACACIA_WHITE_SIMPLE_BED, DARK_OAK_RED_SIMPLE_BED, DARK_OAK_ORANGE_SIMPLE_BED, DARK_OAK_YELLOW_SIMPLE_BED, DARK_OAK_GREEN_SIMPLE_BED, DARK_OAK_LIME_SIMPLE_BED, DARK_OAK_CYAN_SIMPLE_BED, DARK_OAK_BLUE_SIMPLE_BED, DARK_OAK_LIGHT_BLUE_SIMPLE_BED, DARK_OAK_LIGHT_GRAY_SIMPLE_BED, DARK_OAK_GRAY_SIMPLE_BED, DARK_OAK_BLACK_SIMPLE_BED, DARK_OAK_PURPLE_SIMPLE_BED, DARK_OAK_MAGENTA_SIMPLE_BED, DARK_OAK_PINK_SIMPLE_BED, DARK_OAK_BROWN_SIMPLE_BED, DARK_OAK_WHITE_SIMPLE_BED, WARPED_RED_SIMPLE_BED, WARPED_ORANGE_SIMPLE_BED, WARPED_YELLOW_SIMPLE_BED, WARPED_GREEN_SIMPLE_BED, WARPED_LIME_SIMPLE_BED, WARPED_CYAN_SIMPLE_BED, WARPED_BLUE_SIMPLE_BED, WARPED_LIGHT_BLUE_SIMPLE_BED, WARPED_LIGHT_GRAY_SIMPLE_BED, WARPED_GRAY_SIMPLE_BED, WARPED_BLACK_SIMPLE_BED, WARPED_PURPLE_SIMPLE_BED, WARPED_MAGENTA_SIMPLE_BED, WARPED_PINK_SIMPLE_BED, WARPED_BROWN_SIMPLE_BED, WARPED_WHITE_SIMPLE_BED, CRIMSON_RED_SIMPLE_BED, CRIMSON_ORANGE_SIMPLE_BED, CRIMSON_YELLOW_SIMPLE_BED, CRIMSON_GREEN_SIMPLE_BED, CRIMSON_LIME_SIMPLE_BED, CRIMSON_CYAN_SIMPLE_BED, CRIMSON_BLUE_SIMPLE_BED, CRIMSON_LIGHT_BLUE_SIMPLE_BED, CRIMSON_LIGHT_GRAY_SIMPLE_BED, CRIMSON_GRAY_SIMPLE_BED, CRIMSON_BLACK_SIMPLE_BED, CRIMSON_PURPLE_SIMPLE_BED, CRIMSON_MAGENTA_SIMPLE_BED, CRIMSON_PINK_SIMPLE_BED, CRIMSON_BROWN_SIMPLE_BED, CRIMSON_WHITE_SIMPLE_BED, OAK_RED_CLASSIC_BED, OAK_ORANGE_CLASSIC_BED, OAK_YELLOW_CLASSIC_BED, OAK_GREEN_CLASSIC_BED, OAK_LIME_CLASSIC_BED, OAK_CYAN_CLASSIC_BED, OAK_BLUE_CLASSIC_BED, OAK_LIGHT_BLUE_CLASSIC_BED, OAK_LIGHT_GRAY_CLASSIC_BED, OAK_GRAY_CLASSIC_BED, OAK_BLACK_CLASSIC_BED, OAK_PURPLE_CLASSIC_BED, OAK_MAGENTA_CLASSIC_BED, OAK_PINK_CLASSIC_BED, OAK_BROWN_CLASSIC_BED, OAK_WHITE_CLASSIC_BED, SPRUCE_RED_CLASSIC_BED, SPRUCE_ORANGE_CLASSIC_BED, SPRUCE_YELLOW_CLASSIC_BED, SPRUCE_GREEN_CLASSIC_BED, SPRUCE_LIME_CLASSIC_BED, SPRUCE_CYAN_CLASSIC_BED, SPRUCE_BLUE_CLASSIC_BED, SPRUCE_LIGHT_BLUE_CLASSIC_BED, SPRUCE_LIGHT_GRAY_CLASSIC_BED, SPRUCE_GRAY_CLASSIC_BED, SPRUCE_BLACK_CLASSIC_BED, SPRUCE_PURPLE_CLASSIC_BED, SPRUCE_MAGENTA_CLASSIC_BED, SPRUCE_PINK_CLASSIC_BED, SPRUCE_BROWN_CLASSIC_BED, SPRUCE_WHITE_CLASSIC_BED, BIRCH_RED_CLASSIC_BED, BIRCH_ORANGE_CLASSIC_BED, BIRCH_YELLOW_CLASSIC_BED, BIRCH_GREEN_CLASSIC_BED, BIRCH_LIME_CLASSIC_BED, BIRCH_CYAN_CLASSIC_BED, BIRCH_BLUE_CLASSIC_BED, BIRCH_LIGHT_BLUE_CLASSIC_BED, BIRCH_LIGHT_GRAY_CLASSIC_BED, BIRCH_GRAY_CLASSIC_BED, BIRCH_BLACK_CLASSIC_BED, BIRCH_PURPLE_CLASSIC_BED, BIRCH_MAGENTA_CLASSIC_BED, BIRCH_PINK_CLASSIC_BED, BIRCH_BROWN_CLASSIC_BED, BIRCH_WHITE_CLASSIC_BED,JUNGLE_RED_CLASSIC_BED, JUNGLE_ORANGE_CLASSIC_BED, JUNGLE_YELLOW_CLASSIC_BED, JUNGLE_GREEN_CLASSIC_BED, JUNGLE_LIME_CLASSIC_BED, JUNGLE_CYAN_CLASSIC_BED, JUNGLE_BLUE_CLASSIC_BED, JUNGLE_LIGHT_BLUE_CLASSIC_BED, JUNGLE_LIGHT_GRAY_CLASSIC_BED, JUNGLE_GRAY_CLASSIC_BED, JUNGLE_BLACK_CLASSIC_BED, JUNGLE_PURPLE_CLASSIC_BED, JUNGLE_MAGENTA_CLASSIC_BED, JUNGLE_PINK_CLASSIC_BED, JUNGLE_BROWN_CLASSIC_BED, JUNGLE_WHITE_CLASSIC_BED, ACACIA_RED_CLASSIC_BED, ACACIA_ORANGE_CLASSIC_BED, ACACIA_YELLOW_CLASSIC_BED, ACACIA_GREEN_CLASSIC_BED, ACACIA_LIME_CLASSIC_BED, ACACIA_CYAN_CLASSIC_BED, ACACIA_BLUE_CLASSIC_BED, ACACIA_LIGHT_BLUE_CLASSIC_BED, ACACIA_LIGHT_GRAY_CLASSIC_BED, ACACIA_GRAY_CLASSIC_BED, ACACIA_BLACK_CLASSIC_BED, ACACIA_PURPLE_CLASSIC_BED, ACACIA_MAGENTA_CLASSIC_BED, ACACIA_PINK_CLASSIC_BED, ACACIA_BROWN_CLASSIC_BED, ACACIA_WHITE_CLASSIC_BED, DARK_OAK_RED_CLASSIC_BED, DARK_OAK_ORANGE_CLASSIC_BED, DARK_OAK_YELLOW_CLASSIC_BED, DARK_OAK_GREEN_CLASSIC_BED, DARK_OAK_LIME_CLASSIC_BED, DARK_OAK_CYAN_CLASSIC_BED, DARK_OAK_BLUE_CLASSIC_BED, DARK_OAK_LIGHT_BLUE_CLASSIC_BED, DARK_OAK_LIGHT_GRAY_CLASSIC_BED, DARK_OAK_GRAY_CLASSIC_BED, DARK_OAK_BLACK_CLASSIC_BED, DARK_OAK_PURPLE_CLASSIC_BED, DARK_OAK_MAGENTA_CLASSIC_BED, DARK_OAK_PINK_CLASSIC_BED, DARK_OAK_BROWN_CLASSIC_BED, DARK_OAK_WHITE_CLASSIC_BED, WARPED_RED_CLASSIC_BED, WARPED_ORANGE_CLASSIC_BED, WARPED_YELLOW_CLASSIC_BED, WARPED_GREEN_CLASSIC_BED, WARPED_LIME_CLASSIC_BED, WARPED_CYAN_CLASSIC_BED, WARPED_BLUE_CLASSIC_BED, WARPED_LIGHT_BLUE_CLASSIC_BED, WARPED_LIGHT_GRAY_CLASSIC_BED, WARPED_GRAY_CLASSIC_BED, WARPED_BLACK_CLASSIC_BED, WARPED_PURPLE_CLASSIC_BED, WARPED_MAGENTA_CLASSIC_BED, WARPED_PINK_CLASSIC_BED, WARPED_BROWN_CLASSIC_BED, WARPED_WHITE_CLASSIC_BED, CRIMSON_RED_CLASSIC_BED, CRIMSON_ORANGE_CLASSIC_BED, CRIMSON_YELLOW_CLASSIC_BED, CRIMSON_GREEN_CLASSIC_BED, CRIMSON_LIME_CLASSIC_BED, CRIMSON_CYAN_CLASSIC_BED, CRIMSON_BLUE_CLASSIC_BED, CRIMSON_LIGHT_BLUE_CLASSIC_BED, CRIMSON_LIGHT_GRAY_CLASSIC_BED, CRIMSON_GRAY_CLASSIC_BED, CRIMSON_BLACK_CLASSIC_BED, CRIMSON_PURPLE_CLASSIC_BED, CRIMSON_MAGENTA_CLASSIC_BED, CRIMSON_PINK_CLASSIC_BED, CRIMSON_BROWN_CLASSIC_BED, CRIMSON_WHITE_CLASSIC_BED};
    }
    public static void registerBlock(String blockName, Block block, boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
            BLOCKS.add(block);
            registerItem(blockName, new BlockItem(block, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        }
        if (block.getDefaultState().getMaterial() == Material.WOOD || block.getDefaultState().getMaterial() == Material.WOOL) {
            FlammableBlockRegistry.getDefaultInstance().add(block, 20, 5);
            FuelRegistry.INSTANCE.add(block, 300);
        }
    }

    public static void registerFurniture(String blockName, Block block, int count) {
        BLOCKS.add(block);
        registerBlock(blockName, block, false);
        registerItem(blockName, new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP).maxCount(count)));
    }

    public static void registerBlock(String blockName, Block block, BlockItem item) {
            registerBlock(blockName, block, false);
            registerItem(blockName, item);
            BLOCKS.add(block);
    }

    public static void registerItem(String itemName, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, itemName), item);
    }

    public static void registerBlocks(){
        //Block Registry
        registerItem("furniture_book", FURNITURE_BOOK);
        registerFurniture("working_table", WORKING_TABLE, true);

        //Basic Chairs
        registerFurniture("oak_chair", OAK_CHAIR, true);
        registerFurniture("birch_chair", BIRCH_CHAIR, true);
        registerFurniture("spruce_chair", SPRUCE_CHAIR, true);
        registerFurniture("acacia_chair", ACACIA_CHAIR, true);
        registerFurniture("jungle_chair", JUNGLE_CHAIR, true);
        registerFurniture("dark_oak_chair", DARK_OAK_CHAIR, true);
        registerFurniture("warped_chair", WARPED_CHAIR, true);
        registerFurniture("crimson_chair", CRIMSON_CHAIR, true);
        registerFurniture("stripped_oak_chair", STRIPPED_OAK_CHAIR, true);
        registerFurniture("stripped_birch_chair", STRIPPED_BIRCH_CHAIR, true);
        registerFurniture("stripped_spruce_chair", STRIPPED_SPRUCE_CHAIR, true);
        registerFurniture("stripped_acacia_chair", STRIPPED_ACACIA_CHAIR, true);
        registerFurniture("stripped_jungle_chair", STRIPPED_JUNGLE_CHAIR, true);
        registerFurniture("stripped_dark_oak_chair", STRIPPED_DARK_OAK_CHAIR, true);
        registerFurniture("stripped_warped_chair", STRIPPED_WARPED_CHAIR, true);
        registerFurniture("stripped_crimson_chair", STRIPPED_CRIMSON_CHAIR, true);
        registerFurniture("quartz_chair", QUARTZ_CHAIR, true);
        registerFurniture("netherite_chair", NETHERITE_CHAIR, true);
        registerFurniture("light_wood_chair", LIGHT_WOOD_CHAIR, true);
        registerFurniture("dark_wood_chair", DARK_WOOD_CHAIR, true);
        registerFurniture("granite_chair", GRANITE_CHAIR, true);
        registerFurniture("calcite_chair", CALCITE_CHAIR, true);
        registerFurniture("andesite_chair", ANDESITE_CHAIR, true);
        registerFurniture("diorite_chair", DIORITE_CHAIR, true);
        registerFurniture("stone_chair", STONE_CHAIR, true);
        registerFurniture("blackstone_chair", BLACKSTONE_CHAIR, true);
        registerFurniture("deepslate_chair", DEEPSLATE_CHAIR, true);

        //Dinner Chairs
        registerFurniture("oak_chair_dinner", OAK_CHAIR_DINNER, true);
        registerFurniture("birch_chair_dinner", BIRCH_CHAIR_DINNER, true);
        registerFurniture("spruce_chair_dinner", SPRUCE_CHAIR_DINNER, true);
        registerFurniture("acacia_chair_dinner", ACACIA_CHAIR_DINNER, true);
        registerFurniture("jungle_chair_dinner", JUNGLE_CHAIR_DINNER, true);
        registerFurniture("dark_oak_chair_dinner", DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("warped_chair_dinner", WARPED_CHAIR_DINNER, true);
        registerFurniture("crimson_chair_dinner", CRIMSON_CHAIR_DINNER, true);
        registerFurniture("stripped_oak_chair_dinner", STRIPPED_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_birch_chair_dinner", STRIPPED_BIRCH_CHAIR_DINNER, true);
        registerFurniture("stripped_spruce_chair_dinner", STRIPPED_SPRUCE_CHAIR_DINNER, true);
        registerFurniture("stripped_acacia_chair_dinner", STRIPPED_ACACIA_CHAIR_DINNER, true);
        registerFurniture("stripped_jungle_chair_dinner", STRIPPED_JUNGLE_CHAIR_DINNER, true);
        registerFurniture("stripped_dark_oak_chair_dinner", STRIPPED_DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_warped_chair_dinner", STRIPPED_WARPED_CHAIR_DINNER, true);
        registerFurniture("stripped_crimson_chair_dinner", STRIPPED_CRIMSON_CHAIR_DINNER, true);
        registerFurniture("quartz_chair_dinner", QUARTZ_CHAIR_DINNER, true);
        registerFurniture("netherite_chair_dinner", NETHERITE_CHAIR_DINNER, true);
        registerFurniture("light_wood_chair_dinner", LIGHT_WOOD_CHAIR_DINNER, true);
        registerFurniture("dark_wood_chair_dinner", DARK_WOOD_CHAIR_DINNER, true);
        registerFurniture("granite_chair_dinner", GRANITE_CHAIR_DINNER, true);
        registerFurniture("calcite_chair_dinner", CALCITE_CHAIR_DINNER, true);
        registerFurniture("andesite_chair_dinner", ANDESITE_CHAIR_DINNER, true);
        registerFurniture("diorite_chair_dinner", DIORITE_CHAIR_DINNER, true);
        registerFurniture("stone_chair_dinner", STONE_CHAIR_DINNER, true);
        registerFurniture("blackstone_chair_dinner", BLACKSTONE_CHAIR_DINNER, true);
        registerFurniture("deepslate_chair_dinner", DEEPSLATE_CHAIR_DINNER, true);

        //Froggy Chairs
        registerFurniture("froggy_chair", FROGGY_CHAIR, true);
        registerFurniture("froggy_chair_pink", FROGGY_CHAIR_PINK, true);
        registerFurniture("froggy_chair_light_blue", FROGGY_CHAIR_LIGHT_BLUE, true);
        registerFurniture("froggy_chair_blue", FROGGY_CHAIR_BLUE, true);
        registerFurniture("froggy_chair_orange",FROGGY_CHAIR_ORANGE, true);
        registerFurniture("froggy_chair_yellow", FROGGY_CHAIR_YELLOW, true);

        //Classic Chairs
        registerFurniture("chair_classic_wool", CHAIR_CLASSIC_WOOL, true);
        registerFurniture("oak_chair_classic", OAK_CHAIR_CLASSIC, true);
        registerFurniture("birch_chair_classic", BIRCH_CHAIR_CLASSIC, true);
        registerFurniture("spruce_chair_classic", SPRUCE_CHAIR_CLASSIC, true);
        registerFurniture("acacia_chair_classic", ACACIA_CHAIR_CLASSIC, true);
        registerFurniture("jungle_chair_classic", JUNGLE_CHAIR_CLASSIC, true);
        registerFurniture("dark_oak_chair_classic", DARK_OAK_CHAIR_CLASSIC, true);
        registerFurniture("warped_chair_classic", WARPED_CHAIR_CLASSIC, true);
        registerFurniture("crimson_chair_classic", CRIMSON_CHAIR_CLASSIC, true);
        registerFurniture("stripped_oak_chair_classic", STRIPPED_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_birch_chair_classic", STRIPPED_BIRCH_CHAIR_CLASSIC, true);
        registerFurniture("stripped_spruce_chair_classic", STRIPPED_SPRUCE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_acacia_chair_classic", STRIPPED_ACACIA_CHAIR_CLASSIC, true);
        registerFurniture("stripped_jungle_chair_classic", STRIPPED_JUNGLE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_dark_oak_chair_classic", STRIPPED_DARK_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_warped_chair_classic", STRIPPED_WARPED_CHAIR_CLASSIC, true);
        registerFurniture("stripped_crimson_chair_classic", STRIPPED_CRIMSON_CHAIR_CLASSIC, true);
        registerFurniture("quartz_chair_classic", QUARTZ_CHAIR_CLASSIC, true);
        registerFurniture("netherite_chair_classic", NETHERITE_CHAIR_CLASSIC, true);
        registerFurniture("light_wood_chair_classic", LIGHT_WOOD_CHAIR_CLASSIC, true);
        registerFurniture("dark_wood_chair_classic", DARK_WOOD_CHAIR_CLASSIC, true);
        registerFurniture("granite_chair_classic", GRANITE_CHAIR_CLASSIC, true);
        registerFurniture("calcite_chair_classic", CALCITE_CHAIR_CLASSIC, true);
        registerFurniture("andesite_chair_classic", ANDESITE_CHAIR_CLASSIC, true);
        registerFurniture("diorite_chair_classic", DIORITE_CHAIR_CLASSIC, true);
        registerFurniture("stone_chair_classic", STONE_CHAIR_CLASSIC, true);
        registerFurniture("blackstone_chair_classic", BLACKSTONE_CHAIR_CLASSIC, true);
        registerFurniture("deepslate_chair_classic", DEEPSLATE_CHAIR_CLASSIC, true);

        //Modern Chair
        registerFurniture("oak_chair_modern", OAK_CHAIR_MODERN, true);
        registerFurniture("birch_chair_modern", BIRCH_CHAIR_MODERN, true);
        registerFurniture("spruce_chair_modern", SPRUCE_CHAIR_MODERN, true);
        registerFurniture("acacia_chair_modern", ACACIA_CHAIR_MODERN, true);
        registerFurniture("jungle_chair_modern", JUNGLE_CHAIR_MODERN, true);
        registerFurniture("dark_oak_chair_modern",DARK_OAK_CHAIR_MODERN , true);
        registerFurniture("warped_chair_modern", WARPED_CHAIR_MODERN, true);
        registerFurniture("crimson_chair_modern", CRIMSON_CHAIR_MODERN, true);
        registerFurniture("stripped_oak_chair_modern", STRIPPED_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_birch_chair_modern", STRIPPED_BIRCH_CHAIR_MODERN, true);
        registerFurniture("stripped_spruce_chair_modern", STRIPPED_SPRUCE_CHAIR_MODERN, true);
        registerFurniture("stripped_acacia_chair_modern", STRIPPED_ACACIA_CHAIR_MODERN, true);
        registerFurniture("stripped_jungle_chair_modern", STRIPPED_JUNGLE_CHAIR_MODERN, true);
        registerFurniture("stripped_dark_oak_chair_modern",STRIPPED_DARK_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_warped_chair_modern", STRIPPED_WARPED_CHAIR_MODERN, true);
        registerFurniture("stripped_crimson_chair_modern", STRIPPED_CRIMSON_CHAIR_MODERN, true);
        registerFurniture("quartz_chair_modern", QUARTZ_CHAIR_MODERN, true);
        registerFurniture("netherite_chair_modern", NETHERITE_CHAIR_MODERN, true);
        registerFurniture("light_wood_chair_modern", LIGHT_WOOD_CHAIR_MODERN, true);
        registerFurniture("dark_wood_chair_modern", DARK_WOOD_CHAIR_MODERN, true);
        registerFurniture("granite_chair_modern", GRANITE_CHAIR_MODERN, true);
        registerFurniture("calcite_chair_modern", CALCITE_CHAIR_MODERN, true);
        registerFurniture("andesite_chair_modern", ANDESITE_CHAIR_MODERN, true);
        registerFurniture("diorite_chair_modern", DIORITE_CHAIR_MODERN, true);
        registerFurniture("stone_chair_modern", STONE_CHAIR_MODERN, true);
        registerFurniture("blackstone_chair_modern", BLACKSTONE_CHAIR_MODERN, true);
        registerFurniture("deepslate_chair_modern", DEEPSLATE_CHAIR_MODERN, true);

        //Armchairs
        registerFurniture("arm_chair_standard", ARM_CHAIR_STANDARD, true);
        registerFurniture("arm_chair_leather", ARM_CHAIR_LEATHER, true);
        registerFurniture("sofa_simple", SOFA_SIMPLE, true);

        //Tables
        registerFurniture("oak_table_basic", OAK_BASIC_TABLE, true);
        registerFurniture("birch_table_basic", BIRCH_BASIC_TABLE, true);
        registerFurniture("spruce_table_basic", SPRUCE_BASIC_TABLE, true);
        registerFurniture("acacia_table_basic", ACACIA_BASIC_TABLE, true);
        registerFurniture("jungle_table_basic", JUNGLE_BASIC_TABLE, true);
        registerFurniture("dark_oak_table_basic",DARK_OAK_BASIC_TABLE, true);
        registerFurniture("crimson_table_basic", CRIMSON_BASIC_TABLE, true);
        registerFurniture("warped_table_basic", WARPED_BASIC_TABLE, true);
        registerFurniture("stripped_oak_table_basic", STRIPPED_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_birch_table_basic", STRIPPED_BIRCH_BASIC_TABLE, true);
        registerFurniture("stripped_spruce_table_basic", STRIPPED_SPRUCE_BASIC_TABLE, true);
        registerFurniture("stripped_acacia_table_basic", STRIPPED_ACACIA_BASIC_TABLE, true);
        registerFurniture("stripped_jungle_table_basic", STRIPPED_JUNGLE_BASIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_basic", STRIPPED_DARK_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_crimson_table_basic", STRIPPED_CRIMSON_BASIC_TABLE, true);
        registerFurniture("stripped_warped_table_basic", STRIPPED_WARPED_BASIC_TABLE, true);
        registerFurniture("quartz_table_basic", QUARTZ_BASIC_TABLE, true);
        registerFurniture("netherite_table_basic", NETHERITE_BASIC_TABLE, true);
        registerFurniture("light_wood_table_basic", LIGHT_WOOD_BASIC_TABLE, true);
        registerFurniture("dark_wood_table_basic", DARK_WOOD_BASIC_TABLE, true);
        registerFurniture("granite_table_basic", GRANITE_BASIC_TABLE, true);
        registerFurniture("calcite_table_basic", CALCITE_BASIC_TABLE, true);
        registerFurniture("andesite_table_basic", ANDESITE_BASIC_TABLE, true);
        registerFurniture("diorite_table_basic", DIORITE_BASIC_TABLE, true);
        registerFurniture("stone_table_basic", STONE_BASIC_TABLE, true);
        registerFurniture("blackstone_table_basic", BLACKSTONE_BASIC_TABLE, true);
        registerFurniture("deepslate_table_basic", DEEPSLATE_BASIC_TABLE, true);

        //Classic Table
        registerFurniture("oak_table_classic", OAK_CLASSIC_TABLE, true);
        registerFurniture("birch_table_classic", BIRCH_CLASSIC_TABLE, true);
        registerFurniture("spruce_table_classic", SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("acacia_table_classic", ACACIA_CLASSIC_TABLE, true);
        registerFurniture("jungle_table_classic", JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("dark_oak_table_classic", DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("crimson_table_classic", CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("warped_table_classic", WARPED_CLASSIC_TABLE, true);
        registerFurniture("stripped_oak_table_classic", STRIPPED_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_birch_table_classic", STRIPPED_BIRCH_CLASSIC_TABLE, true);
        registerFurniture("stripped_spruce_table_classic", STRIPPED_SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("stripped_acacia_table_classic", STRIPPED_ACACIA_CLASSIC_TABLE, true);
        registerFurniture("stripped_jungle_table_classic", STRIPPED_JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_classic", STRIPPED_DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_crimson_table_classic", STRIPPED_CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("stripped_warped_table_classic", STRIPPED_WARPED_CLASSIC_TABLE, true);
        registerFurniture("quartz_table_classic", QUARTZ_CLASSIC_TABLE, true);
        registerFurniture("netherite_table_classic", NETHERITE_CLASSIC_TABLE, true);
        registerFurniture("light_wood_table_classic", LIGHT_WOOD_CLASSIC_TABLE, true);
        registerFurniture("dark_wood_table_classic", DARK_WOOD_CLASSIC_TABLE, true);
        registerFurniture("granite_table_classic", GRANITE_CLASSIC_TABLE, true);
        registerFurniture("calcite_table_classic", CALCITE_CLASSIC_TABLE, true);
        registerFurniture("andesite_table_classic", ANDESITE_CLASSIC_TABLE, true);
        registerFurniture("diorite_table_classic", DIORITE_CLASSIC_TABLE, true);
        registerFurniture("stone_table_classic", STONE_CLASSIC_TABLE, true);
        registerFurniture("blackstone_table_classic", BLACKSTONE_CLASSIC_TABLE, true);
        registerFurniture("deepslate_table_classic", DEEPSLATE_CLASSIC_TABLE, true);

        //Log Table
        registerFurniture("oak_table_log", OAK_LOG_TABLE, true);
        registerFurniture("birch_table_log", BIRCH_LOG_TABLE, true);
        registerFurniture("spruce_table_log", SPRUCE_LOG_TABLE, true);
        registerFurniture("acacia_table_log", ACACIA_LOG_TABLE, true);
        registerFurniture("jungle_table_log", JUNGLE_LOG_TABLE, true);
        registerFurniture("dark_oak_table_log", DARK_OAK_LOG_TABLE, true);
        registerFurniture("crimson_table_stem", CRIMSON_STEM_TABLE, true);
        registerFurniture("warped_table_stem", WARPED_STEM_TABLE, true);
        registerFurniture("stripped_oak_table_log", STRIPPED_OAK_LOG_TABLE, true);
        registerFurniture("stripped_birch_table_log", STRIPPED_BIRCH_LOG_TABLE, true);
        registerFurniture("stripped_spruce_table_log", STRIPPED_SPRUCE_LOG_TABLE, true);
        registerFurniture("stripped_acacia_table_log", STRIPPED_ACACIA_LOG_TABLE, true);
        registerFurniture("stripped_jungle_table_log", STRIPPED_JUNGLE_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_table_log", STRIPPED_DARK_OAK_LOG_TABLE, true);
        registerFurniture("stripped_crimson_table_stem", STRIPPED_CRIMSON_STEM_TABLE, true);
        registerFurniture("stripped_warped_table_stem", STRIPPED_WARPED_STEM_TABLE, true);
        registerFurniture("quartz_table_natural", QUARTZ_NATURAL_TABLE, true);
        registerFurniture("netherite_table_natural", NETHERITE_NATURAL_TABLE, true);
        registerFurniture("light_wood_table_natural", LIGHT_WOOD_NATURAL_TABLE, true);
        registerFurniture("dark_wood_table_natural", DARK_WOOD_NATURAL_TABLE, true);
        registerFurniture("granite_table_natural", GRANITE_NATURAL_TABLE, true);
        registerFurniture("calcite_table_natural", CALCITE_NATURAL_TABLE, true);
        registerFurniture("andesite_table_natural", ANDESITE_NATURAL_TABLE, true);
        registerFurniture("diorite_table_natural", DIORITE_NATURAL_TABLE, true);
        registerFurniture("stone_table_natural", STONE_NATURAL_TABLE, true);
        registerFurniture("blackstone_table_natural", BLACKSTONE_NATURAL_TABLE, true);
        registerFurniture("deepslate_table_natural", DEEPSLATE_NATURAL_TABLE, true);

        //Raw Log Table
        registerFurniture("oak_raw_table_log", OAK_RAW_LOG_TABLE, true);
        registerFurniture("birch_raw_table_log", BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("acacia_raw_table_log", ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("spruce_raw_table_log", SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("jungle_raw_table_log", JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("dark_oak_raw_table_log", DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("warped_raw_table_stem", WARPED_RAW_STEM_TABLE, true);
        registerFurniture("crimson_raw_table_stem", CRIMSON_RAW_STEM_TABLE, true);
        registerFurniture("stripped_oak_raw_table_log", STRIPPED_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_birch_raw_table_log", STRIPPED_BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("stripped_acacia_raw_table_log", STRIPPED_ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("stripped_spruce_raw_table_log", STRIPPED_SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_jungle_raw_table_log", STRIPPED_JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_raw_table_log", STRIPPED_DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_warped_raw_table_stem", STRIPPED_WARPED_RAW_STEM_TABLE, true);
        registerFurniture("stripped_crimson_raw_table_stem", STRIPPED_CRIMSON_RAW_STEM_TABLE, true);

        registerFurniture("oak_table_dinner", OAK_DINNER_TABLE, true);
        registerFurniture("birch_table_dinner", BIRCH_DINNER_TABLE, true);
        registerFurniture("spruce_table_dinner", SPRUCE_DINNER_TABLE, true);
        registerFurniture("acacia_table_dinner", ACACIA_DINNER_TABLE, true);
        registerFurniture("jungle_table_dinner", JUNGLE_DINNER_TABLE, true);
        registerFurniture("dark_oak_table_dinner", DARK_OAK_DINNER_TABLE, true);
        registerFurniture("crimson_table_dinner", CRIMSON_DINNER_TABLE, true);
        registerFurniture("warped_table_dinner", WARPED_DINNER_TABLE, true);
        registerFurniture("stripped_oak_table_dinner", STRIPPED_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_birch_table_dinner", STRIPPED_BIRCH_DINNER_TABLE, true);
        registerFurniture("stripped_spruce_table_dinner", STRIPPED_SPRUCE_DINNER_TABLE, true);
        registerFurniture("stripped_acacia_table_dinner", STRIPPED_ACACIA_DINNER_TABLE, true);
        registerFurniture("stripped_jungle_table_dinner", STRIPPED_JUNGLE_DINNER_TABLE, true);
        registerFurniture("stripped_dark_oak_table_dinner", STRIPPED_DARK_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_crimson_table_dinner", STRIPPED_CRIMSON_DINNER_TABLE, true);
        registerFurniture("stripped_warped_table_dinner", STRIPPED_WARPED_DINNER_TABLE, true);
        registerFurniture("quartz_table_dinner", QUARTZ_DINNER_TABLE, true);
        registerFurniture("netherite_table_dinner", NETHERITE_DINNER_TABLE, true);
        registerFurniture("light_wood_table_dinner", LIGHT_WOOD_DINNER_TABLE, true);
        registerFurniture("dark_wood_table_dinner", DARK_WOOD_DINNER_TABLE, true);
        registerFurniture("granite_table_dinner", GRANITE_DINNER_TABLE, true);
        registerFurniture("calcite_table_dinner", CALCITE_DINNER_TABLE, true);
        registerFurniture("andesite_table_dinner", ANDESITE_DINNER_TABLE, true);
        registerFurniture("diorite_table_dinner", DIORITE_DINNER_TABLE, true);
        registerFurniture("stone_table_dinner", STONE_DINNER_TABLE, true);
        registerFurniture("blackstone_table_dinner", BLACKSTONE_DINNER_TABLE, true);
        registerFurniture("deepslate_table_dinner", DEEPSLATE_DINNER_TABLE, true);

        registerFurniture("oak_table_modern_dinner", OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("birch_table_modern_dinner", BIRCH_MODERN_DINNER_TABLE, true);
        registerFurniture("spruce_table_modern_dinner", SPRUCE_MODERN_DINNER_TABLE, true);
        registerFurniture("acacia_table_modern_dinner", ACACIA_MODERN_DINNER_TABLE, true);
        registerFurniture("jungle_table_modern_dinner", JUNGLE_MODERN_DINNER_TABLE, true);
        registerFurniture("dark_oak_table_modern_dinner",DARK_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("crimson_table_modern_dinner", CRIMSON_MODERN_DINNER_TABLE, true);
        registerFurniture("warped_table_modern_dinner", WARPED_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_oak_table_modern_dinner", STRIPPED_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_birch_table_modern_dinner", STRIPPED_BIRCH_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_spruce_table_modern_dinner", STRIPPED_SPRUCE_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_acacia_table_modern_dinner", STRIPPED_ACACIA_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_jungle_table_modern_dinner", STRIPPED_JUNGLE_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_dark_oak_table_modern_dinner", STRIPPED_DARK_OAK_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_crimson_table_modern_dinner", STRIPPED_CRIMSON_MODERN_DINNER_TABLE, true);
        registerFurniture("stripped_warped_table_modern_dinner", STRIPPED_WARPED_MODERN_DINNER_TABLE, true);
        registerFurniture("quartz_table_modern_dinner", QUARTZ_MODERN_DINNER_TABLE, true);
        registerFurniture("netherite_table_modern_dinner", NETHERITE_MODERN_DINNER_TABLE, true);
        registerFurniture("light_wood_table_modern_dinner", LIGHT_WOOD_MODERN_DINNER_TABLE, true);
        registerFurniture("dark_wood_table_modern_dinner", DARK_WOOD_MODERN_DINNER_TABLE, true);
        registerFurniture("granite_table_modern_dinner", GRANITE_MODERN_DINNER_TABLE, true);
        registerFurniture("calcite_table_modern_dinner", CALCITE_MODERN_DINNER_TABLE, true);
        registerFurniture("andesite_table_modern_dinner", ANDESITE_MODERN_DINNER_TABLE, true);
        registerFurniture("diorite_table_modern_dinner", DIORITE_MODERN_DINNER_TABLE, true);
        registerFurniture("stone_table_modern_dinner", STONE_MODERN_DINNER_TABLE, true);
        registerFurniture("blackstone_table_modern_dinner", BLACKSTONE_MODERN_DINNER_TABLE, true);
        registerFurniture("deepslate_table_modern_dinner", DEEPSLATE_MODERN_DINNER_TABLE, true);

        registerFurniture("oak_classic_nightstand", OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("birch_classic_nightstand", BIRCH_CLASSIC_NIGHTSTAND, true);
        registerFurniture("spruce_classic_nightstand", SPRUCE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("acacia_classic_nightstand", ACACIA_CLASSIC_NIGHTSTAND, true);
        registerFurniture("jungle_classic_nightstand", JUNGLE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("dark_oak_classic_nightstand", DARK_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("crimson_classic_nightstand", CRIMSON_CLASSIC_NIGHTSTAND, true);
        registerFurniture("warped_classic_nightstand", WARPED_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_oak_classic_nightstand", STRIPPED_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_birch_classic_nightstand", STRIPPED_BIRCH_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_spruce_classic_nightstand", STRIPPED_SPRUCE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_acacia_classic_nightstand", STRIPPED_ACACIA_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_jungle_classic_nightstand", STRIPPED_JUNGLE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_dark_oak_classic_nightstand", STRIPPED_DARK_OAK_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_crimson_classic_nightstand", STRIPPED_CRIMSON_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stripped_warped_classic_nightstand", STRIPPED_WARPED_CLASSIC_NIGHTSTAND, true);
        registerFurniture("quartz_classic_nightstand", QUARTZ_CLASSIC_NIGHTSTAND, true);
        registerFurniture("netherite_classic_nightstand", NETHERITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("light_wood_classic_nightstand", LIGHT_WOOD_CLASSIC_NIGHTSTAND, true);
        registerFurniture("dark_wood_classic_nightstand", DARK_WOOD_CLASSIC_NIGHTSTAND, true);
        registerFurniture("granite_classic_nightstand", GRANITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("calcite_classic_nightstand", CALCITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("andesite_classic_nightstand", ANDESITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("diorite_classic_nightstand", DIORITE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("stone_classic_nightstand", STONE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("blackstone_classic_nightstand", BLACKSTONE_CLASSIC_NIGHTSTAND, true);
        registerFurniture("deepslate_classic_nightstand", DEEPSLATE_CLASSIC_NIGHTSTAND, true);

        registerFurniture("oak_red_simple_bed", OAK_RED_SIMPLE_BED, 1);
        registerFurniture("oak_orange_simple_bed", OAK_ORANGE_SIMPLE_BED, 1);
        registerFurniture("oak_yellow_simple_bed", OAK_YELLOW_SIMPLE_BED, 1);
        registerFurniture("oak_green_simple_bed", OAK_GREEN_SIMPLE_BED, 1);
        registerFurniture("oak_lime_simple_bed", OAK_LIME_SIMPLE_BED, 1);
        registerFurniture("oak_blue_simple_bed", OAK_BLUE_SIMPLE_BED, 1);
        registerFurniture("oak_cyan_simple_bed", OAK_CYAN_SIMPLE_BED, 1);
        registerFurniture("oak_light_blue_simple_bed", OAK_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("oak_light_gray_simple_bed", OAK_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("oak_purple_simple_bed", OAK_PURPLE_SIMPLE_BED, 1);
        registerFurniture("oak_magenta_simple_bed", OAK_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("oak_pink_simple_bed", OAK_PINK_SIMPLE_BED, 1);
        registerFurniture("oak_brown_simple_bed", OAK_BROWN_SIMPLE_BED, 1);
        registerFurniture("oak_gray_simple_bed", OAK_GRAY_SIMPLE_BED, 1);
        registerFurniture("oak_black_simple_bed", OAK_BLACK_SIMPLE_BED, 1);
        registerFurniture("oak_white_simple_bed", OAK_WHITE_SIMPLE_BED, 1);
        registerFurniture("spruce_red_simple_bed", SPRUCE_RED_SIMPLE_BED, 1);
        registerFurniture("spruce_orange_simple_bed", SPRUCE_ORANGE_SIMPLE_BED, 1);
        registerFurniture("spruce_yellow_simple_bed", SPRUCE_YELLOW_SIMPLE_BED, 1);
        registerFurniture("spruce_green_simple_bed", SPRUCE_GREEN_SIMPLE_BED, 1);
        registerFurniture("spruce_lime_simple_bed", SPRUCE_LIME_SIMPLE_BED, 1);
        registerFurniture("spruce_blue_simple_bed", SPRUCE_BLUE_SIMPLE_BED, 1);
        registerFurniture("spruce_cyan_simple_bed", SPRUCE_CYAN_SIMPLE_BED, 1);
        registerFurniture("spruce_light_blue_simple_bed", SPRUCE_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("spruce_light_gray_simple_bed", SPRUCE_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("spruce_purple_simple_bed", SPRUCE_PURPLE_SIMPLE_BED, 1);
        registerFurniture("spruce_magenta_simple_bed", SPRUCE_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("spruce_pink_simple_bed", SPRUCE_PINK_SIMPLE_BED, 1);
        registerFurniture("spruce_brown_simple_bed", SPRUCE_BROWN_SIMPLE_BED, 1);
        registerFurniture("spruce_gray_simple_bed", SPRUCE_GRAY_SIMPLE_BED, 1);
        registerFurniture("spruce_black_simple_bed", SPRUCE_BLACK_SIMPLE_BED, 1);
        registerFurniture("spruce_white_simple_bed", SPRUCE_WHITE_SIMPLE_BED, 1);
        registerFurniture("birch_red_simple_bed", BIRCH_RED_SIMPLE_BED, 1);
        registerFurniture("birch_orange_simple_bed", BIRCH_ORANGE_SIMPLE_BED, 1);
        registerFurniture("birch_yellow_simple_bed", BIRCH_YELLOW_SIMPLE_BED, 1);
        registerFurniture("birch_green_simple_bed", BIRCH_GREEN_SIMPLE_BED, 1);
        registerFurniture("birch_lime_simple_bed", BIRCH_LIME_SIMPLE_BED, 1);
        registerFurniture("birch_blue_simple_bed", BIRCH_BLUE_SIMPLE_BED, 1);
        registerFurniture("birch_cyan_simple_bed", BIRCH_CYAN_SIMPLE_BED, 1);
        registerFurniture("birch_light_blue_simple_bed", BIRCH_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("birch_light_gray_simple_bed", BIRCH_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("birch_purple_simple_bed", BIRCH_PURPLE_SIMPLE_BED, 1);
        registerFurniture("birch_magenta_simple_bed", BIRCH_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("birch_pink_simple_bed", BIRCH_PINK_SIMPLE_BED, 1);
        registerFurniture("birch_brown_simple_bed", BIRCH_BROWN_SIMPLE_BED, 1);
        registerFurniture("birch_gray_simple_bed", BIRCH_GRAY_SIMPLE_BED, 1);
        registerFurniture("birch_black_simple_bed", BIRCH_BLACK_SIMPLE_BED, 1);
        registerFurniture("birch_white_simple_bed", BIRCH_WHITE_SIMPLE_BED, 1);
        registerFurniture("acacia_red_simple_bed", ACACIA_RED_SIMPLE_BED, 1);
        registerFurniture("acacia_orange_simple_bed", ACACIA_ORANGE_SIMPLE_BED, 1);
        registerFurniture("acacia_yellow_simple_bed", ACACIA_YELLOW_SIMPLE_BED, 1);
        registerFurniture("acacia_green_simple_bed", ACACIA_GREEN_SIMPLE_BED, 1);
        registerFurniture("acacia_lime_simple_bed", ACACIA_LIME_SIMPLE_BED, 1);
        registerFurniture("acacia_blue_simple_bed", ACACIA_BLUE_SIMPLE_BED, 1);
        registerFurniture("acacia_cyan_simple_bed", ACACIA_CYAN_SIMPLE_BED, 1);
        registerFurniture("acacia_light_blue_simple_bed", ACACIA_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("acacia_light_gray_simple_bed", ACACIA_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("acacia_purple_simple_bed", ACACIA_PURPLE_SIMPLE_BED, 1);
        registerFurniture("acacia_magenta_simple_bed", ACACIA_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("acacia_pink_simple_bed", ACACIA_PINK_SIMPLE_BED, 1);
        registerFurniture("acacia_brown_simple_bed", ACACIA_BROWN_SIMPLE_BED, 1);
        registerFurniture("acacia_gray_simple_bed", ACACIA_GRAY_SIMPLE_BED, 1);
        registerFurniture("acacia_black_simple_bed", ACACIA_BLACK_SIMPLE_BED, 1);
        registerFurniture("acacia_white_simple_bed", ACACIA_WHITE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_red_simple_bed", DARK_OAK_RED_SIMPLE_BED, 1);
        registerFurniture("dark_oak_orange_simple_bed", DARK_OAK_ORANGE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_yellow_simple_bed", DARK_OAK_YELLOW_SIMPLE_BED, 1);
        registerFurniture("dark_oak_green_simple_bed", DARK_OAK_GREEN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_lime_simple_bed", DARK_OAK_LIME_SIMPLE_BED, 1);
        registerFurniture("dark_oak_blue_simple_bed", DARK_OAK_BLUE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_cyan_simple_bed", DARK_OAK_CYAN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_light_blue_simple_bed", DARK_OAK_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_light_gray_simple_bed", DARK_OAK_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("dark_oak_purple_simple_bed", DARK_OAK_PURPLE_SIMPLE_BED, 1);
        registerFurniture("dark_oak_magenta_simple_bed", DARK_OAK_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("dark_oak_pink_simple_bed", DARK_OAK_PINK_SIMPLE_BED, 1);
        registerFurniture("dark_oak_brown_simple_bed", DARK_OAK_BROWN_SIMPLE_BED, 1);
        registerFurniture("dark_oak_gray_simple_bed", DARK_OAK_GRAY_SIMPLE_BED, 1);
        registerFurniture("dark_oak_black_simple_bed", DARK_OAK_BLACK_SIMPLE_BED, 1);
        registerFurniture("dark_oak_white_simple_bed", DARK_OAK_WHITE_SIMPLE_BED, 1);
        registerFurniture("jungle_red_simple_bed", JUNGLE_RED_SIMPLE_BED, 1);
        registerFurniture("jungle_orange_simple_bed", JUNGLE_ORANGE_SIMPLE_BED, 1);
        registerFurniture("jungle_yellow_simple_bed", JUNGLE_YELLOW_SIMPLE_BED, 1);
        registerFurniture("jungle_green_simple_bed", JUNGLE_GREEN_SIMPLE_BED, 1);
        registerFurniture("jungle_lime_simple_bed", JUNGLE_LIME_SIMPLE_BED, 1);
        registerFurniture("jungle_blue_simple_bed", JUNGLE_BLUE_SIMPLE_BED, 1);
        registerFurniture("jungle_cyan_simple_bed", JUNGLE_CYAN_SIMPLE_BED, 1);
        registerFurniture("jungle_light_blue_simple_bed", JUNGLE_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("jungle_light_gray_simple_bed", JUNGLE_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("jungle_purple_simple_bed", JUNGLE_PURPLE_SIMPLE_BED, 1);
        registerFurniture("jungle_magenta_simple_bed", JUNGLE_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("jungle_pink_simple_bed", JUNGLE_PINK_SIMPLE_BED, 1);
        registerFurniture("jungle_brown_simple_bed", JUNGLE_BROWN_SIMPLE_BED, 1);
        registerFurniture("jungle_gray_simple_bed", JUNGLE_GRAY_SIMPLE_BED, 1);
        registerFurniture("jungle_black_simple_bed", JUNGLE_BLACK_SIMPLE_BED, 1);
        registerFurniture("jungle_white_simple_bed", JUNGLE_WHITE_SIMPLE_BED, 1);
        registerFurniture("warped_red_simple_bed", WARPED_RED_SIMPLE_BED, 1);
        registerFurniture("warped_orange_simple_bed", WARPED_ORANGE_SIMPLE_BED, 1);
        registerFurniture("warped_yellow_simple_bed", WARPED_YELLOW_SIMPLE_BED, 1);
        registerFurniture("warped_green_simple_bed", WARPED_GREEN_SIMPLE_BED, 1);
        registerFurniture("warped_lime_simple_bed", WARPED_LIME_SIMPLE_BED, 1);
        registerFurniture("warped_blue_simple_bed", WARPED_BLUE_SIMPLE_BED, 1);
        registerFurniture("warped_cyan_simple_bed", WARPED_CYAN_SIMPLE_BED, 1);
        registerFurniture("warped_light_blue_simple_bed", WARPED_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("warped_light_gray_simple_bed", WARPED_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("warped_purple_simple_bed", WARPED_PURPLE_SIMPLE_BED, 1);
        registerFurniture("warped_magenta_simple_bed", WARPED_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("warped_pink_simple_bed", WARPED_PINK_SIMPLE_BED, 1);
        registerFurniture("warped_brown_simple_bed", WARPED_BROWN_SIMPLE_BED, 1);
        registerFurniture("warped_gray_simple_bed", WARPED_GRAY_SIMPLE_BED, 1);
        registerFurniture("warped_black_simple_bed", WARPED_BLACK_SIMPLE_BED, 1);
        registerFurniture("warped_white_simple_bed", WARPED_WHITE_SIMPLE_BED, 1);
        registerFurniture("crimson_red_simple_bed", CRIMSON_RED_SIMPLE_BED, 1);
        registerFurniture("crimson_orange_simple_bed", CRIMSON_ORANGE_SIMPLE_BED, 1);
        registerFurniture("crimson_yellow_simple_bed", CRIMSON_YELLOW_SIMPLE_BED, 1);
        registerFurniture("crimson_green_simple_bed", CRIMSON_GREEN_SIMPLE_BED, 1);
        registerFurniture("crimson_lime_simple_bed", CRIMSON_LIME_SIMPLE_BED, 1);
        registerFurniture("crimson_blue_simple_bed", CRIMSON_BLUE_SIMPLE_BED, 1);
        registerFurniture("crimson_cyan_simple_bed", CRIMSON_CYAN_SIMPLE_BED, 1);
        registerFurniture("crimson_light_blue_simple_bed", CRIMSON_LIGHT_BLUE_SIMPLE_BED, 1);
        registerFurniture("crimson_light_gray_simple_bed", CRIMSON_LIGHT_GRAY_SIMPLE_BED, 1);
        registerFurniture("crimson_purple_simple_bed", CRIMSON_PURPLE_SIMPLE_BED, 1);
        registerFurniture("crimson_magenta_simple_bed", CRIMSON_MAGENTA_SIMPLE_BED, 1);
        registerFurniture("crimson_pink_simple_bed", CRIMSON_PINK_SIMPLE_BED, 1);
        registerFurniture("crimson_brown_simple_bed", CRIMSON_BROWN_SIMPLE_BED, 1);
        registerFurniture("crimson_gray_simple_bed", CRIMSON_GRAY_SIMPLE_BED, 1);
        registerFurniture("crimson_black_simple_bed", CRIMSON_BLACK_SIMPLE_BED, 1);
        registerFurniture("crimson_white_simple_bed", CRIMSON_WHITE_SIMPLE_BED, 1);

        registerFurniture("oak_red_classic_bed", OAK_RED_CLASSIC_BED, 1);
        registerFurniture("oak_orange_classic_bed", OAK_ORANGE_CLASSIC_BED, 1);
        registerFurniture("oak_yellow_classic_bed", OAK_YELLOW_CLASSIC_BED, 1);
        registerFurniture("oak_green_classic_bed", OAK_GREEN_CLASSIC_BED, 1);
        registerFurniture("oak_lime_classic_bed", OAK_LIME_CLASSIC_BED, 1);
        registerFurniture("oak_blue_classic_bed", OAK_BLUE_CLASSIC_BED, 1);
        registerFurniture("oak_cyan_classic_bed", OAK_CYAN_CLASSIC_BED, 1);
        registerFurniture("oak_light_blue_classic_bed", OAK_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("oak_light_gray_classic_bed", OAK_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("oak_purple_classic_bed", OAK_PURPLE_CLASSIC_BED, 1);
        registerFurniture("oak_magenta_classic_bed", OAK_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("oak_pink_classic_bed", OAK_PINK_CLASSIC_BED, 1);
        registerFurniture("oak_brown_classic_bed", OAK_BROWN_CLASSIC_BED, 1);
        registerFurniture("oak_gray_classic_bed", OAK_GRAY_CLASSIC_BED, 1);
        registerFurniture("oak_black_classic_bed", OAK_BLACK_CLASSIC_BED, 1);
        registerFurniture("oak_white_classic_bed", OAK_WHITE_CLASSIC_BED, 1);
        registerFurniture("spruce_red_classic_bed", SPRUCE_RED_CLASSIC_BED, 1);
        registerFurniture("spruce_orange_classic_bed", SPRUCE_ORANGE_CLASSIC_BED, 1);
        registerFurniture("spruce_yellow_classic_bed", SPRUCE_YELLOW_CLASSIC_BED, 1);
        registerFurniture("spruce_green_classic_bed", SPRUCE_GREEN_CLASSIC_BED, 1);
        registerFurniture("spruce_lime_classic_bed", SPRUCE_LIME_CLASSIC_BED, 1);
        registerFurniture("spruce_blue_classic_bed", SPRUCE_BLUE_CLASSIC_BED, 1);
        registerFurniture("spruce_cyan_classic_bed", SPRUCE_CYAN_CLASSIC_BED, 1);
        registerFurniture("spruce_light_blue_classic_bed", SPRUCE_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("spruce_light_gray_classic_bed", SPRUCE_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("spruce_purple_classic_bed", SPRUCE_PURPLE_CLASSIC_BED, 1);
        registerFurniture("spruce_magenta_classic_bed", SPRUCE_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("spruce_pink_classic_bed", SPRUCE_PINK_CLASSIC_BED, 1);
        registerFurniture("spruce_brown_classic_bed", SPRUCE_BROWN_CLASSIC_BED, 1);
        registerFurniture("spruce_gray_classic_bed", SPRUCE_GRAY_CLASSIC_BED, 1);
        registerFurniture("spruce_black_classic_bed", SPRUCE_BLACK_CLASSIC_BED, 1);
        registerFurniture("spruce_white_classic_bed", SPRUCE_WHITE_CLASSIC_BED, 1);
        registerFurniture("birch_red_classic_bed", BIRCH_RED_CLASSIC_BED, 1);
        registerFurniture("birch_orange_classic_bed", BIRCH_ORANGE_CLASSIC_BED, 1);
        registerFurniture("birch_yellow_classic_bed", BIRCH_YELLOW_CLASSIC_BED, 1);
        registerFurniture("birch_green_classic_bed", BIRCH_GREEN_CLASSIC_BED, 1);
        registerFurniture("birch_lime_classic_bed", BIRCH_LIME_CLASSIC_BED, 1);
        registerFurniture("birch_blue_classic_bed", BIRCH_BLUE_CLASSIC_BED, 1);
        registerFurniture("birch_cyan_classic_bed", BIRCH_CYAN_CLASSIC_BED, 1);
        registerFurniture("birch_light_blue_classic_bed", BIRCH_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("birch_light_gray_classic_bed", BIRCH_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("birch_purple_classic_bed", BIRCH_PURPLE_CLASSIC_BED, 1);
        registerFurniture("birch_magenta_classic_bed", BIRCH_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("birch_pink_classic_bed", BIRCH_PINK_CLASSIC_BED, 1);
        registerFurniture("birch_brown_classic_bed", BIRCH_BROWN_CLASSIC_BED, 1);
        registerFurniture("birch_gray_classic_bed", BIRCH_GRAY_CLASSIC_BED, 1);
        registerFurniture("birch_black_classic_bed", BIRCH_BLACK_CLASSIC_BED, 1);
        registerFurniture("birch_white_classic_bed", BIRCH_WHITE_CLASSIC_BED, 1);
        registerFurniture("acacia_red_classic_bed", ACACIA_RED_CLASSIC_BED, 1);
        registerFurniture("acacia_orange_classic_bed", ACACIA_ORANGE_CLASSIC_BED, 1);
        registerFurniture("acacia_yellow_classic_bed", ACACIA_YELLOW_CLASSIC_BED, 1);
        registerFurniture("acacia_green_classic_bed", ACACIA_GREEN_CLASSIC_BED, 1);
        registerFurniture("acacia_lime_classic_bed", ACACIA_LIME_CLASSIC_BED, 1);
        registerFurniture("acacia_blue_classic_bed", ACACIA_BLUE_CLASSIC_BED, 1);
        registerFurniture("acacia_cyan_classic_bed", ACACIA_CYAN_CLASSIC_BED, 1);
        registerFurniture("acacia_light_blue_classic_bed", ACACIA_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("acacia_light_gray_classic_bed", ACACIA_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("acacia_purple_classic_bed", ACACIA_PURPLE_CLASSIC_BED, 1);
        registerFurniture("acacia_magenta_classic_bed", ACACIA_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("acacia_pink_classic_bed", ACACIA_PINK_CLASSIC_BED, 1);
        registerFurniture("acacia_brown_classic_bed", ACACIA_BROWN_CLASSIC_BED, 1);
        registerFurniture("acacia_gray_classic_bed", ACACIA_GRAY_CLASSIC_BED, 1);
        registerFurniture("acacia_black_classic_bed", ACACIA_BLACK_CLASSIC_BED, 1);
        registerFurniture("acacia_white_classic_bed", ACACIA_WHITE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_red_classic_bed", DARK_OAK_RED_CLASSIC_BED, 1);
        registerFurniture("dark_oak_orange_classic_bed", DARK_OAK_ORANGE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_yellow_classic_bed", DARK_OAK_YELLOW_CLASSIC_BED, 1);
        registerFurniture("dark_oak_green_classic_bed", DARK_OAK_GREEN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_lime_classic_bed", DARK_OAK_LIME_CLASSIC_BED, 1);
        registerFurniture("dark_oak_blue_classic_bed", DARK_OAK_BLUE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_cyan_classic_bed", DARK_OAK_CYAN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_light_blue_classic_bed", DARK_OAK_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_light_gray_classic_bed", DARK_OAK_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("dark_oak_purple_classic_bed", DARK_OAK_PURPLE_CLASSIC_BED, 1);
        registerFurniture("dark_oak_magenta_classic_bed", DARK_OAK_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("dark_oak_pink_classic_bed", DARK_OAK_PINK_CLASSIC_BED, 1);
        registerFurniture("dark_oak_brown_classic_bed", DARK_OAK_BROWN_CLASSIC_BED, 1);
        registerFurniture("dark_oak_gray_classic_bed", DARK_OAK_GRAY_CLASSIC_BED, 1);
        registerFurniture("dark_oak_black_classic_bed", DARK_OAK_BLACK_CLASSIC_BED, 1);
        registerFurniture("dark_oak_white_classic_bed", DARK_OAK_WHITE_CLASSIC_BED, 1);
        registerFurniture("jungle_red_classic_bed", JUNGLE_RED_CLASSIC_BED, 1);
        registerFurniture("jungle_orange_classic_bed", JUNGLE_ORANGE_CLASSIC_BED, 1);
        registerFurniture("jungle_yellow_classic_bed", JUNGLE_YELLOW_CLASSIC_BED, 1);
        registerFurniture("jungle_green_classic_bed", JUNGLE_GREEN_CLASSIC_BED, 1);
        registerFurniture("jungle_lime_classic_bed", JUNGLE_LIME_CLASSIC_BED, 1);
        registerFurniture("jungle_blue_classic_bed", JUNGLE_BLUE_CLASSIC_BED, 1);
        registerFurniture("jungle_cyan_classic_bed", JUNGLE_CYAN_CLASSIC_BED, 1);
        registerFurniture("jungle_light_blue_classic_bed", JUNGLE_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("jungle_light_gray_classic_bed", JUNGLE_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("jungle_purple_classic_bed", JUNGLE_PURPLE_CLASSIC_BED, 1);
        registerFurniture("jungle_magenta_classic_bed", JUNGLE_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("jungle_pink_classic_bed", JUNGLE_PINK_CLASSIC_BED, 1);
        registerFurniture("jungle_brown_classic_bed", JUNGLE_BROWN_CLASSIC_BED, 1);
        registerFurniture("jungle_gray_classic_bed", JUNGLE_GRAY_CLASSIC_BED, 1);
        registerFurniture("jungle_black_classic_bed", JUNGLE_BLACK_CLASSIC_BED, 1);
        registerFurniture("jungle_white_classic_bed", JUNGLE_WHITE_CLASSIC_BED, 1);
        registerFurniture("warped_red_classic_bed", WARPED_RED_CLASSIC_BED, 1);
        registerFurniture("warped_orange_classic_bed", WARPED_ORANGE_CLASSIC_BED, 1);
        registerFurniture("warped_yellow_classic_bed", WARPED_YELLOW_CLASSIC_BED, 1);
        registerFurniture("warped_green_classic_bed", WARPED_GREEN_CLASSIC_BED, 1);
        registerFurniture("warped_lime_classic_bed", WARPED_LIME_CLASSIC_BED, 1);
        registerFurniture("warped_blue_classic_bed", WARPED_BLUE_CLASSIC_BED, 1);
        registerFurniture("warped_cyan_classic_bed", WARPED_CYAN_CLASSIC_BED, 1);
        registerFurniture("warped_light_blue_classic_bed", WARPED_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("warped_light_gray_classic_bed", WARPED_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("warped_purple_classic_bed", WARPED_PURPLE_CLASSIC_BED, 1);
        registerFurniture("warped_magenta_classic_bed", WARPED_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("warped_pink_classic_bed", WARPED_PINK_CLASSIC_BED, 1);
        registerFurniture("warped_brown_classic_bed", WARPED_BROWN_CLASSIC_BED, 1);
        registerFurniture("warped_gray_classic_bed", WARPED_GRAY_CLASSIC_BED, 1);
        registerFurniture("warped_black_classic_bed", WARPED_BLACK_CLASSIC_BED, 1);
        registerFurniture("warped_white_classic_bed", WARPED_WHITE_CLASSIC_BED, 1);
        registerFurniture("crimson_red_classic_bed", CRIMSON_RED_CLASSIC_BED, 1);
        registerFurniture("crimson_orange_classic_bed", CRIMSON_ORANGE_CLASSIC_BED, 1);
        registerFurniture("crimson_yellow_classic_bed", CRIMSON_YELLOW_CLASSIC_BED, 1);
        registerFurniture("crimson_green_classic_bed", CRIMSON_GREEN_CLASSIC_BED, 1);
        registerFurniture("crimson_lime_classic_bed", CRIMSON_LIME_CLASSIC_BED, 1);
        registerFurniture("crimson_blue_classic_bed", CRIMSON_BLUE_CLASSIC_BED, 1);
        registerFurniture("crimson_cyan_classic_bed", CRIMSON_CYAN_CLASSIC_BED, 1);
        registerFurniture("crimson_light_blue_classic_bed", CRIMSON_LIGHT_BLUE_CLASSIC_BED, 1);
        registerFurniture("crimson_light_gray_classic_bed", CRIMSON_LIGHT_GRAY_CLASSIC_BED, 1);
        registerFurniture("crimson_purple_classic_bed", CRIMSON_PURPLE_CLASSIC_BED, 1);
        registerFurniture("crimson_magenta_classic_bed", CRIMSON_MAGENTA_CLASSIC_BED, 1);
        registerFurniture("crimson_pink_classic_bed", CRIMSON_PINK_CLASSIC_BED, 1);
        registerFurniture("crimson_brown_classic_bed", CRIMSON_BROWN_CLASSIC_BED, 1);
        registerFurniture("crimson_gray_classic_bed", CRIMSON_GRAY_CLASSIC_BED, 1);
        registerFurniture("crimson_black_classic_bed", CRIMSON_BLACK_CLASSIC_BED, 1);
        registerFurniture("crimson_white_classic_bed", CRIMSON_WHITE_CLASSIC_BED, 1);

        registerFurniture("oak_simple_bunk_ladder", OAK_SIMPLE_BUNK_LADDER, true);
        registerFurniture("birch_simple_bunk_ladder", BIRCH_SIMPLE_BUNK_LADDER, true);
        registerFurniture("spruce_simple_bunk_ladder", SPRUCE_SIMPLE_BUNK_LADDER, true);
        registerFurniture("acacia_simple_bunk_ladder", ACACIA_SIMPLE_BUNK_LADDER, true);
        registerFurniture("jungle_simple_bunk_ladder", JUNGLE_SIMPLE_BUNK_LADDER, true);
        registerFurniture("dark_oak_simple_bunk_ladder", DARK_OAK_SIMPLE_BUNK_LADDER, true);
        registerFurniture("crimson_simple_bunk_ladder", CRIMSON_SIMPLE_BUNK_LADDER, true);
        registerFurniture("warped_simple_bunk_ladder", WARPED_SIMPLE_BUNK_LADDER, true);

        registerFurniture("oak_log_stool", OAK_LOG_STOOL, true);
        registerFurniture("birch_log_stool", BIRCH_LOG_STOOL, true);
        registerFurniture("spruce_log_stool", SPRUCE_LOG_STOOL, true);
        registerFurniture("acacia_log_stool", ACACIA_LOG_STOOL, true);
        registerFurniture("jungle_log_stool", JUNGLE_LOG_STOOL, true);
        registerFurniture("dark_oak_log_stool", DARK_OAK_LOG_STOOL, true);
        registerFurniture("crimson_stem_stool", CRIMSON_STEM_STOOL, true);
        registerFurniture("warped_stem_stool", WARPED_STEM_STOOL, true);

        registerFurniture("oak_simple_stool", OAK_SIMPLE_STOOL, true);
        registerFurniture("birch_simple_stool", BIRCH_SIMPLE_STOOL, true);
        registerFurniture("spruce_simple_stool", SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("acacia_simple_stool", ACACIA_SIMPLE_STOOL, true);
        registerFurniture("jungle_simple_stool", JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("dark_oak_simple_stool", DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("crimson_simple_stool", CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("warped_simple_stool", WARPED_SIMPLE_STOOL, true);
        registerFurniture("stripped_oak_simple_stool", STRIPPED_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_birch_simple_stool", STRIPPED_BIRCH_SIMPLE_STOOL, true);
        registerFurniture("stripped_spruce_simple_stool", STRIPPED_SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("stripped_acacia_simple_stool", STRIPPED_ACACIA_SIMPLE_STOOL, true);
        registerFurniture("stripped_jungle_simple_stool", STRIPPED_JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("stripped_dark_oak_simple_stool", STRIPPED_DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_crimson_simple_stool", STRIPPED_CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("stripped_warped_simple_stool", STRIPPED_WARPED_SIMPLE_STOOL, true);
        registerFurniture("quartz_simple_stool", QUARTZ_SIMPLE_STOOL, true);
        registerFurniture("netherite_simple_stool", NETHERITE_SIMPLE_STOOL, true);
        registerFurniture("light_wood_simple_stool", LIGHT_WOOD_SIMPLE_STOOL, true);
        registerFurniture("dark_wood_simple_stool", DARK_WOOD_SIMPLE_STOOL, true);
        registerFurniture("granite_simple_stool", GRANITE_SIMPLE_STOOL, true);
        registerFurniture("calcite_simple_stool", CALCITE_SIMPLE_STOOL, true);
        registerFurniture("andesite_simple_stool", ANDESITE_SIMPLE_STOOL, true);
        registerFurniture("diorite_simple_stool", DIORITE_SIMPLE_STOOL, true);
        registerFurniture("stone_simple_stool", STONE_SIMPLE_STOOL, true);
        registerFurniture("blackstone_simple_stool", BLACKSTONE_SIMPLE_STOOL, true);
        registerFurniture("deepslate_simple_stool", DEEPSLATE_SIMPLE_STOOL, true);

        registerFurniture("oak_classic_stool", OAK_CLASSIC_STOOL, true);
        registerFurniture("birch_classic_stool", BIRCH_CLASSIC_STOOL, true);
        registerFurniture("spruce_classic_stool", SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("acacia_classic_stool", ACACIA_CLASSIC_STOOL, true);
        registerFurniture("jungle_classic_stool", JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("dark_oak_classic_stool", DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("crimson_classic_stool", CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("warped_classic_stool", WARPED_CLASSIC_STOOL, true);
        registerFurniture("stripped_oak_classic_stool", STRIPPED_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_birch_classic_stool", STRIPPED_BIRCH_CLASSIC_STOOL, true);
        registerFurniture("stripped_spruce_classic_stool", STRIPPED_SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("stripped_acacia_classic_stool", STRIPPED_ACACIA_CLASSIC_STOOL, true);
        registerFurniture("stripped_jungle_classic_stool", STRIPPED_JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("stripped_dark_oak_classic_stool", STRIPPED_DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_crimson_classic_stool", STRIPPED_CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("stripped_warped_classic_stool", STRIPPED_WARPED_CLASSIC_STOOL, true);
        registerFurniture("quartz_classic_stool", QUARTZ_CLASSIC_STOOL, true);
        registerFurniture("netherite_classic_stool", NETHERITE_CLASSIC_STOOL, true);
        registerFurniture("light_wood_classic_stool", LIGHT_WOOD_CLASSIC_STOOL, true);
        registerFurniture("dark_wood_classic_stool", DARK_WOOD_CLASSIC_STOOL, true);
        registerFurniture("granite_classic_stool", GRANITE_CLASSIC_STOOL, true);
        registerFurniture("calcite_classic_stool", CALCITE_CLASSIC_STOOL, true);
        registerFurniture("andesite_classic_stool", ANDESITE_CLASSIC_STOOL, true);
        registerFurniture("diorite_classic_stool", DIORITE_CLASSIC_STOOL, true);
        registerFurniture("stone_classic_stool", STONE_CLASSIC_STOOL, true);
        registerFurniture("blackstone_classic_stool", BLACKSTONE_CLASSIC_STOOL, true);
        registerFurniture("deepslate_classic_stool", DEEPSLATE_CLASSIC_STOOL, true);

        registerFurniture("oak_modern_stool", OAK_MODERN_STOOL, true);
        registerFurniture("birch_modern_stool", BIRCH_MODERN_STOOL, true);
        registerFurniture("spruce_modern_stool", SPRUCE_MODERN_STOOL, true);
        registerFurniture("acacia_modern_stool", ACACIA_MODERN_STOOL, true);
        registerFurniture("jungle_modern_stool", JUNGLE_MODERN_STOOL, true);
        registerFurniture("dark_oak_modern_stool", DARK_OAK_MODERN_STOOL, true);
        registerFurniture("crimson_modern_stool", CRIMSON_MODERN_STOOL, true);
        registerFurniture("warped_modern_stool", WARPED_MODERN_STOOL, true);
        registerFurniture("stripped_oak_modern_stool", STRIPPED_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_birch_modern_stool", STRIPPED_BIRCH_MODERN_STOOL, true);
        registerFurniture("stripped_spruce_modern_stool", STRIPPED_SPRUCE_MODERN_STOOL, true);
        registerFurniture("stripped_acacia_modern_stool", STRIPPED_ACACIA_MODERN_STOOL, true);
        registerFurniture("stripped_jungle_modern_stool", STRIPPED_JUNGLE_MODERN_STOOL, true);
        registerFurniture("stripped_dark_oak_modern_stool", STRIPPED_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_crimson_modern_stool", STRIPPED_CRIMSON_MODERN_STOOL, true);
        registerFurniture("stripped_warped_modern_stool", STRIPPED_WARPED_MODERN_STOOL, true);
        registerFurniture("white_modern_stool", WHITE_MODERN_STOOL, true);
        registerFurniture("gray_modern_stool", GRAY_MODERN_STOOL, true);
        registerFurniture("dark_wood_modern_stool", DARK_WOOD_MODERN_STOOL, true);
        registerFurniture("gray_dark_oak_modern_stool", GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("light_gray_dark_oak_modern_stool", LIGHT_GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("light_wood_modern_stool", LIGHT_WOOD_MODERN_STOOL, true);
        registerFurniture("quartz_modern_stool", QUARTZ_MODERN_STOOL, true);
        registerFurniture("netherite_modern_stool", NETHERITE_MODERN_STOOL, true);
        registerFurniture("granite_modern_stool", GRANITE_MODERN_STOOL, true);
        registerFurniture("calcite_modern_stool", CALCITE_MODERN_STOOL, true);
        registerFurniture("andesite_modern_stool", ANDESITE_MODERN_STOOL, true);
        registerFurniture("diorite_modern_stool", DIORITE_MODERN_STOOL, true);
        registerFurniture("stone_modern_stool", STONE_MODERN_STOOL, true);
        registerFurniture("blackstone_modern_stool", BLACKSTONE_MODERN_STOOL, true);
        registerFurniture("deepslate_modern_stool", DEEPSLATE_MODERN_STOOL, true);

        //Counter time
        registerFurniture("oak_kitchen_counter", OAK_KITCHEN_COUNTER, true);
        registerFurniture("oak_kitchen_drawer", OAK_KITCHEN_DRAWER, true);
        registerFurniture("oak_kitchen_cabinet", OAK_KITCHEN_CABINET, true);
        registerFurniture("oak_kitchen_sink", OAK_KITCHEN_SINK, true);
        registerFurniture("oak_kitchen_counter_oven", OAK_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("spruce_kitchen_counter", SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("spruce_kitchen_drawer", SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("spruce_kitchen_cabinet", SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("spruce_kitchen_sink", SPRUCE_KITCHEN_SINK, true);
        registerFurniture("spruce_kitchen_counter_oven", SPRUCE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("birch_kitchen_counter", BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("birch_kitchen_drawer", BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("birch_kitchen_cabinet", BIRCH_KITCHEN_CABINET, true);
        registerFurniture("birch_kitchen_sink", BIRCH_KITCHEN_SINK, true);
        registerFurniture("birch_kitchen_counter_oven", BIRCH_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("acacia_kitchen_counter", ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("acacia_kitchen_drawer", ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("acacia_kitchen_cabinet", ACACIA_KITCHEN_CABINET, true);
        registerFurniture("acacia_kitchen_sink", ACACIA_KITCHEN_SINK, true);
        registerFurniture("acacia_kitchen_counter_oven", ACACIA_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("jungle_kitchen_counter", JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("jungle_kitchen_drawer", JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("jungle_kitchen_cabinet", JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("jungle_kitchen_sink", JUNGLE_KITCHEN_SINK, true);
        registerFurniture("jungle_kitchen_counter_oven", JUNGLE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("dark_oak_kitchen_counter", DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("dark_oak_kitchen_drawer", DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("dark_oak_kitchen_cabinet", DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("dark_oak_kitchen_sink", DARK_OAK_KITCHEN_SINK, true);
        registerFurniture("dark_oak_kitchen_counter_oven", DARK_OAK_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("crimson_kitchen_counter", CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("crimson_kitchen_drawer", CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("crimson_kitchen_cabinet", CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("crimson_kitchen_sink", CRIMSON_KITCHEN_SINK, true);
        registerFurniture("crimson_kitchen_counter_oven", CRIMSON_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("warped_kitchen_counter", WARPED_KITCHEN_COUNTER, true);
        registerFurniture("warped_kitchen_drawer", WARPED_KITCHEN_DRAWER, true);
        registerFurniture("warped_kitchen_cabinet", WARPED_KITCHEN_CABINET, true);
        registerFurniture("warped_kitchen_sink", WARPED_KITCHEN_SINK, true);
        registerFurniture("warped_kitchen_counter_oven", WARPED_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_oak_kitchen_counter", STRIPPED_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_oak_kitchen_drawer", STRIPPED_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_oak_kitchen_cabinet", STRIPPED_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_oak_kitchen_sink", STRIPPED_OAK_KITCHEN_SINK, true);
        registerFurniture("stripped_oak_kitchen_counter_oven", STRIPPED_OAK_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_spruce_kitchen_counter", STRIPPED_SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_spruce_kitchen_drawer", STRIPPED_SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_spruce_kitchen_cabinet", STRIPPED_SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("stripped_spruce_kitchen_sink", STRIPPED_SPRUCE_KITCHEN_SINK, true);
        registerFurniture("stripped_spruce_kitchen_counter_oven", STRIPPED_SPRUCE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_birch_kitchen_counter", STRIPPED_BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("stripped_birch_kitchen_drawer", STRIPPED_BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("stripped_birch_kitchen_cabinet", STRIPPED_BIRCH_KITCHEN_CABINET, true);
        registerFurniture("stripped_birch_kitchen_sink", STRIPPED_BIRCH_KITCHEN_SINK, true);
        registerFurniture("stripped_birch_kitchen_counter_oven", STRIPPED_BIRCH_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_acacia_kitchen_counter", STRIPPED_ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("stripped_acacia_kitchen_drawer", STRIPPED_ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("stripped_acacia_kitchen_cabinet", STRIPPED_ACACIA_KITCHEN_CABINET, true);
        registerFurniture("stripped_acacia_kitchen_sink", STRIPPED_ACACIA_KITCHEN_SINK, true);
        registerFurniture("stripped_acacia_kitchen_counter_oven", STRIPPED_ACACIA_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_jungle_kitchen_counter", STRIPPED_JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_jungle_kitchen_drawer", STRIPPED_JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_jungle_kitchen_cabinet", STRIPPED_JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("stripped_jungle_kitchen_sink", STRIPPED_JUNGLE_KITCHEN_SINK, true);
        registerFurniture("stripped_jungle_kitchen_counter_oven", STRIPPED_JUNGLE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_dark_oak_kitchen_counter", STRIPPED_DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_dark_oak_kitchen_drawer", STRIPPED_DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_dark_oak_kitchen_cabinet", STRIPPED_DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_dark_oak_kitchen_sink", STRIPPED_DARK_OAK_KITCHEN_SINK, true);
        registerFurniture("stripped_dark_oak_kitchen_counter_oven", STRIPPED_DARK_OAK_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_crimson_kitchen_counter", STRIPPED_CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("stripped_crimson_kitchen_drawer", STRIPPED_CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("stripped_crimson_kitchen_cabinet", STRIPPED_CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("stripped_crimson_kitchen_sink", STRIPPED_CRIMSON_KITCHEN_SINK, true);
        registerFurniture("stripped_crimson_kitchen_counter_oven", STRIPPED_CRIMSON_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stripped_warped_kitchen_counter", STRIPPED_WARPED_KITCHEN_COUNTER, true);
        registerFurniture("stripped_warped_kitchen_drawer", STRIPPED_WARPED_KITCHEN_DRAWER, true);
        registerFurniture("stripped_warped_kitchen_cabinet", STRIPPED_WARPED_KITCHEN_CABINET, true);
        registerFurniture("stripped_warped_kitchen_sink", STRIPPED_WARPED_KITCHEN_SINK, true);
        registerFurniture("stripped_warped_kitchen_counter_oven", STRIPPED_WARPED_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("concrete_kitchen_counter", CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("concrete_kitchen_drawer", CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("concrete_kitchen_cabinet", CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("concrete_kitchen_sink", CONCRETE_KITCHEN_SINK, true);
        registerFurniture("concrete_kitchen_counter_oven", CONCRETE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("dark_concrete_kitchen_counter", DARK_CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("dark_concrete_kitchen_drawer", DARK_CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("dark_concrete_kitchen_cabinet", DARK_CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("dark_concrete_kitchen_sink", DARK_CONCRETE_KITCHEN_SINK, true);
        registerFurniture("dark_concrete_kitchen_counter_oven", DARK_CONCRETE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("light_wood_kitchen_counter", LIGHT_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("light_wood_kitchen_drawer", LIGHT_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("light_wood_kitchen_cabinet", LIGHT_WOOD_KITCHEN_CABINET, true);
        registerFurniture("light_wood_kitchen_sink", LIGHT_WOOD_KITCHEN_SINK, true);
        registerFurniture("light_wood_kitchen_counter_oven", LIGHT_WOOD_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("dark_wood_kitchen_counter", DARK_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("dark_wood_kitchen_drawer", DARK_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("dark_wood_kitchen_cabinet", DARK_WOOD_KITCHEN_CABINET, true);
        registerFurniture("dark_wood_kitchen_sink", DARK_WOOD_KITCHEN_SINK, true);
        registerFurniture("dark_wood_kitchen_counter_oven", DARK_WOOD_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("granite_kitchen_counter", GRANITE_KITCHEN_COUNTER, true);
        registerFurniture("granite_kitchen_drawer", GRANITE_KITCHEN_DRAWER, true);
        registerFurniture("granite_kitchen_cabinet", GRANITE_KITCHEN_CABINET, true);
        registerFurniture("granite_kitchen_sink", GRANITE_KITCHEN_SINK, true);
        registerFurniture("granite_kitchen_counter_oven", GRANITE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("calcite_kitchen_counter", CALCITE_KITCHEN_COUNTER, true);
        registerFurniture("calcite_kitchen_drawer", CALCITE_KITCHEN_DRAWER, true);
        registerFurniture("calcite_kitchen_cabinet", CALCITE_KITCHEN_CABINET, true);
        registerFurniture("calcite_kitchen_sink", CALCITE_KITCHEN_SINK, true);
        registerFurniture("calcite_kitchen_counter_oven", CALCITE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("netherite_kitchen_counter", NETHERITE_KITCHEN_COUNTER, true);
        registerFurniture("netherite_kitchen_drawer", NETHERITE_KITCHEN_DRAWER, true);
        registerFurniture("netherite_kitchen_cabinet", NETHERITE_KITCHEN_CABINET, true);
        registerFurniture("netherite_kitchen_sink", NETHERITE_KITCHEN_SINK, true);
        registerFurniture("netherite_kitchen_counter_oven", NETHERITE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("andesite_kitchen_counter", ANDESITE_KITCHEN_COUNTER, true);
        registerFurniture("andesite_kitchen_drawer", ANDESITE_KITCHEN_DRAWER, true);
        registerFurniture("andesite_kitchen_cabinet", ANDESITE_KITCHEN_CABINET, true);
        registerFurniture("andesite_kitchen_sink", ANDESITE_KITCHEN_SINK, true);
        registerFurniture("andesite_kitchen_counter_oven", ANDESITE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("diorite_kitchen_counter", DIORITE_KITCHEN_COUNTER, true);
        registerFurniture("diorite_kitchen_drawer", DIORITE_KITCHEN_DRAWER, true);
        registerFurniture("diorite_kitchen_cabinet", DIORITE_KITCHEN_CABINET, true);
        registerFurniture("diorite_kitchen_sink", DIORITE_KITCHEN_SINK, true);
        registerFurniture("diorite_kitchen_counter_oven", DIORITE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("smooth_stone_kitchen_counter", SMOOTH_STONE_KITCHEN_COUNTER, true);
        registerFurniture("smooth_stone_kitchen_drawer", SMOOTH_STONE_KITCHEN_DRAWER, true);
        registerFurniture("smooth_stone_kitchen_cabinet", SMOOTH_STONE_KITCHEN_CABINET, true);
        registerFurniture("smooth_stone_kitchen_sink", SMOOTH_STONE_KITCHEN_SINK, true);
        registerFurniture("smooth_stone_kitchen_counter_oven", SMOOTH_STONE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("stone_kitchen_counter", STONE_KITCHEN_COUNTER, true);
        registerFurniture("stone_kitchen_drawer", STONE_KITCHEN_DRAWER, true);
        registerFurniture("stone_kitchen_cabinet", STONE_KITCHEN_CABINET, true);
        registerFurniture("stone_kitchen_sink", STONE_KITCHEN_SINK, true);
        registerFurniture("stone_kitchen_counter_oven", STONE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("deepslate_tile_kitchen_counter", DEEPSLATE_TILE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_tile_kitchen_drawer", DEEPSLATE_TILE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_tile_kitchen_cabinet", DEEPSLATE_TILE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_tile_kitchen_sink", DEEPSLATE_TILE_KITCHEN_SINK, true);
        registerFurniture("deepslate_tile_kitchen_counter_oven", DEEPSLATE_TILE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("blackstone_kitchen_counter", BLACKSTONE_KITCHEN_COUNTER, true);
        registerFurniture("blackstone_kitchen_drawer", BLACKSTONE_KITCHEN_DRAWER, true);
        registerFurniture("blackstone_kitchen_cabinet", BLACKSTONE_KITCHEN_CABINET, true);
        registerFurniture("blackstone_kitchen_sink", BLACKSTONE_KITCHEN_SINK, true);
        registerFurniture("blackstone_kitchen_counter_oven", BLACKSTONE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("deepslate_kitchen_counter", DEEPSLATE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_kitchen_drawer", DEEPSLATE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_kitchen_cabinet", DEEPSLATE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_kitchen_sink", DEEPSLATE_KITCHEN_SINK, true);
        registerFurniture("deepslate_kitchen_counter_oven", DEEPSLATE_KITCHEN_COUNTER_OVEN, true);

        registerFurniture("white_fridge", WHITE_FRIDGE, true);
        registerFurniture("white_freezer", WHITE_FREEZER, false);
        registerFurniture("iron_fridge", IRON_FRIDGE, true);
        registerFurniture("iron_freezer", IRON_FREEZER, false);
        registerFurniture("xbox_fridge", XBOX_FRIDGE, true);
        registerFurniture("iron_microwave", IRON_MICROWAVE, true);
        registerFurniture("white_stove", WHITE_STOVE, true);
        registerFurniture("iron_stove", IRON_STOVE, true);
        registerFurniture("kitchen_stovetop", KITCHEN_STOVETOP, true);
        registerFurniture("basic_plate", BASIC_PLATE, true);
        registerFurniture("basic_cutlery", BASIC_CUTLERY, true);

        registerBlock("oak_herringbone_planks", OAK_HERRINGBONE_PLANKS, true);
        registerBlock("spruce_herringbone_planks", SPRUCE_HERRINGBONE_PLANKS, true);
        registerBlock("birch_herringbone_planks", BIRCH_HERRINGBONE_PLANKS, true);
        registerBlock("jungle_herringbone_planks", JUNGLE_HERRINGBONE_PLANKS, true);
        registerBlock("acacia_herringbone_planks", ACACIA_HERRINGBONE_PLANKS, true);
        registerBlock("dark_oak_herringbone_planks", DARK_OAK_HERRINGBONE_PLANKS, true);
        registerBlock("crimson_herringbone_planks", CRIMSON_HERRINGBONE_PLANKS, true);
        registerBlock("warped_herringbone_planks", WARPED_HERRINGBONE_PLANKS, true);

        registerBlock("raw_concrete", RAW_CONCRETE, true);
        registerBlock("raw_concrete_powder", RAW_CONCRETE_POWDER, true);
        registerBlock("iron_chain", IRON_CHAIN, true);
        registerBlock("leather_block", LEATHER_BLOCK, true);

        registerFurniture("gray_modern_pendant", GRAY_MODERN_PENDANT, true);
        registerFurniture("white_modern_pendant", WHITE_MODERN_PENDANT, true);
        registerFurniture("glass_modern_pendant", GLASS_MODERN_PENDANT, true);
        registerFurniture("simple_light", SIMPLE_LIGHT, true);
        registerBlock("light_switch", LIGHT_SWITCH, LIGHT_SWITCH_ITEM);


        //Dye Kits
        registerItem("dye_kit_red", DYE_KIT_RED);
        registerItem("dye_kit_orange", DYE_KIT_ORANGE);
        registerItem("dye_kit_yellow", DYE_KIT_YELLOW);
        registerItem("dye_kit_lime", DYE_KIT_LIME);
        registerItem("dye_kit_green", DYE_KIT_GREEN);
        registerItem("dye_kit_cyan", DYE_KIT_CYAN);
        registerItem("dye_kit_light_blue", DYE_KIT_LIGHT_BLUE);
        registerItem("dye_kit_blue", DYE_KIT_BLUE);
        registerItem("dye_kit_purple", DYE_KIT_PURPLE);
        registerItem("dye_kit_magenta", DYE_KIT_MAGENTA);
        registerItem("dye_kit_pink", DYE_KIT_PINK);
        registerItem("dye_kit_brown", DYE_KIT_BROWN);
        registerItem("dye_kit_black", DYE_KIT_BLACK);
        registerItem("dye_kit_gray", DYE_KIT_GRAY);
        registerItem("dye_kit_light_gray", DYE_KIT_LIGHT_GRAY);
        registerItem("dye_kit_white", DYE_KIT_WHITE);
    }

}
