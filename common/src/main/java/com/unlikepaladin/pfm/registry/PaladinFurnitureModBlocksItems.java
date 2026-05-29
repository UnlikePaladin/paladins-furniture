package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.items.DyeKit;
import com.unlikepaladin.pfm.registry.dynamic.FurnitureEntry;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.DyeColor;

import net.minecraft.world.level.material.MapColor;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class PaladinFurnitureModBlocksItems {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final Map<Tuple<String, CreativeModeTab>, Set<Item>> ITEM_GROUP_LIST_MAP = new LinkedHashMap<>();
    public static Set<BlockState> originalHomePOIBedStates = new HashSet<>();
    public static final FreezerBlock WHITE_FREEZER = new FreezerBlock(BlockBehaviour.Properties.of().explosionResistance(3.5f).strength(5.0f).sound(SoundType.STONE).mapColor(MapColor.SNOW).setId(LateBlockRegistry.getBlockRegistryKey("white_freezer")), () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final FridgeBlock WHITE_FRIDGE = new FridgeBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_FREEZER).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("white_fridge")), () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final FreezerBlock GRAY_FREEZER = new FreezerBlock(BlockBehaviour.Properties.of().explosionResistance(3.5f).strength(5.0f).sound(SoundType.STONE).mapColor(MapColor.COLOR_GRAY).setId(LateBlockRegistry.getBlockRegistryKey("gray_freezer")), () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
    public static final FridgeBlock GRAY_FRIDGE = new FridgeBlock(BlockBehaviour.Properties.ofFullCopy(GRAY_FREEZER).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("gray_fridge")), () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER);
    public static final FreezerBlock IRON_FREEZER = new IronFreezerBlock(BlockBehaviour.Properties.of().explosionResistance(3.5f).strength(5.0f).sound(SoundType.METAL).mapColor(MapColor.METAL).setId(LateBlockRegistry.getBlockRegistryKey("iron_freezer")), () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE);
    public static final FridgeBlock IRON_FRIDGE = new IronFridgeBlock(BlockBehaviour.Properties.ofFullCopy(IRON_FREEZER).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("iron_fridge")), () -> PaladinFurnitureModBlocksItems.IRON_FREEZER);
    public static final FridgeBlock XBOX_FRIDGE = new XboxFridgeBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_FREEZER).explosionResistance(1200.0F).noOcclusion().mapColor(MapColor.COLOR_BLACK).setId(LateBlockRegistry.getBlockRegistryKey("xbox_fridge")), null);

    public static final StoveBlock WHITE_STOVE = new StoveBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_FREEZER).setId(LateBlockRegistry.getBlockRegistryKey("white_stove")));
    public static final KitchenRangeHoodBlock WHITE_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_FREEZER).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("white_oven_range_hood")));
    public static final StoveBlock GRAY_STOVE = new StoveBlock(BlockBehaviour.Properties.ofFullCopy(GRAY_FREEZER).setId(LateBlockRegistry.getBlockRegistryKey("gray_stove")));
    public static final KitchenRangeHoodBlock GRAY_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(BlockBehaviour.Properties.ofFullCopy(GRAY_FREEZER).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("gray_oven_range_hood")));
    public static final StoveBlock IRON_STOVE = new IronStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(LateBlockRegistry.getBlockRegistryKey("iron_stove")));
    public static final KitchenRangeHoodBlock IRON_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("iron_oven_range_hood")));
    public static final MicrowaveBlock IRON_MICROWAVE = new MicrowaveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(LateBlockRegistry.getBlockRegistryKey("iron_microwave")));
    public static final TrashcanBlock TRASHCAN = new TrashcanBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(LateBlockRegistry.getBlockRegistryKey("trashcan")));
    public static final InnerTrashcanBlock MESH_TRASHCAN = new InnerTrashcanBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("mesh_trashcan")));

    public static final Item DYE_KIT_YELLOW = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_yellow")), DyeColor.YELLOW);
    public static final Item DYE_KIT_BLUE = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_blue")), DyeColor.BLUE);
    public static final Item DYE_KIT_WHITE = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_white")), DyeColor.WHITE);
    public static final Item DYE_KIT_PINK = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_pink")), DyeColor.PINK);
    public static final Item DYE_KIT_PURPLE = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_purple")), DyeColor.PURPLE);
    public static final Item DYE_KIT_GREEN = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_green")), DyeColor.GREEN);
    public static final Item DYE_KIT_LIGHT_BLUE = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_light_blue")), DyeColor.LIGHT_BLUE);
    public static final Item DYE_KIT_LIGHT_GRAY = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_light_gray")), DyeColor.LIGHT_GRAY);
    public static final Item DYE_KIT_LIME = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_lime")), DyeColor.LIME);
    public static final Item DYE_KIT_ORANGE = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_orange")), DyeColor.ORANGE);
    public static final Item DYE_KIT_BLACK = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_black")), DyeColor.BLACK);
    public static final Item DYE_KIT_BROWN = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_brown")), DyeColor.BROWN);
    public static final Item DYE_KIT_MAGENTA = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_magenta")), DyeColor.MAGENTA);
    public static final Item DYE_KIT_RED = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_red")), DyeColor.RED);
    public static final Item DYE_KIT_CYAN = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_cyan")), DyeColor.CYAN);
    public static final Item DYE_KIT_GRAY = new DyeKit(new Item.Properties().stacksTo(16).setId(LateBlockRegistry.getItemRegistryKey("dye_kit_gray")), DyeColor.GRAY);

    public static final Block RAW_CONCRETE = new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE).sound(SoundType.STONE).setId(LateBlockRegistry.getBlockRegistryKey("raw_concrete")));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE_POWDER).sound(SoundType.SAND).setId(LateBlockRegistry.getBlockRegistryKey("raw_concrete_powder")));
    public static final Block LEATHER_BLOCK = new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).sound(SoundType.WOOL).mapColor(MapColor.COLOR_ORANGE).setId(LateBlockRegistry.getBlockRegistryKey("leather_block")));

    public static final Block IRON_CHAIN = new ChainBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.METAL).setId(LateBlockRegistry.getBlockRegistryKey("iron_chain")));
    public static final PendantBlock GRAY_MODERN_PENDANT = new PendantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)).mapColor(MapColor.COLOR_GRAY).setId(LateBlockRegistry.getBlockRegistryKey("gray_modern_pendant")));
    public static final PendantBlock WHITE_MODERN_PENDANT = new PendantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)).mapColor(MapColor.SNOW).setId(LateBlockRegistry.getBlockRegistryKey("white_modern_pendant")));
    public static final PendantBlock GLASS_MODERN_PENDANT = new PendantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)).mapColor(MapColor.QUARTZ).setId(LateBlockRegistry.getBlockRegistryKey("glass_modern_pendant")));
    public static final SimpleLightBlock SIMPLE_LIGHT = new SimpleLightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)).mapColor(MapColor.COLOR_LIGHT_GRAY).setId(LateBlockRegistry.getBlockRegistryKey("simple_light")));

    public static final LightSwitchBlock LIGHT_SWITCH = new LightSwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE).sound(SoundType.STONE).noOcclusion().mapColor(MapColor.SNOW).setId(LateBlockRegistry.getBlockRegistryKey("light_switch")));
    public static Item LIGHT_SWITCH_ITEM;
    public static Item FURNITURE_BOOK;
    public static final Block BASIC_LAMP = new BasicLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).lightLevel(createLightLevelFromLitBlockState(15)).setId(LateBlockRegistry.getBlockRegistryKey("basic_lamp")));
    public static final Block TOASTER_BLOCK = new PFMToasterBlock(BlockBehaviour.Properties.ofFullCopy(IRON_STOVE).setId(LateBlockRegistry.getBlockRegistryKey("iron_toaster")));
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.getValue(BlockStateProperties.LIT) ? litLevel : 0;
    }

    public static final KitchenStovetopBlock KITCHEN_STOVETOP = new KitchenStovetopBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(LateBlockRegistry.getBlockRegistryKey("kitchen_stovetop")));

    public static final WorkingTableBlock WORKING_TABLE = new WorkingTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD).setId(LateBlockRegistry.getBlockRegistryKey("working_table")));
    public static final PlateBlock BASIC_PLATE = new PlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_plate")));
    public static final CutleryBlock BASIC_CUTLERY = new CutleryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_cutlery")));

    public static final BasicToiletBlock BASIC_TOILET = new BasicToiletBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_toilet")));
    public static final WallToiletPaperBlock WALL_TOILET_PAPER = new WallToiletPaperBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("wall_toilet_paper")));
    public static final BasicBathtubBlock BASIC_BATHTUB = new BasicBathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_bathtub")), BathtubBehavior.TUB_BEHAVIOR, Biome.Precipitation.RAIN);


    public static Block WHITE_MIRROR;
    public static Block GRAY_MIRROR;

    public static final BasicShowerHeadBlock BASIC_SHOWER_HEAD = new BasicShowerHeadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_shower_head")));
    public static final BasicShowerHandleBlock BASIC_SHOWER_HANDLE = new BasicShowerHandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_shower_handle")));
    public static Item BASIC_SHOWER_HANDLE_ITEM;
    public static Item BASIC_LAMP_ITEM;
    public static Item OFFICE_CHAIR_ITEM;

    public static final BasicSinkBlock BASIC_SINK = new BasicSinkBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion().setId(LateBlockRegistry.getBlockRegistryKey("basic_sink")), Biome.Precipitation.RAIN, SinkBehavior.WATER_SINK_BEHAVIOR);
    public static final List<BedBlock> beds = new ArrayList<>();

    public static Block[] getBeds() {
        List<Block> blocks = new ArrayList<>(PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getAllBlocks());
        blocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(ClassicBedBlock.class).getAllBlocks());
        return blocks.toArray(new Block[0]);
    }
    public static Stream<Block> streamBlocks() {
        return BLOCKS.stream();
    }
}