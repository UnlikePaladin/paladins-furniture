package com.unlikepaladin.pfm.registry.dynamic;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.items.*;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.Tuple;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class LateBlockRegistry {

    @ExpectPlatform
    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> block, boolean registerItem, Tuple<String, CreativeModeTab> group)
    {
        return block.get();
    }

    @ExpectPlatform
    public static <T extends Block> T registerLateBlockClassic(String blockName, T block, boolean registerItem, Tuple<String, CreativeModeTab> group)
    {
        return block;
    }

    @ExpectPlatform
    public static void registerLateItem(String itemName, Supplier<Item> item, Tuple<String, CreativeModeTab> group) {
        return;
    }

    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> blockSupplier, int count, Tuple<String, CreativeModeTab> group) {
        T block = registerLateBlock(blockName, blockSupplier, false, group);
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerLateItem(blockName, () -> new BlockItem(block, new Item.Properties().stacksTo(count)), group);
        return block;
    }

    public static void registerBlocks() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::createBlocks);
        PaladinFurnitureMod.furnitureEntryMap.put(HerringbonePlankBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_herringbone_planks", () -> new HerringbonePlankBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).sound(SoundType.WOOD).color(color).noOcclusion()), true, PaladinFurnitureMod.BUILDING_BLOCKS), true);
            }
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicChairBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair", () -> new BasicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair", () -> new BasicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair", () -> new BasicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureMod.furnitureEntryMap.put(DinnerChairBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicChairBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicChairDyeableBlock.class, new FurnitureEntry<>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock("oak_chair_classic_" + color.getName(), () -> new ClassicChairDyeableBlock(color, BlockBehaviour.Properties.copy(PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).allBlocks.get(0))), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});

        PaladinFurnitureMod.furnitureEntryMap.put(ModernChairBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(FroggyChairBlock.class, new FurnitureEntry<>() {{
            this.addBlock(registerLateBlock("froggy_chair", () -> new FroggyChairBlock(BlockBehaviour.Properties.of(Material.METAL).strength(9.0f).explosionResistance(8.0f).noOcclusion().requiresCorrectToolForDrops().color(MaterialColor.COLOR_GREEN)), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_pink", () -> new FroggyChairBlock(BlockBehaviour.Properties.copy(this.allBlocks.get(0)).color(MaterialColor.COLOR_PINK)), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_light_blue", () -> new FroggyChairBlock(BlockBehaviour.Properties.copy(this.allBlocks.get(0)).color(MaterialColor.COLOR_LIGHT_BLUE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_blue", () -> new FroggyChairBlock(BlockBehaviour.Properties.copy(this.allBlocks.get(0)).color(MaterialColor.COLOR_BLUE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_orange", () -> new FroggyChairBlock(BlockBehaviour.Properties.copy(this.allBlocks.get(0)).color(MaterialColor.COLOR_ORANGE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_yellow", () -> new FroggyChairBlock(BlockBehaviour.Properties.copy(this.allBlocks.get(0)).color(MaterialColor.COLOR_YELLOW)), true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(SimpleSofaBlock.class, new FurnitureEntry<>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_simple_sofa", () -> new SimpleSofaBlock(color, BlockBehaviour.Properties.of(Material.WOOL).strength(2.0f).explosionResistance(2.0f).noOcclusion().sound(SoundType.WOOL).color(color.getMaterialColor())), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(ArmChairBlock.class, new FurnitureEntry<>() {{
            this.addBlock(registerLateBlock("arm_chair_leather", () -> new ArmChairBlock(BlockBehaviour.Properties.of(Material.CLAY).strength(2.0f).explosionResistance(2.0f).noOcclusion().sound(SoundType.WOOL)), true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(ArmChairColoredBlock.class, new FurnitureEntry<>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_arm_chair", () -> new ArmChairColoredBlock(color, BlockBehaviour.Properties.of(Material.WOOL).strength(2.0f).explosionResistance(2.0f).noOcclusion().sound(SoundType.WOOL).color(color.getMaterialColor())), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});

        PaladinFurnitureMod.furnitureEntryMap.put(BasicTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureMod.furnitureEntryMap.put(LogTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_" + postfix, () -> new LogTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_" + postfix, () -> new LogTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_natural", () -> new LogTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureMod.furnitureEntryMap.put(RawLogTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_raw_table_" + postfix, () -> new RawLogTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" +variant.getSerializedName()+"_raw_table_" + postfix, () -> new RawLogTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(DinnerTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ModernDinnerTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicCoffeeTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ModernCoffeeTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicCoffeeTableBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicDeskBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicDeskCabinetBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicDeskBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicDeskCabinetBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM = OfficeChairItem.getItemFactory(new Item.Properties());
        LateBlockRegistry.registerLateItem("office_chair", () -> PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM, PaladinFurnitureMod.FURNITURE_GROUP);
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicNightstandBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(SimpleBedBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                int i = 0;for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                        break;
                    String blockName = variant.getSerializedName() + "_" + color.getName() +  "_simple_bed";
                    SimpleBedBlock block = LateBlockRegistry.registerLateBlock(blockName, () -> new SimpleBedBlock(color, BlockBehaviour.Properties.of(variant.getVanillaMaterial(), state -> state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL).sound(variant.getBaseBlock().getSoundType(variant.getBaseBlock().defaultBlockState())).requiredFeatures(variant.getFeatureList().toArray(new FeatureFlag[0])).strength(0.2f).noOcclusion()), false, PaladinFurnitureMod.FURNITURE_GROUP);
                    LateBlockRegistry.registerLateItem(blockName, () -> BedBlockItem.getItemFactory(block, new Item.Properties().stacksTo(1)), PaladinFurnitureMod.FURNITURE_GROUP);
                    this.addBlock(variant, block, true);
                    PaladinFurnitureModBlocksItems.beds.add(block);
                    i++;
                }
            }
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicBedBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                int i = 0;for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                        break;
                    String blockName = variant.getSerializedName() + "_" + color.getName() +  "_classic_bed";
                    ClassicBedBlock block = LateBlockRegistry.registerLateBlock(blockName, () -> new ClassicBedBlock(color, BlockBehaviour.Properties.of(variant.getVanillaMaterial(), state -> state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL).sound(variant.getBaseBlock().getSoundType(variant.getBaseBlock().defaultBlockState())).strength(0.2f).noOcclusion().requiredFeatures(variant.getFeatureList().toArray(new FeatureFlag[0]))), false, PaladinFurnitureMod.FURNITURE_GROUP);
                    LateBlockRegistry.registerLateItem(blockName, () -> BedBlockItem.getItemFactory(block, new Item.Properties().stacksTo(1)), PaladinFurnitureMod.FURNITURE_GROUP);
                    this.addBlock(variant, block, true);
                    PaladinFurnitureModBlocksItems.beds.add(block);
                    i++;
                }
            }
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(SimpleBunkLadderBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_simple_bunk_ladder", () -> new SimpleBunkLadderBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(LogStoolBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName() + "_" + postfix + "_stool", () -> new LogStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(SimpleStoolBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(ClassicStoolBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureMod.furnitureEntryMap.put(ModernStoolBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
            for (ExtraStoolVariant variant : ExtraStoolVariant.values()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
        }});

        PaladinFurnitureMod.furnitureEntryMap.put(KitchenDrawerBlock.class, new FurnitureEntry<KitchenDrawerBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenCabinetBlock.class, new FurnitureEntry<KitchenCabinetBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenSinkBlock.class, new FurnitureEntry<KitchenSinkBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenCounterOvenBlock.class, new FurnitureEntry<KitchenCounterOvenBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenWallCounterBlock.class, new FurnitureEntry<KitchenWallCounterBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenWallDrawerBlock.class, new FurnitureEntry<KitchenWallDrawerBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenWallDrawerSmallBlock.class, new FurnitureEntry<KitchenWallDrawerSmallBlock>());
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenCounterBlock.class, new FurnitureEntry<>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion(), LayeredCauldronBlock.RAIN, SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped()) {
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion(), LayeredCauldronBlock.RAIN, SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                }
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {if (variant.identifier.getPath().equals("quartz"))
                    continue;
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion(), LayeredCauldronBlock.RAIN, SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
            for (ExtraCounterVariant variant : ExtraCounterVariant.values()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion(), LayeredCauldronBlock.RAIN, SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(BlockBehaviour.Properties.copy(variant.getBaseBlock()).color(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureMod.furnitureEntryMap.put(FreezerBlock.class, new FurnitureEntry<FreezerBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "white_freezer", () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "gray_freezer", () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "iron_freezer", () -> PaladinFurnitureModBlocksItems.IRON_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});

        PaladinFurnitureMod.furnitureEntryMap.put(FridgeBlock.class, new FurnitureEntry<FridgeBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "white_fridge", () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "gray_fridge", () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_fridge", () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "xbox_fridge", () -> PaladinFurnitureModBlocksItems.XBOX_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(MicrowaveBlock.class, new FurnitureEntry<MicrowaveBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "iron_microwave", () -> PaladinFurnitureModBlocksItems.IRON_MICROWAVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});

        PaladinFurnitureMod.furnitureEntryMap.put(KitchenRangeHoodBlock.class, new FurnitureEntry<KitchenRangeHoodBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_oven_range_hood", () -> PaladinFurnitureModBlocksItems.WHITE_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_oven_range_hood", () -> PaladinFurnitureModBlocksItems.GRAY_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_oven_range_hood", () -> PaladinFurnitureModBlocksItems.IRON_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(StoveBlock.class, new FurnitureEntry<StoveBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_stove", () -> PaladinFurnitureModBlocksItems.WHITE_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_stove", () -> PaladinFurnitureModBlocksItems.GRAY_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_stove",() -> PaladinFurnitureModBlocksItems.IRON_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(KitchenStovetopBlock.class, new FurnitureEntry<KitchenStovetopBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "kitchen_stovetop",() -> PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(PFMToasterBlock.class, new FurnitureEntry<>() {{
            addBlock(registerLateBlockClassic("iron_toaster", PaladinFurnitureModBlocksItems.TOASTER_BLOCK, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(PlateBlock.class, new FurnitureEntry<PlateBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "basic_plate",() -> PaladinFurnitureModBlocksItems.BASIC_PLATE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(CutleryBlock.class, new FurnitureEntry<CutleryBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_cutlery",() -> PaladinFurnitureModBlocksItems.BASIC_CUTLERY, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(PendantBlock.class, new FurnitureEntry<PendantBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_modern_pendant",() -> PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_modern_pendant",() -> PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "glass_modern_pendant",() -> PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(SimpleLightBlock.class, new FurnitureEntry<SimpleLightBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "simple_light",() -> PaladinFurnitureModBlocksItems.SIMPLE_LIGHT, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM = new LightSwitchItem(PaladinFurnitureModBlocksItems.LIGHT_SWITCH, new Item.Properties());
        PaladinFurnitureMod.furnitureEntryMap.put(LightSwitchBlock.class, new FurnitureEntry<LightSwitchBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "light_switch",() -> PaladinFurnitureModBlocksItems.LIGHT_SWITCH, false, PaladinFurnitureMod.FURNITURE_GROUP));
            PaladinFurnitureModBlocksItems.BLOCKS.add(PaladinFurnitureModBlocksItems.LIGHT_SWITCH);
            LateBlockRegistry.registerLateItem("light_switch",() -> PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM, PaladinFurnitureMod.FURNITURE_GROUP);
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicToiletBlock.class, new FurnitureEntry<BasicToiletBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_toilet",() -> PaladinFurnitureModBlocksItems.BASIC_TOILET, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(WallToiletPaperBlock.class, new FurnitureEntry<WallToiletPaperBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "wall_toilet_paper",() -> PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicSinkBlock.class, new FurnitureEntry<BasicSinkBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "basic_sink",() -> PaladinFurnitureModBlocksItems.BASIC_SINK, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicBathtubBlock.class, new FurnitureEntry<BasicBathtubBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_bathtub",() -> PaladinFurnitureModBlocksItems.BASIC_BATHTUB, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(BasicShowerHeadBlock.class, new FurnitureEntry<BasicShowerHeadBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_shower_head",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM = new ShowerHandleItem(() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, new Item.Properties());
        PaladinFurnitureMod.furnitureEntryMap.put(BasicShowerHandleBlock.class, new FurnitureEntry<BasicShowerHandleBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_shower_handle",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, false, PaladinFurnitureMod.FURNITURE_GROUP));
            LateBlockRegistry.registerLateItem( "basic_shower_handle",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM, PaladinFurnitureMod.FURNITURE_GROUP);
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(TrashcanBlock.class, new FurnitureEntry<TrashcanBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "trashcan",() -> PaladinFurnitureModBlocksItems.TRASHCAN, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(InnerTrashcanBlock.class, new FurnitureEntry<InnerTrashcanBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "mesh_trashcan",() -> PaladinFurnitureModBlocksItems.MESH_TRASHCAN, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureMod.furnitureEntryMap.put(ShowerTowelBlock.class, new FurnitureEntry<ShowerTowelBlock>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_shower_towel", () -> new ShowerTowelBlock(color, BlockBehaviour.Properties.of(Material.WOOL).strength(2.0f).explosionResistance(2.0f).noOcclusion().sound(SoundType.WOOL).color(color.getMaterialColor())), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});
        if (!BlockItemRegistry.isModLoaded("imm_ptl_core")) {
            PaladinFurnitureModBlocksItems.WHITE_MIRROR = new MirrorBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW).noOcclusion());
            PaladinFurnitureModBlocksItems.GRAY_MIRROR = new MirrorBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).noOcclusion());
        }
        registerLateBlock("white_mirror",() -> PaladinFurnitureModBlocksItems.WHITE_MIRROR, true, PaladinFurnitureMod.FURNITURE_GROUP);
        registerLateBlock("gray_mirror",() -> PaladinFurnitureModBlocksItems.GRAY_MIRROR, true, PaladinFurnitureMod.FURNITURE_GROUP);
        PaladinFurnitureMod.furnitureEntryMap.put(BasicLampBlock.class, new FurnitureEntry<>() {{
            Block lampBlock = registerLateBlockClassic("basic_lamp", PaladinFurnitureModBlocksItems.BASIC_LAMP, false, PaladinFurnitureMod.FURNITURE_GROUP);
            for (WoodVariant variant : WoodVariantRegistry.getVariants())
                addBlock(variant, lampBlock, true);
        }});
        PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM = LampItem.getItemFactory(PaladinFurnitureModBlocksItems.BASIC_LAMP, new Item.Properties());
        LateBlockRegistry.registerLateItem( "basic_lamp", () -> PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM, PaladinFurnitureMod.FURNITURE_GROUP);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerBlocks);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerItems);
    }
}
