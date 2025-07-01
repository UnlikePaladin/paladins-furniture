package com.unlikepaladin.pfm.runtime.assets;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.UnbakedCoffeeBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.basicDesk.UnbakedBasicDeskModel;
import com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.UnbakedBasicDeskCabinetModel;
import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.chair.UnbakedChairModel;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
import com.unlikepaladin.pfm.blocks.models.chairDinner.UnbakedChairDinnerModel;
import com.unlikepaladin.pfm.blocks.models.chairModern.UnbakedChairModernModel;
import com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.UnbakedClassicCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.classicNightstand.UnbakedClassicNightstandModel;
import com.unlikepaladin.pfm.blocks.models.classicStool.UnbakedClassicStoolModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFreezerModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFridgeModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedIronFridgeModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCabinet.UnbakedKitchenCabinetModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounter.UnbakedKitchenCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.UnbakedKitchenCounterOvenModel;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.UnbakedKitchenDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenSink.UnbakedKitchenSinkModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
import com.unlikepaladin.pfm.blocks.models.ladder.UnbakedLadderModel;
import com.unlikepaladin.pfm.blocks.models.logStool.UnbakedLogStoolModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import com.unlikepaladin.pfm.client.model.PFMBedModelRenderer;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.client.model.PFMModelVariantExtension;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.mixin.PFMTextureKeyFactory;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.*;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.json.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.Direction;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class PFMBlockstateModelProvider extends PFMProvider {

    public static Map<Block, Identifier> modelPathMap = new HashMap<>();

    public PFMBlockstateModelProvider(PFMGenerator parent) {
        super(parent, "PFM Blockstates and Models");
        parent.setProgress("Generating Blockstates and Models");
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();

        Path path = getParent().getOutput();

        Consumer<BlockModelDefinitionCreator> blockStateSupplierConsumer = blockStateSupplier -> {
            Path jsonPath = getBlockStateJsonPath(path, blockStateSupplier.getBlock());
            JsonElement element = BlockModelDefinition.CODEC.encodeStart(JsonOps.INSTANCE, blockStateSupplier.createBlockModelDefinition()).getOrThrow();
            String jsonContent = PFMDataGenerator.GSON.toJson(element);
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        BiConsumer<Identifier, ModelSupplier> identifierSupplierBiConsumer = (identifier, supplier) -> {
            Path jsonPath = getModelJsonPath(path, identifier);
            String jsonContent = PFMDataGenerator.GSON.toJson(supplier.get());
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        Set<Identifier> models = new HashSet<>();
        new PFMBlockStateModelGenerator(this, blockStateSupplierConsumer, identifierSupplierBiConsumer).registerModelsAndStates();
        List<Item> generateModelFor = new ArrayList<>();
        modelPathMap.keySet().forEach(block -> {
            Item item = Item.BLOCK_ITEMS.get(block);
            if (item != null) {
                Identifier identifier = ModelIds.getItemModelId(item);
                if (!models.contains(identifier)) {
                    Path jsonPath = getModelJsonPath(path, identifier);
                    enqueueJsonWrite(getWriteQueue(), jsonPath, new SimpleModelSupplier(modelPathMap.get(block)).get());
                    models.add(identifier);
                    generateModelFor.add(item);
                }
            }
        });


        Set<Identifier> itemModels = new HashSet<>();
        BiConsumer<Identifier, ItemModel.Unbaked> consumer = (id, unbakedModel) -> {
            ItemAsset asset = new ItemAsset(unbakedModel, ItemAsset.Properties.DEFAULT);
            DataResult<JsonElement> result = ItemAsset.CODEC.encodeStart(JsonOps.INSTANCE, asset);
            Path dest = getItemsJsonPath(path, id);
            if (result.isSuccess() && !itemModels.contains(id)) {
                enqueueJsonWrite(getWriteQueue(), dest, result.getOrThrow());
                itemModels.add(id);
            }
            else if (result.isError())
                getParent().getLogger().error("Failed to load item model for: {} {}", id, result.error().get());
        };
        new PFMItemModelGenerator(consumer, identifierSupplierBiConsumer).register(generateModelFor);

        waitForWrite();
        endProviderRun();
    }

    private static Path getBlockStateJsonPath(Path root, Block block) {
        Identifier identifier = Registries.BLOCK.getId(block);
        return root.resolve("assets/" + identifier.getNamespace() + "/blockstates/" + identifier.getPath() + ".json");
    }

    private static Path getModelJsonPath(Path root, Identifier id) {
        return root.resolve("assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json");
    }

    private static Path getItemsJsonPath(Path root, Identifier id) {
        return root.resolve("assets/" + id.getNamespace() + "/items/" + id.getPath() + ".json");
    }

    private static final Identifier replaceable = Identifier.of("block/stone");

    static class PFMItemModelGenerator {
        final BiConsumer<Identifier, ItemModel.Unbaked> output;
        public final BiConsumer<Identifier, ModelSupplier> modelCollector;

        PFMItemModelGenerator(BiConsumer<Identifier, ItemModel.Unbaked> output, BiConsumer<Identifier, ModelSupplier> modelCollector) {
            this.output = output;
            this.modelCollector = modelCollector;
        }


        public final void registerBasicModel(Item item) {
            this.output.accept(Registries.ITEM.getId(item), ItemModels.basic(ModelIds.getItemModelId(item)));
        }

        public final void registerFurnitureModel(Item item) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.empty(), List.of(), Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), List.of(), Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, List<TintSource> tints) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), tints, Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, List<TintSource> tints, BlockState state) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), tints, Optional.of(state)));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, BlockState state) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), List.of(), Optional.of(state)));
        }

        public final void registerFurnitureModel(Item item, BlockState state) {
            this.output.accept(Registries.ITEM.getId(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.empty(), List.of(), Optional.of(state)));
        }


        public void register(List<Item> items) {
            Set<Item> processed = new HashSet<>();
            for (Block block : PaladinFurnitureModBlocksItems.getBeds()) {
                if (block instanceof DyeableFurnitureBlock && !processed.contains(block.asItem())) {
                    registerFurnitureModel(block.asItem(), new PFMBedModelRenderer.Unbaked(((DyeableFurnitureBlock) block).getPFMColor()));
                    processed.add(block.asItem());
                }
            }


            for (Item item : items) {
                if (!processed.contains(item)) {
                    if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof CustomItemBlockState itemBlockState) {
                        registerFurnitureModel(item, itemBlockState.getItemBlockState());
                    }  else {
                        registerFurnitureModel(item);
                    }
                    processed.add(item);
                }
            }
        }
    }

    static class PFMBlockStateModelGenerator {
        public static Map<Model, Identifier> ModelIDS = new HashMap<>();

        final Consumer<BlockModelDefinitionCreator> blockStateCollector;
        final BiConsumer<Identifier, ModelSupplier> modelCollector;

        final List<Identifier> generatedStates = new ArrayList<>();
        final PFMBlockstateModelProvider provider;

        PFMBlockStateModelGenerator(PFMBlockstateModelProvider provider, Consumer<BlockModelDefinitionCreator> blockStateCollector, BiConsumer<Identifier, ModelSupplier> modelCollector) {
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
            provider.getParent().log("Generating Appliances");
            registerAppliances();
            provider.getParent().log("Generating Decorations");
            registerDecorations();
            provider.getParent().log("Generating Desks");
            registerDesks();
        }

        public void registerTuckableChairs() {
            provider.getParent().log("Basic Chairs");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap(), "chair", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairModel.CHAIR_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMapNonBase(), "chair", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairModel.CHAIR_MODEL_ID, identifiers, 90));

            provider.getParent().log("Dining Chairs");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMap(), "chair_dinner", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairDinnerModel.CHAIR_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(DinnerChairBlock.class).getVariantToBlockMapNonBase(), "chair_dinner", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairDinnerModel.CHAIR_MODEL_ID, identifiers, 90));

            provider.getParent().log("Modern Chairs");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMap(), "chair_modern", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairModernModel.CHAIR_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernChairBlock.class).getVariantToBlockMapNonBase(), "chair_modern", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairModernModel.CHAIR_MODEL_ID, identifiers, 90));

            provider.getParent().log("Classic Chairs");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMap(), "chair_classic", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairClassicModel.CHAIR_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicChairBlock.class).getVariantToBlockMapNonBase(), "chair_classic", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedChairClassicModel.CHAIR_MODEL_ID, identifiers, 90));

            provider.getParent().log("Log Stools");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(LogStoolBlock.class).getVariantToBlockMap(), "log_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedLogStoolModel.STOOL_MODEL_ID, identifiers, 90));

            provider.getParent().log("Simple Stools");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMap(), "simple_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedSimpleStoolModel.STOOL_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(SimpleStoolBlock.class).getVariantToBlockMapNonBase(), "simple_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedSimpleStoolModel.STOOL_MODEL_ID, identifiers, 90));

            provider.getParent().log("Classic Stools");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMap(), "classic_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedClassicStoolModel.STOOL_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicStoolBlock.class).getVariantToBlockMapNonBase(), "classic_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedClassicStoolModel.STOOL_MODEL_ID, identifiers, 90));

            provider.getParent().log("Modern Stools");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMap(), "modern_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedModernStoolModel.STOOL_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernStoolBlock.class).getVariantToBlockMapNonBase(), "modern_stool", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedModernStoolModel.STOOL_MODEL_ID, identifiers, 90));
        }

        public void registerTables() {
            provider.getParent().log("Basic Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMap(), "table_basic", (block, id) -> createAxisOrientableTableBlockState(block, UnbakedBasicTableModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicTableBlock.class).getVariantToBlockMapNonBase(), "table_basic", (block, id) -> createAxisOrientableTableBlockState(block, UnbakedBasicTableModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Classic Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMap(), "table_classic", (block, id) -> createSingleStateBlockState(block, UnbakedClassicTableModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicTableBlock.class).getVariantToBlockMapNonBase(), "table_classic", (block, id) -> createSingleStateBlockState(block, UnbakedClassicTableModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Log Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMap(), "log_table", (block, id) -> createOrientableTableBlockState(block, UnbakedLogTableModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(LogTableBlock.class).getVariantToBlockMapNonBase(), "log_table", (block, id) -> createOrientableTableBlockState(block, UnbakedLogTableModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Raw Log Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMap(), "log_table", (block, id) -> createOrientableTableBlockState(block, UnbakedLogTableModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(RawLogTableBlock.class).getVariantToBlockMapNonBase(), "log_table", (block, id) -> createOrientableTableBlockState(block, UnbakedLogTableModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Dining Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMap(), "dinner_table", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedDinnerTableModel.TABLE_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(DinnerTableBlock.class).getVariantToBlockMapNonBase(), "dinner_table", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedDinnerTableModel.TABLE_MODEL_ID, identifiers, 90));

            provider.getParent().log("Modern Dining Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMap(), "modern_dinner_table", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedModernDinnerTableModel.TABLE_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariantToBlockMapNonBase(), "modern_dinner_table", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedModernDinnerTableModel.TABLE_MODEL_ID, identifiers, 90));

            provider.getParent().log("Basic Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_basic", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedCoffeeBasicTableModel.TABLE_MODEL_ID, identifiers));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_basic", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedCoffeeBasicTableModel.TABLE_MODEL_ID, identifiers));

            provider.getParent().log("Modern Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_modern", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedModernCoffeeTableModel.TABLE_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_modern", (block, identifiers) -> createAxisOrientableTableBlockState(block, UnbakedModernCoffeeTableModel.TABLE_MODEL_ID, identifiers, 90));

            provider.getParent().log("Classic Coffee Tables");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMap(), "coffee_table_classic", (block, id) -> createSingleStateBlockState(block, UnbakedClassicCoffeeTableModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariantToBlockMapNonBase(), "coffee_table_classic", (block, id) -> createSingleStateBlockState(block, UnbakedClassicCoffeeTableModel.TABLE_MODEL_ID, id));
        }

        public void registerDesks() {
            provider.getParent().log("Basic Desks");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMap(), "desk_basic", (block, id) -> createSingleStateBlockState(block, UnbakedBasicDeskModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskBlock.class).getVariantToBlockMapNonBase(), "desk_basic", (block, id) -> createSingleStateBlockState(block, UnbakedBasicDeskModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Basic Desk Cabinets");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMap(), "desk_cabinet_basic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedBasicDeskCabinetModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), "desk_cabinet_basic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedBasicDeskCabinetModel.TABLE_MODEL_ID, id));
        }

        public void registerNightStands() {
            provider.getParent().log("Classic Nightstands");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMap(), "classic_nightstand", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_ID, identifiers, 90));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariantToBlockMapNonBase(), "classic_nightstand", (block, identifiers) -> createOrientableTableBlockState(block, UnbakedClassicNightstandModel.NIGHTSTAND_MODEL_ID, identifiers, 90));
        }

        public void registerBeds() {
            provider.getParent().log("Simple Beds");
            generateModelAndBlockStateForBed(PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariantToBlockMapList(), "simple_bed", (block, identifiers) -> createBedBlockState(block, UnbakedBedModel.BED_MODEL_ID, identifiers));
            provider.getParent().log("Classic Beds");
            generateModelAndBlockStateForBed(PaladinFurnitureMod.furnitureEntryMap.get(ClassicBedBlock.class).getVariantToBlockMapList(), "simple_bed", (block, identifiers) -> createBedBlockState(block, UnbakedBedModel.BED_MODEL_ID, identifiers));
        }

        public void registerLadders() {
            provider.getParent().log("Simple Bunk Ladders");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(SimpleBunkLadderBlock.class).getVariantToBlockMap(), "simple_bunk_ladder", (block, ids) -> createOrientableTableBlockState(block, UnbakedLadderModel.LADDER_MODEL_ID, ids));
        }

        public void registerCounters() {
            provider.getParent().log("Kitchen Counters");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMap(), "kitchen_counter", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenCounterModel.COUNTER_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterBlock.class).getVariantToBlockMapNonBase(), "kitchen_counter", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenCounterModel.COUNTER_MODEL_ID, ids));

            provider.getParent().log("Kitchen Drawers");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMap(), "kitchen_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenDrawerModel.DRAWER_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariantToBlockMapNonBase(), "kitchen_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenDrawerModel.DRAWER_MODEL_ID, ids));

            provider.getParent().log("Kitchen Cabinets");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMap(), "kitchen_cabinet", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenCabinetModel.CABINET_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariantToBlockMapNonBase(), "kitchen_cabinet", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenCabinetModel.CABINET_MODEL_ID, ids));

            provider.getParent().log("Kitchen Wall Drawers");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMap(), "kitchen_wall_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallDrawerModel.DRAWER_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallDrawerModel.DRAWER_MODEL_ID, ids));

            provider.getParent().log("Kitchen Wall Cabinets");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMap(), "kitchen_wall_counter", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallCounterModel.COUNTER_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_counter", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallCounterModel.COUNTER_MODEL_ID, ids));

            provider.getParent().log("Small Kitchen Cabinets");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMap(), "kitchen_wall_small_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariantToBlockMapNonBase(), "kitchen_wall_small_drawer", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenWallDrawerSmallModel.DRAWER_MODEL_ID, ids));

            provider.getParent().log("Kitchen Counter Ovens");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMap(), "kitchen_counter_oven", (block, identifiers) -> createOrientableUvLockedBlock(block, UnbakedKitchenCounterOvenModel.OVEN_MODEL_ID, identifiers, "", "", "", 180));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariantToBlockMapNonBase(), "kitchen_counter_oven", (block, identifiers) -> createOrientableUvLockedBlock(block, UnbakedKitchenCounterOvenModel.OVEN_MODEL_ID, identifiers, "", "", "", 180));

            provider.getParent().log("Kitchen Sinks");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMap(), "kitchen_sink", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenSinkModel.SINK_MODEL_ID, ids));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).getVariantToBlockMapNonBase(), "kitchen_sink", (block, ids) -> createOrientableUvLockedBlock(block, UnbakedKitchenSinkModel.SINK_MODEL_ID, ids));
        }

        public void registerAppliances() {
            provider.getParent().log("Fridges");
            Identifier grayFridgeID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, UnbakedFridgeModel.FRIDGE_MODEL_ID, List.of(grayFridgeID), 180));
            Identifier whiteFridgeID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, UnbakedFridgeModel.FRIDGE_MODEL_ID, List.of(whiteFridgeID), 180));
            Identifier ironFridgeID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.IRON_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.IRON_FRIDGE, UnbakedIronFridgeModel.IRON_FRIDGE_ID, List.of(ironFridgeID), 180));

            provider.getParent().log("Freezers");
            Identifier whiteFreezerID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.WHITE_FREEZER);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_FREEZER, UnbakedFreezerModel.FREEZER_MODEL_ID, List.of(whiteFreezerID), 180));
            Identifier grayFreezerID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.GRAY_FREEZER);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_FREEZER, UnbakedFreezerModel.FREEZER_MODEL_ID, List.of(grayFreezerID), 180));
        }

        public void registerDecorations() {
            provider.getParent().log("Mirrors");
            Identifier grayMirrorID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.GRAY_MIRROR);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_MIRROR, UnbakedMirrorModel.MIRROR_ID, List.of(grayMirrorID)));
            Identifier whiteMirrorID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.WHITE_MIRROR);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_MIRROR, UnbakedMirrorModel.MIRROR_ID, List.of(whiteMirrorID)));
        }

        public void registerLamp() {
            provider.getParent().log("Basic Lamps");
            Identifier modelID = ModelIds.getBlockModelId(PaladinFurnitureModBlocksItems.BASIC_LAMP);
            this.blockStateCollector.accept(createSingleStateBlockState(PaladinFurnitureModBlocksItems.BASIC_LAMP, UnbakedBasicLampModel.LAMP_MODEL_ID, List.of(modelID)));
            PFMBlockstateModelProvider.modelPathMap.put(PaladinFurnitureModBlocksItems.BASIC_LAMP, UnbakedBasicLampModel.getItemModelId());
        }

        public static TextureMap createPlankBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            Identifier top = ModelHelper.getTextureId(variantBase.getBaseBlock());
            Identifier legs =  ModelHelper.getTextureId(variantBase.getBaseBlock());
            return new TextureMap().put(TextureKey.TEXTURE, top).put(LOG_KEY, legs);

        }

        public static TextureMap createRawBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            Identifier top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            Identifier legs = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            return new TextureMap().put(TextureKey.TEXTURE, top).put(LOG_KEY, legs);
        }

        public static TextureMap createPlankLogBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            Identifier top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getBaseBlock());
            Identifier legs = stripped ? ModelHelper.getTextureId(variantBase.getBaseBlock()) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            return new TextureMap().put(TextureKey.TEXTURE, top).put(LOG_KEY, legs);
        }

        public static TextureMap createCounterBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            Identifier counterBase = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getBaseBlock());
            Identifier counterTop = stripped ? ModelHelper.getTextureId(variantBase.getBaseBlock()) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            if (variantBase.identifier.getPath().equals("granite")) {
                counterTop = ModelHelper.getTextureId(Blocks.POLISHED_GRANITE);
                counterBase = ModelHelper.getTextureId(Blocks.WHITE_TERRACOTTA);
            } else if (variantBase.identifier.getPath().equals("calcite") || variantBase.identifier.getPath().equals("netherite")) {
                Identifier temp = counterBase;
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
            return new TextureMap().put(TextureKey.TEXTURE, counterBase).put(LOG_KEY, counterTop);
        }

        public static TextureMap createLogLogTopBlockTexture(Boolean stripped, VariantBase<?> variantBase) {
            Identifier legs = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log")) : ModelHelper.getTextureId(variantBase.getSecondaryBlock());
            Identifier top = stripped ? ModelHelper.getTextureId((Block) variantBase.getChild("stripped_log"), "_top") : ModelHelper.getTextureId(variantBase.getSecondaryBlock(), "_top");
            return new TextureMap().put(LOG_KEY, legs).put(LOG_TOP_KEY, top);
        }

        public void generateBlockStateForBlock(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, BiFunction<Block, List<Identifier>, BlockModelDefinitionCreator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(Registries.BLOCK.getId(block))) {
                    Identifier modelID = ModelIds.getBlockModelId(block);
                    Identifier id = Identifier.of(modelID.getNamespace(), "block/" + blockName);
                    List<Identifier> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(Registries.BLOCK.getId(block));
                    PFMBlockstateModelProvider.modelPathMap.put(block, replaceable);
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends Set<?>> variantBaseHashMap, String blockName, BiFunction<Block, List<Identifier>, BlockModelDefinitionCreator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                blockList.forEach(block1 -> {
                Block block = (Block) block1;
                if (!generatedStates.contains(Registries.BLOCK.getId(block))) {
                    Identifier modelID = ModelIds.getBlockModelId(block);
                    Identifier id = Identifier.of(modelID.getNamespace(), "block/" + blockName);
                    List<Identifier> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(Registries.BLOCK.getId(block));
                    PFMBlockstateModelProvider.modelPathMap.put(block, replaceable);
                }});
            });

        }

        public void generateModelAndBlockStateForVariants(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, Model[] models, BiFunction<Block, List<Identifier>, BlockModelDefinitionCreator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMap> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(Registries.BLOCK.getId(block))) {
                    String blockName2 = blockName;

                    boolean stripped = block.getTranslationKey().contains("stripped");
                    TextureMap blockTexture = textureBiFunction.apply(stripped, variantBase);
                    List<Identifier> ids = new ArrayList<>();
                    String strippedprefix  = stripped ? "stripped_" : "";
                    if (block instanceof RawLogTableBlock) {
                        blockName2 = "raw_log_table";
                    }
                    if (variantBase instanceof StoneVariant && block instanceof LogTableBlock) {
                        blockName2 = blockName2.replace("log", "natural");
                    } else if (variantBase.isNetherWood() && block instanceof LogTableBlock) {
                        blockName2 = blockName2.replace("log", "stem");
                    }
                    Identifier modelID = ModelIds.getBlockModelId(block);
                    for (Model model : models) {
                        Identifier id = Identifier.of(modelID.getNamespace(), ModelIDS.get(model).getPath().replace("template_", "").replace("template", "").replaceAll(blockName, strippedprefix + variantBase.asString() + "_" + blockName2).replace("block/", "block/" + blockName + "/").replace("//", "/"));
                        model.upload(id, blockTexture, this.modelCollector);
                        ids.add(id);
                    }
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                    generatedStates.add(Registries.BLOCK.getId(block));
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends List<?>> variantBaseHashMap, String blockName, Model[] models, TriFunc<Block, List<Identifier>, String, BlockModelDefinitionCreator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMap> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                List<Identifier> allids = new ArrayList<>();
                blockList.forEach(block1 -> {
                    Block block = (Block) block1;
                    if (!generatedStates.contains(Registries.BLOCK.getId(block))) {
                        boolean stripped = block.getTranslationKey().contains("stripped");
                        TextureMap blockTexture = textureBiFunction.apply(stripped, variantBase);
                        Identifier modelID = ModelIds.getBlockModelId(block);
                        String color = block instanceof SimpleBedBlock ? ((SimpleBedBlock) block).getPFMColor().asString() : "";
                        List<Identifier> ids = new ArrayList<>();
                        for (Model model : models) {
                            Identifier id = Identifier.of(modelID.getNamespace(), ModelIDS.get(model).getPath().replaceAll("white", color).replaceAll("template", variantBase.asString()));

                            if (allids.contains(id))
                                continue;
                            if (model == models[0]) {
                                block(blockName+"/template/full/"+ blockName+ "_"+color, TextureKey.TEXTURE).upload(id, blockTexture, this.modelCollector);
                            }  else {
                                model.upload(block, blockTexture, this.modelCollector);
                            }
                            ids.add(id);
                        }
                        allids.addAll(ids);
                        this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids, color));
                        PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                        generatedStates.add(Registries.BLOCK.getId(block));
                    }
                });
            });
        }
        public static final TextureKey LOG_KEY = of("log");
        public static final TextureKey LOG_TOP_KEY = of("log_top");
        public static final Model[] TEMPLATE_CHAIR = new Model[]{block("chair/template_chair", TextureKey.TEXTURE, LOG_KEY), block("chair/template_chair", "_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CHAIR_DINNER = new Model[]{block("chair_dinner/template_chair_dinner", TextureKey.TEXTURE, LOG_KEY), block("chair_dinner/template_chair_dinner","_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CHAIR_CLASSIC = new Model[]{block("chair_classic/template_chair_classic", TextureKey.TEXTURE, LOG_KEY), block("chair_classic/template_chair_classic","_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CHAIR_MODERN = new Model[]{block("chair_modern/template_chair_modern", TextureKey.TEXTURE, LOG_KEY), block("chair_modern/template_chair_modern","_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_LOG_STOOL = new Model[]{block("log_stool/log_stool", LOG_KEY, LOG_TOP_KEY), block("log_stool/log_stool", "_tucked",LOG_KEY, LOG_TOP_KEY)};
        public static final Model[] TEMPLATE_SIMPLE_STOOL = new Model[]{block("simple_stool/simple_stool", TextureKey.TEXTURE, LOG_KEY), block("simple_stool/simple_stool", "_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CLASSIC_STOOL = new Model[]{block("classic_stool/classic_stool", TextureKey.TEXTURE, LOG_KEY), block("classic_stool/classic_stool", "_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_MODERN_STOOL = new Model[]{block("modern_stool/modern_stool", TextureKey.TEXTURE, LOG_KEY), block("modern_stool/modern_stool", "_tucked", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_BASIC_TABLE_ARRAY = new Model[]{block("table_basic/table_basic", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_base", TextureKey.TEXTURE, LOG_KEY),  block("table_basic/table_basic_north_east", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_west", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_east", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_west", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east_top", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west_top", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_east_west_north", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_east_west_south", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east_bottom", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west_bottom", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_east", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_south_west", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_east_corner", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_north_west_corner", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_east_corner", TextureKey.TEXTURE, LOG_KEY), block("table_basic/table_basic_south_west_corner", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CLASSIC_TABLE_ARRAY = new Model[]{block("table_classic/table_classic", TextureKey.TEXTURE, LOG_KEY), block("table_classic/table_classic_middle", TextureKey.TEXTURE, LOG_KEY), block("table_classic/table_classic_one_uved", TextureKey.TEXTURE, LOG_KEY), block("table_classic/table_classic_one", TextureKey.TEXTURE, LOG_KEY), block("table_classic/table_classic_two_uved", TextureKey.TEXTURE, LOG_KEY), block("table_classic/table_classic_two", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_LOG_TABLE_ARRAY = new Model[]{block("log_table/log_table", TextureKey.TEXTURE, LOG_KEY), block("log_table/log_table_right", TextureKey.TEXTURE, LOG_KEY), block("log_table/log_table_left", TextureKey.TEXTURE, LOG_KEY), block("log_table/log_table_middle", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_DINNER_TABLE_ARRAY = new Model[]{block("dinner_table/dinner_table", TextureKey.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_middle", TextureKey.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_right", TextureKey.TEXTURE, LOG_KEY), block("dinner_table/dinner_table_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_MODERN_DINNER_TABLE_ARRAY = new Model[]{block("table_modern_dinner/table_modern_dinner", TextureKey.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_middle", TextureKey.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_right", TextureKey.TEXTURE, LOG_KEY), block("table_modern_dinner/table_modern_dinner_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_CLASSIC_NIGHTSTAND_ARRAY = new Model[]{block("classic_nightstand/classic_nightstand", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_middle", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_right", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_left", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_open", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_middle_open", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_right_open", TextureKey.TEXTURE, LOG_KEY), block("classic_nightstand/classic_nightstand_left_open", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_SIMPLE_BED_ARRAY = new Model[]{block("simple_bed/template/full/simple_bed_white", TextureKey.TEXTURE), block("simple_bed/template/head/simple_bed_head", TextureKey.TEXTURE), block("simple_bed/template/head/simple_bed_head_left", TextureKey.TEXTURE), block("simple_bed/template/head/simple_bed_head_right", TextureKey.TEXTURE), block("simple_bed/template/foot/simple_bed_foot", TextureKey.TEXTURE), block("simple_bed/template/foot/simple_bed_foot_right", TextureKey.TEXTURE), block("simple_bed/template/foot/simple_bed_foot_left", TextureKey.TEXTURE), block("simple_bed/template/bunk/foot/simple_bed_foot_left", TextureKey.TEXTURE), block("simple_bed/template/bunk/foot/simple_bed_foot_right", TextureKey.TEXTURE), block("simple_bed/template/bunk/head/simple_bed_head", TextureKey.TEXTURE)};
        public static final Model[] TEMPLATE_CLASSIC_BED_ARRAY = new Model[]{block("classic_bed/template/full/classic_bed_white", TextureKey.TEXTURE), block("classic_bed/template/head/classic_bed_head", TextureKey.TEXTURE), block("classic_bed/template/head/classic_bed_head_left", TextureKey.TEXTURE), block("classic_bed/template/head/classic_bed_head_right", TextureKey.TEXTURE), block("classic_bed/template/foot/classic_bed_foot", TextureKey.TEXTURE), block("classic_bed/template/foot/classic_bed_foot_right", TextureKey.TEXTURE), block("classic_bed/template/foot/classic_bed_foot_left", TextureKey.TEXTURE), block("classic_bed/template/bunk/foot/classic_bed_foot_left", TextureKey.TEXTURE), block("classic_bed/template/bunk/foot/classic_bed_foot_right", TextureKey.TEXTURE)};
        public static final Model[] TEMPLATE_SIMPLE_BUNK_LADDER_ARRAY = new Model[]{block("simple_bunk_ladder/template/simple_ladder", TextureKey.TEXTURE), block("simple_bunk_ladder/template/simple_ladder_top", TextureKey.TEXTURE)};
        public static final Model[] TEMPLATE_KITCHEN_COUNTER = new Model[]{block("kitchen_counter/kitchen_counter", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_edge_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_edge_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_inner_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_inner_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_outer_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_outer_corner_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_DRAWER = new Model[]{block("kitchen_drawer/kitchen_drawer", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_inner_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_inner_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_left_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_edge_right_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_open_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_outer_corner_open_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_CABINET = new Model[]{block("kitchen_cabinet/kitchen_cabinet", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_open_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_inner_corner_open_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_open_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_cabinet/kitchen_cabinet_outer_corner_open_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_WALL_DRAWER = new Model[]{block("kitchen_drawer/kitchen_drawer_middle", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_inner_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_inner_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_open_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_drawer/kitchen_drawer_middle_outer_corner_open_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_WALL_COUNTER = new Model[]{block("kitchen_counter/kitchen_counter_middle", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_inner_corner_left", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_inner_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_outer_corner_right", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter/kitchen_counter_middle_outer_corner_left", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_WALL_DRAWER_SMALL = new Model[]{block("kitchen_wall_drawer_small/kitchen_wall_drawer_small", TextureKey.TEXTURE, LOG_KEY), block("kitchen_wall_drawer_small/kitchen_wall_drawer_small", "_open", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_COUNTER_OVEN = new Model[]{block("kitchen_counter_oven/kitchen_counter_oven", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven_middle",TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven", "_open", TextureKey.TEXTURE, LOG_KEY), block("kitchen_counter_oven/kitchen_counter_oven_middle", "_open", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_KITCHEN_SINK = new Model[]{block("kitchen_sink/kitchen_sink", TextureKey.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_level1",TextureKey.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_level2", TextureKey.TEXTURE, LOG_KEY), block("kitchen_sink/kitchen_sink_full", TextureKey.TEXTURE, LOG_KEY)};
        public static final Model[] TEMPLATE_LAMP_ARRAY = new Model[]{block("basic_lamp/basic_lamp_bottom", TextureKey.TEXTURE), block("basic_lamp/basic_lamp_middle", TextureKey.TEXTURE),  block("basic_lamp/basic_lamp_single", TextureKey.TEXTURE), block("basic_lamp/basic_lamp_top", TextureKey.TEXTURE)};

        private static Model make(TextureKey ... requiredTextures) {
            return new Model(Optional.empty(), Optional.empty(), requiredTextures);
        }

        private static Model block(String parent, TextureKey ... requiredTextures) {
            Identifier id = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + parent);
            Model model = new Model(Optional.of(id), Optional.empty(), requiredTextures);
            ModelIDS.put(model, id);
            return model;
        }

        private static Model item(String parent, TextureKey ... requiredTextures) {
            return new Model(Optional.of(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextures);
        }

        private static Model block(String parent, String variant, TextureKey ... requiredTextures) {
            Identifier id = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + parent + variant);
            Model model = new Model(Optional.of(id), Optional.of(variant), requiredTextures);
            ModelIDS.put(model, id);
            return model;
        }

        private static TextureKey of(String name) {
            return PFMTextureKeyFactory.newTextureKey(name, null);
        }

        private static TextureKey of(String name, TextureKey parent) {
            return PFMTextureKeyFactory.newTextureKey(name, parent);
        }

        private static BlockModelDefinitionCreator createSingleStateBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers) {
            String path = modelIdentifiers.get(0).getPath();
            //Ugly hack to get the folder name for the Baked Block Model
            Identifier id = Identifier.of(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            ModelVariant var = new ModelVariant(id);
            ((PFMModelVariantExtension)(Object)var).pfm$setCustomType(typeId);
            return VariantsBlockModelDefinitionCreator.of(block, new WeightedVariant(Pool.of(var)));
        }
        private static BlockModelDefinitionCreator createAxisOrientableTableBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers, int rotation) {
            Map<Direction.Axis, AxisRotation> variantMap = new HashMap<>();
            String path = modelIdentifiers.get(0).getPath();
            Identifier id;

            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = Identifier.of(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 180;
            }

            variantMap.put(Direction.Axis.Z, AxisRotation.valueOf('R'+String.valueOf(rotationArray[0])));
            variantMap.put(Direction.Axis.X, AxisRotation.valueOf('R'+String.valueOf(rotationArray[1])));
            return VariantsBlockModelDefinitionCreator.of(block).with(BlockStateVariantMap.models(Properties.HORIZONTAL_AXIS).generate(axis -> {
                for (Direction.Axis axis1 : variantMap.keySet()) {
                    if (axis.equals(axis1)) {
                        ModelVariant variant = new ModelVariant(id).withRotationY(variantMap.get(axis));
                        ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(typeId);
                        return new WeightedVariant(Pool.of(variant));
                    }
                }
                return null;
            }));
        }
        private static BlockModelDefinitionCreator createAxisOrientableTableBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers) {
            return createAxisOrientableTableBlockState(block, typeId, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionCreator createOrientableTableBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers) {
            return createOrientableTableBlockState(block, typeId, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionCreator createOrientableTableBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers, int rotation) {
            String path = modelIdentifiers.get(0).getPath();
            Identifier id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = Identifier.of(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
            }

            ModelVariant north = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[0])));
            ((PFMModelVariantExtension)(Object)north).pfm$setCustomType(typeId);
            ModelVariant east = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[1])));
            ((PFMModelVariantExtension)(Object)east).pfm$setCustomType(typeId);
            ModelVariant south = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[2])));
            ((PFMModelVariantExtension)(Object)south).pfm$setCustomType(typeId);
            ModelVariant west = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[3])));
            ((PFMModelVariantExtension)(Object)west).pfm$setCustomType(typeId);

            BlockStateVariantMap<WeightedVariant> variant = BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
                    .register(Direction.NORTH, new WeightedVariant(Pool.of(north)))
                    .register(Direction.EAST, new WeightedVariant(Pool.of(east)))
                    .register(Direction.SOUTH, new WeightedVariant(Pool.of(south)))
                    .register(Direction.WEST, new WeightedVariant(Pool.of(west)));

            return VariantsBlockModelDefinitionCreator.of(block).with(variant);
        }
        private static BlockModelDefinitionCreator createOrientableUvLockedBlock(Block block, Identifier typeId, List<Identifier> modelIdentifiers){
            return createOrientableUvLockedBlock(block, typeId, modelIdentifiers, "", "", "", 0);
        }
        private static BlockModelDefinitionCreator createOrientableUvLockedBlock(Block block, Identifier typeId, List<Identifier> modelIdentifiers, int rotation){
            return createOrientableUvLockedBlock(block, typeId, modelIdentifiers, "", "", "", rotation);
        }

        private static BlockModelDefinitionCreator createOrientableUvLockedBlock(Block block, Identifier typeId, List<Identifier> modelIdentifiers, String override, String furnitureName, String replacement, int rotation) {
            String path = modelIdentifiers.get(0).getPath().replaceAll(override, "");
            String name = path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/'));
            Identifier id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = Identifier.of(modelIdentifiers.get(0).getNamespace(), name.replace(furnitureName, replacement));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
            }

            ModelVariant north = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[0]))).withUVLock(true);
            ((PFMModelVariantExtension)(Object)north).pfm$setCustomType(typeId);
            ModelVariant east = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[1]))).withUVLock(true);
            ((PFMModelVariantExtension)(Object)east).pfm$setCustomType(typeId);
            ModelVariant south = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[2]))).withUVLock(true);
            ((PFMModelVariantExtension)(Object)south).pfm$setCustomType(typeId);
            ModelVariant west = new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[3]))).withUVLock(true);
            ((PFMModelVariantExtension)(Object)west).pfm$setCustomType(typeId);

            BlockStateVariantMap<WeightedVariant> variant = BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
                    .register(Direction.NORTH, new WeightedVariant(Pool.of(north)))
                    .register(Direction.EAST, new WeightedVariant(Pool.of(east)))
                    .register(Direction.SOUTH, new WeightedVariant(Pool.of(south)))
                    .register(Direction.WEST, new WeightedVariant(Pool.of(west)));

            return VariantsBlockModelDefinitionCreator.of(block).with(variant);
        }

        private static BlockModelDefinitionCreator createKitchenSink(Block block, List<Identifier> modelIdentifiers) {
            Map<Direction, AxisRotation> rotationMap = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};

            rotationMap.put(Direction.NORTH, AxisRotation.valueOf('R'+String.valueOf(rotation[0])));
            rotationMap.put(Direction.EAST, AxisRotation.valueOf('R'+String.valueOf(rotation[1])));
            rotationMap.put(Direction.SOUTH, AxisRotation.valueOf('R'+String.valueOf(rotation[2])));
            rotationMap.put(Direction.WEST, AxisRotation.valueOf('R'+String.valueOf(rotation[3])));

            return VariantsBlockModelDefinitionCreator.of(block).with(BlockStateVariantMap.models(AbstractSinkBlock.LEVEL_4, Properties.HORIZONTAL_FACING).generate(((level, facing) -> {
                return new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(level)).withRotationY(rotationMap.get(facing)).withUVLock(true)));
            })));
        }

        private static BlockModelDefinitionCreator createSmallKitchenDrawer(Block block, List<Identifier> modelIdentifiers, String override, String furnitureName, String replacement) {
            Map<Direction, ModelVariant> variantMap = new HashMap<>();
            Map<Direction, ModelVariant> variantMapOpen = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};

            variantMap.put(Direction.NORTH, new ModelVariant(modelIdentifiers.getFirst()).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[0]))).withUVLock(true));
            variantMap.put(Direction.EAST, new ModelVariant(modelIdentifiers.getFirst()).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[1]))).withUVLock(true));
            variantMap.put(Direction.SOUTH, new ModelVariant(modelIdentifiers.getFirst()).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[2]))).withUVLock(true));
            variantMap.put(Direction.WEST, new ModelVariant(modelIdentifiers.getFirst()).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[3]))).withUVLock(true));

            variantMapOpen.put(Direction.NORTH, new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[0]))).withUVLock(true));
            variantMapOpen.put(Direction.EAST, new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[1]))).withUVLock(true));
            variantMapOpen.put(Direction.SOUTH, new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[2]))).withUVLock(true));
            variantMapOpen.put(Direction.WEST, new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotation[3]))).withUVLock(true));

            return VariantsBlockModelDefinitionCreator.of(block).with(BlockStateVariantMap.models(net.minecraft.state.property.Properties.HORIZONTAL_FACING, net.minecraft.state.property.Properties.OPEN).generate((facing, open) -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return new WeightedVariant(Pool.of(open ? variantMapOpen.get(direction) : variantMap.get(direction)));
                }
                return null;
            }));
        }


        private static BlockModelDefinitionCreator createLadderBlockState(Block block, List<Identifier> modelIdentifiers) {
            MultipartModelCondition northFalse = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.NORTH).put(net.minecraft.state.property.Properties.UP, false).build()));
            MultipartModelCondition northTrue = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.NORTH).put(net.minecraft.state.property.Properties.UP, true).build()));

            MultipartModelCondition eastFalse = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.EAST).put(net.minecraft.state.property.Properties.UP, false).build()));
            MultipartModelCondition eastTrue = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.EAST).put(net.minecraft.state.property.Properties.UP, true).build()));

            MultipartModelCondition westFalse = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.WEST).put(net.minecraft.state.property.Properties.UP, false).build()));
            MultipartModelCondition westTrue = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.WEST).put(net.minecraft.state.property.Properties.UP, true).build()));

            MultipartModelCondition southFalse = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.SOUTH).put(net.minecraft.state.property.Properties.UP, false).build()));
            MultipartModelCondition southTrue = new MultipartModelCombinedCondition(MultipartModelCombinedCondition.LogicalOperator.AND, List.of(new MultipartModelConditionBuilder().put(net.minecraft.state.property.Properties.HORIZONTAL_FACING, Direction.SOUTH).put(net.minecraft.state.property.Properties.UP, true).build()));

            return MultipartBlockModelDefinitionCreator.create(block)
                    .with(northFalse, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(0)))))
                    .with(northTrue, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(1)))))
                    .with(eastFalse, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(0)).withRotationY(AxisRotation.R90))))
                    .with(eastTrue, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.R90))))
                    .with(westFalse, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(0)).withRotationY(AxisRotation.R270))))
                    .with(westTrue, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.R270))))
                    .with(southFalse, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(0)).withRotationY(AxisRotation.R180))))
                    .with(southTrue, new WeightedVariant(Pool.of(new ModelVariant(modelIdentifiers.get(1)).withRotationY(AxisRotation.R180))));
        }

        private static BlockModelDefinitionCreator createBedBlockState(Block block, Identifier typeId, List<Identifier> modelIdentifiers) {
            Map<Direction, AxisRotation> variantMap = new HashMap<>();
            Identifier id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ModelIds.getBlockModelId(block);
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            variantMap.put(Direction.NORTH, AxisRotation.valueOf('R'+String.valueOf(rotationArray[0])));
            variantMap.put(Direction.EAST, AxisRotation.valueOf('R'+String.valueOf(rotationArray[1])));
            variantMap.put(Direction.SOUTH, AxisRotation.valueOf('R'+String.valueOf(rotationArray[2])));
            variantMap.put(Direction.WEST, AxisRotation.valueOf('R'+String.valueOf(rotationArray[3])));

            return VariantsBlockModelDefinitionCreator.of(block).with(BlockStateVariantMap.models(net.minecraft.state.property.Properties.HORIZONTAL_FACING).generate(facing -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction)) {
                        ModelVariant variant = new ModelVariant(id).withRotationY(variantMap.get(direction));
                        ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(typeId);
                        return new WeightedVariant(Pool.of(variant));
                    }
                }
                return null;
            }));
        }
        private static BlockModelDefinitionCreator createOrientableTuckableBlockState(Block block, List<Identifier> modelIdentifiers) {
            return createOrientableTuckableBlockState(block, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionCreator createOrientableTuckableBlockState(Block block, List<Identifier> modelIdentifiers, int rotation) {
            Map<TuckableVariant, ModelVariant> variantList = new HashMap<>();
            Integer[] rotationArray = new Integer[]{90, 270, 180, 0};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
            }
            for (int i = 0; i <= 1; i++) {
                boolean tucked =  i == 1;
                Identifier id = tucked ? modelIdentifiers.get(1) : modelIdentifiers.get(0);
                for (Direction direction : Direction.values())
                {
                    if (direction.getAxis().isVertical())
                        continue;
                    switch (direction) {
                        case NORTH -> {
                            variantList.put(new TuckableVariant(tucked, direction), new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[0]))));
                            break;
                        }
                        case SOUTH -> {
                            variantList.put(new TuckableVariant(tucked, direction), new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[1]))));
                            break;
                        }
                        case EAST ->  {
                            variantList.put(new TuckableVariant(tucked, direction), new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[2]))));
                            break;
                        }
                        case WEST -> {
                            variantList.put(new TuckableVariant(tucked, direction), new ModelVariant(id).withRotationY(AxisRotation.valueOf('R'+String.valueOf(rotationArray[3]))));
                            break;
                        }
                    }
                }
            }
            return VariantsBlockModelDefinitionCreator.of(block).with(BlockStateVariantMap.models(Properties.HORIZONTAL_FACING, BasicChairBlock.TUCKED).generate((direction, aBoolean) -> {
                for (TuckableVariant tuckableVariant : variantList.keySet()){
                    if (tuckableVariant.direction.equals(direction) && tuckableVariant.tucked == aBoolean) {
                        return new WeightedVariant(Pool.of(variantList.get(tuckableVariant)));
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
