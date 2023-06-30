package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.items.DyeKit;
import com.unlikepaladin.pfm.registry.dynamic.FurnitureEntry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class PaladinFurnitureModBlocksItems {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final HashMap<Class<? extends Block>, FurnitureEntry<?>> furnitureEntryMap = new LinkedHashMap<>();
    public static Set<BlockState> originalHomePOIBedStates = new HashSet<>();

    public static final FreezerBlock WHITE_FREEZER = new FreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE).mapColor(MapColor.WHITE), () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final FridgeBlock WHITE_FRIDGE = new FridgeBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final FreezerBlock GRAY_FREEZER = new FreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.STONE).mapColor(MapColor.GRAY), () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
    public static final FridgeBlock GRAY_FRIDGE = new FridgeBlock(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER);
    public static final FreezerBlock IRON_FREEZER = new IronFreezerBlock(AbstractBlock.Settings.of(Material.METAL).resistance(3.5f).strength(5.0f).sounds(BlockSoundGroup.METAL).mapColor(MapColor.IRON_GRAY), () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE);
    public static final FridgeBlock IRON_FRIDGE = new IronFridgeBlock(AbstractBlock.Settings.copy(IRON_FREEZER).nonOpaque(), () -> PaladinFurnitureModBlocksItems.IRON_FREEZER);
    public static final FridgeBlock XBOX_FRIDGE = new XboxFridgeBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque().mapColor(MapColor.BLACK), null);

    public static final StoveBlock WHITE_STOVE = new StoveBlock(AbstractBlock.Settings.copy(WHITE_FREEZER));
    public static final KitchenRangeHoodBlock WHITE_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque());
    public static final StoveBlock GRAY_STOVE = new StoveBlock(AbstractBlock.Settings.copy(GRAY_FREEZER));
    public static final KitchenRangeHoodBlock GRAY_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque());
    public static final StoveBlock IRON_STOVE = new IronStoveBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final KitchenRangeHoodBlock IRON_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque());
    public static final MicrowaveBlock IRON_MICROWAVE = new MicrowaveBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final TrashcanBlock TRASHCAN = new TrashcanBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
    public static final InnerTrashcanBlock MESH_TRASHCAN = new InnerTrashcanBlock(AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque());

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
    public static final PendantBlock GRAY_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.GRAY));
    public static final PendantBlock WHITE_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.WHITE));
    public static final PendantBlock GLASS_MODERN_PENDANT = new PendantBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.OFF_WHITE));
    public static final SimpleLightBlock SIMPLE_LIGHT = new SimpleLightBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)).mapColor(MapColor.LIGHT_GRAY));

    public static final LightSwitchBlock LIGHT_SWITCH = new LightSwitchBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).sounds(BlockSoundGroup.STONE).nonOpaque().mapColor(MapColor.WHITE));
    public static Item LIGHT_SWITCH_ITEM;
    public static Item FURNITURE_BOOK;

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }


    public static final KitchenStovetopBlock KITCHEN_STOVETOP = new KitchenStovetopBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));

    public static final WorkingTableBlock WORKING_TABLE = new WorkingTableBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(BlockSoundGroup.WOOD));
    public static final PlateBlock BASIC_PLATE = new PlateBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).nonOpaque());
    public static final CutleryBlock BASIC_CUTLERY = new CutleryBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE).nonOpaque());

    public static final BasicToiletBlock BASIC_TOILET = new BasicToiletBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static final WallToiletPaperBlock WALL_TOILET_PAPER = new WallToiletPaperBlock(AbstractBlock.Settings.of(Material.WOOL, MapColor.OFF_WHITE).nonOpaque());
    public static final BasicBathtubBlock BASIC_BATHTUB = new BasicBathtubBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), BathtubBehavior.TUB_BEHAVIOR, LeveledCauldronBlock.RAIN_PREDICATE);


    public static Block WHITE_MIRROR;
    public static Block GRAY_MIRROR;

    public static final BasicShowerHeadBlock BASIC_SHOWER_HEAD = new BasicShowerHeadBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static final BasicShowerHandleBlock BASIC_SHOWER_HANDLE = new BasicShowerHandleBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque());
    public static Item BASIC_SHOWER_HANDLE_ITEM;
    public static final BasicSinkBlock BASIC_SINK = new BasicSinkBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), LeveledCauldronBlock.RAIN_PREDICATE, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final List<BedBlock> beds = new ArrayList<>();

    //TODO: Fix light switch crashing with Create :pain:

    public static Block[] getBeds() {
        List<Block> blocks = new ArrayList<>(furnitureEntryMap.get(SimpleBedBlock.class).getAllBlocks());
        blocks.addAll(furnitureEntryMap.get(ClassicBedBlock.class).getAllBlocks());
        return blocks.toArray(new Block[0]);
    }
    public static Stream<Block> streamBlocks() {
        return BLOCKS.stream();
    }
}