package com.unlikepaladin.pfm.data;

import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTablesProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;

import java.util.stream.Stream;

public class PaladinFurnitureModDataEntrypoint implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
            dataGenerator.addProvider(PFMBlockTagProvider::new);
            dataGenerator.addProvider(PFMLootTableProvider::new);
    }
    private static class PFMLootTableProvider extends FabricBlockLootTablesProvider {
        private PFMLootTableProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateBlockLootTables() {
            Stream<Block> blocks = BlockItemRegistry.streamBlocks();
            blocks.forEach(block ->
                this.addDrop(block)
            );

        }
    }

    private static class PFMBlockTagProvider extends FabricTagProvider.BlockTagProvider {
        private PFMBlockTagProvider(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            KitchenCounter[] stoneCounters = KitchenCounter.streamStoneCounters().toList().toArray(new KitchenCounter[0]);
            KitchenCabinet[] stoneCabinets = KitchenCabinet.streamStoneCabinets().toList().toArray(new KitchenCabinet[0]);
            KitchenDrawer[] stoneDrawers = KitchenDrawer.streamStoneDrawers().toList().toArray(new KitchenDrawer[0]);
            KitchenCounterOven[] stoneCounterOvens = KitchenCounterOven.streamStoneCounterOvens().toList().toArray(new KitchenCounterOven[0]);
            KitchenSink[] stoneSinks = KitchenSink.streamStoneSinks().toList().toArray(new KitchenSink[0]);
            BasicChair[] stoneBasicChairs = BasicChair.streamStoneBasicChairs().toList().toArray(new BasicChair[0]);
            BasicTable[] stoneBasicTables = BasicTable.streamStoneBasicTables().toList().toArray(new BasicTable[0]);
            ClassicChair[] stoneClassicChairs = ClassicChair.streamStoneClassicChairs().toList().toArray(new ClassicChair[0]);
            ClassicChairDyeable[] stoneDyeableClassicChairs = ClassicChairDyeable.streamStoneDyeableChair().toList().toArray(new ClassicChairDyeable[0]);
            ClassicStool[] stoneClassicStools = ClassicStool.streamStoneClassicStools().toList().toArray(new ClassicStool[0]);
            ClassicTable[] stoneClassicTables = ClassicTable.streamStoneClassicTables().toList().toArray(new ClassicTable[0]);
            DinnerChair[] stoneDinnerChairs = DinnerChair.streamStoneDinnerChairs().toList().toArray(new DinnerChair[0]);
            DinnerTable[] stoneDinnerTables = DinnerTable.streamStoneDinnerTables().toList().toArray(new DinnerTable[0]);
            ModernChair[] stoneModernChairs = ModernChair.streamStoneModernChairs().toList().toArray(new ModernChair[0]);
            ModernStool[] stoneModernStools = ModernStool.streamStoneModernStools().toList().toArray(new ModernStool[0]);
            SimpleStool[] stoneSimpleStools = SimpleStool.streamStoneSimpleStools().toList().toArray(new SimpleStool[0]);
            PendantBlock[] pendantLights = PendantBlock.streamPendantLights().toList().toArray(new PendantBlock[0]);
            SimpleLight[] simpleLights = SimpleLight.streamSimpleLights().toList().toArray(new SimpleLight[0]);
            Fridge[] fridges = Fridge.streamFridges().toList().toArray(new Fridge[0]);
            Freezer[] freezers = Freezer.streamFreezers().toList().toArray(new Freezer[0]);
            LightSwitch[] lightSwitches = LightSwitch.streamlightSwitches().toList().toArray(new LightSwitch[0]);
            Microwave[] microwaves = Microwave.streamMicrowaves().toList().toArray(new Microwave[0]);
            KitchenStovetop[] kitchenStovetops = KitchenStovetop.streamKitchenStovetop().toList().toArray(new KitchenStovetop[0]);
            IronStove[] ironStoves = IronStove.streamIronStoves().toList().toArray(new IronStove[0]);
            FroggyChair[] froggyChairs = FroggyChair.streamFroggyChair().toList().toArray(new FroggyChair[0]);
            Stove[] stove = Stove.streamStoves().toList().toArray(new Stove[0]);

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
                    .add(BlockItemRegistry.RAW_CONCRETE)
                    .add(BlockItemRegistry.IRON_CHAIN);

            KitchenCounter[] woodCounters = KitchenCounter.streamWoodCounters().toList().toArray(new KitchenCounter[0]);
            KitchenCabinet[] woodCabinets = KitchenCabinet.streamWoodCabinets().toList().toArray(new KitchenCabinet[0]);
            KitchenDrawer[] woodDrawers = KitchenDrawer.streamWoodDrawers().toList().toArray(new KitchenDrawer[0]);
            KitchenCounterOven[] woodCounterOvens = KitchenCounterOven.streamWoodCounterOvens().toList().toArray(new KitchenCounterOven[0]);
            KitchenSink[] woodSinks = KitchenSink.streamWoodSinks().toList().toArray(new KitchenSink[0]);
            BasicChair[] woodBasicChairs = BasicChair.streamWoodBasicChairs().toList().toArray(new BasicChair[0]);
            BasicTable[] woodBasicTables = BasicTable.streamWoodBasicTables().toList().toArray(new BasicTable[0]);
            ClassicChair[] woodClassicChairs = ClassicChair.streamWoodClassicChairs().toList().toArray(new ClassicChair[0]);
            ClassicChairDyeable[] woodDyeableClassicChairs = ClassicChairDyeable.streamWoodDyeableChair().toList().toArray(new ClassicChairDyeable[0]);
            ClassicStool[] woodClassicStools = ClassicStool.streamWoodClassicStools().toList().toArray(new ClassicStool[0]);
            ClassicTable[] woodClassicTables = ClassicTable.streamWoodClassicTables().toList().toArray(new ClassicTable[0]);
            DinnerChair[] woodDinnerChairs = DinnerChair.streamWoodDinnerChairs().toList().toArray(new DinnerChair[0]);
            DinnerTable[] woodDinnerTables = DinnerTable.streamWoodDinnerTables().toList().toArray(new DinnerTable[0]);
            LogStool[] woodLogStools = LogStool.streamWoodLogStools().toList().toArray(new LogStool[0]);
            LogTable[] woodLogTables = LogTable.streamWoodLogTables().toList().toArray(new LogTable[0]);
            ModernChair[] woodModernChairs = ModernChair.streamWoodModernChairs().toList().toArray(new ModernChair[0]);
            ModernStool[] woodModernStools = ModernStool.streamWoodModernStools().toList().toArray(new ModernStool[0]);
            SimpleStool[] woodSimpleStools = SimpleStool.streamWoodSimpleStools().toList().toArray(new SimpleStool[0]);
            SimpleSofa[] simpleSofas = SimpleSofa.streamSimpleSofas().toList().toArray(new SimpleSofa[0]);
            ArmChairDyeable[] armChairDyeables = ArmChairDyeable.streamArmChairDyeable().toList().toArray(new ArmChairDyeable[0]);
            ArmChair[] armChairs = ArmChair.streamArmChairs().toList().toArray(new ArmChair[0]);
            WorkingTable[] workingTables = WorkingTable.streamWorkingTables().toList().toArray(new WorkingTable[0]);

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
                    .add(woodModernChairs)
                    .add(woodModernStools)
                    .add(woodSimpleStools)
                    .add(simpleSofas)
                    .add(armChairDyeables)
                    .add(armChairs)
                    .add(workingTables)
                    .add(BlockItemRegistry.OAK_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.BIRCH_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.ACACIA_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.SPRUCE_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.JUNGLE_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.DARK_OAK_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.WARPED_HERRINGBONE_PLANKS)
                    .add(BlockItemRegistry.CRIMSON_HERRINGBONE_PLANKS);
            }

        }
}
