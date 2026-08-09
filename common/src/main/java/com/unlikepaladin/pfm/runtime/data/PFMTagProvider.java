package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.data.PFMTag;
import com.unlikepaladin.pfm.data.PFMTags;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PFMTagProvider extends PFMProvider {
    public PFMTagProvider(PFMGenerator parent) {
        super(parent, "PFM Tags");
        parent.setProgress("Generating Tags");
    }

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
        ClassicChairBlock[] stoneClassicChairs = ClassicChairBlock.streamStoneClassicChairs().map(FurnitureBlock::getBlock).toArray(ClassicChairBlock[]::new);
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
        BasicCoffeeTableBlock[] stoneBasicCoffeeTables = BasicCoffeeTableBlock.streamStoneBasicTables().map(FurnitureBlock::getBlock).toArray(BasicCoffeeTableBlock[]::new);
        ModernCoffeeTableBlock[] stoneModernCoffeeTables = ModernCoffeeTableBlock.streamStoneModernCoffeeTables().map(FurnitureBlock::getBlock).toArray(ModernCoffeeTableBlock[]::new);
        ClassicCoffeeTableBlock[] stoneClassicCoffeeTables = ClassicCoffeeTableBlock.streamStoneClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicCoffeeTableBlock[]::new);
        BasicDeskBlock[] stoneBasicDesks = BasicDeskBlock.streamStoneBasicDesks().map(FurnitureBlock::getBlock).toArray(BasicDeskBlock[]::new);
        BasicDeskCabinetBlock[] stoneBasicDeskCabinets = BasicDeskCabinetBlock.streamStoneBasicDeskCabinets().map(FurnitureBlock::getBlock).toArray(BasicDeskCabinetBlock[]::new);
        ClassicDeskBlock[] stoneClassicDesks = ClassicDeskBlock.streamStoneClassicDesks().map(FurnitureBlock::getBlock).toArray(ClassicDeskBlock[]::new);
        ClassicDeskCabinetBlock[] stoneClassicDeskCabinets = ClassicDeskCabinetBlock.streamStoneClassicDeskCabinets().map(FurnitureBlock::getBlock).toArray(ClassicDeskCabinetBlock[]::new);

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

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTags(showerTowels)
                .addTags(stoneCounters)
                .addTags(stoneCabinets)
                .addTags(stoneDrawers)
                .addTags(stoneCounterOvens)
                .addTags(stoneSinks)
                .addTags(stoneBasicChairs)
                .addTags(stoneBasicTables)
                .addTags(stoneClassicChairs)
                .addTags(stoneDyeableClassicChairs)
                .addTags(stoneClassicStools)
                .addTags(stoneClassicTables)
                .addTags(stoneDinnerChairs)
                .addTags(stoneDinnerTables)
                .addTags(stoneModernDinnerTables)
                .addTags(stoneModernChairs)
                .addTags(stoneModernStools)
                .addTags(stoneSimpleStools)
                .addTags(pendantLights)
                .addTags(simpleLights)
                .addTags(fridges)
                .addTags(freezers)
                .addTags(lightSwitches)
                .addTags(microwaves)
                .addTags(kitchenStovetops)
                .addTags(ironStoves)
                .addTags(froggyChairs)
                .addTags(stove)
                .addTags(stoneWallCounters)
                .addTags(stoneWallDrawers)
                .addTags(stoneWallSmallDrawers)
                .addTags(stoneNaturalTables)
                .addTags(stoneClassicNightstands)
                .addTags(plates)
                .addTags(cutleries)
                .addTags(basicToilets)
                .addTags(rangeHoods)
                .addTags(PaladinFurnitureModBlocksItems.RAW_CONCRETE)
                .addTags(PaladinFurnitureModBlocksItems.IRON_CHAIN)
                .addTags(sinkBlocks)
                .addTags(PaladinFurnitureModBlocksItems.TOASTER_BLOCK)
                .addTags(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE)
                .addTags(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD)
                .addTags(PaladinFurnitureModBlocksItems.BASIC_BATHTUB)
                .addTags(PaladinFurnitureModBlocksItems.TRASHCAN)
                .addTags(PaladinFurnitureModBlocksItems.MESH_TRASHCAN)
                .addTags(stoneBasicCoffeeTables)
                .addTags(stoneModernCoffeeTables)
                .addTags(stoneClassicCoffeeTables)
                .addTags(stoneBasicDesks)
                .addTags(stoneBasicDeskCabinets)
                .addTags(stoneClassicDesks)
                .addTags(stoneClassicDeskCabinets);

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
        BasicCoffeeTableBlock[] woodBasicCoffeeTables = BasicCoffeeTableBlock.streamWoodBasicTables().map(FurnitureBlock::getBlock).toArray(BasicCoffeeTableBlock[]::new);
        ModernCoffeeTableBlock[] woodModernCoffeeTables = ModernCoffeeTableBlock.streamWoodModernCoffeeTables().map(FurnitureBlock::getBlock).toArray(ModernCoffeeTableBlock[]::new);
        ClassicCoffeeTableBlock[] woodClassicCoffeeTables = ClassicCoffeeTableBlock.streamWoodClassicTables().map(FurnitureBlock::getBlock).toArray(ClassicCoffeeTableBlock[]::new);

        ClassicNightstandBlock[] woodClassicNightstands = ClassicNightstandBlock.streamWoodClassicNightstands().map(FurnitureBlock::getBlock).toArray(ClassicNightstandBlock[]::new);
        ModernStoolBlock[] woodModernStools = ModernStoolBlock.streamWoodModernStools().map(FurnitureBlock::getBlock).toArray(ModernStoolBlock[]::new);
        SimpleStoolBlock[] woodSimpleStools = SimpleStoolBlock.streamWoodSimpleStools().map(FurnitureBlock::getBlock).toArray(SimpleStoolBlock[]::new);
        SimpleSofaBlock[] simpleSofas = SimpleSofaBlock.streamSimpleSofas().map(FurnitureBlock::getBlock).toArray(SimpleSofaBlock[]::new);
        ArmChairColoredBlock[] armChairDyeables = ArmChairColoredBlock.streamArmChairColored().map(FurnitureBlock::getBlock).toArray(ArmChairColoredBlock[]::new);
        ArmChairBlock[] armChairs = ArmChairBlock.streamArmChairs().map(FurnitureBlock::getBlock).toArray(ArmChairBlock[]::new);
        WorkingTableBlock[] workingTables = WorkingTableBlock.streamWorkingTables().toList().toArray(new WorkingTableBlock[0]);
        HerringbonePlankBlock[] herringbonePlanks = HerringbonePlankBlock.streamPlanks().map(FurnitureBlock::getBlock).toArray(HerringbonePlankBlock[]::new);
        SimpleBunkLadderBlock[] simpleBunkLadders = SimpleBunkLadderBlock.streamSimpleBunkLadder().map(FurnitureBlock::getBlock).toArray(SimpleBunkLadderBlock[]::new);
        BasicDeskBlock[] woodBasicDesks = BasicDeskBlock.streamWoodBasicDesks().map(FurnitureBlock::getBlock).toArray(BasicDeskBlock[]::new);
        BasicDeskCabinetBlock[] woodBasicDeskCabinets = BasicDeskCabinetBlock.streamWoodBasicDeskCabinets().map(FurnitureBlock::getBlock).toArray(BasicDeskCabinetBlock[]::new);
        ClassicDeskBlock[] woodClassicDesks= ClassicDeskBlock.streamWoodClassicDesks().map(FurnitureBlock::getBlock).toArray(ClassicDeskBlock[]::new);
        ClassicDeskCabinetBlock[] woodClassicDeskCabinets = ClassicDeskCabinetBlock.streamWoodClassicDeskCabinets().map(FurnitureBlock::getBlock).toArray(ClassicDeskCabinetBlock[]::new);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .addTags(showerTowels)
                .addTags(woodCounters)
                .addTags(woodCabinets)
                .addTags(woodDrawers)
                .addTags(woodCounterOvens)
                .addTags(woodSinks)
                .addTags(woodBasicChairs)
                .addTags(woodBasicTables)
                .addTags(woodClassicChairs)
                .addTags(woodDyeableClassicChairs)
                .addTags(woodClassicStools)
                .addTags(woodClassicTables)
                .addTags(woodDinnerChairs)
                .addTags(woodDinnerTables)
                .addTags(woodLogStools)
                .addTags(woodLogTables)
                .addTags(woodModernDinnerTables)
                .addTags(woodModernChairs)
                .addTags(woodModernStools)
                .addTags(woodSimpleStools)
                .addTags(simpleSofas)
                .addTags(armChairDyeables)
                .addTags(armChairs)
                .addTags(woodClassicNightstands)
                .addTags(workingTables)
                .addTags(herringbonePlanks)
                .addTags(simpleBeds)
                .addTags(woodWallDrawers)
                .addTags(woodWallCounters)
                .addTags(woodWallSmallDrawers)
                .addTags(simpleBunkLadders)
                .addTags(classicBeds)
                .addTags(logTables)
                .addTags(PaladinFurnitureModBlocksItems.BASIC_LAMP)
                .addTags(woodBasicCoffeeTables)
                .addTags(woodModernCoffeeTables)
                .addTags(woodClassicCoffeeTables)
                .addTags(woodBasicDesks)
                .addTags(woodBasicDeskCabinets)
                .addTags(woodClassicDesks)
                .addTags(woodClassicDeskCabinets);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTags(PaladinFurnitureModBlocksItems.RAW_CONCRETE_POWDER);

        getOrCreateTagBuilder(BlockTags.BEDS)
                .addTags(simpleBeds)
                .addTags(classicBeds);

        getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .addTags(simpleBunkLadders);

        getOrCreateTagBuilder(PFMTags.TUCKABLE_BLOCKS)
                .addTags(woodBasicTables)
                .addTags(stoneBasicTables)
                .addTags(woodClassicTables)
                .addTags(stoneClassicTables)
                .addTags(woodDinnerTables)
                .addTags(stoneDinnerTables)
                .addTags(woodModernDinnerTables)
                .addTags(stoneModernDinnerTables)
                .addTags(woodLogTables)
                .addTags(stoneNaturalTables)
                .addTags(logTables)
                .addTags(woodBasicDesks)
                .addTags(stoneBasicDesks)
                .addTags(woodClassicDesks)
                .addTags(stoneClassicDesks);

        getOrCreateTagBuilder(PFMTags.FURNITURE)
                .addTags(PaladinFurnitureModBlocksItems.BLOCKS.toArray(Block[]::new));

        PaladinFurnitureMod.pfmModCompatibilities.forEach(PFMModCompatibility::generateTags);
    }

    public static PFMTag<Block> getOrCreateTagBuilder(TagKey<Block> tag) {
        TagBuilder builder = getTagBuilder(tag);
        return getProviderPlatform(builder, BuiltInRegistries.BLOCK, "pfm");
    }

    @ExpectPlatform
    private static <T> PFMTag<T> getProviderPlatform(TagBuilder builder, Registry<T> registry, String modID) {
        throw new AssertionError();
    }

    private static final Map<Identifier, TagBuilder> tagBuilders = Maps.newLinkedHashMap();

    public static <T> TagBuilder getTagBuilder(TagKey<T> tag) {
        return tagBuilders.computeIfAbsent(tag.location(), (id) -> new TagBuilder());
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();
        tagBuilders.clear();
        this.generateTags();
        tagBuilders.forEach((id, builder) -> {
            List<TagEntry> list = builder.build();
            List<TagEntry> list2 = list.stream().filter((tag) -> !tag.verifyIfPresent(BuiltInRegistries.BLOCK::containsKey, tagBuilders::containsKey)).toList();
            if (!list2.isEmpty()) {
                throw new IllegalArgumentException(String.format("Couldn't define tag %s as it is missing following references: %s", id, list.stream().map(Objects::toString).collect(Collectors.joining(","))));
            }
            DataResult<JsonElement> jsonObject = TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(builder.build(), false));
            Path path = this.getResultItem(id);
            if (jsonObject.error().isEmpty())
                enqueueJsonWrite(getWriteQueue(), path, jsonObject.getOrThrow());
        });

        endProviderRun();
    }

    protected Path getResultItem(Identifier id) {
        return getParent().getOutput().resolve("data/" + id.getNamespace() + "/tags/block/" + id.getPath() + ".json");
    }
}