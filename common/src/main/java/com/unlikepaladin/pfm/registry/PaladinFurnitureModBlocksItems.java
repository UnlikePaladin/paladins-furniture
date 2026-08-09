package com.unlikepaladin.pfm.registry;


import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.data.PFMBlockSettings;
import com.unlikepaladin.pfm.data.ToolType;
import com.unlikepaladin.pfm.ducks.AbstractBlock$SettingsExtension;
import com.unlikepaladin.pfm.items.DyeKit;
import com.unlikepaladin.pfm.registry.dynamic.FurnitureEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class PaladinFurnitureModBlocksItems {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final HashMap<Class<? extends Block>, FurnitureEntry<?>> furnitureEntryMap = new LinkedHashMap<>();
    public static Set<BlockState> originalHomePOIBedStates = new HashSet<>();

    public static final FreezerBlock WHITE_FREEZER = new FreezerBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.SNOW).noOcclusion().strength(5.0f, 3.5f).sound(SoundType.STONE), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final FridgeBlock WHITE_FRIDGE = new FridgeBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(WHITE_FREEZER).noOcclusion(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final FreezerBlock GRAY_FREEZER = new FreezerBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).noOcclusion().strength(5.0f,3.5f).sound(SoundType.STONE), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
    public static final FridgeBlock GRAY_FRIDGE = new FridgeBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(GRAY_FREEZER).noOcclusion(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER);
    public static final FreezerBlock IRON_FREEZER = new IronFreezerBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).noOcclusion().strength(5.0f,3.5f).sound(SoundType.METAL), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE);
    public static final FridgeBlock IRON_FRIDGE = new IronFridgeBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(IRON_FREEZER).noOcclusion(), ToolType.PICKAXE), () -> PaladinFurnitureModBlocksItems.IRON_FREEZER);
    public static final FridgeBlock XBOX_FRIDGE = new XboxFridgeBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).strength(5.0f, 1200.0f).sound(SoundType.STONE).noOcclusion(), ToolType.PICKAXE), null);

    public static final StoveBlock WHITE_STOVE = new StoveBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(WHITE_FREEZER), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock WHITE_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(WHITE_FREEZER).noOcclusion(), ToolType.PICKAXE));
    public static final StoveBlock GRAY_STOVE = new StoveBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(GRAY_FREEZER), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock GRAY_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(GRAY_FREEZER).noOcclusion(), ToolType.PICKAXE));
    public static final StoveBlock IRON_STOVE = new IronStoveBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));
    public static final KitchenRangeHoodBlock IRON_OVEN_RANGEHOOD = new KitchenRangeHoodBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), ToolType.PICKAXE));
    public static final MicrowaveBlock IRON_MICROWAVE = new MicrowaveBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), ToolType.PICKAXE));
    public static final TrashcanBlock TRASHCAN = new TrashcanBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), ToolType.PICKAXE));
    public static final InnerTrashcanBlock MESH_TRASHCAN = new InnerTrashcanBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.CHAIN).noOcclusion(), ToolType.PICKAXE));

    public static final Item DYE_KIT_YELLOW = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.YELLOW);
    public static final Item DYE_KIT_BLUE = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.BLUE);
    public static final Item DYE_KIT_WHITE = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.WHITE);
    public static final Item DYE_KIT_PINK = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.PINK);
    public static final Item DYE_KIT_PURPLE = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.PURPLE);
    public static final Item DYE_KIT_GREEN = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.GREEN);
    public static final Item DYE_KIT_LIGHT_BLUE = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.LIGHT_BLUE);
    public static final Item DYE_KIT_LIGHT_GRAY = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.LIGHT_GRAY);
    public static final Item DYE_KIT_LIME = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.LIME);
    public static final Item DYE_KIT_ORANGE = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.ORANGE);
    public static final Item DYE_KIT_BLACK = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.BLACK);
    public static final Item DYE_KIT_BROWN = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.BROWN);
    public static final Item DYE_KIT_MAGENTA = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.MAGENTA);
    public static final Item DYE_KIT_RED = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.RED);
    public static final Item DYE_KIT_CYAN = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.CYAN);
    public static final Item DYE_KIT_GRAY = new DyeKit(new Item.Properties().stacksTo(16), DyeColor.GRAY);

    public static final Block RAW_CONCRETE = new Block(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE).sound(SoundType.STONE));
    public static final Block RAW_CONCRETE_POWDER = new ConcretePowderBlock(RAW_CONCRETE, BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE_POWDER).sound(SoundType.SAND));
    public static final Block LEATHER_BLOCK = new Block(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).sound(SoundType.WOOL)).pfm$setMapColor(MaterialColor.COLOR_ORANGE));

    public static final Block IRON_CHAIN = new ChainBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).sound(SoundType.METAL), ToolType.PICKAXE));
    public static final PendantBlock GRAY_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final PendantBlock WHITE_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final PendantBlock GLASS_MODERN_PENDANT = new PendantBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));
    public static final SimpleLightBlock SIMPLE_LIGHT = new SimpleLightBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE).noOcclusion().lightLevel(createLightLevelFromLitBlockState(15)), ToolType.PICKAXE));

    public static final LightSwitchBlock LIGHT_SWITCH = new LightSwitchBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE).sound(SoundType.STONE).noOcclusion(), ToolType.PICKAXE));
    public static Item LIGHT_SWITCH_ITEM;
    public static Item FURNITURE_BOOK;
    public static final Block BASIC_LAMP = new BasicLampBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).lightLevel(createLightLevelFromLitBlockState(15)), ToolType.AXE));
    public static final Block TOASTER_BLOCK = new PFMToasterBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(IRON_STOVE), ToolType.PICKAXE));
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.getValue(BlockStateProperties.LIT) ? litLevel : 0;
    }


    public static final KitchenStovetopBlock KITCHEN_STOVETOP = new KitchenStovetopBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), ToolType.PICKAXE));

    public static final WorkingTableBlock WORKING_TABLE = new WorkingTableBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD), ToolType.AXE));
    public static final PlateBlock BASIC_PLATE = new PlateBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.WHITE_CONCRETE).noOcclusion(), ToolType.PICKAXE));
    public static final CutleryBlock BASIC_CUTLERY = new CutleryBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion(), ToolType.PICKAXE));

    public static final BasicToiletBlock BASIC_TOILET = new BasicToiletBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.SMOOTH_QUARTZ).noOcclusion(), ToolType.PICKAXE));
    public static final WallToiletPaperBlock WALL_TOILET_PAPER = new WallToiletPaperBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.QUARTZ).noOcclusion());
    public static final BasicBathtubBlock BASIC_BATHTUB = new BasicBathtubBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.SMOOTH_QUARTZ).noOcclusion(), ToolType.PICKAXE), BathtubBehavior.TUB_BEHAVIOR);


    public static Block WHITE_MIRROR;
    public static Block GRAY_MIRROR;

    public static final BasicShowerHeadBlock BASIC_SHOWER_HEAD = new BasicShowerHeadBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.SMOOTH_QUARTZ).noOcclusion(), ToolType.PICKAXE));
    public static final BasicShowerHandleBlock BASIC_SHOWER_HANDLE = new BasicShowerHandleBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.SMOOTH_QUARTZ).noOcclusion(), ToolType.PICKAXE));
    public static Item BASIC_SHOWER_HANDLE_ITEM;
    public static Item BASIC_LAMP_ITEM;
    public static Item OFFICE_CHAIR_ITEM;

    public static final BasicSinkBlock BASIC_SINK = new BasicSinkBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(Blocks.SMOOTH_QUARTZ).noOcclusion(), ToolType.PICKAXE), SinkBehavior.WATER_SINK_BEHAVIOR);
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