package com.unlikepaladin.pfm.registry.dynamic;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.data.PFMBlockSettings;
import com.unlikepaladin.pfm.data.ToolType;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.ducks.AbstractBlock$SettingsExtension;
import com.unlikepaladin.pfm.items.*;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
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
    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> block, boolean registerItem, CreativeModeTab tab)
    {
        return block.get();
    }

    @ExpectPlatform
    public static <T extends Block> T registerLateBlockClassic(String blockName, T block, boolean registerItem, CreativeModeTab tab)
    {
        return block;
    }

    @ExpectPlatform
    public static Item registerLateItem(String itemName, Supplier<Item> item) {
        return item.get();
    }

    public static <T extends Block> T registerLateBlock(String blockName, Supplier<T> blockSupplier, int count, CreativeModeTab tab) {
        T block = registerLateBlock(blockName, blockSupplier, false, tab);
        PaladinFurnitureModBlocksItems.BLOCKS.add(block);
        registerLateItem(blockName, () -> new BlockItem(block, new BlockItem.Properties().tab(tab).stacksTo(count)));
        return block;
    }

    public static void registerBlocks() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::createBlocks);
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(HerringbonePlankBlock.class, new FurnitureEntry<HerringbonePlankBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_herringbone_planks", () -> new HerringbonePlankBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, CreativeModeTab.TAB_BUILDING_BLOCKS), true);
            }
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicChairBlock.class, new FurnitureEntry<BasicChairBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair", () -> new BasicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair", () -> new BasicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair", () -> new BasicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(DinnerChairBlock.class, new FurnitureEntry<DinnerChairBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_dinner", () -> new DinnerChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicChairBlock.class, new FurnitureEntry<ClassicChairBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_classic", () -> new ClassicChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicChairDyeableBlock.class, new FurnitureEntry<ClassicChairDyeableBlock>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock("oak_chair_classic_" + color.getName(), () -> new ClassicChairDyeableBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.copy(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).allBlocks.get(0)), ToolType.AXE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ModernChairBlock.class, new FurnitureEntry<ModernChairBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_chair_modern", () -> new ModernChairBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(FroggyChairBlock.class, new FurnitureEntry<FroggyChairBlock>() {{
            this.addBlock(registerLateBlock("froggy_chair", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_pink", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_PINK).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_light_blue", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_blue", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_orange", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(registerLateBlock("froggy_chair_yellow", () -> new FroggyChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW).strength(9.0f, 8.0f).noOcclusion().requiresCorrectToolForDrops(), ToolType.getToolTypeFromMaterial(Material.METAL))), true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(SimpleSofaBlock.class, new FurnitureEntry<SimpleSofaBlock>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_simple_sofa", () -> new SimpleSofaBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.WOOL, color.getMaterialColor()).strength(2.0f).noOcclusion().sound(SoundType.WOOL), ToolType.AXE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ArmChairBlock.class, new FurnitureEntry<ArmChairBlock>() {{
            this.addBlock(registerLateBlock("arm_chair_leather", () -> new ArmChairBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.CLAY).strength(2.0f).noOcclusion().sound(SoundType.WOOL), ToolType.AXE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ArmChairColoredBlock.class, new FurnitureEntry<ArmChairColoredBlock>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_arm_chair", () -> new ArmChairColoredBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.WOOL, color.getMaterialColor()).strength(2.0f).noOcclusion().sound(SoundType.WOOL), ToolType.AXE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicTableBlock.class, new FurnitureEntry<BasicTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_basic", () -> new BasicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicTableBlock.class, new FurnitureEntry<ClassicTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_classic", () -> new ClassicTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(LogTableBlock.class, new FurnitureEntry<LogTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_" + postfix, () -> new LogTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_" + postfix, () -> new LogTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_natural", () -> new LogTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(RawLogTableBlock.class, new FurnitureEntry<RawLogTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_raw_table_" + postfix, () -> new RawLogTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" +variant.getSerializedName()+"_raw_table_" + postfix, () -> new RawLogTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(DinnerTableBlock.class, new FurnitureEntry<DinnerTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_dinner", () -> new DinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ModernDinnerTableBlock.class, new FurnitureEntry<ModernDinnerTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_table_modern_dinner", () -> new ModernDinnerTableBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
        }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicCoffeeTableBlock.class, new FurnitureEntry<BasicCoffeeTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_basic", () -> new BasicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ModernCoffeeTableBlock.class, new FurnitureEntry<ModernCoffeeTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_modern", () -> new ModernCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicCoffeeTableBlock.class, new FurnitureEntry<ClassicCoffeeTableBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_coffee_table_classic", () -> new ClassicCoffeeTableBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicDeskBlock.class, new FurnitureEntry<BasicDeskBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(variant.getBaseBlock())).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_basic", () -> new BasicDeskBlock(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(variant.getBaseBlock())).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicDeskCabinetBlock.class, new FurnitureEntry<BasicDeskCabinetBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(variant.getBaseBlock())).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(variant.getBaseBlock())).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_cabinet_basic", () -> new BasicDeskCabinetBlock(((AbstractBlock$SettingsExtension)BlockBehaviour.Properties.copy(variant.getBaseBlock())).pfm$setMapColor(color).noOcclusion()), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicDeskBlock.class, new FurnitureEntry<ClassicDeskBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_classic", () -> new ClassicDeskBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicDeskCabinetBlock.class, new FurnitureEntry<ClassicDeskCabinetBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_desk_cabinet_classic", () -> new ClassicDeskCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM = OfficeChairItem.getItemFactory(new BlockItem.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP));
        LateBlockRegistry.registerLateItem("office_chair", () -> PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM);
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicNightstandBlock.class, new FurnitureEntry<ClassicNightstandBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_classic_nightstand", () -> new ClassicNightstandBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(SimpleBedBlock.class, new FurnitureEntry<SimpleBedBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                int i = 0;for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                        break;
                    String blockName = variant.getSerializedName() + "_" + color.getName() +  "_simple_bed";
                    SimpleBedBlock block = LateBlockRegistry.registerLateBlock(blockName, () -> new SimpleBedBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.WOOL, state -> state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL).sound(SoundType.WOOD).strength(0.2f).noOcclusion(), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), false, PaladinFurnitureMod.FURNITURE_GROUP);
                    LateBlockRegistry.registerLateItem(blockName, () -> BedBlockItem.getItemFactory(block, new Item.Properties().stacksTo(1).tab(PaladinFurnitureMod.FURNITURE_GROUP)));
                    this.addBlock(variant, block, true);
                    PaladinFurnitureModBlocksItems.beds.add(block);
                    i++;
                }
            }
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicBedBlock.class, new FurnitureEntry<ClassicBedBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                int i = 0;
                for (DyeColor color : DyeColor.values()) {
                    if (i > 15)
                        break;
                    String blockName = variant.getSerializedName() + "_" + color.getName() +  "_classic_bed";
                    ClassicBedBlock block = LateBlockRegistry.registerLateBlock(blockName, () -> new ClassicBedBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.WOOL, state -> state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL).sound(SoundType.WOOD).strength(0.2f).noOcclusion(), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), false, PaladinFurnitureMod.FURNITURE_GROUP);
                    LateBlockRegistry.registerLateItem(blockName, () -> BedBlockItem.getItemFactory(block, new Item.Properties().stacksTo(1).tab(PaladinFurnitureMod.FURNITURE_GROUP)));
                    this.addBlock(variant, block, true);
                    PaladinFurnitureModBlocksItems.beds.add(block);
                    i++;
                }
            }
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(SimpleBunkLadderBlock.class, new FurnitureEntry<SimpleBunkLadderBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_simple_bunk_ladder", () -> new SimpleBunkLadderBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(LogStoolBlock.class, new FurnitureEntry<LogStoolBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                String postfix = variant.isNetherWood() ? "stem" : "log";
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName() + "_" + postfix + "_stool", () -> new LogStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(SimpleStoolBlock.class, new FurnitureEntry<SimpleStoolBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_simple_stool", () -> new SimpleStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ClassicStoolBlock.class, new FurnitureEntry<ClassicStoolBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_classic_stool", () -> new ClassicStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ModernStoolBlock.class, new FurnitureEntry<ModernStoolBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped())
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
            for (ExtraStoolVariant variant : ExtraStoolVariant.values()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock( variant.getSerializedName()+"_modern_stool", () -> new ModernStoolBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
        }});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenDrawerBlock.class, new FurnitureEntry<KitchenDrawerBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenCabinetBlock.class, new FurnitureEntry<KitchenCabinetBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenSinkBlock.class, new FurnitureEntry<KitchenSinkBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenCounterOvenBlock.class, new FurnitureEntry<KitchenCounterOvenBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenWallCounterBlock.class, new FurnitureEntry<KitchenWallCounterBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenWallDrawerBlock.class, new FurnitureEntry<KitchenWallDrawerBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenWallDrawerSmallBlock.class, new FurnitureEntry<KitchenWallDrawerSmallBlock>());
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenCounterBlock.class, new FurnitureEntry<KitchenCounterBlock>() {{
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial())), SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                if (variant.hasStripped()) {
                    this.addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial())), SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                    PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock("stripped_" + variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), false);
                }
            }
            for (StoneVariant variant : StoneVariantRegistry.getVariants()) {if (variant.identifier.getPath().equals("quartz"))
                    continue;
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial())), SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }
            for (ExtraCounterVariant variant : ExtraCounterVariant.values()) {
                MaterialColor color = variant.getBaseBlock().defaultMaterialColor();
                this.addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter", () -> new KitchenCounterBlock((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion())), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_drawer", () -> new KitchenDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_cabinet", () -> new KitchenCabinetBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_sink", () -> new KitchenSinkBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial())), SinkBehavior.WATER_SINK_BEHAVIOR), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_counter_oven", () -> new KitchenCounterOvenBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_counter", () -> new KitchenWallCounterBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_drawer", () -> new KitchenWallDrawerBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
                PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).addBlock(variant, LateBlockRegistry.registerLateBlock(variant.getSerializedName()+"_kitchen_wall_small_drawer", () -> new KitchenWallDrawerSmallBlock(PFMBlockSettings.breaksWithTool((((AbstractBlock$SettingsExtension)(BlockBehaviour.Properties.copy(variant.getBaseBlock()))).pfm$setMapColor(color).noOcclusion()), ToolType.getToolTypeFromMaterial(variant.getBaseMaterial()))), true, PaladinFurnitureMod.FURNITURE_GROUP), true);
            }}});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(FreezerBlock.class, new FurnitureEntry<FreezerBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "white_freezer", () -> PaladinFurnitureModBlocksItems.WHITE_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "gray_freezer", () -> PaladinFurnitureModBlocksItems.GRAY_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "iron_freezer", () -> PaladinFurnitureModBlocksItems.IRON_FREEZER, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(FridgeBlock.class, new FurnitureEntry<FridgeBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "white_fridge", () -> PaladinFurnitureModBlocksItems.WHITE_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock(LateBlockRegistry.registerLateBlock( "gray_fridge", () -> PaladinFurnitureModBlocksItems.GRAY_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_fridge", () -> PaladinFurnitureModBlocksItems.IRON_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "xbox_fridge", () -> PaladinFurnitureModBlocksItems.XBOX_FRIDGE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(MicrowaveBlock.class, new FurnitureEntry<MicrowaveBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "iron_microwave", () -> PaladinFurnitureModBlocksItems.IRON_MICROWAVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});

        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenRangeHoodBlock.class, new FurnitureEntry<KitchenRangeHoodBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_oven_range_hood", () -> PaladinFurnitureModBlocksItems.WHITE_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_oven_range_hood", () -> PaladinFurnitureModBlocksItems.GRAY_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_oven_range_hood", () -> PaladinFurnitureModBlocksItems.IRON_OVEN_RANGEHOOD, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(StoveBlock.class, new FurnitureEntry<StoveBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_stove", () -> PaladinFurnitureModBlocksItems.WHITE_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_stove", () -> PaladinFurnitureModBlocksItems.GRAY_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "iron_stove",() -> PaladinFurnitureModBlocksItems.IRON_STOVE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(KitchenStovetopBlock.class, new FurnitureEntry<KitchenStovetopBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "kitchen_stovetop",() -> PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(PFMToasterBlock.class, new FurnitureEntry<PFMToasterBlock>() {{
            addBlock((PFMToasterBlock) registerLateBlockClassic("iron_toaster", PaladinFurnitureModBlocksItems.TOASTER_BLOCK, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(PlateBlock.class, new FurnitureEntry<PlateBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "basic_plate",() -> PaladinFurnitureModBlocksItems.BASIC_PLATE, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(CutleryBlock.class, new FurnitureEntry<CutleryBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_cutlery",() -> PaladinFurnitureModBlocksItems.BASIC_CUTLERY, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(PendantBlock.class, new FurnitureEntry<PendantBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "gray_modern_pendant",() -> PaladinFurnitureModBlocksItems.GRAY_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "white_modern_pendant",() -> PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
            this.addBlock( LateBlockRegistry.registerLateBlock( "glass_modern_pendant",() -> PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(SimpleLightBlock.class, new FurnitureEntry<SimpleLightBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "simple_light",() -> PaladinFurnitureModBlocksItems.SIMPLE_LIGHT, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM = new LightSwitchItem(PaladinFurnitureModBlocksItems.LIGHT_SWITCH, new BlockItem.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP));
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(LightSwitchBlock.class, new FurnitureEntry<LightSwitchBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "light_switch",() -> PaladinFurnitureModBlocksItems.LIGHT_SWITCH, false, PaladinFurnitureMod.FURNITURE_GROUP));
            PaladinFurnitureModBlocksItems.BLOCKS.add(PaladinFurnitureModBlocksItems.LIGHT_SWITCH);
            LateBlockRegistry.registerLateItem("light_switch",() -> PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM);
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicToiletBlock.class, new FurnitureEntry<BasicToiletBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_toilet",() -> PaladinFurnitureModBlocksItems.BASIC_TOILET, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(WallToiletPaperBlock.class, new FurnitureEntry<WallToiletPaperBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "wall_toilet_paper",() -> PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicSinkBlock.class, new FurnitureEntry<BasicSinkBlock>() {{
            this.addBlock(LateBlockRegistry.registerLateBlock( "basic_sink",() -> PaladinFurnitureModBlocksItems.BASIC_SINK, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicBathtubBlock.class, new FurnitureEntry<BasicBathtubBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_bathtub",() -> PaladinFurnitureModBlocksItems.BASIC_BATHTUB, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicShowerHeadBlock.class, new FurnitureEntry<BasicShowerHeadBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_shower_head",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM = new ShowerHandleItem(() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, new BlockItem.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP));
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicShowerHandleBlock.class, new FurnitureEntry<BasicShowerHandleBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "basic_shower_handle",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE, false, PaladinFurnitureMod.FURNITURE_GROUP));
            LateBlockRegistry.registerLateItem( "basic_shower_handle",() -> PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM);
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(TrashcanBlock.class, new FurnitureEntry<TrashcanBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "trashcan",() -> PaladinFurnitureModBlocksItems.TRASHCAN, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(InnerTrashcanBlock.class, new FurnitureEntry<InnerTrashcanBlock>() {{
            this.addBlock( LateBlockRegistry.registerLateBlock( "mesh_trashcan",() -> PaladinFurnitureModBlocksItems.MESH_TRASHCAN, true, PaladinFurnitureMod.FURNITURE_GROUP));
        }});
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(ShowerTowelBlock.class, new FurnitureEntry<ShowerTowelBlock>() {{
            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                if (i > 15)
                    break;
                this.addBlock(registerLateBlock(color.getName() + "_shower_towel", () -> new ShowerTowelBlock(color, PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.WOOL, color.getMaterialColor()).strength(2.0f).noOcclusion().sound(SoundType.WOOL), ToolType.PICKAXE)), true, PaladinFurnitureMod.FURNITURE_GROUP));
                i++;
            }
        }});
        if (!BlockItemRegistry.isModLoaded("imm_ptl_core") && !BlockItemRegistry.isModLoaded("immersive_portals")) {
            PaladinFurnitureModBlocksItems.WHITE_MIRROR = new MirrorBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SNOW).noOcclusion(), ToolType.PICKAXE));
            PaladinFurnitureModBlocksItems.GRAY_MIRROR = new MirrorBlock(PFMBlockSettings.breaksWithTool(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).noOcclusion(), ToolType.PICKAXE));
        }
        registerLateBlock("white_mirror",() -> PaladinFurnitureModBlocksItems.WHITE_MIRROR, true, PaladinFurnitureMod.FURNITURE_GROUP);
        registerLateBlock("gray_mirror",() -> PaladinFurnitureModBlocksItems.GRAY_MIRROR, true, PaladinFurnitureMod.FURNITURE_GROUP);
        PaladinFurnitureModBlocksItems.furnitureEntryMap.put(BasicLampBlock.class, new FurnitureEntry<BasicLampBlock>() {{
            BasicLampBlock lampBlock = (BasicLampBlock)registerLateBlockClassic("basic_lamp", PaladinFurnitureModBlocksItems.BASIC_LAMP, false, PaladinFurnitureMod.FURNITURE_GROUP);
            for (WoodVariant variant : WoodVariantRegistry.getVariants())
                addBlock(variant, lampBlock, true);
        }});
        PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM = LampItem.getItemFactory(PaladinFurnitureModBlocksItems.BASIC_LAMP, new BlockItem.Properties().tab(PaladinFurnitureMod.FURNITURE_GROUP));
        LateBlockRegistry.registerLateItem( "basic_lamp", () -> PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerBlocks);
        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::registerItems);
    }
}
