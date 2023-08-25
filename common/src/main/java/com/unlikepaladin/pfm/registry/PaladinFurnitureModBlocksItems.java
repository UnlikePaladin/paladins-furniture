package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.data.PFMBlockSettings;
import com.unlikepaladin.pfm.data.ToolType;
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

    public static final FreezerBlock WHITE_FREEZER = new FreezerBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.WHITE).strength(5.0f, 3.5f).sounds(BlockSoundGroup.STONE), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final FridgeBlock WHITE_FRIDGE = new FridgeBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final FreezerBlock GRAY_FREEZER = new FreezerBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.GRAY).strength(5.0f,3.5f).sounds(BlockSoundGroup.STONE), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
    public static final FridgeBlock GRAY_FRIDGE = new FridgeBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER);
    public static final FreezerBlock IRON_FREEZER = new IronFreezerBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.IRON_GRAY).strength(5.0f,3.5f).sounds(BlockSoundGroup.METAL), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE);
    public static final FridgeBlock IRON_FRIDGE = new IronFridgeBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(IRON_FREEZER).nonOpaque(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.IRON_FREEZER);
    public static final FridgeBlock XBOX_FRIDGE = new XboxFridgeBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.BLACK).strength(5.0f, 3.5f).sounds(BlockSoundGroup.STONE).nonOpaque(), ToolType.PICKAXE), null);

    public static final StoveBlock WHITE_STOVE = new StoveBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(WHITE_FREEZER), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock WHITE_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(WHITE_FREEZER).nonOpaque(), ToolType.PICKAXE));
    public static final StoveBlock GRAY_STOVE = new StoveBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(GRAY_FREEZER), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock GRAY_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(GRAY_FREEZER).nonOpaque(), ToolType.PICKAXE));
    public static final StoveBlock IRON_STOVE = new IronStoveBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock IRON_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque(), ToolType.PICKAXE));
    public static final MicrowaveBlock IRON_MICROWAVE = new MicrowaveBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));
    public static final TrashcanBlock TRASHCAN = new TrashcanBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));
    public static final InnerTrashcanBlock MESH_TRASHCAN = new InnerTrashcanBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque(), ToolType.PICKAXE));

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

    public static final Block ACACIA_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block SPRUCE_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block OAK_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block DARK_OAK_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block JUNGLE_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.JUNGLE_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block BIRCH_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.BIRCH_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block WARPED_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.WARPED_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final Block CRIMSON_HERRINGBONE_PLANKS = new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS).sounds(BlockSoundGroup.WOOD), ToolType.AXE));


    public static final Block RAW_CONCRETE = new Block(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE).sounds(BlockSoundGroup.STONE), ToolType.PICKAXE));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE_POWDER).sounds(BlockSoundGroup.SAND), ToolType.SHOVEL));
    public static final Block LEATHER_BLOCK = new Block(AbstractBlock.Settings.copy(Blocks.ORANGE_WOOL).sounds(BlockSoundGroup.WOOL));

    public static final Block IRON_CHAIN = new ChainBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BARS).sounds(BlockSoundGroup.METAL), ToolType.PICKAXE));
    public static final PendantBlock GRAY_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final PendantBlock WHITE_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final PendantBlock GLASS_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.OFF_WHITE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final SimpleLightBlock SIMPLE_LIGHT = new SimpleLightBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));

    public static final LightSwitchBlock LIGHT_SWITCH = new LightSwitchBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).sounds(BlockSoundGroup.STONE).nonOpaque(), ToolType.PICKAXE));
    public static Item LIGHT_SWITCH_ITEM;
    public static Item FURNITURE_BOOK;
    public static final Block BASIC_LAMP = new BasicLampBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).luminance(createLightLevelFromLitBlockState(15)), ToolType.AXE));
    public static final Block TOASTER_BLOCK = new PFMToasterBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(IRON_STOVE), ToolType.PICKAXE));
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }


    public static final KitchenStovetopBlock KITCHEN_STOVETOP = new KitchenStovetopBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));

    public static final WorkingTableBlock WORKING_TABLE = new WorkingTableBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).sounds(BlockSoundGroup.WOOD), ToolType.AXE));
    public static final PlateBlock BASIC_PLATE = new PlateBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).nonOpaque(), ToolType.PICKAXE));
    public static final CutleryBlock BASIC_CUTLERY = new CutleryBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE).nonOpaque(), ToolType.PICKAXE));

    public static final BasicToiletBlock BASIC_TOILET = new BasicToiletBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), ToolType.PICKAXE));
    public static final WallToiletPaperBlock WALL_TOILET_PAPER = new WallToiletPaperBlock(AbstractBlock.Settings.of(Material.WOOL, MapColor.OFF_WHITE).nonOpaque());
    public static final BasicBathtubBlock BASIC_BATHTUB = new BasicBathtubBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), ToolType.PICKAXE), BathtubBehavior.TUB_BEHAVIOR);


    public static Block WHITE_MIRROR;
    public static Block GRAY_MIRROR;

    public static final BasicShowerHeadBlock BASIC_SHOWER_HEAD = new BasicShowerHeadBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), ToolType.PICKAXE));
    public static final BasicShowerHandleBlock BASIC_SHOWER_HANDLE = new BasicShowerHandleBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), ToolType.PICKAXE));
    public static Item BASIC_SHOWER_HANDLE_ITEM;
    public static Item BASIC_LAMP_ITEM;

    public static final BasicSinkBlock BASIC_SINK = new BasicSinkBlock(PFMBlockSettings.breaksWithTool(AbstractBlock.Settings.copy(Blocks.SMOOTH_QUARTZ).nonOpaque(), ToolType.PICKAXE), SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final List<BedBlock> beds = new ArrayList<>();

    public static Block[] getBeds() {
        List<Block> blocks = new ArrayList<>(furnitureEntryMap.get(SimpleBedBlock.class).getAllBlocks());
        blocks.addAll(furnitureEntryMap.get(ClassicBedBlock.class).getAllBlocks());
        return blocks.toArray(new Block[0]);
    }
    public static Stream<Block> streamBlocks() {
        return BLOCKS.stream();
    }
}