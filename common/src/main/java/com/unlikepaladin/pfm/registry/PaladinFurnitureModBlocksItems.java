package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.items.DyeKit;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class PaladinFurnitureModBlocksItems {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block OAK_CHAIR = new BasicChairBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.OAK_TAN));
    public static final Block BIRCH_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR).mapColor(MapColor.PALE_YELLOW));
    public static final Block SPRUCE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR).mapColor(MapColor.SPRUCE_BROWN));
    public static final Block ACACIA_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR).mapColor(MapColor.ORANGE));
    public static final Block JUNGLE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR).mapColor(MapColor.DIRT_BROWN));
    public static final Block DARK_OAK_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR).mapColor(MapColor.BROWN));
    public static final Block CRIMSON_CHAIR = new BasicChairBlock(AbstractBlock.Settings.of(Material.NETHER_WOOD).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.DULL_PINK));
    public static final Block WARPED_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR).mapColor(MapColor.DARK_AQUA));
    public static final Block STRIPPED_OAK_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block QUARTZ_CHAIR = new BasicChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));
    public static final Block NETHERITE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR = new BasicChairBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    //Dinner Chairs
    public static final Block OAK_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));

    public static final Block QUARTZ_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_DINNER = new DinnerChairBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    //Froggy Chairs
    public static final Block FROGGY_CHAIR = new FroggyChairBlock(AbstractBlock.Settings.of(Material.METAL).strength(9.0f).resistance(8.0f).nonOpaque().requiresTool().mapColor(MapColor.GREEN));
    public static final Block FROGGY_CHAIR_PINK = new FroggyChairBlock(AbstractBlock.Settings.copy(FROGGY_CHAIR).mapColor(MapColor.PINK));
    public static final Block FROGGY_CHAIR_BLUE = new FroggyChairBlock(AbstractBlock.Settings.copy(FROGGY_CHAIR).mapColor(MapColor.BLUE));
    public static final Block FROGGY_CHAIR_LIGHT_BLUE = new FroggyChairBlock(AbstractBlock.Settings.copy(FROGGY_CHAIR).mapColor(MapColor.LIGHT_BLUE));
    public static final Block FROGGY_CHAIR_ORANGE = new FroggyChairBlock(AbstractBlock.Settings.copy(FROGGY_CHAIR).mapColor(MapColor.ORANGE));
    public static final Block FROGGY_CHAIR_YELLOW = new FroggyChairBlock(AbstractBlock.Settings.copy(FROGGY_CHAIR).mapColor(MapColor.YELLOW));

    //Classic Chair
    public static final Block OAK_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_WHITE = new ClassicChairDyeableBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_ORANGE = new ClassicChairDyeableBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_MAGENTA = new ClassicChairDyeableBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_LIGHT_BLUE = new ClassicChairDyeableBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_YELLOW = new ClassicChairDyeableBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_LIME = new ClassicChairDyeableBlock(DyeColor.LIME, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_PINK = new ClassicChairDyeableBlock(DyeColor.PINK, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_GRAY = new ClassicChairDyeableBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_LIGHT_GRAY = new ClassicChairDyeableBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_CYAN = new ClassicChairDyeableBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_PURPLE = new ClassicChairDyeableBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_BLUE = new ClassicChairDyeableBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_BROWN = new ClassicChairDyeableBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_GREEN = new ClassicChairDyeableBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_RED = new ClassicChairDyeableBlock(DyeColor.RED, AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block OAK_CHAIR_CLASSIC_BLACK = new ClassicChairDyeableBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block QUARTZ_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block NETHERITE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(NETHERITE_CHAIR));

    public static final Block LIGHT_WOOD_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_CLASSIC = new ClassicChairBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    //Modern Chair
    public static final Block OAK_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));

    public static final Block QUARTZ_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CHAIR_MODERN = new ModernChairBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));


    //Arm Chairs
    public static final Block WHITE_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.WHITE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BLUE_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.BLUE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block GREEN_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.GREEN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block GRAY_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.GRAY, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block PINK_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.PINK, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BROWN_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.BROWN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block ORANGE_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.ORANGE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block YELLOW_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.YELLOW, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block PURPLE_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.PURPLE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block CYAN_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.CYAN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIME_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.LIME, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block MAGENTA_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.MAGENTA, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIGHT_BLUE_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIGHT_GRAY_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block RED_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.RED, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BLACK_ARM_CHAIR = new ArmChairColoredBlock(DyeColor.BLACK, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));

    public static final Block ARM_CHAIR_LEATHER = new ArmChairBlock(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));

    public static final Block WHITE_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.WHITE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BLUE_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.BLUE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block GREEN_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.GREEN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block GRAY_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.GRAY, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block PINK_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.PINK, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BROWN_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.BROWN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block ORANGE_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.ORANGE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block YELLOW_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.YELLOW, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block PURPLE_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.PURPLE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block CYAN_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.CYAN, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIME_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.LIME, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block MAGENTA_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.MAGENTA, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIGHT_BLUE_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block LIGHT_GRAY_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block RED_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.RED, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));
    public static final Block BLACK_SIMPLE_SOFA = new SimpleSofaBlock(DyeColor.BLACK, AbstractBlock.Settings.of(Material.WOOL).strength(2.0f).resistance(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL));

    //tables
    public static final Block OAK_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_BASIC_TABLE = new BasicTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_TABLE = new ClassicTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));


    public static final Block OAK_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_NATURAL_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_RAW_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_RAW_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_RAW_LOG_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_RAW_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_RAW_STEM_TABLE = new LogTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block OAK_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_MODERN_DINNER_TABLE = new ModernDinnerTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block JUNGLE_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block ACACIA_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block DARK_OAK_LOG_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_STEM_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_STEM_STOOL = new LogStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block OAK_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block JUNGLE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block ACACIA_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block DARK_OAK_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_SIMPLE_STOOL = new SimpleStoolBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block JUNGLE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block ACACIA_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block DARK_OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block WHITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE));
    public static final Block GRAY_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE));
    public static final Block DARK_WOOD_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_MODERN_STOOL));
    public static final Block GRAY_DARK_OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_MODERN_STOOL));
    public static final Block LIGHT_GRAY_DARK_OAK_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_MODERN_STOOL));
    public static final Block LIGHT_WOOD_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(OAK_MODERN_STOOL));

    public static final Block QUARTZ_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block GRANITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_MODERN_STOOL = new ModernStoolBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block JUNGLE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block ACACIA_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block DARK_OAK_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_STOOL = new ClassicStoolBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));

    public static final Block OAK_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block JUNGLE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block ACACIA_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block DARK_OAK_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_JUNGLE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_ACACIA_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_DARK_OAK_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_CRIMSON_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block STRIPPED_WARPED_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));

    public static final Block QUARTZ_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_DINNER_TABLE = new DinnerTableBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));


    public static final Block OAK_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.0f).resistance(3.0f).nonOpaque().sounds(BlockSoundGroup.WOOD).mapColor(MapColor.OAK_TAN));
    public static final Block OAK_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block OAK_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));

    public static final Block BIRCH_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER).mapColor(MapColor.PALE_YELLOW));
    public static final Block BIRCH_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block BIRCH_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block BIRCH_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));

    public static final Block SPRUCE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER).mapColor(MapColor.SPRUCE_BROWN));
    public static final Block SPRUCE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block SPRUCE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block SPRUCE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));

    public static final Block JUNGLE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER).mapColor(MapColor.DIRT_BROWN));
    public static final Block JUNGLE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block JUNGLE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block JUNGLE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block JUNGLE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block JUNGLE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block JUNGLE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block JUNGLE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));

    public static final Block ACACIA_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER).mapColor(MapColor.ORANGE));
    public static final Block ACACIA_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block ACACIA_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block ACACIA_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));

    public static final Block DARK_OAK_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER).mapColor(MapColor.BROWN));
    public static final Block DARK_OAK_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_OAK_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));

    public static final Block CRIMSON_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.of(Material.NETHER_WOOD).strength(2.0f).resistance(3.0f).nonOpaque().sounds(BlockSoundGroup.NETHER_STEM).mapColor(MapColor.DULL_PINK));
    public static final Block CRIMSON_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CRIMSON_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block CRIMSON_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));

    public static final Block WARPED_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER).mapColor(MapColor.DARK_AQUA));
    public static final Block WARPED_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block WARPED_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block WARPED_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));

    public static final Block STRIPPED_OAK_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_OAK_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));

    public static final Block STRIPPED_BIRCH_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_BIRCH_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));
    public static final Block STRIPPED_BIRCH_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(BIRCH_KITCHEN_COUNTER));

    public static final Block STRIPPED_SPRUCE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_SPRUCE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));
    public static final Block STRIPPED_SPRUCE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(SPRUCE_KITCHEN_COUNTER));

    public static final Block STRIPPED_JUNGLE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_JUNGLE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));
    public static final Block STRIPPED_JUNGLE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(JUNGLE_KITCHEN_COUNTER));

    public static final Block STRIPPED_ACACIA_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_ACACIA_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));
    public static final Block STRIPPED_ACACIA_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(ACACIA_KITCHEN_COUNTER));

    public static final Block STRIPPED_DARK_OAK_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_DARK_OAK_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block STRIPPED_DARK_OAK_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));

    public static final Block STRIPPED_CRIMSON_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_CRIMSON_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));
    public static final Block STRIPPED_CRIMSON_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(CRIMSON_KITCHEN_COUNTER));

    public static final Block STRIPPED_WARPED_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STRIPPED_WARPED_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));
    public static final Block STRIPPED_WARPED_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(WARPED_KITCHEN_COUNTER));

    public static final Block WHITE_FREEZER = new FreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE).mapColor(MapColor.WHITE), () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final Block WHITE_FRIDGE = new FridgeBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final Block GRAY_FREEZER = new FreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE).mapColor(MapColor.GRAY), () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
    public static final Block GRAY_FRIDGE = new FridgeBlock(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER);
    public static final Block IRON_FREEZER = new IronFreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.METAL).mapColor(MapColor.IRON_GRAY), () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE);
    public static final Block IRON_FRIDGE = new IronFridgeBlock(AbstractBlock.Settings.copy(IRON_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.IRON_FREEZER);

    public static final Block XBOX_FRIDGE = new XboxFridgeBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque().mapColor(MapColor.BLACK), null);

    public static final Block WHITE_STOVE = new StoveBlock(AbstractBlock.Settings.copy(WHITE_FREEZER));
    public static final Block WHITE_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque());
    public static final Block GRAY_STOVE = new StoveBlock(AbstractBlock.Settings.copy(GRAY_FREEZER));
    public static final Block GRAY_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque());
    public static final Block IRON_STOVE = new IronStoveBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final Block IRON_MICROWAVE = new MicrowaveBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final Block IRON_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque());
    public static final Block TRASHCAN = new TrashcanBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final Block MESH_TRASHCAN = new InnerTrashcanBlock(AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque());

    public static final Block OAK_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block BIRCH_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block SPRUCE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block ACACIA_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block JUNGLE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block DARK_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block CRIMSON_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));
    public static final Block WARPED_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(OAK_CHAIR));
    public static final Block STRIPPED_BIRCH_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(BIRCH_CHAIR));
    public static final Block STRIPPED_SPRUCE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(SPRUCE_CHAIR));
    public static final Block STRIPPED_ACACIA_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(ACACIA_CHAIR));
    public static final Block STRIPPED_JUNGLE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(JUNGLE_CHAIR));
    public static final Block STRIPPED_DARK_OAK_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));
    public static final Block STRIPPED_WARPED_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(WARPED_CHAIR));
    public static final Block STRIPPED_CRIMSON_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(CRIMSON_CHAIR));

    public static final Block QUARTZ_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.of(Material.STONE).strength(0.8f).resistance(2.0f).nonOpaque().requiresTool().mapColor(MapColor.OFF_WHITE));

    public static final Block NETHERITE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.of(Material.STONE).strength(50.0f).resistance(1200.0f).nonOpaque().requiresTool().sounds(BlockSoundGroup.NETHERITE).mapColor(MapColor.BLACK));

    public static final Block LIGHT_WOOD_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(OAK_CHAIR));

    public static final Block DARK_WOOD_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(DARK_OAK_CHAIR));

    public static final Block GRANITE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block ANDESITE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block STONE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block BLACKSTONE_CLASSIC_NIGHTSTAND = new ClassicNightstandBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE));


    public static final Block OAK_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block OAK_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block OAK_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block OAK_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block OAK_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block OAK_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block OAK_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block OAK_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block OAK_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block OAK_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block OAK_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block OAK_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block OAK_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block OAK_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block OAK_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block OAK_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block SPRUCE_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block SPRUCE_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block SPRUCE_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block SPRUCE_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block SPRUCE_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block SPRUCE_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block SPRUCE_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block SPRUCE_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block SPRUCE_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block SPRUCE_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block SPRUCE_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block SPRUCE_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block SPRUCE_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block SPRUCE_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block SPRUCE_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block SPRUCE_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block DARK_OAK_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block DARK_OAK_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block DARK_OAK_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block DARK_OAK_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block DARK_OAK_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block DARK_OAK_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block DARK_OAK_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block DARK_OAK_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block DARK_OAK_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block DARK_OAK_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block DARK_OAK_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block DARK_OAK_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block DARK_OAK_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block DARK_OAK_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block DARK_OAK_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block DARK_OAK_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block BIRCH_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block BIRCH_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block BIRCH_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block BIRCH_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block BIRCH_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block BIRCH_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block BIRCH_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block BIRCH_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block BIRCH_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block BIRCH_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block BIRCH_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block BIRCH_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block BIRCH_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block BIRCH_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block BIRCH_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block BIRCH_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block ACACIA_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block ACACIA_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block ACACIA_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block ACACIA_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block ACACIA_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block ACACIA_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block ACACIA_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block ACACIA_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block ACACIA_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block ACACIA_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block ACACIA_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block ACACIA_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block ACACIA_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block ACACIA_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block ACACIA_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block ACACIA_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block JUNGLE_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block JUNGLE_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block JUNGLE_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block JUNGLE_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block JUNGLE_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block JUNGLE_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block JUNGLE_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block JUNGLE_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block JUNGLE_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block JUNGLE_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block JUNGLE_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block JUNGLE_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block JUNGLE_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block JUNGLE_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block JUNGLE_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block JUNGLE_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block WARPED_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block WARPED_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block WARPED_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block WARPED_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block WARPED_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block WARPED_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block WARPED_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block WARPED_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block WARPED_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block WARPED_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block WARPED_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block WARPED_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block WARPED_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block WARPED_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block WARPED_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block WARPED_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block CRIMSON_RED_SIMPLE_BED = new SimpleBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block CRIMSON_ORANGE_SIMPLE_BED = new SimpleBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block CRIMSON_YELLOW_SIMPLE_BED = new SimpleBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block CRIMSON_GREEN_SIMPLE_BED = new SimpleBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block CRIMSON_LIME_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block CRIMSON_CYAN_SIMPLE_BED = new SimpleBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block CRIMSON_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block CRIMSON_LIGHT_BLUE_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block CRIMSON_LIGHT_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block CRIMSON_MAGENTA_SIMPLE_BED = new SimpleBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block CRIMSON_PINK_SIMPLE_BED = new SimpleBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block CRIMSON_PURPLE_SIMPLE_BED = new SimpleBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block CRIMSON_WHITE_SIMPLE_BED = new SimpleBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block CRIMSON_BROWN_SIMPLE_BED = new SimpleBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block CRIMSON_GRAY_SIMPLE_BED = new SimpleBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block CRIMSON_BLACK_SIMPLE_BED = new SimpleBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));


    public static final Block OAK_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block OAK_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block OAK_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block OAK_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block OAK_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block OAK_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block OAK_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block OAK_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block OAK_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block OAK_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block OAK_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block OAK_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block OAK_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block OAK_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block OAK_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block OAK_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block SPRUCE_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block SPRUCE_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block SPRUCE_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block SPRUCE_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block SPRUCE_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block SPRUCE_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block SPRUCE_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block SPRUCE_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block SPRUCE_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block SPRUCE_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block SPRUCE_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block SPRUCE_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block SPRUCE_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block SPRUCE_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block SPRUCE_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block SPRUCE_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block DARK_OAK_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block DARK_OAK_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block DARK_OAK_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block DARK_OAK_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block DARK_OAK_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block DARK_OAK_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block DARK_OAK_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block DARK_OAK_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block DARK_OAK_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block DARK_OAK_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block DARK_OAK_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block DARK_OAK_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block DARK_OAK_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block DARK_OAK_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block DARK_OAK_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block DARK_OAK_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block BIRCH_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block BIRCH_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block BIRCH_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block BIRCH_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block BIRCH_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block BIRCH_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block BIRCH_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block BIRCH_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block BIRCH_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block BIRCH_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block BIRCH_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block BIRCH_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block BIRCH_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block BIRCH_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block BIRCH_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block BIRCH_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block ACACIA_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block ACACIA_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block ACACIA_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block ACACIA_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block ACACIA_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block ACACIA_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block ACACIA_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block ACACIA_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block ACACIA_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block ACACIA_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block ACACIA_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block ACACIA_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block ACACIA_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block ACACIA_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block ACACIA_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block ACACIA_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block JUNGLE_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block JUNGLE_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block JUNGLE_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block JUNGLE_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block JUNGLE_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block JUNGLE_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block JUNGLE_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block JUNGLE_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block JUNGLE_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block JUNGLE_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block JUNGLE_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block JUNGLE_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block JUNGLE_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block JUNGLE_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block JUNGLE_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block JUNGLE_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block WARPED_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block WARPED_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block WARPED_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block WARPED_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block WARPED_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block WARPED_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block WARPED_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block WARPED_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block WARPED_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block WARPED_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block WARPED_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block WARPED_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block WARPED_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block WARPED_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block WARPED_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block WARPED_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Block CRIMSON_RED_CLASSIC_BED = new ClassicBedBlock(DyeColor.RED, AbstractBlock.Settings.copy(Blocks.RED_BED));
    public static final Block CRIMSON_ORANGE_CLASSIC_BED = new ClassicBedBlock(DyeColor.ORANGE, AbstractBlock.Settings.copy(Blocks.ORANGE_BED));
    public static final Block CRIMSON_YELLOW_CLASSIC_BED = new ClassicBedBlock(DyeColor.YELLOW, AbstractBlock.Settings.copy(Blocks.YELLOW_BED));
    public static final Block CRIMSON_GREEN_CLASSIC_BED = new ClassicBedBlock(DyeColor.GREEN, AbstractBlock.Settings.copy(Blocks.GREEN_BED));
    public static final Block CRIMSON_LIME_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIME, AbstractBlock.Settings.copy(Blocks.LIME_BED));
    public static final Block CRIMSON_CYAN_CLASSIC_BED = new ClassicBedBlock(DyeColor.CYAN, AbstractBlock.Settings.copy(Blocks.CYAN_BED));
    public static final Block CRIMSON_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLUE, AbstractBlock.Settings.copy(Blocks.BLUE_BED));
    public static final Block CRIMSON_LIGHT_BLUE_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_BED));
    public static final Block CRIMSON_LIGHT_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_BED));
    public static final Block CRIMSON_MAGENTA_CLASSIC_BED = new ClassicBedBlock(DyeColor.MAGENTA, AbstractBlock.Settings.copy(Blocks.MAGENTA_BED));
    public static final Block CRIMSON_PINK_CLASSIC_BED = new ClassicBedBlock(DyeColor.PINK, AbstractBlock.Settings.copy(Blocks.PINK_BED));
    public static final Block CRIMSON_PURPLE_CLASSIC_BED = new ClassicBedBlock(DyeColor.PURPLE, AbstractBlock.Settings.copy(Blocks.PURPLE_BED));
    public static final Block CRIMSON_WHITE_CLASSIC_BED = new ClassicBedBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BED));
    public static final Block CRIMSON_BROWN_CLASSIC_BED = new ClassicBedBlock(DyeColor.BROWN, AbstractBlock.Settings.copy(Blocks.BROWN_BED));
    public static final Block CRIMSON_GRAY_CLASSIC_BED = new ClassicBedBlock(DyeColor.GRAY, AbstractBlock.Settings.copy(Blocks.GRAY_BED));
    public static final Block CRIMSON_BLACK_CLASSIC_BED = new ClassicBedBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_BED));

    public static final Item DYE_KIT_YELLOW = new DyeKit(new Item.Settings().maxCount(16), DyeColor.YELLOW);
    public static final Item DYE_KIT_BLUE = new DyeKit(new Item.Settings().maxCount(16), DyeColor.BLUE);
    public static final Item DYE_KIT_WHITE = new DyeKit(new Item.Settings().maxCount(16), DyeColor.WHITE);
    public static final Item DYE_KIT_PINK = new DyeKit(new Item.Settings().maxCount(16), DyeColor.PINK);
    public static final Item DYE_KIT_PURPLE = new DyeKit(new Item.Settings().maxCount(16), DyeColor.PURPLE);
    public static final Item DYE_KIT_GREEN = new DyeKit(new Item.Settings().maxCount(16), DyeColor.GREEN);
    public static final Item DYE_KIT_LIGHT_BLUE = new DyeKit(new Item.Settings().maxCount(16), DyeColor.LIGHT_BLUE);
    public static final Item DYE_KIT_LIGHT_GRAY = new DyeKit(new Item.Settings().maxCount(16), DyeColor.LIGHT_GRAY);
    public static final Item DYE_KIT_LIME = new DyeKit(new Item.Settings().maxCount(16), DyeColor.LIME);
    public static final Item DYE_KIT_ORANGE = new DyeKit(new Item.Settings().maxCount(16), DyeColor.ORANGE);
    public static final Item DYE_KIT_BLACK = new DyeKit(new Item.Settings().maxCount(16), DyeColor.BLACK);
    public static final Item DYE_KIT_BROWN = new DyeKit(new Item.Settings().maxCount(16), DyeColor.BROWN);
    public static final Item DYE_KIT_MAGENTA = new DyeKit(new Item.Settings().maxCount(16), DyeColor.MAGENTA);
    public static final Item DYE_KIT_RED = new DyeKit(new Item.Settings().maxCount(16), DyeColor.RED);
    public static final Item DYE_KIT_CYAN = new DyeKit(new Item.Settings().maxCount(16), DyeColor.CYAN);
    public static final Item DYE_KIT_GRAY = new DyeKit(new Item.Settings().maxCount(16), DyeColor.GRAY);

    public static final Block ACACIA_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block SPRUCE_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block OAK_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block DARK_OAK_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block JUNGLE_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block BIRCH_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.BIRCH_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block WARPED_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.WARPED_PLANKS).sounds(BlockSoundGroup.WOOD));
    public static final Block CRIMSON_HERRINGBONE_PLANKS = new HerringbonePlankBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS).sounds(BlockSoundGroup.WOOD));


    public static final Block RAW_CONCRETE = new Block(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE).sounds(BlockSoundGroup.STONE));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE_POWDER).sounds(BlockSoundGroup.SAND));
    public static final Block LEATHER_BLOCK = new Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).sounds(BlockSoundGroup.WOOL).mapColor(MapColor.ORANGE));

    public static final Block IRON_CHAIN = new ChainBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.METAL));
    public static final Block GRAY_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.GRAY));
    public static final Block WHITE_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.WHITE));
    public static final Block GLASS_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.OFF_WHITE));
    public static final Block SIMPLE_LIGHT = new SimpleLightBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.LIGHT_GRAY));

    public static final Block LIGHT_SWITCH = new LightSwitchBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).sounds(BlockSoundGroup.STONE).nonOpaque().mapColor(MapColor.WHITE));
    public static BlockItem LIGHT_SWITCH_ITEM;
    public static Item FURNITURE_BOOK;

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    public static final Block CONCRETE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(RAW_CONCRETE).mapColor(MapColor.LIGHT_GRAY));
    public static final Block CONCRETE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CONCRETE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));
    public static final Block CONCRETE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(CONCRETE_KITCHEN_COUNTER));

    public static final Block DARK_CONCRETE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(RAW_CONCRETE).mapColor(MapColor.GRAY));
    public static final Block DARK_CONCRETE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_CONCRETE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));
    public static final Block DARK_CONCRETE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(DARK_CONCRETE_KITCHEN_COUNTER));

    public static final Block LIGHT_WOOD_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block LIGHT_WOOD_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));
    public static final Block LIGHT_WOOD_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(OAK_KITCHEN_COUNTER));

    public static final Block DARK_WOOD_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DARK_WOOD_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));
    public static final Block DARK_WOOD_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(DARK_OAK_KITCHEN_COUNTER));

    public static final Block GRANITE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block GRANITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));
    public static final Block GRANITE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE));

    public static final Block CALCITE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.CALCITE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block CALCITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));
    public static final Block CALCITE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.CALCITE));

    public static final Block NETHERITE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block NETHERITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));
    public static final Block NETHERITE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK));

    public static final Block ANDESITE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block ANDESITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));
    public static final Block ANDESITE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE));

    public static final Block DIORITE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DIORITE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));
    public static final Block DIORITE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DIORITE));

    public static final Block SMOOTH_STONE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block SMOOTH_STONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));
    public static final Block SMOOTH_STONE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE));

    public static final Block STONE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.STONE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block STONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block STONE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.STONE));

    public static final Block DEEPSLATE_TILE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DEEPSLATE_TILE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static final Block DEEPSLATE_TILE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));

    public static final Block BLACKSTONE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block BLACKSTONE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block BLACKSTONE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));

    public static final Block KITCHEN_STOVETOP = new KitchenStovetopBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));

    public static final Block DEEPSLATE_KITCHEN_COUNTER = new KitchenCounterBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_DRAWER = new KitchenDrawerBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_CABINET = new KitchenCabinetBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_SINK = new KitchenSinkBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final Block DEEPSLATE_KITCHEN_COUNTER_OVEN = new KitchenCounterOvenBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_WALL_COUNTER = new KitchenWallCounterBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_WALL_DRAWER = new KitchenWallDrawerBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static final Block DEEPSLATE_KITCHEN_WALL_SMALL_DRAWER = new KitchenWallDrawerSmallBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE));

    public static final Block WORKING_TABLE = new WorkingTableBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(BlockSoundGroup.WOOD));
    public static final Block BASIC_PLATE = new PlateBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).nonOpaque());
    public static final Block BASIC_CUTLERY = new CutleryBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE).nonOpaque());

    public static final Block BASIC_TOILET = new BasicToiletBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static final Block WALL_TOILET_PAPER = new WallToiletPaperBlock(AbstractBlock.Settings.of(Material.WOOL, MapColor.OFF_WHITE).nonOpaque());
    public static final Block BASIC_BATHTUB = new BasicBathtubBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), BathtubBehavior.TUB_BEHAVIOR, LeveledCauldronBlock.RAIN_PREDICATE);

    public static final Block OAK_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block SPRUCE_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block BIRCH_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block DARK_OAK_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block ACACIA_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block JUNGLE_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block CRIMSON_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));
    public static final Block WARPED_SIMPLE_BUNK_LADDER = new SimpleBunkLadderBlock(AbstractBlock.Settings.copy(Blocks.LADDER));

    public static final Block BASIC_SHOWER_HEAD = new BasicShowerHeadBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static final Block BASIC_SHOWER_HANDLE = new BasicShowerHandleBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static BlockItem BASIC_SHOWER_HANDLE_ITEM;

    public static final Block BASIC_SINK = new BasicSinkBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);

    public static Block[] getBeds() {
        return new Block[] {OAK_RED_SIMPLE_BED, OAK_ORANGE_SIMPLE_BED, OAK_YELLOW_SIMPLE_BED, OAK_GREEN_SIMPLE_BED, OAK_LIME_SIMPLE_BED, OAK_CYAN_SIMPLE_BED, OAK_BLUE_SIMPLE_BED, OAK_LIGHT_BLUE_SIMPLE_BED, OAK_LIGHT_GRAY_SIMPLE_BED, OAK_GRAY_SIMPLE_BED, OAK_BLACK_SIMPLE_BED, OAK_PURPLE_SIMPLE_BED, OAK_MAGENTA_SIMPLE_BED, OAK_PINK_SIMPLE_BED, OAK_BROWN_SIMPLE_BED, OAK_WHITE_SIMPLE_BED, SPRUCE_RED_SIMPLE_BED, SPRUCE_ORANGE_SIMPLE_BED, SPRUCE_YELLOW_SIMPLE_BED, SPRUCE_GREEN_SIMPLE_BED, SPRUCE_LIME_SIMPLE_BED, SPRUCE_CYAN_SIMPLE_BED, SPRUCE_BLUE_SIMPLE_BED, SPRUCE_LIGHT_BLUE_SIMPLE_BED, SPRUCE_LIGHT_GRAY_SIMPLE_BED, SPRUCE_GRAY_SIMPLE_BED, SPRUCE_BLACK_SIMPLE_BED, SPRUCE_PURPLE_SIMPLE_BED, SPRUCE_MAGENTA_SIMPLE_BED, SPRUCE_PINK_SIMPLE_BED, SPRUCE_BROWN_SIMPLE_BED, SPRUCE_WHITE_SIMPLE_BED, BIRCH_RED_SIMPLE_BED, BIRCH_ORANGE_SIMPLE_BED, BIRCH_YELLOW_SIMPLE_BED, BIRCH_GREEN_SIMPLE_BED, BIRCH_LIME_SIMPLE_BED, BIRCH_CYAN_SIMPLE_BED, BIRCH_BLUE_SIMPLE_BED, BIRCH_LIGHT_BLUE_SIMPLE_BED, BIRCH_LIGHT_GRAY_SIMPLE_BED, BIRCH_GRAY_SIMPLE_BED, BIRCH_BLACK_SIMPLE_BED, BIRCH_PURPLE_SIMPLE_BED, BIRCH_MAGENTA_SIMPLE_BED, BIRCH_PINK_SIMPLE_BED, BIRCH_BROWN_SIMPLE_BED, BIRCH_WHITE_SIMPLE_BED,JUNGLE_RED_SIMPLE_BED, JUNGLE_ORANGE_SIMPLE_BED, JUNGLE_YELLOW_SIMPLE_BED, JUNGLE_GREEN_SIMPLE_BED, JUNGLE_LIME_SIMPLE_BED, JUNGLE_CYAN_SIMPLE_BED, JUNGLE_BLUE_SIMPLE_BED, JUNGLE_LIGHT_BLUE_SIMPLE_BED, JUNGLE_LIGHT_GRAY_SIMPLE_BED, JUNGLE_GRAY_SIMPLE_BED, JUNGLE_BLACK_SIMPLE_BED, JUNGLE_PURPLE_SIMPLE_BED, JUNGLE_MAGENTA_SIMPLE_BED, JUNGLE_PINK_SIMPLE_BED, JUNGLE_BROWN_SIMPLE_BED, JUNGLE_WHITE_SIMPLE_BED, ACACIA_RED_SIMPLE_BED, ACACIA_ORANGE_SIMPLE_BED, ACACIA_YELLOW_SIMPLE_BED, ACACIA_GREEN_SIMPLE_BED, ACACIA_LIME_SIMPLE_BED, ACACIA_CYAN_SIMPLE_BED, ACACIA_BLUE_SIMPLE_BED, ACACIA_LIGHT_BLUE_SIMPLE_BED, ACACIA_LIGHT_GRAY_SIMPLE_BED, ACACIA_GRAY_SIMPLE_BED, ACACIA_BLACK_SIMPLE_BED, ACACIA_PURPLE_SIMPLE_BED, ACACIA_MAGENTA_SIMPLE_BED, ACACIA_PINK_SIMPLE_BED, ACACIA_BROWN_SIMPLE_BED, ACACIA_WHITE_SIMPLE_BED, DARK_OAK_RED_SIMPLE_BED, DARK_OAK_ORANGE_SIMPLE_BED, DARK_OAK_YELLOW_SIMPLE_BED, DARK_OAK_GREEN_SIMPLE_BED, DARK_OAK_LIME_SIMPLE_BED, DARK_OAK_CYAN_SIMPLE_BED, DARK_OAK_BLUE_SIMPLE_BED, DARK_OAK_LIGHT_BLUE_SIMPLE_BED, DARK_OAK_LIGHT_GRAY_SIMPLE_BED, DARK_OAK_GRAY_SIMPLE_BED, DARK_OAK_BLACK_SIMPLE_BED, DARK_OAK_PURPLE_SIMPLE_BED, DARK_OAK_MAGENTA_SIMPLE_BED, DARK_OAK_PINK_SIMPLE_BED, DARK_OAK_BROWN_SIMPLE_BED, DARK_OAK_WHITE_SIMPLE_BED, WARPED_RED_SIMPLE_BED, WARPED_ORANGE_SIMPLE_BED, WARPED_YELLOW_SIMPLE_BED, WARPED_GREEN_SIMPLE_BED, WARPED_LIME_SIMPLE_BED, WARPED_CYAN_SIMPLE_BED, WARPED_BLUE_SIMPLE_BED, WARPED_LIGHT_BLUE_SIMPLE_BED, WARPED_LIGHT_GRAY_SIMPLE_BED, WARPED_GRAY_SIMPLE_BED, WARPED_BLACK_SIMPLE_BED, WARPED_PURPLE_SIMPLE_BED, WARPED_MAGENTA_SIMPLE_BED, WARPED_PINK_SIMPLE_BED, WARPED_BROWN_SIMPLE_BED, WARPED_WHITE_SIMPLE_BED, CRIMSON_RED_SIMPLE_BED, CRIMSON_ORANGE_SIMPLE_BED, CRIMSON_YELLOW_SIMPLE_BED, CRIMSON_GREEN_SIMPLE_BED, CRIMSON_LIME_SIMPLE_BED, CRIMSON_CYAN_SIMPLE_BED, CRIMSON_BLUE_SIMPLE_BED, CRIMSON_LIGHT_BLUE_SIMPLE_BED, CRIMSON_LIGHT_GRAY_SIMPLE_BED, CRIMSON_GRAY_SIMPLE_BED, CRIMSON_BLACK_SIMPLE_BED, CRIMSON_PURPLE_SIMPLE_BED, CRIMSON_MAGENTA_SIMPLE_BED, CRIMSON_PINK_SIMPLE_BED, CRIMSON_BROWN_SIMPLE_BED, CRIMSON_WHITE_SIMPLE_BED, OAK_RED_CLASSIC_BED, OAK_ORANGE_CLASSIC_BED, OAK_YELLOW_CLASSIC_BED, OAK_GREEN_CLASSIC_BED, OAK_LIME_CLASSIC_BED, OAK_CYAN_CLASSIC_BED, OAK_BLUE_CLASSIC_BED, OAK_LIGHT_BLUE_CLASSIC_BED, OAK_LIGHT_GRAY_CLASSIC_BED, OAK_GRAY_CLASSIC_BED, OAK_BLACK_CLASSIC_BED, OAK_PURPLE_CLASSIC_BED, OAK_MAGENTA_CLASSIC_BED, OAK_PINK_CLASSIC_BED, OAK_BROWN_CLASSIC_BED, OAK_WHITE_CLASSIC_BED, SPRUCE_RED_CLASSIC_BED, SPRUCE_ORANGE_CLASSIC_BED, SPRUCE_YELLOW_CLASSIC_BED, SPRUCE_GREEN_CLASSIC_BED, SPRUCE_LIME_CLASSIC_BED, SPRUCE_CYAN_CLASSIC_BED, SPRUCE_BLUE_CLASSIC_BED, SPRUCE_LIGHT_BLUE_CLASSIC_BED, SPRUCE_LIGHT_GRAY_CLASSIC_BED, SPRUCE_GRAY_CLASSIC_BED, SPRUCE_BLACK_CLASSIC_BED, SPRUCE_PURPLE_CLASSIC_BED, SPRUCE_MAGENTA_CLASSIC_BED, SPRUCE_PINK_CLASSIC_BED, SPRUCE_BROWN_CLASSIC_BED, SPRUCE_WHITE_CLASSIC_BED, BIRCH_RED_CLASSIC_BED, BIRCH_ORANGE_CLASSIC_BED, BIRCH_YELLOW_CLASSIC_BED, BIRCH_GREEN_CLASSIC_BED, BIRCH_LIME_CLASSIC_BED, BIRCH_CYAN_CLASSIC_BED, BIRCH_BLUE_CLASSIC_BED, BIRCH_LIGHT_BLUE_CLASSIC_BED, BIRCH_LIGHT_GRAY_CLASSIC_BED, BIRCH_GRAY_CLASSIC_BED, BIRCH_BLACK_CLASSIC_BED, BIRCH_PURPLE_CLASSIC_BED, BIRCH_MAGENTA_CLASSIC_BED, BIRCH_PINK_CLASSIC_BED, BIRCH_BROWN_CLASSIC_BED, BIRCH_WHITE_CLASSIC_BED,JUNGLE_RED_CLASSIC_BED, JUNGLE_ORANGE_CLASSIC_BED, JUNGLE_YELLOW_CLASSIC_BED, JUNGLE_GREEN_CLASSIC_BED, JUNGLE_LIME_CLASSIC_BED, JUNGLE_CYAN_CLASSIC_BED, JUNGLE_BLUE_CLASSIC_BED, JUNGLE_LIGHT_BLUE_CLASSIC_BED, JUNGLE_LIGHT_GRAY_CLASSIC_BED, JUNGLE_GRAY_CLASSIC_BED, JUNGLE_BLACK_CLASSIC_BED, JUNGLE_PURPLE_CLASSIC_BED, JUNGLE_MAGENTA_CLASSIC_BED, JUNGLE_PINK_CLASSIC_BED, JUNGLE_BROWN_CLASSIC_BED, JUNGLE_WHITE_CLASSIC_BED, ACACIA_RED_CLASSIC_BED, ACACIA_ORANGE_CLASSIC_BED, ACACIA_YELLOW_CLASSIC_BED, ACACIA_GREEN_CLASSIC_BED, ACACIA_LIME_CLASSIC_BED, ACACIA_CYAN_CLASSIC_BED, ACACIA_BLUE_CLASSIC_BED, ACACIA_LIGHT_BLUE_CLASSIC_BED, ACACIA_LIGHT_GRAY_CLASSIC_BED, ACACIA_GRAY_CLASSIC_BED, ACACIA_BLACK_CLASSIC_BED, ACACIA_PURPLE_CLASSIC_BED, ACACIA_MAGENTA_CLASSIC_BED, ACACIA_PINK_CLASSIC_BED, ACACIA_BROWN_CLASSIC_BED, ACACIA_WHITE_CLASSIC_BED, DARK_OAK_RED_CLASSIC_BED, DARK_OAK_ORANGE_CLASSIC_BED, DARK_OAK_YELLOW_CLASSIC_BED, DARK_OAK_GREEN_CLASSIC_BED, DARK_OAK_LIME_CLASSIC_BED, DARK_OAK_CYAN_CLASSIC_BED, DARK_OAK_BLUE_CLASSIC_BED, DARK_OAK_LIGHT_BLUE_CLASSIC_BED, DARK_OAK_LIGHT_GRAY_CLASSIC_BED, DARK_OAK_GRAY_CLASSIC_BED, DARK_OAK_BLACK_CLASSIC_BED, DARK_OAK_PURPLE_CLASSIC_BED, DARK_OAK_MAGENTA_CLASSIC_BED, DARK_OAK_PINK_CLASSIC_BED, DARK_OAK_BROWN_CLASSIC_BED, DARK_OAK_WHITE_CLASSIC_BED, WARPED_RED_CLASSIC_BED, WARPED_ORANGE_CLASSIC_BED, WARPED_YELLOW_CLASSIC_BED, WARPED_GREEN_CLASSIC_BED, WARPED_LIME_CLASSIC_BED, WARPED_CYAN_CLASSIC_BED, WARPED_BLUE_CLASSIC_BED, WARPED_LIGHT_BLUE_CLASSIC_BED, WARPED_LIGHT_GRAY_CLASSIC_BED, WARPED_GRAY_CLASSIC_BED, WARPED_BLACK_CLASSIC_BED, WARPED_PURPLE_CLASSIC_BED, WARPED_MAGENTA_CLASSIC_BED, WARPED_PINK_CLASSIC_BED, WARPED_BROWN_CLASSIC_BED, WARPED_WHITE_CLASSIC_BED, CRIMSON_RED_CLASSIC_BED, CRIMSON_ORANGE_CLASSIC_BED, CRIMSON_YELLOW_CLASSIC_BED, CRIMSON_GREEN_CLASSIC_BED, CRIMSON_LIME_CLASSIC_BED, CRIMSON_CYAN_CLASSIC_BED, CRIMSON_BLUE_CLASSIC_BED, CRIMSON_LIGHT_BLUE_CLASSIC_BED, CRIMSON_LIGHT_GRAY_CLASSIC_BED, CRIMSON_GRAY_CLASSIC_BED, CRIMSON_BLACK_CLASSIC_BED, CRIMSON_PURPLE_CLASSIC_BED, CRIMSON_MAGENTA_CLASSIC_BED, CRIMSON_PINK_CLASSIC_BED, CRIMSON_BROWN_CLASSIC_BED, CRIMSON_WHITE_CLASSIC_BED};
    }
    public static Stream<Block> streamBlocks() {
        return BLOCKS.stream();
    }
}