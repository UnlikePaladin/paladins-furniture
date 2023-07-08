package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.data.Tags;
import com.unlikepaladin.pfm.mixin.PFMAbstractTagProvider$ObjectBuilderMixin;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.PFMDataGen;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.block.Block;
import net.minecraft.data.DataCache;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PFMTagProvider {
    protected void generateTags() {
        KitchenCounterBlock[] stoneCounters = KitchenCounterBlock.streamStoneCounters().map(FurnitureBlock::getBlock).toArray(KitchenCounterBlock[]::new);
        KitchenCabinetBlock[] stoneCabinets = KitchenCabinetBlock.streamStoneCabinets().map(FurnitureBlock::getBlock).toArray(KitchenCabinetBlock[]::new);
        KitchenDrawerBlock[] stoneDrawers = KitchenDrawerBlock.streamStoneDrawers().map(FurnitureBlock::getBlock).toArray(KitchenDrawerBlock[]::new);
        KitchenCounterOvenBlock[] stoneCounterOvens = KitchenCounterOvenBlock.streamStoneCounterOvens().map(FurnitureBlock::getBlock).toArray(KitchenCounterOvenBlock[]::new);
        KitchenWallCounterBlock[] stoneWallCounters = KitchenWallCounterBlock.streamWallStoneCounters().map(FurnitureBlock::getBlock).toArray(KitchenWallCounterBlock[]::new);
        KitchenWallDrawerBlock[] stoneWallDrawers = KitchenWallDrawerBlock.streamWallStoneDrawers().map(FurnitureBlock::getBlock).toArray(KitchenWallDrawerBlock[]::new);
        KitchenWallDrawerSmallBlock[] stoneWallSmallDrawers = KitchenWallDrawerSmallBlock.streamStoneWallSmallDrawers().map(FurnitureBlock::getBlock).toArray(KitchenWallDrawerSmallBlock[]::new);

        KitchenSinkBlock[] stoneSinks = KitchenSinkBlock.streamStoneSinks().map(FurnitureBlock::getBlock).toArray(KitchenSinkBlock[]::new);
        BasicChairBlock[] stoneBasicChairs = BasicChairBlock.streamStoneBasicChairs().map(FurnitureBlock::getBlock).toArray(BasicChairBlock[]::new);
        BasicTableBlock[] stoneBasicTables = BasicTableBlock.streamStoneBasicTables().map(FurnitureBlock::getBlock).toArray(BasicTableBlock[]::new);
        ClassicChairBlock[] stoneClassicChairs = ClassicChairBlock.streamWoodClassicChairs().map(FurnitureBlock::getBlock).toArray(ClassicChairBlock[]::new);
        ClassicChairDyeableBlock[] stoneDyeableClassicChairs = ClassicChairDyeableBlock.streamStoneDyeableChair().toList().toArray(new ClassicChairDyeableBlock[0]);
        ClassicStoolBlock[] stoneClassicStools = ClassicStoolBlock.streamStoneClassicStools().map(FurnitureBlock::getBlock).toArray(ClassicStoolBlock[]::new);

        ClassicTableBlock[] stoneClassicTables = ClassicTableBlock.streamStoneClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicTableBlock[]::new);
        DinnerChairBlock[] stoneDinnerChairs = DinnerChairBlock.streamStoneDinnerChairs().map(FurnitureBlock::getBlock).toArray(DinnerChairBlock[]::new);
        DinnerTableBlock[] stoneDinnerTables = DinnerTableBlock.streamStoneDinnerTables().map(FurnitureBlock::getBlock).toArray(DinnerTableBlock[]::new);
        ModernChairBlock[] stoneModernChairs = ModernChairBlock.streamStoneModernChairs().map(FurnitureBlock::getBlock).toArray(ModernChairBlock[]::new);
        ModernStoolBlock[] stoneModernStools = ModernStoolBlock.streamStoneModernStools().map(FurnitureBlock::getBlock).toArray(ModernStoolBlock[]::new);
        ModernDinnerTableBlock[] stoneModernDinnerTables = ModernDinnerTableBlock.streamStoneModernDinnerTables().map(FurnitureBlock::getBlock).toArray(ModernDinnerTableBlock[]::new);
        ClassicNightstandBlock[] stoneClassicNightstands = ClassicNightstandBlock.streamStoneClassicNightstands().map(FurnitureBlock::getBlock).toArray(ClassicNightstandBlock[]::new);
        LogTableBlock[] stoneNaturalTables = LogTableBlock.streamStoneNaturalTables().map(FurnitureBlock::getBlock).toArray(LogTableBlock[]::new);

        SimpleStoolBlock[] stoneSimpleStools = SimpleStoolBlock.streamStoneSimpleStools().map(FurnitureBlock::getBlock).toArray(SimpleStoolBlock[]::new);
        PendantBlock[] pendantLights = PendantBlock.streamPendantLights().toList().toArray(new PendantBlock[0]);
        SimpleLightBlock[] simpleLights = SimpleLightBlock.streamSimpleLights().toList().toArray(new SimpleLightBlock[0]);
        FridgeBlock[] fridges = FridgeBlock.streamFridges().map(FurnitureBlock::getBlock).toArray(FridgeBlock[]::new);
        FreezerBlock[] freezers = FreezerBlock.streamFreezers().map(FurnitureBlock::getBlock).toArray(FreezerBlock[]::new);
        LightSwitchBlock[] lightSwitches = LightSwitchBlock.streamlightSwitches().toList().toArray(new LightSwitchBlock[0]);
        MicrowaveBlock[] microwaves = MicrowaveBlock.streamMicrowaves().map(FurnitureBlock::getBlock).toArray(MicrowaveBlock[]::new);
        KitchenStovetopBlock[] kitchenStovetops = KitchenStovetopBlock.streamKitchenStovetop().toList().toArray(new KitchenStovetopBlock[0]);
        IronStoveBlock[] ironStoves = IronStoveBlock.streamIronStoves().map(FurnitureBlock::getBlock).toArray(IronStoveBlock[]::new);
        FroggyChairBlock[] froggyChairs = FroggyChairBlock.streamFroggyChair().map(FurnitureBlock::getBlock).toArray(FroggyChairBlock[]::new);
        StoveBlock[] stove = StoveBlock.streamStoves().map(FurnitureBlock::getBlock).toArray(StoveBlock[]::new);
        SimpleBedBlock[] simpleBeds = SimpleBedBlock.streamSimpleBeds().map(FurnitureBlock::getBlock).toArray(SimpleBedBlock[]::new);
        ClassicBedBlock[] classicBeds = ClassicBedBlock.streamClassicBeds().map(FurnitureBlock::getBlock).toArray(ClassicBedBlock[]::new);
        PlateBlock[] plates = PlateBlock.streamPlates().map(FurnitureBlock::getBlock).toArray(PlateBlock[]::new);
        CutleryBlock[] cutleries = CutleryBlock.streamCutlery().map(FurnitureBlock::getBlock).toArray(CutleryBlock[]::new);
        BasicToiletBlock[] basicToilets = BasicToiletBlock.streamBasicToilet().map(FurnitureBlock::getBlock).toArray(BasicToiletBlock[]::new);
        KitchenRangeHoodBlock[] rangeHoods = KitchenRangeHoodBlock.streamOvenRangeHoods().map(FurnitureBlock::getBlock).toArray(KitchenRangeHoodBlock[]::new);
        BasicSinkBlock[] sinkBlocks = BasicSinkBlock.streamSinks().toList().toArray(new BasicSinkBlock[0]);
        ShowerTowelBlock[] showerTowels = ShowerTowelBlock.streamShowerTowels().map(FurnitureBlock::getBlock).toArray(ShowerTowelBlock[]::new);

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(showerTowels)
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
                .add(stoneWallCounters)
                .add(stoneWallDrawers)
                .add(stoneWallSmallDrawers)
                .add(stoneNaturalTables)
                .add(stoneClassicNightstands)
                .add(plates)
                .add(cutleries)
                .add(basicToilets)
                .add(rangeHoods)
                .add(PaladinFurnitureModBlocksItems.RAW_CONCRETE)
                .add(PaladinFurnitureModBlocksItems.IRON_CHAIN)
                .add(sinkBlocks)
                .add(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE)
                .add(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD);

        KitchenCounterBlock[] woodCounters = KitchenCounterBlock.streamWoodCounters().map(FurnitureBlock::getBlock).toArray(KitchenCounterBlock[]::new);
        KitchenWallCounterBlock[] woodWallCounters = KitchenWallCounterBlock.streamWallWoodCounters().map(FurnitureBlock::getBlock).toArray(KitchenWallCounterBlock[]::new);
        KitchenWallDrawerBlock[] woodWallDrawers = KitchenWallDrawerBlock.streamWallWoodDrawers().map(FurnitureBlock::getBlock).toArray(KitchenWallDrawerBlock[]::new);
        KitchenCabinetBlock[] woodCabinets = KitchenCabinetBlock.streamWoodCabinets().map(FurnitureBlock::getBlock).toArray(KitchenCabinetBlock[]::new);
        KitchenDrawerBlock[] woodDrawers = KitchenDrawerBlock.streamWoodDrawers().map(FurnitureBlock::getBlock).toArray(KitchenDrawerBlock[]::new);
        KitchenWallDrawerSmallBlock[] woodWallSmallDrawers = KitchenWallDrawerSmallBlock.streamWoodWallSmallDrawers().map(FurnitureBlock::getBlock).toArray(KitchenWallDrawerSmallBlock[]::new);
        KitchenCounterOvenBlock[] woodCounterOvens = KitchenCounterOvenBlock.streamWoodCounterOvens().map(FurnitureBlock::getBlock).toArray(KitchenCounterOvenBlock[]::new);

        KitchenSinkBlock[] woodSinks = KitchenSinkBlock.streamWoodSinks().map(FurnitureBlock::getBlock).toArray(KitchenSinkBlock[]::new);

        BasicChairBlock[] woodBasicChairs = BasicChairBlock.streamWoodBasicChairs().map(FurnitureBlock::getBlock).toArray(BasicChairBlock[]::new);

        BasicTableBlock[] woodBasicTables = BasicTableBlock.streamWoodBasicTables().map(FurnitureBlock::getBlock).toArray(BasicTableBlock[]::new);
        ClassicChairBlock[] woodClassicChairs = ClassicChairBlock.streamWoodClassicChairs().map(FurnitureBlock::getBlock).toArray(ClassicChairBlock[]::new);
        ClassicChairDyeableBlock[] woodDyeableClassicChairs = ClassicChairDyeableBlock.streamWoodDyeableChair().map(FurnitureBlock::getBlock).toArray(ClassicChairDyeableBlock[]::new);
        ClassicStoolBlock[] woodClassicStools = ClassicStoolBlock.streamWoodClassicStools().map(FurnitureBlock::getBlock).toArray(ClassicStoolBlock[]::new);
        ClassicTableBlock[] woodClassicTables = ClassicTableBlock.streamWoodClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicTableBlock[]::new);
        DinnerChairBlock[] woodDinnerChairs = DinnerChairBlock.streamWoodDinnerChairs().map(FurnitureBlock::getBlock).toArray(DinnerChairBlock[]::new);
        DinnerTableBlock[] woodDinnerTables = DinnerTableBlock.streamWoodDinnerTables().map(FurnitureBlock::getBlock).toArray(DinnerTableBlock[]::new);
        LogStoolBlock[] woodLogStools = LogStoolBlock.streamWoodLogStools().map(FurnitureBlock::getBlock).toArray(LogStoolBlock[]::new);
        LogTableBlock[] woodLogTables = LogTableBlock.streamWoodLogTables().map(FurnitureBlock::getBlock).toArray(LogTableBlock[]::new);
        ModernChairBlock[] woodModernChairs = ModernChairBlock.streamWoodModernChairs().map(FurnitureBlock::getBlock).toArray(ModernChairBlock[]::new);
        ModernDinnerTableBlock[] woodModernDinnerTables = ModernDinnerTableBlock.streamWoodModernDinnerTables().map(FurnitureBlock::getBlock).toArray(ModernDinnerTableBlock[]::new);
        RawLogTableBlock[] logTables = RawLogTableBlock.streamLogTables().map(FurnitureBlock::getBlock).toArray(RawLogTableBlock[]::new);

        ClassicNightstandBlock[] woodClassicNightstands = ClassicNightstandBlock.streamWoodClassicNightstands().map(FurnitureBlock::getBlock).toArray(ClassicNightstandBlock[]::new);
        ModernStoolBlock[] woodModernStools = ModernStoolBlock.streamWoodModernStools().map(FurnitureBlock::getBlock).toArray(ModernStoolBlock[]::new);
        SimpleStoolBlock[] woodSimpleStools = SimpleStoolBlock.streamWoodSimpleStools().map(FurnitureBlock::getBlock).toArray(SimpleStoolBlock[]::new);
        SimpleSofaBlock[] simpleSofas = SimpleSofaBlock.streamSimpleSofas().map(FurnitureBlock::getBlock).toArray(SimpleSofaBlock[]::new);
        ArmChairColoredBlock[] armChairDyeables = ArmChairColoredBlock.streamArmChairColored().map(FurnitureBlock::getBlock).toArray(ArmChairColoredBlock[]::new);
        ArmChairBlock[] armChairs = ArmChairBlock.streamArmChairs().map(FurnitureBlock::getBlock).toArray(ArmChairBlock[]::new);
        WorkingTableBlock[] workingTables = WorkingTableBlock.streamWorkingTables().toList().toArray(new WorkingTableBlock[0]);
        HerringbonePlankBlock[] herringbonePlanks = HerringbonePlankBlock.streamPlanks().map(FurnitureBlock::getBlock).toArray(HerringbonePlankBlock[]::new);
        SimpleBunkLadderBlock[] simpleBunkLadders = SimpleBunkLadderBlock.streamSimpleBunkLadder().map(FurnitureBlock::getBlock).toArray(SimpleBunkLadderBlock[]::new);

        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(showerTowels)
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
                .add(woodWallDrawers)
                .add(woodWallCounters)
                .add(woodWallSmallDrawers)
                .add(simpleBunkLadders)
                .add(classicBeds)
                .add(PaladinFurnitureModBlocksItems.BASIC_LAMP);

        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(PaladinFurnitureModBlocksItems.RAW_CONCRETE_POWDER);

        this.getOrCreateTagBuilder(BlockTags.BEDS)
                .add(simpleBeds)
                .add(classicBeds);

        this.getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .add(simpleBunkLadders);

        this.getOrCreateTagBuilder(Tags.getTuckableBlocks())
                .add(woodBasicTables)
                .add(stoneBasicTables)
                .add(woodClassicTables)
                .add(stoneClassicTables)
                .add(woodDinnerTables)
                .add(stoneDinnerTables)
                .add(woodModernDinnerTables)
                .add(stoneModernDinnerTables)
                .add(woodLogTables)
                .add(stoneNaturalTables)
                .add(logTables);
    }

    protected AbstractTagProvider.ObjectBuilder<Block> getOrCreateTagBuilder(Tag.Identified<Block> tag) {
        Tag.Builder builder = this.getTagBuilder(tag);
        return PFMAbstractTagProvider$ObjectBuilderMixin.newTagProvider(builder, Registry.BLOCK, "pfm");
    }
    private final Map<Identifier, Tag.Builder> tagBuilders = Maps.newLinkedHashMap();

    protected <T> Tag.Builder getTagBuilder(Tag.Identified<T> tag) {
        return this.tagBuilders.computeIfAbsent(tag.getId(), (id) -> new Tag.Builder());
    }

    public void run(DataCache cache) {
        this.tagBuilders.clear();
        this.generateTags();
        this.tagBuilders.forEach((id, builder) -> {
            List list = builder.streamEntries().filter(trackedEntry -> !trackedEntry.getEntry().canAdd(Registry.BLOCK::containsId, this.tagBuilders::containsKey)).collect(Collectors.toList());
            if (!list.isEmpty()) {
                throw new IllegalArgumentException(String.format("Couldn't define tag %s as it is missing following references: %s", id, list.stream().map(Objects::toString).collect(Collectors.joining(","))));
            }
            JsonObject jsonObject = builder.toJson();
            Path path = this.getOutput(id);
            try {
                String string = PFMDataGen.GSON.toJson(jsonObject);
                String string2 = PFMDataGen.SHA1.hashUnencodedChars(string).toString();
                if (!Objects.equals(cache.getOldSha1(path), string2) || !Files.exists(path, new LinkOption[0])) {
                    Files.createDirectories(path.getParent(), new FileAttribute[0]);
                    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, new OpenOption[0]);){
                        bufferedWriter.write(string);
                    }
                }
                cache.updateSha1(path, string2);
            }
            catch (IOException iOException) {
                PFMDataGen.LOGGER.error("Couldn't save tags to {}", path, iOException);
            }
        });
    }

    protected Path getOutput(Identifier id) {
        return PFMRuntimeResources.getResourceDirectory().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }
}