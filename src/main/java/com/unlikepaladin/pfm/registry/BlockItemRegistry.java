package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.items.DyeKit;
import com.unlikepaladin.pfm.items.LightSwitchItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.ToIntFunction;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class BlockItemRegistry {
    public static final Block OAK_CHAIR = new BasicChair(FabricBlockSettings.of(Material.WOOD).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.WOOD));
    public static final Block BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.of(Material.NETHER_WOOD).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.WOOD));
    public static final Block WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR = new BasicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block QUARTZ_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(2.0f).resistance(2.0f).nonOpaque().requiresTool());
    public static final Block NETHERITE_CHAIR = new BasicChair(FabricBlockSettings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE));



    //Dinner Chairs
    public static final Block OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_DINNER = new DinnerChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    //Froggy Chairs
    public static final Block FROGGY_CHAIR = new FroggyChair(FabricBlockSettings.of(Material.METAL).strength(9.0f).resistance(8.0f).nonOpaque().requiresTool());
    public static final Block FROGGY_CHAIR_PINK = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_LIGHT_BLUE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_ORANGE = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));
    public static final Block FROGGY_CHAIR_YELLOW = new FroggyChair(FabricBlockSettings.copyOf(FROGGY_CHAIR));

    //Classic Chair
    public static final Block OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_CLASSIC = new ClassicChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block CHAIR_CLASSIC_WOOL = new ClassicChairDyeable(FabricBlockSettings.copyOf(OAK_CHAIR));

    //Modern Chair
    public static final Block OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_MODERN = new ModernChair(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    //Arm Chairs
    public static final Block ARM_CHAIR_STANDARD = new ArmChairDyeable(FabricBlockSettings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block ARM_CHAIR_LEATHER = new ArmChair(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));




    //tables
    public static final Block OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_BASIC_TABLE = new BasicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    public static final Block OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_TABLE = new ClassicTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));


    public static final Block OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_RAW_LOG_TABLE = new LogTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_RAW_STEM_TABLE = new LogTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_LOG_STOOL = new LogStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_STEM_STOOL = new LogStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_SIMPLE_STOOL = new SimpleStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WHITE_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));
    public static final Block GRAY_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));
    public static final Block WHITE_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));
    public static final Block GRAY_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));
    public static final Block LIGHT_GRAY_DARK_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_CONCRETE));
    public static final Block WHITE_OAK_MODERN_STOOL = new ModernStool(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));

    public static final Block OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_STOOL = new ClassicStool(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_DINNER_TABLE = new DinnerTable(FabricBlockSettings.copyOf(CRIMSON_CHAIR));

    public static final Block OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block BIRCH_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block BIRCH_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block SPRUCE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block SPRUCE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block JUNGLE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block JUNGLE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block ACACIA_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block ACACIA_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DARK_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block MANGROVE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block MANGROVE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block CRIMSON_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block CRIMSON_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block CRIMSON_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block CRIMSON_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block WARPED_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block WARPED_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block WARPED_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block WARPED_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_BIRCH_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_SPRUCE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_SPRUCE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_JUNGLE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_JUNGLE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_ACACIA_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_ACACIA_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_DARK_OAK_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_MANGROVE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block STRIPPED_MANGROVE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_CRIMSON_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STRIPPED_WARPED_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_WARPED_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_WARPED_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(WARPED_CHAIR));
    public static final Block STRIPPED_WARPED_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(WARPED_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);





    public static final Block WHITE_FREEZER = new Freezer(FabricBlockSettings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE),() -> BlockItemRegistry.WHITE_FRIDGE);
    public static final Block WHITE_FRIDGE = new Fridge(FabricBlockSettings.copyOf(WHITE_FREEZER).nonOpaque(), () -> BlockItemRegistry.WHITE_FREEZER);
    public static final Block IRON_FREEZER = new IronFreezer(FabricBlockSettings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.METAL),() -> BlockItemRegistry.IRON_FRIDGE);
    public static final Block IRON_FRIDGE = new IronFridge(FabricBlockSettings.copyOf(IRON_FREEZER).nonOpaque(), () -> BlockItemRegistry.IRON_FREEZER);

    public static final Block XBOX_FRIDGE = new XboxFridge(FabricBlockSettings.copyOf(WHITE_FREEZER).nonOpaque(), null);

    public static final Block SIMPLE_STOVE = new Stove(FabricBlockSettings.copyOf(WHITE_FREEZER));
    public static final Block IRON_STOVE = new IronStove(FabricBlockSettings.copyOf(WHITE_FREEZER));
    public static final Block MICROWAVE = new Microwave(FabricBlockSettings.copyOf(WHITE_FREEZER));

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

    public static final Block ACACIA_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.ACACIA_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block SPRUCE_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_OAK_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.DARK_OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block JUNGLE_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.JUNGLE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block BIRCH_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block MANGROVE_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.MANGROVE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block WARPED_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block CRIMSON_HERRINGBONE_PLANKS = new Block(FabricBlockSettings.copyOf(Blocks.CRIMSON_PLANKS).sounds(BlockSoundGroup.WOOD));


    public static final Block RAW_CONCRETE = new Block(FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE).sounds(BlockSoundGroup.STONE));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE_POWDER).sounds(BlockSoundGroup.SAND));
    public static final Block LEATHER_BLOCK = new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL));

    public static final Block IRON_CHAIN = new ChainBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.METAL));
    public static final Block GRAY_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)));
    public static final Block WHITE_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)));
    public static final Block GLASS_MODERN_PENDANT = new PendantBlock(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)));
    public static final Block SIMPLE_LIGHT = new SimpleLight(FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)));

    public static final Block LIGHT_SWITCH = new LightSwitch(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).sounds(BlockSoundGroup.STONE).nonOpaque());
    public static final BlockItem LIGHT_SWITCH_ITEM = new LightSwitchItem(LIGHT_SWITCH, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP));
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT)? litLevel : 0;
    }
    public static final Block CONCRETE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block CONCRETE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block CONCRETE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block CONCRETE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(RAW_CONCRETE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DARK_CONCRETE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block DARK_CONCRETE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block DARK_CONCRETE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(RAW_CONCRETE));
    public static final Block DARK_CONCRETE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block LIGHT_WOOD_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block LIGHT_WOOD_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block LIGHT_WOOD_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block LIGHT_WOOD_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DARK_WOOD_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_WOOD_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_WOOD_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(OAK_CHAIR));
    public static final Block DARK_WOOD_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(OAK_CHAIR),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block GRANITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block CALCITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.CALCITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block NETHERITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block ANDESITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DIORITE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block STONE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block STONE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block STONE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE));
    public static final Block STONE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DEEPSLATE_TILE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_TILES),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block BLACKSTONE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block DEEPSLATE_KITCHEN_COUNTER = new KitchenCounter(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_DRAWER = new KitchenDrawer(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_CABINET = new KitchenCabinet(FabricBlockSettings.copyOf(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_SINK = new KitchenSink(FabricBlockSettings.copyOf(Blocks.DEEPSLATE),  LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static final Block WORKING_TABLE = new WorkingTable(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE).sounds(BlockSoundGroup.WOOD));

    public static void registerFurniture(String blockName, Block block, Boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
        }
    }

    public static void registerBlock(String blockName, Block block, Boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        }
    }
    public static void registerBlock(String blockName, Block block, Boolean registerItem, BlockItem item) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), item);
        }
    }
    public static void registerItem(String itemName, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, itemName), item);
    }

    public static void register(){
        //Block Registry
        registerFurniture("working_table", WORKING_TABLE, true);

        //Basic Chairs
        registerFurniture("oak_chair", OAK_CHAIR, true);
        registerFurniture("birch_chair", BIRCH_CHAIR, true);
        registerFurniture("spruce_chair", SPRUCE_CHAIR, true);
        registerFurniture("acacia_chair", ACACIA_CHAIR, true);
        registerFurniture("jungle_chair", JUNGLE_CHAIR, true);
        registerFurniture("dark_oak_chair", DARK_OAK_CHAIR, true);
        registerFurniture("mangrove_chair", MANGROVE_CHAIR, true);
        registerFurniture("warped_chair", WARPED_CHAIR, true);
        registerFurniture("crimson_chair", CRIMSON_CHAIR, true);
        registerFurniture("stripped_oak_chair", STRIPPED_OAK_CHAIR, true);
        registerFurniture("stripped_birch_chair", STRIPPED_BIRCH_CHAIR, true);
        registerFurniture("stripped_spruce_chair", STRIPPED_SPRUCE_CHAIR, true);
        registerFurniture("stripped_acacia_chair", STRIPPED_ACACIA_CHAIR, true);
        registerFurniture("stripped_jungle_chair", STRIPPED_JUNGLE_CHAIR, true);
        registerFurniture("stripped_dark_oak_chair", STRIPPED_DARK_OAK_CHAIR, true);
        registerFurniture("stripped_mangrove_chair", STRIPPED_MANGROVE_CHAIR, true);
        registerFurniture("stripped_warped_chair", STRIPPED_WARPED_CHAIR, true);
        registerFurniture("stripped_crimson_chair", STRIPPED_CRIMSON_CHAIR, true);
        registerFurniture("quartz_chair", QUARTZ_CHAIR, true);
        registerFurniture("netherite_chair", NETHERITE_CHAIR, true);



        //Dinner Chairs
        registerFurniture("oak_chair_dinner", OAK_CHAIR_DINNER, true);
        registerFurniture("birch_chair_dinner", BIRCH_CHAIR_DINNER, true);
        registerFurniture("spruce_chair_dinner", SPRUCE_CHAIR_DINNER, true);
        registerFurniture("acacia_chair_dinner", ACACIA_CHAIR_DINNER, true);
        registerFurniture("jungle_chair_dinner", JUNGLE_CHAIR_DINNER, true);
        registerFurniture("dark_oak_chair_dinner", DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("mangrove_chair_dinner", MANGROVE_CHAIR_DINNER, true);
        registerFurniture("warped_chair_dinner", WARPED_CHAIR_DINNER, true);
        registerFurniture("crimson_chair_dinner", CRIMSON_CHAIR_DINNER, true);
        registerFurniture("stripped_oak_chair_dinner", STRIPPED_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_birch_chair_dinner", STRIPPED_BIRCH_CHAIR_DINNER, true);
        registerFurniture("stripped_spruce_chair_dinner", STRIPPED_SPRUCE_CHAIR_DINNER, true);
        registerFurniture("stripped_acacia_chair_dinner", STRIPPED_ACACIA_CHAIR_DINNER, true);
        registerFurniture("stripped_jungle_chair_dinner", STRIPPED_JUNGLE_CHAIR_DINNER, true);
        registerFurniture("stripped_mangrove_chair_dinner", STRIPPED_MANGROVE_CHAIR_DINNER, true);
        registerFurniture("stripped_dark_oak_chair_dinner", STRIPPED_DARK_OAK_CHAIR_DINNER, true);
        registerFurniture("stripped_warped_chair_dinner", STRIPPED_WARPED_CHAIR_DINNER, true);
        registerFurniture("stripped_crimson_chair_dinner", STRIPPED_CRIMSON_CHAIR_DINNER, true);


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
        registerFurniture("mangrove_chair_classic", MANGROVE_CHAIR_CLASSIC, true);
        registerFurniture("warped_chair_classic", WARPED_CHAIR_CLASSIC, true);
        registerFurniture("crimson_chair_classic", CRIMSON_CHAIR_CLASSIC, true);
        registerFurniture("stripped_oak_chair_classic", STRIPPED_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_birch_chair_classic", STRIPPED_BIRCH_CHAIR_CLASSIC, true);
        registerFurniture("stripped_spruce_chair_classic", STRIPPED_SPRUCE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_acacia_chair_classic", STRIPPED_ACACIA_CHAIR_CLASSIC, true);
        registerFurniture("stripped_jungle_chair_classic", STRIPPED_JUNGLE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_dark_oak_chair_classic", STRIPPED_DARK_OAK_CHAIR_CLASSIC, true);
        registerFurniture("stripped_mangrove_chair_classic", STRIPPED_MANGROVE_CHAIR_CLASSIC, true);
        registerFurniture("stripped_warped_chair_classic", STRIPPED_WARPED_CHAIR_CLASSIC, true);
        registerFurniture("stripped_crimson_chair_classic", STRIPPED_CRIMSON_CHAIR_CLASSIC, true);

        //Modern Chair
        registerFurniture("oak_chair_modern", OAK_CHAIR_MODERN, true);
        registerFurniture("birch_chair_modern", BIRCH_CHAIR_MODERN, true);
        registerFurniture("spruce_chair_modern", SPRUCE_CHAIR_MODERN, true);
        registerFurniture("acacia_chair_modern", ACACIA_CHAIR_MODERN, true);
        registerFurniture("jungle_chair_modern", JUNGLE_CHAIR_MODERN, true);
        registerFurniture("dark_oak_chair_modern",DARK_OAK_CHAIR_MODERN , true);
        registerFurniture("mangrove_chair_modern", MANGROVE_CHAIR_MODERN, true);
        registerFurniture("warped_chair_modern", WARPED_CHAIR_MODERN, true);
        registerFurniture("crimson_chair_modern", CRIMSON_CHAIR_MODERN, true);
        registerFurniture("stripped_oak_chair_modern", STRIPPED_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_birch_chair_modern", STRIPPED_BIRCH_CHAIR_MODERN, true);
        registerFurniture("stripped_spruce_chair_modern", STRIPPED_SPRUCE_CHAIR_MODERN, true);
        registerFurniture("stripped_acacia_chair_modern", STRIPPED_ACACIA_CHAIR_MODERN, true);
        registerFurniture("stripped_jungle_chair_modern", STRIPPED_JUNGLE_CHAIR_MODERN, true);
        registerFurniture("stripped_dark_oak_chair_modern",STRIPPED_DARK_OAK_CHAIR_MODERN, true);
        registerFurniture("stripped_mangrove_chair_modern", STRIPPED_MANGROVE_CHAIR_MODERN, true);
        registerFurniture("stripped_warped_chair_modern", STRIPPED_WARPED_CHAIR_MODERN, true);
        registerFurniture("stripped_crimson_chair_modern", STRIPPED_CRIMSON_CHAIR_MODERN, true);

        //Armchairs
        registerFurniture("arm_chair_standard", ARM_CHAIR_STANDARD, true);
        registerFurniture("arm_chair_leather", ARM_CHAIR_LEATHER, true);

        //Tables
        registerFurniture("oak_table_basic", OAK_BASIC_TABLE, true);
        registerFurniture("birch_table_basic", BIRCH_BASIC_TABLE, true);
        registerFurniture("spruce_table_basic", SPRUCE_BASIC_TABLE, true);
        registerFurniture("acacia_table_basic", ACACIA_BASIC_TABLE, true);
        registerFurniture("jungle_table_basic", JUNGLE_BASIC_TABLE, true);
        registerFurniture("dark_oak_table_basic",DARK_OAK_BASIC_TABLE, true);
        registerFurniture("mangrove_table_basic", MANGROVE_BASIC_TABLE, true);
        registerFurniture("crimson_table_basic", CRIMSON_BASIC_TABLE, true);
        registerFurniture("warped_table_basic", WARPED_BASIC_TABLE, true);
        registerFurniture("stripped_oak_table_basic", STRIPPED_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_birch_table_basic", STRIPPED_BIRCH_BASIC_TABLE, true);
        registerFurniture("stripped_spruce_table_basic", STRIPPED_SPRUCE_BASIC_TABLE, true);
        registerFurniture("stripped_acacia_table_basic", STRIPPED_ACACIA_BASIC_TABLE, true);
        registerFurniture("stripped_jungle_table_basic", STRIPPED_JUNGLE_BASIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_basic", STRIPPED_DARK_OAK_BASIC_TABLE, true);
        registerFurniture("stripped_mangrove_table_basic", STRIPPED_MANGROVE_BASIC_TABLE, true);
        registerFurniture("stripped_crimson_table_basic", STRIPPED_CRIMSON_BASIC_TABLE, true);
        registerFurniture("stripped_warped_table_basic", STRIPPED_WARPED_BASIC_TABLE, true);

        //Classic Table
        registerFurniture("oak_table_classic", OAK_CLASSIC_TABLE, true);
        registerFurniture("birch_table_classic", BIRCH_CLASSIC_TABLE, true);
        registerFurniture("spruce_table_classic", SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("acacia_table_classic", ACACIA_CLASSIC_TABLE, true);
        registerFurniture("jungle_table_classic", JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("dark_oak_table_classic", DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("mangrove_table_classic", MANGROVE_CLASSIC_TABLE, true);
        registerFurniture("crimson_table_classic", CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("warped_table_classic", WARPED_CLASSIC_TABLE, true);
        registerFurniture("stripped_oak_table_classic", STRIPPED_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_birch_table_classic", STRIPPED_BIRCH_CLASSIC_TABLE, true);
        registerFurniture("stripped_spruce_table_classic", STRIPPED_SPRUCE_CLASSIC_TABLE, true);
        registerFurniture("stripped_acacia_table_classic", STRIPPED_ACACIA_CLASSIC_TABLE, true);
        registerFurniture("stripped_jungle_table_classic", STRIPPED_JUNGLE_CLASSIC_TABLE, true);
        registerFurniture("stripped_dark_oak_table_classic", STRIPPED_DARK_OAK_CLASSIC_TABLE, true);
        registerFurniture("stripped_mangrove_table_classic", STRIPPED_MANGROVE_CLASSIC_TABLE, true);
        registerFurniture("stripped_crimson_table_classic", STRIPPED_CRIMSON_CLASSIC_TABLE, true);
        registerFurniture("stripped_warped_table_classic", STRIPPED_WARPED_CLASSIC_TABLE, true);

        //Log Table
        registerFurniture("oak_table_log", OAK_LOG_TABLE, true);
        registerFurniture("birch_table_log", BIRCH_LOG_TABLE, true);
        registerFurniture("spruce_table_log", SPRUCE_LOG_TABLE, true);
        registerFurniture("acacia_table_log", ACACIA_LOG_TABLE, true);
        registerFurniture("jungle_table_log", JUNGLE_LOG_TABLE, true);
        registerFurniture("dark_oak_table_log", DARK_OAK_LOG_TABLE, true);
        registerFurniture("mangrove_table_log", MANGROVE_LOG_TABLE, true);
        registerFurniture("crimson_table_log", CRIMSON_LOG_TABLE, true);
        registerFurniture("warped_table_log", WARPED_LOG_TABLE, true);
        registerFurniture("stripped_oak_table_log", STRIPPED_OAK_LOG_TABLE, true);
        registerFurniture("stripped_birch_table_log", STRIPPED_BIRCH_LOG_TABLE, true);
        registerFurniture("stripped_spruce_table_log", STRIPPED_SPRUCE_LOG_TABLE, true);
        registerFurniture("stripped_acacia_table_log", STRIPPED_ACACIA_LOG_TABLE, true);
        registerFurniture("stripped_jungle_table_log", STRIPPED_JUNGLE_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_table_log", STRIPPED_DARK_OAK_LOG_TABLE, true);
        registerFurniture("stripped_mangrove_table_log", STRIPPED_MANGROVE_LOG_TABLE, true);
        registerFurniture("stripped_crimson_table_log", STRIPPED_CRIMSON_LOG_TABLE, true);
        registerFurniture("stripped_warped_table_log", STRIPPED_WARPED_LOG_TABLE, true);
        //Raw Log Table
        registerFurniture("oak_raw_table_log", OAK_RAW_LOG_TABLE, true);
        registerFurniture("birch_raw_table_log", BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("acacia_raw_table_log", ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("spruce_raw_table_log", SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("jungle_raw_table_log", JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("dark_oak_raw_table_log", DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("mangrove_raw_table_log", MANGROVE_RAW_LOG_TABLE, true);
        registerFurniture("warped_raw_table_stem", WARPED_RAW_STEM_TABLE, true);
        registerFurniture("crimson_raw_table_stem", CRIMSON_RAW_STEM_TABLE, true);
        registerFurniture("stripped_oak_raw_table_log", STRIPPED_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_birch_raw_table_log", STRIPPED_BIRCH_RAW_LOG_TABLE, true);
        registerFurniture("stripped_acacia_raw_table_log", STRIPPED_ACACIA_RAW_LOG_TABLE, true);
        registerFurniture("stripped_spruce_raw_table_log", STRIPPED_SPRUCE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_jungle_raw_table_log", STRIPPED_JUNGLE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_dark_oak_raw_table_log", STRIPPED_DARK_OAK_RAW_LOG_TABLE, true);
        registerFurniture("stripped_mangrove_raw_table_log", STRIPPED_MANGROVE_RAW_LOG_TABLE, true);
        registerFurniture("stripped_warped_raw_table_stem", STRIPPED_WARPED_RAW_STEM_TABLE, true);
        registerFurniture("stripped_crimson_raw_table_stem", STRIPPED_CRIMSON_RAW_STEM_TABLE, true);

        registerFurniture("oak_table_dinner", OAK_DINNER_TABLE, true);
        registerFurniture("birch_table_dinner", BIRCH_DINNER_TABLE, true);
        registerFurniture("spruce_table_dinner", SPRUCE_DINNER_TABLE, true);
        registerFurniture("acacia_table_dinner", ACACIA_DINNER_TABLE, true);
        registerFurniture("jungle_table_dinner", JUNGLE_DINNER_TABLE, true);
        registerFurniture("dark_oak_table_dinner", DARK_OAK_DINNER_TABLE, true);
        registerFurniture("mangrove_table_dinner", MANGROVE_DINNER_TABLE, true);
        registerFurniture("crimson_table_dinner", CRIMSON_DINNER_TABLE, true);
        registerFurniture("warped_table_dinner", WARPED_DINNER_TABLE, true);
        registerFurniture("stripped_oak_table_dinner", STRIPPED_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_birch_table_dinner", STRIPPED_BIRCH_DINNER_TABLE, true);
        registerFurniture("stripped_spruce_table_dinner", STRIPPED_SPRUCE_DINNER_TABLE, true);
        registerFurniture("stripped_acacia_table_dinner", STRIPPED_ACACIA_DINNER_TABLE, true);
        registerFurniture("stripped_jungle_table_dinner", STRIPPED_JUNGLE_DINNER_TABLE, true);
        registerFurniture("stripped_dark_oak_table_dinner", STRIPPED_DARK_OAK_DINNER_TABLE, true);
        registerFurniture("stripped_mangrove_table_dinner", STRIPPED_MANGROVE_DINNER_TABLE, true);
        registerFurniture("stripped_crimson_table_dinner", STRIPPED_CRIMSON_DINNER_TABLE, true);
        registerFurniture("stripped_warped_table_dinner", STRIPPED_WARPED_DINNER_TABLE, true);

        registerFurniture("oak_log_stool", OAK_LOG_STOOL, true);
        registerFurniture("birch_log_stool", BIRCH_LOG_STOOL, true);
        registerFurniture("spruce_log_stool", SPRUCE_LOG_STOOL, true);
        registerFurniture("acacia_log_stool", ACACIA_LOG_STOOL, true);
        registerFurniture("jungle_log_stool", JUNGLE_LOG_STOOL, true);
        registerFurniture("dark_oak_log_stool", DARK_OAK_LOG_STOOL, true);
        registerFurniture("mangrove_log_stool", MANGROVE_LOG_STOOL, true);
        registerFurniture("crimson_stem_stool", CRIMSON_STEM_STOOL, true);
        registerFurniture("warped_stem_stool", WARPED_STEM_STOOL, true);

        registerFurniture("oak_simple_stool", OAK_SIMPLE_STOOL, true);
        registerFurniture("birch_simple_stool", BIRCH_SIMPLE_STOOL, true);
        registerFurniture("spruce_simple_stool", SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("acacia_simple_stool", ACACIA_SIMPLE_STOOL, true);
        registerFurniture("jungle_simple_stool", JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("dark_oak_simple_stool", DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("mangrove_simple_stool", MANGROVE_SIMPLE_STOOL, true);
        registerFurniture("crimson_simple_stool", CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("warped_simple_stool", WARPED_SIMPLE_STOOL, true);
        registerFurniture("stripped_oak_simple_stool", STRIPPED_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_birch_simple_stool", STRIPPED_BIRCH_SIMPLE_STOOL, true);
        registerFurniture("stripped_spruce_simple_stool", STRIPPED_SPRUCE_SIMPLE_STOOL, true);
        registerFurniture("stripped_acacia_simple_stool", STRIPPED_ACACIA_SIMPLE_STOOL, true);
        registerFurniture("stripped_jungle_simple_stool", STRIPPED_JUNGLE_SIMPLE_STOOL, true);
        registerFurniture("stripped_dark_oak_simple_stool", STRIPPED_DARK_OAK_SIMPLE_STOOL, true);
        registerFurniture("stripped_mangrove_simple_stool", STRIPPED_MANGROVE_SIMPLE_STOOL, true);
        registerFurniture("stripped_crimson_simple_stool", STRIPPED_CRIMSON_SIMPLE_STOOL, true);
        registerFurniture("stripped_warped_simple_stool", STRIPPED_WARPED_SIMPLE_STOOL, true);

        registerFurniture("oak_classic_stool", OAK_CLASSIC_STOOL, true);
        registerFurniture("birch_classic_stool", BIRCH_CLASSIC_STOOL, true);
        registerFurniture("spruce_classic_stool", SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("acacia_classic_stool", ACACIA_CLASSIC_STOOL, true);
        registerFurniture("jungle_classic_stool", JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("dark_oak_classic_stool", DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("mangrove_classic_stool", MANGROVE_CLASSIC_STOOL, true);
        registerFurniture("crimson_classic_stool", CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("warped_classic_stool", WARPED_CLASSIC_STOOL, true);
        registerFurniture("stripped_oak_classic_stool", STRIPPED_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_birch_classic_stool", STRIPPED_BIRCH_CLASSIC_STOOL, true);
        registerFurniture("stripped_spruce_classic_stool", STRIPPED_SPRUCE_CLASSIC_STOOL, true);
        registerFurniture("stripped_acacia_classic_stool", STRIPPED_ACACIA_CLASSIC_STOOL, true);
        registerFurniture("stripped_jungle_classic_stool", STRIPPED_JUNGLE_CLASSIC_STOOL, true);
        registerFurniture("stripped_dark_oak_classic_stool", STRIPPED_DARK_OAK_CLASSIC_STOOL, true);
        registerFurniture("stripped_mangrove_classic_stool", STRIPPED_MANGROVE_CLASSIC_STOOL, true);
        registerFurniture("stripped_crimson_classic_stool", STRIPPED_CRIMSON_CLASSIC_STOOL, true);
        registerFurniture("stripped_warped_classic_stool", STRIPPED_WARPED_CLASSIC_STOOL, true);

        registerFurniture("oak_modern_stool", OAK_MODERN_STOOL, true);
        registerFurniture("birch_modern_stool", BIRCH_MODERN_STOOL, true);
        registerFurniture("spruce_modern_stool", SPRUCE_MODERN_STOOL, true);
        registerFurniture("acacia_modern_stool", ACACIA_MODERN_STOOL, true);
        registerFurniture("jungle_modern_stool", JUNGLE_MODERN_STOOL, true);
        registerFurniture("dark_oak_modern_stool", DARK_OAK_MODERN_STOOL, true);
        registerFurniture("mangrove_modern_stool", MANGROVE_MODERN_STOOL, true);
        registerFurniture("crimson_modern_stool", CRIMSON_MODERN_STOOL, true);
        registerFurniture("warped_modern_stool", WARPED_MODERN_STOOL, true);
        registerFurniture("stripped_oak_modern_stool", STRIPPED_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_birch_modern_stool", STRIPPED_BIRCH_MODERN_STOOL, true);
        registerFurniture("stripped_spruce_modern_stool", STRIPPED_SPRUCE_MODERN_STOOL, true);
        registerFurniture("stripped_acacia_modern_stool", STRIPPED_ACACIA_MODERN_STOOL, true);
        registerFurniture("stripped_jungle_modern_stool", STRIPPED_JUNGLE_MODERN_STOOL, true);
        registerFurniture("stripped_dark_oak_modern_stool", STRIPPED_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("stripped_mangrove_modern_stool", STRIPPED_MANGROVE_MODERN_STOOL, true);
        registerFurniture("stripped_crimson_modern_stool", STRIPPED_CRIMSON_MODERN_STOOL, true);
        registerFurniture("stripped_warped_modern_stool", STRIPPED_WARPED_MODERN_STOOL, true);
        registerFurniture("white_modern_stool", WHITE_MODERN_STOOL, true);
        registerFurniture("gray_modern_stool", GRAY_MODERN_STOOL, true);
        registerFurniture("white_dark_oak_modern_stool", WHITE_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("gray_dark_oak_modern_stool", GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("light_gray_dark_oak_modern_stool", LIGHT_GRAY_DARK_OAK_MODERN_STOOL, true);
        registerFurniture("white_oak_modern_stool", WHITE_OAK_MODERN_STOOL, true);

        //Counter time
        registerFurniture("oak_kitchen_counter", OAK_KITCHEN_COUNTER, true);
        registerFurniture("oak_kitchen_drawer", OAK_KITCHEN_DRAWER, true);
        registerFurniture("oak_kitchen_cabinet", OAK_KITCHEN_CABINET, true);
        registerFurniture("oak_kitchen_sink", OAK_KITCHEN_SINK, true);

        registerFurniture("spruce_kitchen_counter", SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("spruce_kitchen_drawer", SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("spruce_kitchen_cabinet", SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("spruce_kitchen_sink", SPRUCE_KITCHEN_SINK, true);

        registerFurniture("birch_kitchen_counter", BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("birch_kitchen_drawer", BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("birch_kitchen_cabinet", BIRCH_KITCHEN_CABINET, true);
        registerFurniture("birch_kitchen_sink", BIRCH_KITCHEN_SINK, true);

        registerFurniture("acacia_kitchen_counter", ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("acacia_kitchen_drawer", ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("acacia_kitchen_cabinet", ACACIA_KITCHEN_CABINET, true);
        registerFurniture("acacia_kitchen_sink", ACACIA_KITCHEN_SINK, true);

        registerFurniture("jungle_kitchen_counter", JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("jungle_kitchen_drawer", JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("jungle_kitchen_cabinet", JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("jungle_kitchen_sink", JUNGLE_KITCHEN_SINK, true);

        registerFurniture("dark_oak_kitchen_counter", DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("dark_oak_kitchen_drawer", DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("dark_oak_kitchen_cabinet", DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("dark_oak_kitchen_sink", DARK_OAK_KITCHEN_SINK, true);

        registerFurniture("mangrove_kitchen_counter", MANGROVE_KITCHEN_COUNTER, true);
        registerFurniture("mangrove_kitchen_drawer", MANGROVE_KITCHEN_DRAWER, true);
        registerFurniture("mangrove_kitchen_cabinet", MANGROVE_KITCHEN_CABINET, true);
        registerFurniture("mangrove_kitchen_sink", MANGROVE_KITCHEN_SINK, true);

        registerFurniture("crimson_kitchen_counter", CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("crimson_kitchen_drawer", CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("crimson_kitchen_cabinet", CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("crimson_kitchen_sink", CRIMSON_KITCHEN_SINK, true);

        registerFurniture("warped_kitchen_counter", WARPED_KITCHEN_COUNTER, true);
        registerFurniture("warped_kitchen_drawer", WARPED_KITCHEN_DRAWER, true);
        registerFurniture("warped_kitchen_cabinet", WARPED_KITCHEN_CABINET, true);
        registerFurniture("warped_kitchen_sink", WARPED_KITCHEN_SINK, true);

        registerFurniture("stripped_oak_kitchen_counter", STRIPPED_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_oak_kitchen_drawer", STRIPPED_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_oak_kitchen_cabinet", STRIPPED_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_oak_kitchen_sink", STRIPPED_OAK_KITCHEN_SINK, true);

        registerFurniture("stripped_spruce_kitchen_counter", STRIPPED_SPRUCE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_spruce_kitchen_drawer", STRIPPED_SPRUCE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_spruce_kitchen_cabinet", STRIPPED_SPRUCE_KITCHEN_CABINET, true);
        registerFurniture("stripped_spruce_kitchen_sink", STRIPPED_SPRUCE_KITCHEN_SINK, true);

        registerFurniture("stripped_birch_kitchen_counter", STRIPPED_BIRCH_KITCHEN_COUNTER, true);
        registerFurniture("stripped_birch_kitchen_drawer", STRIPPED_BIRCH_KITCHEN_DRAWER, true);
        registerFurniture("stripped_birch_kitchen_cabinet", STRIPPED_BIRCH_KITCHEN_CABINET, true);
        registerFurniture("stripped_birch_kitchen_sink", STRIPPED_BIRCH_KITCHEN_SINK, true);

        registerFurniture("stripped_acacia_kitchen_counter", STRIPPED_ACACIA_KITCHEN_COUNTER, true);
        registerFurniture("stripped_acacia_kitchen_drawer", STRIPPED_ACACIA_KITCHEN_DRAWER, true);
        registerFurniture("stripped_acacia_kitchen_cabinet", STRIPPED_ACACIA_KITCHEN_CABINET, true);
        registerFurniture("stripped_acacia_kitchen_sink", STRIPPED_ACACIA_KITCHEN_SINK, true);

        registerFurniture("stripped_jungle_kitchen_counter", STRIPPED_JUNGLE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_jungle_kitchen_drawer", STRIPPED_JUNGLE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_jungle_kitchen_cabinet", STRIPPED_JUNGLE_KITCHEN_CABINET, true);
        registerFurniture("stripped_jungle_kitchen_sink", STRIPPED_JUNGLE_KITCHEN_SINK, true);

        registerFurniture("stripped_dark_oak_kitchen_counter", STRIPPED_DARK_OAK_KITCHEN_COUNTER, true);
        registerFurniture("stripped_dark_oak_kitchen_drawer", STRIPPED_DARK_OAK_KITCHEN_DRAWER, true);
        registerFurniture("stripped_dark_oak_kitchen_cabinet", STRIPPED_DARK_OAK_KITCHEN_CABINET, true);
        registerFurniture("stripped_dark_oak_kitchen_sink", STRIPPED_DARK_OAK_KITCHEN_SINK, true);

        registerFurniture("stripped_mangrove_kitchen_counter", STRIPPED_MANGROVE_KITCHEN_COUNTER, true);
        registerFurniture("stripped_mangrove_kitchen_drawer", STRIPPED_MANGROVE_KITCHEN_DRAWER, true);
        registerFurniture("stripped_mangrove_kitchen_cabinet", STRIPPED_MANGROVE_KITCHEN_CABINET, true);
        registerFurniture("stripped_mangrove_kitchen_sink", STRIPPED_MANGROVE_KITCHEN_SINK, true);

        registerFurniture("stripped_crimson_kitchen_counter", STRIPPED_CRIMSON_KITCHEN_COUNTER, true);
        registerFurniture("stripped_crimson_kitchen_drawer", STRIPPED_CRIMSON_KITCHEN_DRAWER, true);
        registerFurniture("stripped_crimson_kitchen_cabinet", STRIPPED_CRIMSON_KITCHEN_CABINET, true);
        registerFurniture("stripped_crimson_kitchen_sink", STRIPPED_CRIMSON_KITCHEN_SINK, true);

        registerFurniture("stripped_warped_kitchen_counter", STRIPPED_WARPED_KITCHEN_COUNTER, true);
        registerFurniture("stripped_warped_kitchen_drawer", STRIPPED_WARPED_KITCHEN_DRAWER, true);
        registerFurniture("stripped_warped_kitchen_cabinet", STRIPPED_WARPED_KITCHEN_CABINET, true);
        registerFurniture("stripped_warped_kitchen_sink", STRIPPED_WARPED_KITCHEN_SINK, true);

        registerFurniture("concrete_kitchen_counter", CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("concrete_kitchen_drawer", CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("concrete_kitchen_cabinet", CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("concrete_kitchen_sink", CONCRETE_KITCHEN_SINK, true);

        registerFurniture("dark_concrete_kitchen_counter", DARK_CONCRETE_KITCHEN_COUNTER, true);
        registerFurniture("dark_concrete_kitchen_drawer", DARK_CONCRETE_KITCHEN_DRAWER, true);
        registerFurniture("dark_concrete_kitchen_cabinet", DARK_CONCRETE_KITCHEN_CABINET, true);
        registerFurniture("dark_concrete_kitchen_sink", DARK_CONCRETE_KITCHEN_SINK, true);

        registerFurniture("light_wood_kitchen_counter", LIGHT_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("light_wood_kitchen_drawer", LIGHT_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("light_wood_kitchen_cabinet", LIGHT_WOOD_KITCHEN_CABINET, true);
        registerFurniture("light_wood_kitchen_sink", LIGHT_WOOD_KITCHEN_SINK, true);

        registerFurniture("dark_wood_kitchen_counter", DARK_WOOD_KITCHEN_COUNTER, true);
        registerFurniture("dark_wood_kitchen_drawer", DARK_WOOD_KITCHEN_DRAWER, true);
        registerFurniture("dark_wood_kitchen_cabinet", DARK_WOOD_KITCHEN_CABINET, true);
        registerFurniture("dark_wood_kitchen_sink", DARK_WOOD_KITCHEN_SINK, true);

        registerFurniture("granite_kitchen_counter", GRANITE_KITCHEN_COUNTER, true);
        registerFurniture("granite_kitchen_drawer", GRANITE_KITCHEN_DRAWER, true);
        registerFurniture("granite_kitchen_cabinet", GRANITE_KITCHEN_CABINET, true);
        registerFurniture("granite_kitchen_sink", GRANITE_KITCHEN_SINK, true);

        registerFurniture("calcite_kitchen_counter", CALCITE_KITCHEN_COUNTER, true);
        registerFurniture("calcite_kitchen_drawer", CALCITE_KITCHEN_DRAWER, true);
        registerFurniture("calcite_kitchen_cabinet", CALCITE_KITCHEN_CABINET, true);
        registerFurniture("calcite_kitchen_sink", CALCITE_KITCHEN_SINK, true);

        registerFurniture("netherite_kitchen_counter", NETHERITE_KITCHEN_COUNTER, true);
        registerFurniture("netherite_kitchen_drawer", NETHERITE_KITCHEN_DRAWER, true);
        registerFurniture("netherite_kitchen_cabinet", NETHERITE_KITCHEN_CABINET, true);
        registerFurniture("netherite_kitchen_sink", NETHERITE_KITCHEN_SINK, true);

        registerFurniture("andesite_kitchen_counter", ANDESITE_KITCHEN_COUNTER, true);
        registerFurniture("andesite_kitchen_drawer", ANDESITE_KITCHEN_DRAWER, true);
        registerFurniture("andesite_kitchen_cabinet", ANDESITE_KITCHEN_CABINET, true);
        registerFurniture("andesite_kitchen_sink", ANDESITE_KITCHEN_SINK, true);

        registerFurniture("diorite_kitchen_counter", DIORITE_KITCHEN_COUNTER, true);
        registerFurniture("diorite_kitchen_drawer", DIORITE_KITCHEN_DRAWER, true);
        registerFurniture("diorite_kitchen_cabinet", DIORITE_KITCHEN_CABINET, true);
        registerFurniture("diorite_kitchen_sink", DIORITE_KITCHEN_SINK, true);

        registerFurniture("stone_kitchen_counter", STONE_KITCHEN_COUNTER, true);
        registerFurniture("stone_kitchen_drawer", STONE_KITCHEN_DRAWER, true);
        registerFurniture("stone_kitchen_cabinet", STONE_KITCHEN_CABINET, true);
        registerFurniture("stone_kitchen_sink", STONE_KITCHEN_SINK, true);

        registerFurniture("deepslate_tile_kitchen_counter", DEEPSLATE_TILE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_tile_kitchen_drawer", DEEPSLATE_TILE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_tile_kitchen_cabinet", DEEPSLATE_TILE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_tile_kitchen_sink", DEEPSLATE_TILE_KITCHEN_SINK, true);

        registerFurniture("blackstone_kitchen_counter", BLACKSTONE_KITCHEN_COUNTER, true);
        registerFurniture("blackstone_kitchen_drawer", BLACKSTONE_KITCHEN_DRAWER, true);
        registerFurniture("blackstone_kitchen_cabinet", BLACKSTONE_KITCHEN_CABINET, true);
        registerFurniture("blackstone_kitchen_sink", BLACKSTONE_KITCHEN_SINK, true);

        registerFurniture("deepslate_kitchen_counter", DEEPSLATE_KITCHEN_COUNTER, true);
        registerFurniture("deepslate_kitchen_drawer", DEEPSLATE_KITCHEN_DRAWER, true);
        registerFurniture("deepslate_kitchen_cabinet", DEEPSLATE_KITCHEN_CABINET, true);
        registerFurniture("deepslate_kitchen_sink", DEEPSLATE_KITCHEN_SINK, true);

        registerFurniture("white_fridge", WHITE_FRIDGE, true);
        registerFurniture("white_freezer", WHITE_FREEZER, false);
        registerFurniture("iron_fridge", IRON_FRIDGE, true);
        registerFurniture("iron_freezer", IRON_FREEZER, false);
        registerFurniture("xbox_fridge", XBOX_FRIDGE, true);
        registerFurniture("microwave", MICROWAVE, true);
        registerFurniture("simple_stove", SIMPLE_STOVE, true);
        registerFurniture("iron_stove", IRON_STOVE, true);

        registerBlock("oak_herringbone_planks", OAK_HERRINGBONE_PLANKS, true);
        registerBlock("spruce_herringbone_planks", SPRUCE_HERRINGBONE_PLANKS, true);
        registerBlock("birch_herringbone_planks", BIRCH_HERRINGBONE_PLANKS, true);
        registerBlock("jungle_herringbone_planks", JUNGLE_HERRINGBONE_PLANKS, true);
        registerBlock("acacia_herringbone_planks", ACACIA_HERRINGBONE_PLANKS, true);
        registerBlock("dark_oak_herringbone_planks", DARK_OAK_HERRINGBONE_PLANKS, true);
        registerBlock("mangrove_herringbone_planks", MANGROVE_HERRINGBONE_PLANKS, true);
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
        registerBlock("light_switch", LIGHT_SWITCH,true, LIGHT_SWITCH_ITEM);

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
