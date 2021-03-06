package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTablesProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PaladinFurnitureModDataEntrypoint implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        dataGenerator.addProvider(PFMBlockTagProvider::new);
        dataGenerator.addProvider(PFMLootTableProvider::new);
        dataGenerator.addProvider(PFMRecipeProvider::new);
    }

    private static class PFMLootTableProvider extends FabricBlockLootTablesProvider {
        private PFMLootTableProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateBlockLootTables() {
            Stream<Block> blocks = BlockItemRegistry.streamBlocks();
            blocks.forEach(this::addDrop);
            Block[] beds = BlockItemRegistry.getBeds();
            Arrays.stream(beds).forEach(bed -> this.addDrop(bed, (Block block) -> BlockLootTableGenerator.dropsWithProperty(block, BedBlock.PART, BedPart.HEAD)));
        }
    }

    private static class PFMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
        private PFMBlockTagProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            KitchenCounter[] stoneCounters = KitchenCounter.streamStoneCounters().map(FurnitureBlock::getBlock).toArray(KitchenCounter[]::new);
            KitchenCabinet[] stoneCabinets = KitchenCabinet.streamStoneCabinets().map(FurnitureBlock::getBlock).toArray(KitchenCabinet[]::new);
            KitchenDrawer[] stoneDrawers = KitchenDrawer.streamStoneDrawers().map(FurnitureBlock::getBlock).toArray(KitchenDrawer[]::new);
            KitchenCounterOven[] stoneCounterOvens = KitchenCounterOven.streamStoneCounterOvens().map(FurnitureBlock::getBlock).toArray(KitchenCounterOven[]::new);

            KitchenSink[] stoneSinks = KitchenSink.streamStoneSinks().map(FurnitureBlock::getBlock).toArray(KitchenSink[]::new);
            BasicChair[] stoneBasicChairs = BasicChair.streamStoneBasicChairs().map(FurnitureBlock::getBlock).toArray(BasicChair[]::new);
            BasicTable[] stoneBasicTables = BasicTable.streamStoneBasicTables().map(FurnitureBlock::getBlock).toArray(BasicTable[]::new);
            ClassicChair[] stoneClassicChairs = ClassicChair.streamWoodClassicChairs().map(FurnitureBlock::getBlock).toArray(ClassicChair[]::new);
            ClassicChairDyeable[] stoneDyeableClassicChairs = ClassicChairDyeable.streamStoneDyeableChair().toList().toArray(new ClassicChairDyeable[0]);
            ClassicStool[] stoneClassicStools = ClassicStool.streamStoneClassicStools().map(FurnitureBlock::getBlock).toArray(ClassicStool[]::new);

            ClassicTable[] stoneClassicTables = ClassicTable.streamStoneClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicTable[]::new);
            DinnerChair[] stoneDinnerChairs = DinnerChair.streamStoneDinnerChairs().map(FurnitureBlock::getBlock).toArray(DinnerChair[]::new);
            DinnerTable[] stoneDinnerTables = DinnerTable.streamStoneDinnerTables().map(FurnitureBlock::getBlock).toArray(DinnerTable[]::new);
            ModernChair[] stoneModernChairs = ModernChair.streamStoneModernChairs().map(FurnitureBlock::getBlock).toArray(ModernChair[]::new);
            ModernStool[] stoneModernStools = ModernStool.streamStoneModernStools().map(FurnitureBlock::getBlock).toArray(ModernStool[]::new);
            ModernDinnerTable[] stoneModernDinnerTables = ModernDinnerTable.streamStoneModernDinnerTables().map(FurnitureBlock::getBlock).toArray(ModernDinnerTable[]::new);
            ClassicNightstand[] stoneClassicNightstands = ClassicNightstand.streamStoneClassicNightstands().map(FurnitureBlock::getBlock).toArray(ClassicNightstand[]::new);
            LogTable[] stoneNaturalTables = LogTable.streamStoneNaturalTables().map(FurnitureBlock::getBlock).toArray(LogTable[]::new);

            SimpleStool[] stoneSimpleStools = SimpleStool.streamStoneSimpleStools().map(FurnitureBlock::getBlock).toArray(SimpleStool[]::new);
            PendantBlock[] pendantLights = PendantBlock.streamPendantLights().toList().toArray(new PendantBlock[0]);
            SimpleLight[] simpleLights = SimpleLight.streamSimpleLights().toList().toArray(new SimpleLight[0]);
            Fridge[] fridges = Fridge.streamFridges().map(FurnitureBlock::getBlock).toArray(Fridge[]::new);
            Freezer[] freezers = Freezer.streamFreezers().map(FurnitureBlock::getBlock).toArray(Freezer[]::new);
            LightSwitch[] lightSwitches = LightSwitch.streamlightSwitches().toList().toArray(new LightSwitch[0]);
            Microwave[] microwaves = Microwave.streamMicrowaves().map(FurnitureBlock::getBlock).toArray(Microwave[]::new);
            KitchenStovetop[] kitchenStovetops = KitchenStovetop.streamKitchenStovetop().toList().toArray(new KitchenStovetop[0]);
            IronStove[] ironStoves = IronStove.streamIronStoves().map(FurnitureBlock::getBlock).toArray(IronStove[]::new);
            FroggyChair[] froggyChairs = FroggyChair.streamFroggyChair().map(FurnitureBlock::getBlock).toArray(FroggyChair[]::new);
            Stove[] stove = Stove.streamStoves().map(FurnitureBlock::getBlock).toArray(Stove[]::new);
            SimpleBed[] simpleBeds = SimpleBed.streamSimpleBeds().map(FurnitureBlock::getBlock).toArray(SimpleBed[]::new);
            ClassicBed[] classicBeds = ClassicBed.streamClassicBeds().map(FurnitureBlock::getBlock).toArray(ClassicBed[]::new);

            this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                    .add(stoneCounters)
                    .add(stoneCabinets)
                    .add(stoneDrawers)
                    .add(stoneCounterOvens)
                    .add(stoneSinks)
                    .add(stoneBasicChairs)
                    .add(stoneBasicTables)
                    .add(stoneClassicChairs)
                    .add(stoneDyeableClassicChairs)
                    .add(stoneClassicStools)
                    .add(stoneClassicTables)
                    .add(stoneDinnerChairs)
                    .add(stoneDinnerTables)
                    .add(stoneModernDinnerTables)
                    .add(stoneModernChairs)
                    .add(stoneModernStools)
                    .add(stoneSimpleStools)
                    .add(pendantLights)
                    .add(simpleLights)
                    .add(fridges)
                    .add(freezers)
                    .add(lightSwitches)
                    .add(microwaves)
                    .add(kitchenStovetops)
                    .add(ironStoves)
                    .add(froggyChairs)
                    .add(stove)
                    .add(stoneNaturalTables)
                    .add(stoneClassicNightstands)
                    .add(BlockItemRegistry.RAW_CONCRETE)
                    .add(BlockItemRegistry.IRON_CHAIN);

            KitchenCounter[] woodCounters = KitchenCounter.streamWoodCounters().map(FurnitureBlock::getBlock).toArray(KitchenCounter[]::new);
            KitchenCabinet[] woodCabinets = KitchenCabinet.streamWoodCabinets().map(FurnitureBlock::getBlock).toArray(KitchenCabinet[]::new);
            KitchenDrawer[] woodDrawers = KitchenDrawer.streamWoodDrawers().map(FurnitureBlock::getBlock).toArray(KitchenDrawer[]::new);
            KitchenCounterOven[] woodCounterOvens = KitchenCounterOven.streamWoodCounterOvens().map(FurnitureBlock::getBlock).toArray(KitchenCounterOven[]::new);

            KitchenSink[] woodSinks = KitchenSink.streamWoodSinks().map(FurnitureBlock::getBlock).toArray(KitchenSink[]::new);

            BasicChair[] woodBasicChairs = BasicChair.streamWoodBasicChairs().map(FurnitureBlock::getBlock).toArray(BasicChair[]::new);

            BasicTable[] woodBasicTables = BasicTable.streamWoodBasicTables().map(FurnitureBlock::getBlock).toArray(BasicTable[]::new);
            ClassicChair[] woodClassicChairs = ClassicChair.streamWoodClassicChairs().map(FurnitureBlock::getBlock).toArray(ClassicChair[]::new);
            ClassicChairDyeable[] woodDyeableClassicChairs = ClassicChairDyeable.streamWoodDyeableChair().map(FurnitureBlock::getBlock).toArray(ClassicChairDyeable[]::new);
            ClassicStool[] woodClassicStools = ClassicStool.streamWoodClassicStools().map(FurnitureBlock::getBlock).toArray(ClassicStool[]::new);
            ClassicTable[] woodClassicTables = ClassicTable.streamWoodClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicTable[]::new);
            DinnerChair[] woodDinnerChairs = DinnerChair.streamWoodDinnerChairs().map(FurnitureBlock::getBlock).toArray(DinnerChair[]::new);
            DinnerTable[] woodDinnerTables = DinnerTable.streamWoodDinnerTables().map(FurnitureBlock::getBlock).toArray(DinnerTable[]::new);
            LogStool[] woodLogStools = LogStool.streamWoodLogStools().map(FurnitureBlock::getBlock).toArray(LogStool[]::new);
            LogTable[] woodLogTables = LogTable.streamWoodLogTables().map(FurnitureBlock::getBlock).toArray(LogTable[]::new);
            ModernChair[] woodModernChairs = ModernChair.streamWoodModernChairs().map(FurnitureBlock::getBlock).toArray(ModernChair[]::new);
            ModernDinnerTable[] woodModernDinnerTables = ModernDinnerTable.streamWoodModernDinnerTables().map(FurnitureBlock::getBlock).toArray(ModernDinnerTable[]::new);

            ClassicNightstand[] woodClassicNightstands = ClassicNightstand.streamWoodClassicNightstands().map(FurnitureBlock::getBlock).toArray(ClassicNightstand[]::new);
            ModernStool[] woodModernStools = ModernStool.streamWoodModernStools().map(FurnitureBlock::getBlock).toArray(ModernStool[]::new);
            SimpleStool[] woodSimpleStools = SimpleStool.streamWoodSimpleStools().map(FurnitureBlock::getBlock).toArray(SimpleStool[]::new);
            SimpleSofa[] simpleSofas = SimpleSofa.streamSimpleSofas().toList().toArray(new SimpleSofa[0]);
            ArmChairDyeable[] armChairDyeables = ArmChairDyeable.streamArmChairDyeable().map(FurnitureBlock::getBlock).toArray(ArmChairDyeable[]::new);
            ArmChair[] armChairs = ArmChair.streamArmChairs().map(FurnitureBlock::getBlock).toArray(ArmChair[]::new);
            WorkingTable[] workingTables = WorkingTable.streamWorkingTables().toList().toArray(new WorkingTable[0]);
            HerringbonePlanks[] herringbonePlanks = HerringbonePlanks.streamPlanks().map(FurnitureBlock::getBlock).toArray(HerringbonePlanks[]::new);

            this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                    .add(woodCounters)
                    .add(woodCabinets)
                    .add(woodDrawers)
                    .add(woodCounterOvens)
                    .add(woodSinks)
                    .add(woodBasicChairs)
                    .add(woodBasicTables)
                    .add(woodClassicChairs)
                    .add(woodDyeableClassicChairs)
                    .add(woodClassicStools)
                    .add(woodClassicTables)
                    .add(woodDinnerChairs)
                    .add(woodDinnerTables)
                    .add(woodLogStools)
                    .add(woodLogTables)
                    .add(woodModernDinnerTables)
                    .add(woodModernChairs)
                    .add(woodModernStools)
                    .add(woodSimpleStools)
                    .add(simpleSofas)
                    .add(armChairDyeables)
                    .add(armChairs)
                    .add(woodClassicNightstands)
                    .add(workingTables)
                    .add(herringbonePlanks)
                    .add(simpleBeds)
                    .add(classicBeds);

                this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                    .add(BlockItemRegistry.RAW_CONCRETE_POWDER);

                this.getOrCreateTagBuilder(BlockTags.BEDS)
                    .add(simpleBeds)
                    .add(classicBeds);
        }

    }

    private static class PFMRecipeProvider extends FabricRecipesProvider {
        private PFMRecipeProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
            FurnitureBlock[] woodClassicChairs = ClassicChair.streamWoodClassicChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicChair : woodClassicChairs) {
                offerClassicChairRecipe(classicChair.block, Ingredient.ofItems(classicChair.getSecondaryMaterial().asItem()), Ingredient.ofItems(classicChair.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneClassicChairs = ClassicChair.streamStoneClassicChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicChair : stoneClassicChairs) {
                offerClassicChairRecipe(classicChair.block, Ingredient.ofItems(classicChair.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(classicChair.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodBasicChairs = BasicChair.streamWoodBasicChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock basicChair : woodBasicChairs) {
                offerBasicChairRecipe(basicChair.block, Ingredient.ofItems(basicChair.getSecondaryMaterial().asItem()), Ingredient.ofItems(basicChair.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneBasicChairs = BasicChair.streamStoneBasicChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock basicChair : stoneBasicChairs) {
                offerBasicChairRecipe(basicChair.block, Ingredient.ofItems(basicChair.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(basicChair.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodDinnerChairs = DinnerChair.streamWoodDinnerChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock dinnerChair : woodDinnerChairs) {
                offerDinnerChairRecipe(dinnerChair.block, Ingredient.ofItems(dinnerChair.getSecondaryMaterial().asItem()), Ingredient.ofItems(dinnerChair.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneDinnerChairs = DinnerChair.streamStoneDinnerChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock dinnerChair : stoneDinnerChairs) {
                offerDinnerChairRecipe(dinnerChair.block, Ingredient.ofItems(dinnerChair.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(dinnerChair.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodModernChairs = ModernChair.streamWoodModernChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernChair : woodModernChairs) {
                offerModernChairRecipe(modernChair.block, Ingredient.ofItems(modernChair.getSecondaryMaterial().asItem()), Ingredient.ofItems(modernChair.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneModernChairs = ModernChair.streamStoneModernChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernChair : stoneModernChairs) {
                offerModernChairRecipe(modernChair.block, Ingredient.ofItems(modernChair.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(modernChair.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodClassicStools = ClassicStool.streamWoodClassicStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicStool : woodClassicStools) {
                offerClassicStoolRecipe(classicStool.block, Ingredient.ofItems(classicStool.getSecondaryMaterial().asItem()), Ingredient.ofItems(classicStool.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneClassicStools = ClassicStool.streamStoneClassicStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicStool : stoneClassicStools) {
                offerClassicStoolRecipe(classicStool.block, Ingredient.ofItems(classicStool.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(classicStool.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] herringbonePlanks = HerringbonePlanks.streamPlanks().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock herringbonePlank : herringbonePlanks) {
                offerHerringbonePlanks(herringbonePlank.block, herringbonePlank.getSlab().asItem(), exporter);
            }

            FurnitureBlock[] woodKitchenCabinets = KitchenCabinet.streamWoodCabinets().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCabinet : woodKitchenCabinets) {
                String cabinetName = kitchenCabinet.block.toString();
                if (cabinetName.contains("light_wood")) {
                    offerCabinetRecipe(kitchenCabinet.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_oak_log"))), Ingredient.ofItems(Items.CHEST), exporter);
                } else if (cabinetName.contains("dark_wood")) {
                    offerCabinetRecipe(kitchenCabinet.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_dark_oak_log"))), Ingredient.ofItems(Items.CHEST), exporter);
                } else {
                    offerCabinetRecipe(kitchenCabinet.block, Ingredient.ofItems(kitchenCabinet.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCabinet.getBaseMaterial().asItem()), Ingredient.ofItems(Items.CHEST), exporter);
                }
            }

            FurnitureBlock[] woodKitchenCounterOvens = KitchenCounterOven.streamWoodCounterOvens().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCounterOven : woodKitchenCounterOvens) {
                String cabinetName = kitchenCounterOven.block.toString();
                if (cabinetName.contains("light_wood")) {
                    offerCounterAppliance(kitchenCounterOven.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_oak_log"))), Ingredient.ofItems(Items.FURNACE), exporter);
                } else if (cabinetName.contains("dark_wood")) {
                    offerCounterAppliance(kitchenCounterOven.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_dark_oak_log"))), Ingredient.ofItems(Items.FURNACE), exporter);
                } else {
                    offerCounterAppliance(kitchenCounterOven.block, Ingredient.ofItems(kitchenCounterOven.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCounterOven.getBaseMaterial().asItem()), Ingredient.ofItems(Items.FURNACE), exporter);
                }
            }

            FurnitureBlock[] woodKitchenCounters = KitchenCounter.streamWoodCounters().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCounter : woodKitchenCounters) {
                String cabinetName = kitchenCounter.block.toString();
                if (cabinetName.contains("light_wood")) {
                    offerCounterRecipe(kitchenCounter.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_oak_log"))), exporter);
                } else if (cabinetName.contains("dark_wood")) {
                    offerCounterRecipe(kitchenCounter.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_dark_oak_log"))), exporter);
                } else {
                    offerCounterRecipe(kitchenCounter.block, Ingredient.ofItems(kitchenCounter.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCounter.getBaseMaterial().asItem()), exporter);
                }
            }

            FurnitureBlock[] woodKitchenDrawers = KitchenDrawer.streamWoodDrawers().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenDrawer : woodKitchenDrawers) {
                String cabinetName = kitchenDrawer.block.toString();
                if (cabinetName.contains("light_wood")) {
                    offerCounterAppliance(kitchenDrawer.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_oak_log"))), Ingredient.ofItems(Items.CHEST), exporter);
                } else if (cabinetName.contains("dark_wood")) {
                    offerCounterAppliance(kitchenDrawer.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_dark_oak_log"))), Ingredient.ofItems(Items.CHEST), exporter);
                } else {
                    offerCounterAppliance(kitchenDrawer.block, Ingredient.ofItems(kitchenDrawer.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenDrawer.getBaseMaterial().asItem()), Ingredient.ofItems(Items.CHEST), exporter);
                }
            }

            FurnitureBlock[] woodKitchenSinks = KitchenSink.streamWoodSinks().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenSink : woodKitchenSinks) {
                String cabinetName = kitchenSink.block.toString();
                if (cabinetName.contains("light_wood")) {
                    offerSinkRecipe(kitchenSink.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_oak_log"))), Ingredient.ofItems(Items.BUCKET), Ingredient.ofItems(Items.IRON_INGOT), exporter);
                } else if (cabinetName.contains("dark_wood")) {
                    offerSinkRecipe(kitchenSink.block, Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "smooth_quartz"))), Ingredient.ofItems(Registry.BLOCK.get(new Identifier("minecraft:" + "stripped_dark_oak_log"))), Ingredient.ofItems(Items.BUCKET), Ingredient.ofItems(Items.IRON_INGOT), exporter);
                } else {
                    offerSinkRecipe(kitchenSink.block, Ingredient.ofItems(kitchenSink.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenSink.getBaseMaterial().asItem()), Ingredient.ofItems(Items.BUCKET), Ingredient.ofItems(Items.IRON_INGOT), exporter);
                }
            }

            FurnitureBlock[] logStools = LogStool.streamWoodLogStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock logStool : logStools) {
                offerLogStoolRecipe(logStool.block, Ingredient.ofItems(logStool.getSecondaryMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodModernStools = ModernStool.streamWoodModernStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernStool : woodModernStools) {
                offerModernStoolRecipe(modernStool.block, Ingredient.ofItems(modernStool.getSecondaryMaterial().asItem()), Ingredient.ofItems(modernStool.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneModernStools = ModernStool.streamStoneModernStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernStool : stoneModernStools) {
                offerModernStoolRecipe(modernStool.block, Ingredient.ofItems(modernStool.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(modernStool.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] logTables = LogTable.streamWoodLogTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock logTable : logTables) {
                if (logTable.block.toString().contains("raw") && logTable.block.toString().contains("stripped")){
                    offerLogTableRecipe(logTable.block, Ingredient.ofItems(logTable.getStrippedBaseMaterial().asItem()), Ingredient.ofItems(logTable.getStrippedBaseMaterial().asItem()), exporter);
                } else if (logTable.block.toString().contains("stripped")){
                    offerLogTableRecipe(logTable.block, Ingredient.ofItems(logTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(logTable.getStrippedBaseMaterial().asItem()), exporter);
                }
                else if (logTable.block.toString().contains("raw")) {
                    offerLogTableRecipe(logTable.block, Ingredient.ofItems(logTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(logTable.getSecondaryMaterial().asItem()), exporter);
                } else {
                    offerLogTableRecipe(logTable.block, Ingredient.ofItems(logTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(logTable.getBaseMaterial().asItem()), exporter);
                }
            }

            FurnitureBlock[] stoneNaturalTables = LogTable.streamStoneNaturalTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock naturalTable : stoneNaturalTables) {
                offerLogTableRecipe(naturalTable.block, Ingredient.ofItems(naturalTable.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(naturalTable.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodSimpleStools = SimpleStool.streamWoodSimpleStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock simpleStool : woodSimpleStools) {
                offerSimpleStoolRecipe(simpleStool.block, Ingredient.ofItems(simpleStool.getSecondaryMaterial().asItem()), Ingredient.ofItems(simpleStool.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneSimpleStools = SimpleStool.streamStoneSimpleStools().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock simpleStool : stoneSimpleStools) {
                offerSimpleStoolRecipe(simpleStool.block, Ingredient.ofItems(simpleStool.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(simpleStool.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodBasicTables = BasicTable.streamWoodBasicTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock basicTable : woodBasicTables) {
                offerBasicTableRecipe(basicTable.block, Ingredient.ofItems(basicTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(basicTable.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneBasicTables = BasicTable.streamStoneBasicTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock simpleStool : stoneBasicTables) {
                offerBasicTableRecipe(simpleStool.block, Ingredient.ofItems(simpleStool.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(simpleStool.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodModernDinnerTables = ModernDinnerTable.streamWoodModernDinnerTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernDinnerTables : woodModernDinnerTables) {
                offerModernDinnerTableRecipe(modernDinnerTables.block, Ingredient.ofItems(modernDinnerTables.getSecondaryMaterial().asItem()), Ingredient.ofItems(modernDinnerTables.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneModernDinnerTables = ModernDinnerTable.streamStoneModernDinnerTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock modernDinnerTable : stoneModernDinnerTables) {
                offerModernDinnerTableRecipe(modernDinnerTable.block, Ingredient.ofItems(modernDinnerTable.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(modernDinnerTable.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodClassicTables = ClassicTable.streamWoodClassicTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicTable : woodClassicTables) {
                offerClassicTableRecipe(classicTable.block, Ingredient.ofItems(classicTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(classicTable.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneClassicTables = ClassicTable.streamStoneClassicTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicTable : stoneClassicTables) {
                offerClassicTableRecipe(classicTable.block, Ingredient.ofItems(classicTable.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(classicTable.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woodDinnerTables = DinnerTable.streamWoodDinnerTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock dinnerTable : woodDinnerTables) {
                offerDinnerTableRecipe(dinnerTable.block, Ingredient.ofItems(dinnerTable.getSecondaryMaterial().asItem()), Ingredient.ofItems(dinnerTable.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneDinnerTables = DinnerTable.streamStoneDinnerTables().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock dinnerTable : stoneDinnerTables) {
                offerDinnerTableRecipe(dinnerTable.block, Ingredient.ofItems(dinnerTable.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(dinnerTable.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneKitchenCabinets = KitchenCabinet.streamStoneCabinets().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCabinet : stoneKitchenCabinets) {
                offerCabinetRecipe(kitchenCabinet.block, Ingredient.ofItems(kitchenCabinet.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCabinet.getBaseMaterial().asItem()), Ingredient.ofItems(Items.CHEST), exporter);
            }

            FurnitureBlock[] stoneKitchenCounterOvens = KitchenCounterOven.streamStoneCounterOvens().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCounterOven : stoneKitchenCounterOvens) {
                    offerCounterAppliance(kitchenCounterOven.block, Ingredient.ofItems(kitchenCounterOven.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCounterOven.getBaseMaterial().asItem()), Ingredient.ofItems(Items.FURNACE), exporter);
            }

            FurnitureBlock[] stoneKitchenCounters = KitchenCounter.streamStoneCounters().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenCounter : stoneKitchenCounters) {
                    offerCounterRecipe(kitchenCounter.block, Ingredient.ofItems(kitchenCounter.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenCounter.getBaseMaterial().asItem()), exporter);
            }

            FurnitureBlock[] stoneKitchenDrawers = KitchenDrawer.streamStoneDrawers().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenDrawer : stoneKitchenDrawers) {
                    offerCounterAppliance(kitchenDrawer.block, Ingredient.ofItems(kitchenDrawer.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenDrawer.getBaseMaterial().asItem()), Ingredient.ofItems(Items.CHEST), exporter);
            }

            FurnitureBlock[] stoneKitchenSinks = KitchenSink.streamStoneSinks().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock kitchenSink : stoneKitchenSinks) {
                    offerSinkRecipe(kitchenSink.block, Ingredient.ofItems(kitchenSink.getSecondaryMaterial().asItem()), Ingredient.ofItems(kitchenSink.getBaseMaterial().asItem()), Ingredient.ofItems(Items.BUCKET), Ingredient.ofItems(Items.IRON_INGOT), exporter);
            }

            FurnitureBlock[] froggyChairs = FroggyChair.streamFroggyChair().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock froggyChair : froggyChairs) {
                offerFroggyChairRecipe(froggyChair.block, Ingredient.ofItems(froggyChair.getFroggyChairMaterial().asItem()), exporter);
            }

            FurnitureBlock[] fridges = Fridge.streamFridges().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock fridge : fridges) {
                offerFridgeRecipe(fridge.block, Ingredient.ofItems(fridge.getFridgeMaterial().asItem()), Ingredient.ofItems(Items.CHEST), exporter);
            }

            FurnitureBlock[] armChairs = ArmChair.streamArmChairs().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock armChair : armChairs) {
                offerArmChair(armChair.block, Ingredient.ofItems(Items.OAK_LOG), Ingredient.ofItems(armChair.getArmChairMaterial().asItem()), exporter);
            }

            FurnitureBlock[] leatherArmChairs = ArmChairDyeable.streamArmChairDyeable().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock armChair : leatherArmChairs) {
                offerArmChair(armChair.block, Ingredient.ofItems(Items.OAK_LOG), Ingredient.ofItems(armChair.getArmChairMaterial().asItem()), exporter);
            }

            FurnitureBlock[] woolClassicChairs = ClassicChairDyeable.streamWoodDyeableChair().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock classicChair : woolClassicChairs) {
                offerClassicChairRecipe(classicChair.block,  Ingredient.ofItems(Items.OAK_LOG), Ingredient.fromTag(ItemTags.WOOL), exporter);
            }

            FurnitureBlock[] microwaves = Microwave.streamMicrowaves().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock microwave : microwaves) {
                offerMicrowaveRecipe(microwave.block,  Ingredient.ofItems(microwave.getFridgeMaterial().asItem()), Ingredient.ofItems(Items.FURNACE), exporter);
            }

            FurnitureBlock[] stoves = Stove.streamStoves().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock stove : stoves) {
                offerStoveRecipe(stove.block,  Ingredient.ofItems(stove.getFridgeMaterial().asItem()), Ingredient.ofItems(Items.FURNACE), exporter);
            }

            FurnitureBlock[] ironStove = IronStove.streamIronStoves().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock stove : ironStove) {
                offerStoveRecipe(stove.block,  Ingredient.ofItems(stove.getFridgeMaterial().asItem()), Ingredient.ofItems(Items.FURNACE), exporter);
            }

            FurnitureBlock[] woodClassicNightStands = ClassicNightstand.streamWoodClassicNightstands().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock nightStand : woodClassicNightStands) {
                offerClassicNightStandRecipe(nightStand.block,  Ingredient.ofItems(nightStand.getSecondaryMaterial()), Ingredient.ofItems(nightStand.getBaseMaterial()), exporter);
            }

            FurnitureBlock[] stoneClassicNightStands = ClassicNightstand.streamStoneClassicNightstands().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock nightStand : stoneClassicNightStands) {
                offerClassicNightStandRecipe(nightStand.block, Ingredient.ofItems(nightStand.getSecondaryStoneMaterial().asItem()), Ingredient.ofItems(nightStand.getBaseStoneMaterial().asItem()), exporter);
            }

            FurnitureBlock[] simpleBeds = SimpleBed.streamSimpleBeds().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock bed : simpleBeds) {
                offerSimpleBed(bed.block,  Ingredient.ofItems(bed.getBaseMaterial()), Ingredient.ofItems(bed.getBed()), exporter);
            }

            FurnitureBlock[] classicBeds = ClassicBed.streamClassicBeds().toList().toArray(new FurnitureBlock[0]);
            for (FurnitureBlock bed : classicBeds) {
                offerClassicBed(bed.block,  Ingredient.ofItems(bed.getBaseMaterial()), Ingredient.ofItems(bed.getBed()), Ingredient.ofItems(bed.getFence()), exporter);
            }
        }
    }

    public static void offerClassicChairRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("S  ").pattern("SXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerBasicChairRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("X  ").pattern("XXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerDinnerChairRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("S  ").pattern("XXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerModernChairRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("X  ").pattern("SXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerFroggyChairRecipe(ItemConvertible output, Ingredient legMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('X', legMaterial).pattern("X  ").pattern("XXX").pattern("X X").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerArmChair(ItemConvertible output, Ingredient baseMaterial, Ingredient legMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 2).input('X', legMaterial).input('S', baseMaterial).pattern("X  ").pattern("XXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerClassicStoolRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("SXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerModernStoolRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern(" S ").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerSimpleStoolRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerHerringbonePlanks(ItemConvertible output, Item baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonFactory.create(output, 4).input('X', baseMaterial).pattern("XX").pattern("XX").criterion("has_wood_slabs", RecipesProvider.conditionsFromItem(baseMaterial)).offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerCabinetRecipe(ItemConvertible output, Ingredient baseMaterial, Ingredient legMaterial, Ingredient chest, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('S', baseMaterial).input('Y', chest).pattern("XSX").pattern("XYX").pattern("XSX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerCounterAppliance(ItemConvertible output, Ingredient baseMaterial, Ingredient legMaterial, Ingredient stove, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('S', baseMaterial).input('Y', stove).pattern("SSS").pattern("XYX").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerCounterRecipe(ItemConvertible output, Ingredient baseMaterial, Ingredient legMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 6).input('X', legMaterial).input('S', baseMaterial).pattern("SSS").pattern("XXX").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerSinkRecipe(ItemConvertible output, Ingredient baseMaterial, Ingredient legMaterial, Ingredient center, Ingredient ingot, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('S', baseMaterial).input('Z', ingot).input('Y', center).pattern("SZS").pattern("XYX").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerFridgeRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient storage, Consumer<RecipeJsonProvider> exporter) {
        if (output.asItem().toString().contains("xbox")) {
            FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Y', storage).input('Z', Ingredient.ofItems(Items.REDSTONE)).input('P', Ingredient.ofItems(Items.WHITE_CONCRETE)).pattern("XPX").pattern("XYX").pattern("XZX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
        }
        else {
            FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Y', storage).input('Z', Ingredient.ofItems(Items.REDSTONE)).pattern("XXX").pattern("XYX").pattern("XZX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
        }
    }

    public static void offerLogStoolRecipe(ItemConvertible output, Ingredient legMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).pattern("S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerLogTableRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern(" S ").pattern(" S ").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerBasicTableRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern("S S").pattern("SSS").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerModernDinnerTableRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern("SSS").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerClassicTableRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern("S S").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerClassicNightStandRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).input('Z', Blocks.CHEST).pattern("SXS").pattern("SZS").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerDinnerTableRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient baseMaterial, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 4).input('S', legMaterial).input('X', baseMaterial).pattern("XXX").pattern(" S ").pattern("S S").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerMicrowaveRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient storage, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Y', storage).input('Z', Ingredient.ofItems(Items.REDSTONE)).pattern("XXX").pattern("XYX").pattern("XZX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerStoveRecipe(ItemConvertible output, Ingredient legMaterial, Ingredient storage, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Y', storage).pattern("XXX").pattern("XYX").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerSimpleBed(ItemConvertible output, Ingredient legMaterial, Ingredient baseBed, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Z', baseBed).pattern("XZX").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }

    public static void offerClassicBed(ItemConvertible output, Ingredient legMaterial, Ingredient baseBed, Ingredient fence, Consumer<RecipeJsonProvider> exporter) {
        FurnitureRecipeJsonFactory.create(output, 1).input('X', legMaterial).input('Z', baseBed).input('Y', fence).pattern("YZY").pattern("XXX").offerTo(exporter, new Identifier("pfm", output.asItem().getTranslationKey().replace("block.pfm.", "")));
    }
}
