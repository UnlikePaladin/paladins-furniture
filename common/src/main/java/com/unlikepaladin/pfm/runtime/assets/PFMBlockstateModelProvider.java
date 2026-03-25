package com.unlikepaladin.pfm.runtime.assets;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.mixin.PFMTextureSlotFactory;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PFMBlockstateModelProvider extends PFMProvider {

    public static Map<Block, ResourceLocation> modelPathMap = new HashMap<>();

    public PFMBlockstateModelProvider(PFMGenerator parent) {
        super(parent, "PFM Blockstates and Models");
        parent.setProgress("Generating Blockstates and Models");
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();

        Path path = getParent().getResultItem();

        Consumer<BlockStateGenerator> blockStateSupplierConsumer = blockStateSupplier -> {
            Path jsonPath = getBlockStateJsonPath(path, blockStateSupplier.getBlock());
            String jsonContent = PFMDataGenerator.GSON.toJson(blockStateSupplier.get());
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        BiConsumer<ResourceLocation, Supplier<JsonElement>> identifierSupplierBiConsumer = (identifier, supplier) -> {
            Path jsonPath = getModelJsonPath(path, identifier);
            String jsonContent = PFMDataGenerator.GSON.toJson(supplier.get());
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        Set<ResourceLocation> models = new HashSet<>();
        new PFMBlockStateModelGenerator(this, blockStateSupplierConsumer, identifierSupplierBiConsumer).registerModelsAndStates();
        modelPathMap.keySet().forEach(block -> {
            Item item = Item.BY_BLOCK.get(block);
            if (item != null) {
                ResourceLocation identifier = ModelLocationUtils.getModelLocation(item);
                if (!models.contains(identifier)) {
                    Path jsonPath = getModelJsonPath(path, identifier);
                    enqueueJsonWrite(getWriteQueue(), jsonPath, new DelegatedModel(modelPathMap.get(block)).get());
                    models.add(identifier);
                }
            }
        });

        waitForWrite();
        endProviderRun();
    }

    private static Path getBlockStateJsonPath(Path root, Block block) {
        ResourceLocation identifier = Registry.BLOCK.getKey(block);
        return root.resolve("assets/" + identifier.getNamespace() + "/blockstates/" + identifier.getPath() + ".json");
    }

    private static Path getModelJsonPath(Path root, ResourceLocation id) {
        return root.resolve("assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json");
    }


    static class PFMBlockStateModelGenerator {
        public static Map<ModelTemplate, ResourceLocation> ModelIDS = new HashMap<>();

        final Consumer<BlockStateGenerator> blockStateCollector;
        final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelCollector;

        final List<ResourceLocation> generatedStates = new ArrayList<>();
        final PFMBlockstateModelProvider provider;

        PFMBlockStateModelGenerator(PFMBlockstateModelProvider provider, Consumer<BlockStateGenerator> blockStateCollector, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelCollector) {
            this.provider = provider;
            this.blockStateCollector = blockStateCollector;
            this.modelCollector = modelCollector;
        }

        public void registerModelsAndStates() {
            provider.getParent().log("Generating Chairs and Stools");
            registerTuckableChairs();
            provider.getParent().log("Generating Tables");
            registerTables();
            provider.getParent().log("Generating Nightstands Chairs");
            registerNightStands();
            provider.getParent().log("Generating Beds");
            registerBeds();
            provider.getParent().log("Generating Bunk Ladders");
            registerLadders();
            provider.getParent().log("Generating Kitchen Counters");
            registerCounters();
            provider.getParent().log("Generating Lamps");
            registerLamp();
            provider.getParent().log("Generating Desks");
            registerDesks();
            provider.getParent().log("Generating Miscellaneous Blocks");
            registerMiscellaneousBlocks();
        }

        public void registerMiscellaneousBlocks() {
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(HerringbonePlankBlock.class).getVariantToBlockMap(), "herringbone_planks", PFMBlockStateModelGenerator::createSingleStateBlockState);
        }

        public void registerTuckableChairs() {
            provider.getParent().log("Basic Chairs");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap(), "chair", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMapNonBase(), "chair", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Dining Chairs");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMap(), "chair_dinner", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMapNonBase(), "chair_dinner", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Modern Chairs");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMap(), "chair_modern", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMapNonBase(), "chair_modern", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Classic Chairs");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMap(), "chair_classic", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMapNonBase(), "chair_classic", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Log Stools");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogStoolBlock.class).getVariantToBlockMap(), "log_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Simple Stools");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMap(), "simple_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMapNonBase(), "simple_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Classic Stools");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMap(), "classic_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMapNonBase(), "classic_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Modern Stools");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMap(), "modern_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMapNonBase(), "modern_stool", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
        }

        public void registerTables() {
            provider.getParent().log("Basic Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMap(), "table_basic", PFMBlockStateModelGenerator::createAxisOrientableTableBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMapNonBase(), "table_basic", PFMBlockStateModelGenerator::createAxisOrientableTableBlockState);

            provider.getParent().log("Classic Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMap(), "table_classic", PFMBlockStateModelGenerator::createSingleStateBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMapNonBase(), "table_classic", PFMBlockStateModelGenerator::createSingleStateBlockState);

            provider.getParent().log("Log Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMap(), "log_table", PFMBlockStateModelGenerator::createOrientableTableBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMapNonBase(), "log_table", PFMBlockStateModelGenerator::createOrientableTableBlockState);

            provider.getParent().log("Raw Log Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMap(), "log_table", PFMBlockStateModelGenerator::createOrientableTableBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMapNonBase(), "log_table", PFMBlockStateModelGenerator::createOrientableTableBlockState);

            provider.getParent().log("Dining Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMap(), "dinner_table", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMapNonBase(), "dinner_table", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Modern Dining Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMap(), "modern_dinner_table", (block, identifiers) -> createAxisOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMapNonBase(), "modern_dinner_table", (block, identifiers) -> createAxisOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Basic Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_basic", PFMBlockStateModelGenerator::createAxisOrientableTableBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_basic", PFMBlockStateModelGenerator::createAxisOrientableTableBlockState);

            provider.getParent().log("Modern Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_modern", (block, identifiers) -> createAxisOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_modern", (block, identifiers) -> createAxisOrientableTableBlockState(block, identifiers, 90));

            provider.getParent().log("Classic Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_classic", PFMBlockStateModelGenerator::createSingleStateBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_classic", PFMBlockStateModelGenerator::createSingleStateBlockState);

        }

        public void registerDesks() {
            provider.getParent().log("Basic Desks");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMap(), "desk_basic", PFMBlockStateModelGenerator::createSingleStateBlockState);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMapNonBase(), "desk_basic", PFMBlockStateModelGenerator::createSingleStateBlockState);

            provider.getParent().log("Basic Desk Cabinets");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMap(), "desk_cabinet_basic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), "desk_cabinet_basic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Classic Desks");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMap(), "desk_classic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMapNonBase(), "desk_classic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Classic Desk Cabinets");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMap(), "desk_classic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), "desk_classic", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

        }

        public void registerNightStands() {
            provider.getParent().log("Classic Nightstands");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMap(), "classic_nightstand", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMapNonBase(), "classic_nightstand", (block, identifiers) -> createOrientableTableBlockState(block, identifiers, 90));
        }

        public void registerBeds() {
            provider.getParent().log("Simple Beds");
            generateModelAndBlockStateForBed(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList(), "simple_bed", PFMBlockStateModelGenerator::createBedBlockState);
            provider.getParent().log("Classic Beds");
            generateModelAndBlockStateForBed(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(ClassicBedBlock.class).getVariantToBlockMapList(), "simple_bed", PFMBlockStateModelGenerator::createBedBlockState);
        }

        public void registerLadders() {
            provider.getParent().log("Simple Bunk Ladders");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(SimpleBunkLadderBlock.class).getVariantToBlockMap(), "simple_bunk_ladder", PFMBlockStateModelGenerator::createOrientableTableBlockState);
        }

        public void registerCounters() {
            provider.getParent().log("Kitchen Counters");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMap(), "kitchen_counter", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMapNonBase(), "kitchen_counter", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Kitchen Drawers");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMap(), "kitchen_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMapNonBase(), "kitchen_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Kitchen Cabinets");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMap(), "kitchen_cabinet", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMapNonBase(), "kitchen_cabinet", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Kitchen Wall Drawers");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMap(), "kitchen_wall_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Kitchen Wall Cabinets");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMap(), "kitchen_wall_counter", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_counter", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Small Kitchen Cabinets");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMap(), "kitchen_wall_small_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_small_drawer", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);

            provider.getParent().log("Kitchen Counter Ovens");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMap(), "kitchen_counter_oven", (block, identifiers) -> createOrientableUvLockedBlock(block, identifiers, "", "", "", 180));
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMapNonBase(), "kitchen_counter_oven", (block, identifiers) -> createOrientableUvLockedBlock(block, identifiers, "", "", "", 180));

            provider.getParent().log("Kitchen Sinks");
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMap(), "kitchen_sink", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
            generateBlockStateForBlock(PaladinFurnitureModBlocksItems.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMapNonBase(), "kitchen_sink", PFMBlockStateModelGenerator::createOrientableUvLockedBlock);
        }

        public void registerLamp() {
            provider.getParent().log("Basic Lamps");
            ResourceLocation modelID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.BASIC_LAMP);
            this.blockStateCollector.accept(createSingleStateBlockState(PaladinFurnitureModBlocksItems.BASIC_LAMP, List.of(modelID)));
            PFMBlockstateModelProvider.modelPathMap.put(PaladinFurnitureModBlocksItems.BASIC_LAMP, UnbakedBasicLampModel.getItemModelId());
        }

        public static TextureMapping createPlankBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            ResourceLocation top = ModelHelper.getTextureId(variantBase.getBaseBlock());
            ResourceLocation legs =  ModelHelper.getTextureId(variantBase.getBaseBlock());
            return new TextureMapping().put(TextureSlot.TEXTURE, top).put(LOG_KEY, legs);

        }

        public static TextureMapping createRawBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            ResourceLocation top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            ResourceLocation legs = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            return new TextureMapping().put(TextureSlot.TEXTURE, top).put(LOG_KEY, legs);
        }

        public static TextureMapping createPlankLogBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            ResourceLocation top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getBaseBlock());
            ResourceLocation legs = stripped ? ModelHelper.getTextureId(variantBase.getBaseBlock()) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            return new TextureMapping().put(TextureSlot.TEXTURE, top).put(LOG_KEY, legs);
        }

        public static TextureMapping createCounterBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            ResourceLocation counterBase = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getBaseBlock());
            ResourceLocation counterTop = stripped ? ModelHelper.getTextureId(variantBase.getBaseBlock()) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            if (variantBase.identifier.getPath().equals("granite")) {
                counterTop = ModelHelper.getTextureId(Blocks.POLISHED_GRANITE);
                counterBase = ModelHelper.getTextureId(Blocks.WHITE_TERRACOTTA);
            } else if (variantBase.identifier.getPath().equals("calcite") || variantBase.identifier.getPath().equals("netherite")) {
                ResourceLocation temp = counterBase;
                counterBase = counterTop;
                counterTop  = temp;
            } else if (variantBase.identifier.getPath().equals("andesite")) {
                counterTop = ModelHelper.getTextureId(Blocks.POLISHED_ANDESITE);
                counterBase = ModelHelper.getTextureId(Blocks.STRIPPED_OAK_LOG);
            } else if (variantBase.identifier.getPath().equals("deepslate")) {
                counterTop = ModelHelper.getTextureId(Blocks.POLISHED_DEEPSLATE);
                counterBase = ModelHelper.getTextureId(Blocks.DARK_OAK_PLANKS);
            } else if (variantBase.identifier.getPath().equals("blackstone")) {
                counterTop = ModelHelper.getTextureId(Blocks.POLISHED_BLACKSTONE);
                counterBase = ModelHelper.getTextureId(Blocks.CRIMSON_PLANKS);
            }
            return new TextureMapping().put(TextureSlot.TEXTURE, counterBase).put(LOG_KEY, counterTop);
        }

        public static TextureMapping createLogLogTopBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            ResourceLocation legs = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            ResourceLocation top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log"), "_top") : ModelHelper.getTextureId(variantBase.getSecondaryBlock(), "_top");
            return new TextureMapping().put(LOG_KEY, legs).put(LOG_TOP_KEY, top);
        }

        public void generateBlockStateForBlock(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, BiFunction<Block, List<ResourceLocation>, BlockStateGenerator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(Registry.BLOCK.getKey(block))) {
                    ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                    ResourceLocation id = new ResourceLocation(modelID.getNamespace(), "block/" + blockName);
                    List<ResourceLocation> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(Registry.BLOCK.getKey(block));
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends Set<?>> variantBaseHashMap, String blockName, BiFunction<Block, List<ResourceLocation>, BlockStateGenerator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                blockList.forEach(block1 -> {
                Block block = (Block) block1;
                if (!generatedStates.contains(Registry.BLOCK.getKey(block))) {
                    ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                    ResourceLocation id = new ResourceLocation(modelID.getNamespace(), "block/" + blockName);
                    List<ResourceLocation> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(Registry.BLOCK.getKey(block));
                }});
            });

        }

        public void generateModelAndBlockStateForVariants(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, ModelTemplate[] models, BiFunction<Block, List<ResourceLocation>, BlockStateGenerator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMapping> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(Registry.BLOCK.getKey(block))) {
                    String blockName2 = blockName;

                    boolean stripped = block.getDescriptionId().contains("stripped");
                    TextureMapping blockTexture = textureBiFunction.apply(stripped, variantBase);
                    List<ResourceLocation> ids = new ArrayList<>();
                    String strippedprefix  = stripped ? "stripped_" : "";
                    if (block instanceof RawLogTableBlock) {
                        blockName2 = "raw_log_table";
                    }
                    if (variantBase instanceof StoneVariant && block instanceof LogTableBlock) {
                        blockName2 = blockName2.replace("log", "natural");
                    } else if (variantBase.isNetherWood() && block instanceof LogTableBlock) {
                        blockName2 = blockName2.replace("log", "stem");
                    }
                    ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                    for (ModelTemplate model : models) {
                        ResourceLocation id = new ResourceLocation(modelID.getNamespace(), ModelIDS.get(model).getPath().replace("template_", "").replace("template", "").replaceAll(blockName, strippedprefix + variantBase.getSerializedName() + "_" + blockName2).replace("block/", "block/" + blockName + "/").replace("//", "/"));
                        model.create(id, blockTexture, this.modelCollector);
                        ids.add(id);
                    }
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                    generatedStates.add(Registry.BLOCK.getKey(block));
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends List<?>> variantBaseHashMap, String blockName, ModelTemplate[] models, TriFunc<Block, List<ResourceLocation>, String, BlockStateGenerator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMapping> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                List<ResourceLocation> allids = new ArrayList<>();
                blockList.forEach(block1 -> {
                    Block block = (Block) block1;
                    if (!generatedStates.contains(Registry.BLOCK.getKey(block))) {
                        boolean stripped = block.getDescriptionId().contains("stripped");
                        TextureMapping blockTexture = textureBiFunction.apply(stripped, variantBase);
                        ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                        String color = block instanceof SimpleBedBlock ? ((SimpleBedBlock) block).getPFMColor().getSerializedName() : "";
                        List<ResourceLocation> ids = new ArrayList<>();
                        for (ModelTemplate model : models) {
                            ResourceLocation id = new ResourceLocation(modelID.getNamespace(), ModelIDS.get(model).getPath().replaceAll("white", color).replaceAll("template", variantBase.getSerializedName()));

                            if (allids.contains(id))
                                continue;
                            if (model == models[0]) {
                                block(blockName+"/template/full/"+ blockName+ "_"+color, TextureSlot.TEXTURE).create(id, blockTexture, this.modelCollector);
                            }  else {
                                model.create(id, blockTexture, this.modelCollector);
                            }
                            ids.add(id);
                        }
                        allids.addAll(ids);
                        this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids, color));
                        PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                        generatedStates.add(Registry.BLOCK.getKey(block));
                    }
                });
            });
        }
        public static final TextureSlot LOG_KEY = of("log");
        public static final TextureSlot LOG_TOP_KEY = of("log_top");
        public static final ModelTemplate[] TEMPLATE_CHAIR = new ModelTemplate[]{block("chair/template_chair", TextureSlot.TEXTURE, LOG_KEY), block("chair/template_chair", "_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CHAIR_DINNER = new ModelTemplate[]{block("chair_dinner/template_chair_dinner", TextureSlot.TEXTURE, LOG_KEY), block("chair_dinner/template_chair_dinner","_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CHAIR_CLASSIC = new ModelTemplate[]{block("chair_classic/template_chair_classic", TextureSlot.TEXTURE, LOG_KEY), block("chair_classic/template_chair_classic","_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CHAIR_MODERN = new ModelTemplate[]{block("chair_modern/template_chair_modern", TextureSlot.TEXTURE, LOG_KEY), block("chair_modern/template_chair_modern","_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_LOG_STOOL = new ModelTemplate[]{block("log_stool/log_stool", LOG_KEY, LOG_TOP_KEY), block("log_stool/log_stool", "_tucked",LOG_KEY, LOG_TOP_KEY)};
        public static final ModelTemplate[] TEMPLATE_SIMPLE_STOOL = new ModelTemplate[]{block("simple_stool/simple_stool", TextureSlot.TEXTURE, LOG_KEY), block("simple_stool/simple_stool", "_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CLASSIC_STOOL = new ModelTemplate[]{block("classic_stool/classic_stool", TextureSlot.TEXTURE, LOG_KEY), block("classic_stool/classic_stool", "_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_MODERN_STOOL = new ModelTemplate[]{block("modern_stool/modern_stool", TextureSlot.TEXTURE, LOG_KEY), block("modern_stool/modern_stool", "_tucked", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_BASIC_TABLE_ARRAY = new ModelTemplate[]{block("table_basic/table_basic", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_base", TextureSlot.TEXTURE, LOG_KEY),  block("table_basic/table_basic_north_east", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_west", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_east", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_west", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east_top", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west_top", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_east_west_north", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_east_west_south", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east_bottom", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west_bottom", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_east_corner", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_west_corner", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_east_corner", TextureSlot.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_west_corner", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CLASSIC_TABLE_ARRAY = new ModelTemplate[]{block("table_classic/table_classic", TextureSlot.TEXTURE, LOG_KEY), block("table_classic/table_classic_middle", TextureSlot.TEXTURE, LOG_KEY), block("table_classic/table_classic_one_uved", TextureSlot.TEXTURE, LOG_KEY), block("table_classic/table_classic_one", TextureSlot.TEXTURE, LOG_KEY), block("table_classic/table_classic_two_uved", TextureSlot.TEXTURE, LOG_KEY), block("table_classic/table_classic_two", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_LOG_TABLE_ARRAY = new ModelTemplate[]{block("log_table/log_table", TextureSlot.TEXTURE, LOG_KEY), block("log_table/log_table_right", TextureSlot.TEXTURE, LOG_KEY), block("log_table/log_table_left", TextureSlot.TEXTURE, LOG_KEY), block("log_table/log_table_middle", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_DINNER_TABLE_ARRAY = new ModelTemplate[]{block("dinner_table/dinner_table", TextureSlot.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_middle", TextureSlot.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_right", TextureSlot.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_MODERN_DINNER_TABLE_ARRAY = new ModelTemplate[]{block("table_modern_dinner/table_modern_dinner", TextureSlot.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_middle", TextureSlot.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_right", TextureSlot.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_CLASSIC_NIGHTSTAND_ARRAY = new ModelTemplate[]{block("classic_nightstand/classic_nightstand", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_middle", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_right", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_left", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_open", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_middle_open", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_right_open", TextureSlot.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_left_open", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_SIMPLE_BED_ARRAY = new ModelTemplate[]{block("simple_bed/template/full/simple_bed_white", TextureSlot.TEXTURE), block("simple_bed/template/head/simple_bed_head", TextureSlot.TEXTURE), block("simple_bed/template/head/simple_bed_head_left", TextureSlot.TEXTURE), block("simple_bed/template/head/simple_bed_head_right", TextureSlot.TEXTURE), block("simple_bed/template/foot/simple_bed_foot", TextureSlot.TEXTURE), block("simple_bed/template/foot/simple_bed_foot_right", TextureSlot.TEXTURE), block("simple_bed/template/foot/simple_bed_foot_left", TextureSlot.TEXTURE), block("simple_bed/template/bunk/foot/simple_bed_foot_left", TextureSlot.TEXTURE), block("simple_bed/template/bunk/foot/simple_bed_foot_right", TextureSlot.TEXTURE), block("simple_bed/template/bunk/head/simple_bed_head", TextureSlot.TEXTURE)};
        public static final ModelTemplate[] TEMPLATE_CLASSIC_BED_ARRAY = new ModelTemplate[]{block("classic_bed/template/full/classic_bed_white", TextureSlot.TEXTURE), block("classic_bed/template/head/classic_bed_head", TextureSlot.TEXTURE), block("classic_bed/template/head/classic_bed_head_left", TextureSlot.TEXTURE), block("classic_bed/template/head/classic_bed_head_right", TextureSlot.TEXTURE), block("classic_bed/template/foot/classic_bed_foot", TextureSlot.TEXTURE), block("classic_bed/template/foot/classic_bed_foot_right", TextureSlot.TEXTURE), block("classic_bed/template/foot/classic_bed_foot_left", TextureSlot.TEXTURE), block("classic_bed/template/bunk/foot/classic_bed_foot_left", TextureSlot.TEXTURE), block("classic_bed/template/bunk/foot/classic_bed_foot_right", TextureSlot.TEXTURE)};
        public static final ModelTemplate[] TEMPLATE_SIMPLE_BUNK_LADDER_ARRAY = new ModelTemplate[]{block("simple_bunk_ladder/template/simple_ladder", TextureSlot.TEXTURE), block("simple_bunk_ladder/template/simple_ladder_top", TextureSlot.TEXTURE)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_COUNTER = new ModelTemplate[]{block("kitchen_counter/kitchen_counter", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_edge_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_edge_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_inner_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_inner_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_outer_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_outer_corner_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_DRAWER = new ModelTemplate[]{block("kitchen_drawer/kitchen_drawer", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_inner_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_inner_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_left_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_right_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_open_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_open_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_CABINET = new ModelTemplate[]{block("kitchen_cabinet/kitchen_cabinet", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_open_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_open_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_open_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_open_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_WALL_DRAWER = new ModelTemplate[]{block("kitchen_drawer/kitchen_drawer_middle", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_inner_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_inner_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_open_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_open_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_WALL_COUNTER = new ModelTemplate[]{block("kitchen_counter/kitchen_counter_middle", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_inner_corner_left", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_inner_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_outer_corner_right", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_outer_corner_left", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_WALL_DRAWER_SMALL = new ModelTemplate[]{block("kitchen_wall_drawer_small/kitchen_wall_drawer_small", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_wall_drawer_small/kitchen_wall_drawer_small", "_open", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_COUNTER_OVEN = new ModelTemplate[]{block("kitchen_counter_oven/kitchen_counter_oven", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven_middle",TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven", "_open", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven_middle", "_open", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_KITCHEN_SINK = new ModelTemplate[]{block("kitchen_sink/kitchen_sink", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_level1",TextureSlot.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_level2", TextureSlot.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_full", TextureSlot.TEXTURE, LOG_KEY)};
        public static final ModelTemplate[] TEMPLATE_LAMP_ARRAY = new ModelTemplate[]{block("basic_lamp/basic_lamp_bottom", TextureSlot.TEXTURE), block("basic_lamp/basic_lamp_middle", TextureSlot.TEXTURE),  block("basic_lamp/basic_lamp_single", TextureSlot.TEXTURE), block("basic_lamp/basic_lamp_top", TextureSlot.TEXTURE)};

        private static ModelTemplate make(TextureSlot ... requiredTextures) {
            return new ModelTemplate(Optional.empty(), Optional.empty(), requiredTextures);
        }

        private static ModelTemplate block(String parent, TextureSlot ... requiredTextures) {
            ResourceLocation id = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/" + parent);
            ModelTemplate model = new ModelTemplate(Optional.of(id), Optional.empty(), requiredTextures);
            ModelIDS.put(model, id);
            return model;
        }

        private static ModelTemplate item(String parent, TextureSlot ... requiredTextures) {
            return new ModelTemplate(Optional.of(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextures);
        }

        private static ModelTemplate block(String parent, String variant, TextureSlot ... requiredTextures) {
            ResourceLocation id = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/" + parent + variant);
            ModelTemplate model = new ModelTemplate(Optional.of(id), Optional.of(variant), requiredTextures);
            ModelIDS.put(model, id);
            return model;
        }

        private static TextureSlot of(String name) {
            return PFMTextureSlotFactory.newTextureKey(name, null);
        }

        private static TextureSlot of(String name, TextureSlot parent) {
            return PFMTextureSlotFactory.newTextureKey(name, parent);
        }

        private static BlockStateGenerator createSingleStateBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            Variant variant;
            String path = modelIdentifiers.get(0).getPath();
            //Ugly hack to get the folder name for the Baked Block ModelTemplate
            ResourceLocation id = new ResourceLocation(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            variant = (Variant.variant().with(VariantProperties.MODEL, id));
            return MultiVariantGenerator.multiVariant(block, variant);
        }
        private static BlockStateGenerator createAxisOrientableTableBlockState(Block block, List<ResourceLocation> modelIdentifiers, int rotation) {
            Map<Direction.Axis, Variant> variantMap = new HashMap<>();
            String path = modelIdentifiers.get(0).getPath();
            ResourceLocation id;

            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = new ResourceLocation(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90};
            for (int i = 0; rotationArray.length > i; i++) {
                if (rotationArray[i] + rotation > 90) {
                    if (rotationArray[i] == 90)
                        rotationArray[i] = 0;
                    else
                        rotationArray[i] = 90;
                } else {
                    rotationArray[i] += rotation;
                }
            }

            variantMap.put(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[0]))));
            variantMap.put(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[1]))));
            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS).generate(axis -> {
                for (Direction.Axis axis1 : variantMap.keySet()) {
                    if (axis.equals(axis1))
                        return variantMap.get(axis1);
                }
                return null;
            }));
        }
        private static BlockStateGenerator createAxisOrientableTableBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            return createAxisOrientableTableBlockState(block, modelIdentifiers, 0);
        }
        private static BlockStateGenerator createOrientableTableBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            return createOrientableTableBlockState(block,  modelIdentifiers, 0);
        }
        private static BlockStateGenerator createOrientableTableBlockState(Block block, List<ResourceLocation> modelIdentifiers, int rotation) {
            Map<Direction, Variant> variantMap = new HashMap<>();
            String path = modelIdentifiers.get(0).getPath();
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = new ResourceLocation(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                if (rotationArray[i] + rotation > 270) {
                    if (rotationArray[i] == 270)
                        rotationArray[i] = 0;
                    else
                        rotationArray[i] = 90;
                } else {
                    rotationArray[i] += rotation;
                }
            }
            variantMap.put(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[0]))));
            variantMap.put(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[1]))));
            variantMap.put(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[2]))));
            variantMap.put(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[3]))));
            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).generate(facing -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return variantMap.get(direction);
                }
                return null;
            }));
        }
        private static BlockStateGenerator createOrientableUvLockedBlock(Block block, List<ResourceLocation> modelIdentifiers){
            return createOrientableUvLockedBlock(block, modelIdentifiers, "", "", "", 0);
        }
        private static BlockStateGenerator createOrientableUvLockedBlock(Block block, List<ResourceLocation> modelIdentifiers, String override, String furnitureName, String replacement, int rotation) {
            Map<Direction, Variant> variantMap = new HashMap<>();
            String path = modelIdentifiers.get(0).getPath().replaceAll(override, "");
            String name = path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/'));
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = new ResourceLocation(modelIdentifiers.get(0).getNamespace(), name.replace(furnitureName, replacement));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                if (rotationArray[i] + rotation > 270) {
                    if (rotationArray[i] == 180)
                        rotationArray[i] = 0;
                    else
                        rotationArray[i] = 90;
                } else {
                    rotationArray[i] += rotation;
                }
            }
            variantMap.put(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[0]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[1]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[2]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[3]))).with(VariantProperties.UV_LOCK, true));
            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).generate(facing -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return variantMap.get(direction);
                }
                return null;
            }));
        }

        private static BlockStateGenerator createKitchenSink(Block block, List<ResourceLocation> modelIdentifiers) {
            Map<Direction, VariantProperties.Rotation> rotationMap = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};
            rotationMap.put(Direction.NORTH, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[0])));
            rotationMap.put(Direction.EAST, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[1])));
            rotationMap.put(Direction.SOUTH, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[2])));
            rotationMap.put(Direction.WEST, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[3])));

            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, AbstractSinkBlock.LEVEL_4).generate((facing, level) -> {
                return Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(level)).with(VariantProperties.Y_ROT, rotationMap.get(facing)).with(VariantProperties.UV_LOCK, true);
            }));
        }

        private static BlockStateGenerator createSmallKitchenDrawer(Block block, List<ResourceLocation> modelIdentifiers, String override, String furnitureName, String replacement) {
            Map<Direction, Variant> variantMap = new HashMap<>();
            Map<Direction, Variant> variantMapOpen = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};

            variantMap.put(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[0]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[1]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[2]))).with(VariantProperties.UV_LOCK, true));
            variantMap.put(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[3]))).with(VariantProperties.UV_LOCK, true));

            variantMapOpen.put(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[0]))).with(VariantProperties.UV_LOCK, true));
            variantMapOpen.put(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[1]))).with(VariantProperties.UV_LOCK, true));
            variantMapOpen.put(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[2]))).with(VariantProperties.UV_LOCK, true));
            variantMapOpen.put(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotation[3]))).with(VariantProperties.UV_LOCK, true));

            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.OPEN).generate((facing, open) -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return open ? variantMapOpen.get(direction) : variantMap.get(direction);
                }
                return null;
            }));
        }


        private static BlockStateGenerator createLadderBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            Condition.TerminalCondition northFalse = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).term(BlockStateProperties.UP, false);
            Condition.TerminalCondition northTrue = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).term(BlockStateProperties.UP, true);
            Condition.TerminalCondition eastFalse = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).term(BlockStateProperties.UP, false);
            Condition.TerminalCondition eastTrue = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).term(BlockStateProperties.UP, true);
            Condition.TerminalCondition westFalse = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).term(BlockStateProperties.UP, false);
            Condition.TerminalCondition westTrue = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).term(BlockStateProperties.UP, true);
            Condition.TerminalCondition southFalse = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).term(BlockStateProperties.UP, false);
            Condition.TerminalCondition southTrue = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).term(BlockStateProperties.UP, true);
            return MultiPartGenerator.multiPart(block)
                    .with(northFalse, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)))
                    .with(northTrue,  Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)))
                    .with(eastFalse, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .with(eastTrue, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .with(westFalse, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                    .with(westTrue, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                    .with(southFalse, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(0)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                    .with(southTrue, Variant.variant().with(VariantProperties.MODEL, modelIdentifiers.get(1)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
        }

        private static BlockStateGenerator createBedBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            Map<Direction, Variant> variantMap = new HashMap<>();
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ModelLocationUtils.getModelLocation(block);
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            variantMap.put(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[0]))));
            variantMap.put(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[1]))));
            variantMap.put(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[2]))));
            variantMap.put(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[3]))));
            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).generate(facing -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return variantMap.get(direction);
                }
                return null;
            }));
        }
        private static BlockStateGenerator createOrientableTuckableBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            return createOrientableTuckableBlockState(block, modelIdentifiers, 0);
        }
        private static BlockStateGenerator createOrientableTuckableBlockState(Block block, List<ResourceLocation> modelIdentifiers, int rotation) {
            Map<TuckableVariant, Variant> variantList = new HashMap<>();
            Integer[] rotationArray = new Integer[]{90, 270, 180, 0};
            for (int i = 0; rotationArray.length > i; i++) {
                if (rotationArray[i] + rotation > 270) {
                    if (rotationArray[i] == 270)
                        rotationArray[i] = 0;
                    else
                        rotationArray[i] = 90;
                } else {
                    rotationArray[i] += rotation;
                }
            }
            for (int i = 0; i <= 1; i++) {
                boolean tucked =  i == 1;
                ResourceLocation id = tucked ? modelIdentifiers.get(1) : modelIdentifiers.get(0);
                for (Direction direction : Direction.values())
                {
                    if (direction.getAxis().isVertical())
                        continue;
                    switch (direction) {
                        case NORTH -> {
                            variantList.put(new TuckableVariant(tucked, direction),Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[0]))));
                            break;
                        }
                        case SOUTH -> {
                            variantList.put(new TuckableVariant(tucked, direction), Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[1]))));
                            break;
                        }
                        case EAST ->  {
                            variantList.put(new TuckableVariant(tucked, direction), Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[2]))));
                            break;
                        }
                        case WEST -> {
                            variantList.put(new TuckableVariant(tucked, direction), Variant.variant().with(VariantProperties.MODEL, id).with(VariantProperties.Y_ROT, VariantProperties.Rotation.valueOf('R'+String.valueOf(rotationArray[3]))));
                            break;
                        }
                    }
                }
            }
            return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BasicChairBlock.TUCKED).generate((direction, aBoolean) -> {
                for (TuckableVariant tuckableVariant : variantList.keySet()){
                    if (tuckableVariant.direction.equals(direction) && tuckableVariant.tucked == aBoolean) {
                        return variantList.get(tuckableVariant);
                    }
                }
                return null;
            }));
        }

        private static class TuckableVariant {
            final boolean tucked;
            final Direction direction;
            static List<TuckableVariant> variants = new ArrayList<>();
            private TuckableVariant(boolean tucked, Direction direction) {
                this.tucked = tucked;
                this.direction = direction;
                variants.add(this);
            }

            public static TuckableVariant get(Direction direction, Boolean tucked) {
                for (TuckableVariant tuckableVariant : variants) {
                    if (tuckableVariant.tucked == tucked && direction.equals(tuckableVariant.direction))
                        return tuckableVariant;
                }
                return null;
            }
        }
    }
}
