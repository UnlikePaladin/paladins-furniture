package com.unlikepaladin.pfm.runtime.assets;

import com.google.gson.JsonElement;
import com.mojang.math.Quadrant;
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
import com.unlikepaladin.pfm.blocks.models.classicDesk.UnbakedClassicDeskModel;
import com.unlikepaladin.pfm.blocks.models.classicNightstand.UnbakedClassicNightstandModel;
import com.unlikepaladin.pfm.blocks.models.classicStool.UnbakedClassicStoolModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFreezerModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFridgeModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedIronFridgeModel;
import com.unlikepaladin.pfm.blocks.models.herringbone.UnbakedHerringboneModel;
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
import com.unlikepaladin.pfm.mixin.PFMTextureSlotFactory;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.TriFunc;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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

        Path path = getParent().getOutput();

        Consumer<BlockModelDefinitionGenerator> blockStateSupplierConsumer = blockStateSupplier -> {
            Path jsonPath = getBlockStateJsonPath(path, blockStateSupplier.block());
            JsonElement element = BlockModelDefinition.CODEC.encodeStart(JsonOps.INSTANCE, blockStateSupplier.create()).getOrThrow();
            String jsonContent = PFMDataGenerator.GSON.toJson(element);
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        BiConsumer<ResourceLocation, ModelInstance> identifierSupplierBiConsumer = (identifier, supplier) -> {
            Path jsonPath = getModelJsonPath(path, identifier);
            String jsonContent = PFMDataGenerator.GSON.toJson(supplier.get());
            enqueueJsonWrite(getWriteQueue(), jsonPath, jsonContent);
        };

        Set<ResourceLocation> models = new HashSet<>();
        new PFMBlockStateModelGenerator(this, blockStateSupplierConsumer, identifierSupplierBiConsumer).registerModelsAndStates();
        List<Item> generateModelFor = new ArrayList<>();
        modelPathMap.keySet().forEach(block -> {
            Item item = Item.BY_BLOCK.get(block);
            if (item != null) {
                ResourceLocation identifier = ModelLocationUtils.getModelLocation(item);
                if (!models.contains(identifier)) {
                    Path jsonPath = getModelJsonPath(path, identifier);
                    enqueueJsonWrite(getWriteQueue(), jsonPath, new DelegatedModel(modelPathMap.get(block)).get());
                    models.add(identifier);
                    generateModelFor.add(item);
                }
            }
        });


        Set<ResourceLocation> itemModels = new HashSet<>();
        BiConsumer<ResourceLocation, ItemModel.Unbaked> consumer = (id, unbakedModel) -> {
            ClientItem asset = new ClientItem(unbakedModel, ClientItem.Properties.DEFAULT);
            DataResult<JsonElement> result = ClientItem.CODEC.encodeStart(JsonOps.INSTANCE, asset);
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
        ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(block);
        return root.resolve("assets/" + identifier.getNamespace() + "/blockstates/" + identifier.getPath() + ".json");
    }

    private static Path getModelJsonPath(Path root, ResourceLocation id) {
        return root.resolve("assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json");
    }

    private static Path getItemsJsonPath(Path root, ResourceLocation id) {
        return root.resolve("assets/" + id.getNamespace() + "/items/" + id.getPath() + ".json");
    }

    private static final ResourceLocation replaceable = ResourceLocation.parse("block/stone");

    static class PFMItemModelGenerator {
        final BiConsumer<ResourceLocation, ItemModel.Unbaked> output;
        public final BiConsumer<ResourceLocation, ModelInstance> modelCollector;

        PFMItemModelGenerator(BiConsumer<ResourceLocation, ItemModel.Unbaked> output, BiConsumer<ResourceLocation, ModelInstance> modelCollector) {
            this.output = output;
            this.modelCollector = modelCollector;
        }


        public final void registerBasicModel(Item item) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)));
        }

        public final void registerFurnitureModel(Item item) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.empty(), List.of(), Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), List.of(), Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, List<ItemTintSource> tints) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), tints, Optional.empty()));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, List<ItemTintSource> tints, BlockState state) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), tints, Optional.of(state)));
        }

        public final void registerFurnitureModel(Item item, SpecialModelRenderer.Unbaked specialModel, BlockState state) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.of(specialModel), List.of(), Optional.of(state)));
        }

        public final void registerFurnitureModel(Item item, BlockState state) {
            this.output.accept(BuiltInRegistries.ITEM.getKey(item), new PFMItemModel.Unbaked(((BlockItem)item).getBlock(), Optional.empty(), List.of(), Optional.of(state)));
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
        public static Map<ModelTemplate, ResourceLocation> ModelIDS = new HashMap<>();

        final Consumer<BlockModelDefinitionGenerator> blockStateCollector;
        final BiConsumer<ResourceLocation, ModelInstance> modelCollector;

        final List<ResourceLocation> generatedStates = new ArrayList<>();
        final PFMBlockstateModelProvider provider;

        PFMBlockStateModelGenerator(PFMBlockstateModelProvider provider, Consumer<BlockModelDefinitionGenerator> blockStateCollector, BiConsumer<ResourceLocation, ModelInstance> modelCollector) {
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
            provider.getParent().log("Generating Miscellaneous Blocks");
            registerMiscellaneousBlocks();
        }

        public void registerMiscellaneousBlocks() {
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(HerringbonePlankBlock.class).getVariantToBlockMap(), "herringbone_planks",  (block, id) -> createSingleStateBlockState(block, UnbakedHerringboneModel.ID, id));
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

            provider.getParent().log("Classic Desks");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMap(), "desk_classic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedClassicDeskModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskBlock.class).getVariantToBlockMapNonBase(), "desk_classic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedClassicDeskModel.TABLE_MODEL_ID, id));

            provider.getParent().log("Classic Desk Cabinets");
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMap(), "desk_classic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedClassicDeskModel.TABLE_MODEL_ID, id));
            generateBlockStateForBlock(PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariantToBlockMapNonBase(), "desk_classic", (block, id) -> createOrientableUvLockedBlock(block, UnbakedClassicDeskModel.TABLE_MODEL_ID, id));
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
            ResourceLocation grayFridgeID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.GRAY_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_FRIDGE, UnbakedFridgeModel.FRIDGE_MODEL_ID, List.of(grayFridgeID), 180));
            ResourceLocation whiteFridgeID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, UnbakedFridgeModel.FRIDGE_MODEL_ID, List.of(whiteFridgeID), 180));
            ResourceLocation ironFridgeID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.IRON_FRIDGE);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.IRON_FRIDGE, UnbakedIronFridgeModel.IRON_FRIDGE_ID, List.of(ironFridgeID), 180));

            provider.getParent().log("Freezers");
            ResourceLocation whiteFreezerID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.WHITE_FREEZER);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_FREEZER, UnbakedFreezerModel.FREEZER_MODEL_ID, List.of(whiteFreezerID), 180));
            ResourceLocation grayFreezerID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.GRAY_FREEZER);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_FREEZER, UnbakedFreezerModel.FREEZER_MODEL_ID, List.of(grayFreezerID), 180));
        }

        public void registerDecorations() {
            provider.getParent().log("Mirrors");
            ResourceLocation grayMirrorID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.GRAY_MIRROR);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.GRAY_MIRROR, UnbakedMirrorModel.MIRROR_ID, List.of(grayMirrorID)));
            ResourceLocation whiteMirrorID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.WHITE_MIRROR);
            this.blockStateCollector.accept(createOrientableTableBlockState(PaladinFurnitureModBlocksItems.WHITE_MIRROR, UnbakedMirrorModel.MIRROR_ID, List.of(whiteMirrorID)));
        }

        public void registerLamp() {
            provider.getParent().log("Basic Lamps");
            ResourceLocation modelID = ModelLocationUtils.getModelLocation(PaladinFurnitureModBlocksItems.BASIC_LAMP);
            this.blockStateCollector.accept(createSingleStateBlockState(PaladinFurnitureModBlocksItems.BASIC_LAMP, UnbakedBasicLampModel.LAMP_MODEL_ID, List.of(modelID)));
            PFMBlockstateModelProvider.modelPathMap.put(PaladinFurnitureModBlocksItems.BASIC_LAMP, UnbakedBasicLampModel.getModelLocation());
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

        public void generateBlockStateForBlock(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, BiFunction<Block, List<ResourceLocation>, BlockModelDefinitionGenerator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(BuiltInRegistries.BLOCK.getKey(block))) {
                    ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modelID.getNamespace(), "block/" + blockName);
                    List<ResourceLocation> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(BuiltInRegistries.BLOCK.getKey(block));
                    PFMBlockstateModelProvider.modelPathMap.put(block, replaceable);
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends Set<?>> variantBaseHashMap, String blockName, BiFunction<Block, List<ResourceLocation>, BlockModelDefinitionGenerator> stateSupplierBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                blockList.forEach(block1 -> {
                Block block = (Block) block1;
                if (!generatedStates.contains(BuiltInRegistries.BLOCK.getKey(block))) {
                    ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modelID.getNamespace(), "block/" + blockName);
                    List<ResourceLocation> ids = new ArrayList<>(1);
                    ids.add(id);
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    generatedStates.add(BuiltInRegistries.BLOCK.getKey(block));
                    PFMBlockstateModelProvider.modelPathMap.put(block, replaceable);
                }});
            });

        }

        public void generateModelAndBlockStateForVariants(Map<VariantBase<?>, ? extends Block> variantBaseHashMap, String blockName, ModelTemplate[] models, BiFunction<Block, List<ResourceLocation>, BlockModelDefinitionGenerator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMapping> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, block) -> {
                if (!generatedStates.contains(BuiltInRegistries.BLOCK.getKey(block))) {
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
                        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modelID.getNamespace(), ModelIDS.get(model).getPath().replace("template_", "").replace("template", "").replaceAll(blockName, strippedprefix + variantBase.getSerializedName() + "_" + blockName2).replace("block/", "block/" + blockName + "/").replace("//", "/"));
                        model.create(id, blockTexture, this.modelCollector);
                        ids.add(id);
                    }
                    this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids));
                    PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                    generatedStates.add(BuiltInRegistries.BLOCK.getKey(block));
                }
            });
        }

        public void generateModelAndBlockStateForBed(HashMap<VariantBase<?>, ? extends List<?>> variantBaseHashMap, String blockName, ModelTemplate[] models, TriFunc<Block, List<ResourceLocation>, String, BlockModelDefinitionGenerator> stateSupplierBiFunction, BiFunction<Boolean, VariantBase<?>, TextureMapping> textureBiFunction) {
            variantBaseHashMap.forEach((variantBase, blockList) -> {
                List<ResourceLocation> allids = new ArrayList<>();
                blockList.forEach(block1 -> {
                    Block block = (Block) block1;
                    if (!generatedStates.contains(BuiltInRegistries.BLOCK.getKey(block))) {
                        boolean stripped = block.getDescriptionId().contains("stripped");
                        TextureMapping blockTexture = textureBiFunction.apply(stripped, variantBase);
                        ResourceLocation modelID = ModelLocationUtils.getModelLocation(block);
                        String color = block instanceof SimpleBedBlock ? ((SimpleBedBlock) block).getPFMColor().getSerializedName() : "";
                        List<ResourceLocation> ids = new ArrayList<>();
                        for (ModelTemplate model : models) {
                            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modelID.getNamespace(), ModelIDS.get(model).getPath().replaceAll("white", color).replaceAll("template", variantBase.getSerializedName()));

                            if (allids.contains(id))
                                continue;
                            if (model == models[0]) {
                                block(blockName+"/template/full/"+ blockName+ "_"+color, TextureSlot.TEXTURE).create(id, blockTexture, this.modelCollector);
                            }  else {
                                model.create(block, blockTexture, this.modelCollector);
                            }
                            ids.add(id);
                        }
                        allids.addAll(ids);
                        this.blockStateCollector.accept(stateSupplierBiFunction.apply(block, ids, color));
                        PFMBlockstateModelProvider.modelPathMap.put(block, ids.get(0));
                        generatedStates.add(BuiltInRegistries.BLOCK.getKey(block));
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
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + parent);
            ModelTemplate model = new ModelTemplate(Optional.of(id), Optional.empty(), requiredTextures);
            ModelIDS.put(model, id);
            return model;
        }

        private static ModelTemplate item(String parent, TextureSlot ... requiredTextures) {
            return new ModelTemplate(Optional.of(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextures);
        }

        private static ModelTemplate block(String parent, String variant, TextureSlot ... requiredTextures) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + parent + variant);
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

        private static BlockModelDefinitionGenerator createSingleStateBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers) {
            String path = modelIdentifiers.get(0).getPath();
            //Ugly hack to get the folder name for the Baked Block Model
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            Variant var = new Variant(id);
            ((PFMModelVariantExtension)(Object)var).pfm$setCustomType(typeId);
            return MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(var)));
        }
        private static BlockModelDefinitionGenerator createAxisOrientableTableBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers, int rotation) {
            Map<Direction.Axis, Quadrant> variantMap = new HashMap<>();
            String path = modelIdentifiers.get(0).getPath();
            ResourceLocation id;

            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ResourceLocation.fromNamespaceAndPath(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 180;
            }

            variantMap.put(Direction.Axis.Z, Quadrant.valueOf('R'+String.valueOf(rotationArray[0])));
            variantMap.put(Direction.Axis.X, Quadrant.valueOf('R'+String.valueOf(rotationArray[1])));
            return MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS).generate(axis -> {
                for (Direction.Axis axis1 : variantMap.keySet()) {
                    if (axis.equals(axis1)) {
                        Variant variant = new Variant(id).withYRot(variantMap.get(axis));
                        ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(typeId);
                        return new MultiVariant(WeightedList.of(variant));
                    }
                }
                return null;
            }));
        }
        private static BlockModelDefinitionGenerator createAxisOrientableTableBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers) {
            return createAxisOrientableTableBlockState(block, typeId, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionGenerator createOrientableTableBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers) {
            return createOrientableTableBlockState(block, typeId, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionGenerator createOrientableTableBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers, int rotation) {
            String path = modelIdentifiers.get(0).getPath();
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ResourceLocation.fromNamespaceAndPath(modelIdentifiers.get(0).getNamespace(), path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/')));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
            }

            Variant north = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[0])));
            ((PFMModelVariantExtension)(Object)north).pfm$setCustomType(typeId);
            Variant east = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[1])));
            ((PFMModelVariantExtension)(Object)east).pfm$setCustomType(typeId);
            Variant south = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[2])));
            ((PFMModelVariantExtension)(Object)south).pfm$setCustomType(typeId);
            Variant west = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[3])));
            ((PFMModelVariantExtension)(Object)west).pfm$setCustomType(typeId);

            PropertyDispatch<MultiVariant> variant = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.NORTH, new MultiVariant(WeightedList.of(north)))
                    .select(Direction.EAST, new MultiVariant(WeightedList.of(east)))
                    .select(Direction.SOUTH, new MultiVariant(WeightedList.of(south)))
                    .select(Direction.WEST, new MultiVariant(WeightedList.of(west)));

            return MultiVariantGenerator.dispatch(block).with(variant);
        }
        private static BlockModelDefinitionGenerator createOrientableUvLockedBlock(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers){
            return createOrientableUvLockedBlock(block, typeId, modelIdentifiers, "", "", "", 0);
        }
        private static BlockModelDefinitionGenerator createOrientableUvLockedBlock(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers, int rotation){
            return createOrientableUvLockedBlock(block, typeId, modelIdentifiers, "", "", "", rotation);
        }

        private static BlockModelDefinitionGenerator createOrientableUvLockedBlock(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers, String override, String furnitureName, String replacement, int rotation) {
            String path = modelIdentifiers.get(0).getPath().replaceAll(override, "");
            String name = path.split(path.substring(path.lastIndexOf('/')))[0] + path.substring(path.lastIndexOf('/'));
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ResourceLocation.fromNamespaceAndPath(modelIdentifiers.get(0).getNamespace(), name.replace(furnitureName, replacement));
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
            }

            Variant north = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[0]))).withUvLock(true);
            ((PFMModelVariantExtension)(Object)north).pfm$setCustomType(typeId);
            Variant east = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[1]))).withUvLock(true);
            ((PFMModelVariantExtension)(Object)east).pfm$setCustomType(typeId);
            Variant south = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[2]))).withUvLock(true);
            ((PFMModelVariantExtension)(Object)south).pfm$setCustomType(typeId);
            Variant west = new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[3]))).withUvLock(true);
            ((PFMModelVariantExtension)(Object)west).pfm$setCustomType(typeId);

            PropertyDispatch<MultiVariant> variant = PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING)
                    .select(Direction.NORTH, new MultiVariant(WeightedList.of(north)))
                    .select(Direction.EAST, new MultiVariant(WeightedList.of(east)))
                    .select(Direction.SOUTH, new MultiVariant(WeightedList.of(south)))
                    .select(Direction.WEST, new MultiVariant(WeightedList.of(west)));

            return MultiVariantGenerator.dispatch(block).with(variant);
        }

        private static BlockModelDefinitionGenerator createKitchenSink(Block block, List<ResourceLocation> modelIdentifiers) {
            Map<Direction, Quadrant> rotationMap = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};

            rotationMap.put(Direction.NORTH, Quadrant.valueOf('R'+String.valueOf(rotation[0])));
            rotationMap.put(Direction.EAST, Quadrant.valueOf('R'+String.valueOf(rotation[1])));
            rotationMap.put(Direction.SOUTH, Quadrant.valueOf('R'+String.valueOf(rotation[2])));
            rotationMap.put(Direction.WEST, Quadrant.valueOf('R'+String.valueOf(rotation[3])));

            return MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(AbstractSinkBlock.LEVEL_4, BlockStateProperties.HORIZONTAL_FACING).generate(((level, facing) -> {
                return new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(level)).withYRot(rotationMap.get(facing)).withUvLock(true)));
            })));
        }

        private static BlockModelDefinitionGenerator createSmallKitchenDrawer(Block block, List<ResourceLocation> modelIdentifiers, String override, String furnitureName, String replacement) {
            Map<Direction, Variant> variantMap = new HashMap<>();
            Map<Direction, Variant> variantMapOpen = new HashMap<>();
            Integer[] rotation = new Integer[]{0, 90, 180, 270};

            variantMap.put(Direction.NORTH, new Variant(modelIdentifiers.getFirst()).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[0]))).withUvLock(true));
            variantMap.put(Direction.EAST, new Variant(modelIdentifiers.getFirst()).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[1]))).withUvLock(true));
            variantMap.put(Direction.SOUTH, new Variant(modelIdentifiers.getFirst()).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[2]))).withUvLock(true));
            variantMap.put(Direction.WEST, new Variant(modelIdentifiers.getFirst()).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[3]))).withUvLock(true));

            variantMapOpen.put(Direction.NORTH, new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[0]))).withUvLock(true));
            variantMapOpen.put(Direction.EAST, new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[1]))).withUvLock(true));
            variantMapOpen.put(Direction.SOUTH, new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[2]))).withUvLock(true));
            variantMapOpen.put(Direction.WEST, new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.valueOf('R'+String.valueOf(rotation[3]))).withUvLock(true));

            return MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.OPEN).generate((facing, open) -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction))
                        return new MultiVariant(WeightedList.of(open ? variantMapOpen.get(direction) : variantMap.get(direction)));
                }
                return null;
            }));
        }


        private static BlockModelDefinitionGenerator createLadderBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            Condition northFalse = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, false).build()));
            Condition northTrue = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, true).build()));

            Condition eastFalse = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, false).build()));
            Condition eastTrue = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, true).build()));

            Condition westFalse = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, false).build()));
            Condition westTrue = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, true).build()));

            Condition southFalse = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, false).build()));
            Condition southTrue = new CombinedCondition(CombinedCondition.Operation.AND, List.of(new ConditionBuilder().term(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).term(net.minecraft.world.level.block.state.properties.BlockStateProperties.UP, true).build()));

            return MultiPartGenerator.multiPart(block)
                    .with(northFalse, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(0)))))
                    .with(northTrue, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(1)))))
                    .with(eastFalse, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(0)).withYRot(Quadrant.R90))))
                    .with(eastTrue, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.R90))))
                    .with(westFalse, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(0)).withYRot(Quadrant.R270))))
                    .with(westTrue, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.R270))))
                    .with(southFalse, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(0)).withYRot(Quadrant.R180))))
                    .with(southTrue, new MultiVariant(WeightedList.of(new Variant(modelIdentifiers.get(1)).withYRot(Quadrant.R180))));
        }

        private static BlockModelDefinitionGenerator createBedBlockState(Block block, ResourceLocation typeId, List<ResourceLocation> modelIdentifiers) {
            Map<Direction, Quadrant> variantMap = new HashMap<>();
            ResourceLocation id;
            if (modelIdentifiers.size() == 1) {
                id = modelIdentifiers.get(0);
            } else {
                id = ModelLocationUtils.getModelLocation(block);
            }
            Integer[] rotationArray = new Integer[]{0, 90, 180, 270};
            variantMap.put(Direction.NORTH, Quadrant.valueOf('R'+String.valueOf(rotationArray[0])));
            variantMap.put(Direction.EAST, Quadrant.valueOf('R'+String.valueOf(rotationArray[1])));
            variantMap.put(Direction.SOUTH, Quadrant.valueOf('R'+String.valueOf(rotationArray[2])));
            variantMap.put(Direction.WEST, Quadrant.valueOf('R'+String.valueOf(rotationArray[3])));

            return MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING).generate(facing -> {
                for (Direction direction : variantMap.keySet()) {
                    if (facing.equals(direction)) {
                        Variant variant = new Variant(id).withYRot(variantMap.get(direction));
                        ((PFMModelVariantExtension)(Object)variant).pfm$setCustomType(typeId);
                        return new MultiVariant(WeightedList.of(variant));
                    }
                }
                return null;
            }));
        }
        private static BlockModelDefinitionGenerator createOrientableTuckableBlockState(Block block, List<ResourceLocation> modelIdentifiers) {
            return createOrientableTuckableBlockState(block, modelIdentifiers, 0);
        }
        private static BlockModelDefinitionGenerator createOrientableTuckableBlockState(Block block, List<ResourceLocation> modelIdentifiers, int rotation) {
            Map<TuckableVariant, Variant> variantList = new HashMap<>();
            Integer[] rotationArray = new Integer[]{90, 270, 180, 0};
            for (int i = 0; rotationArray.length > i; i++) {
                rotationArray[i] = (rotationArray[i] + rotation) % 360;
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
                            variantList.put(new TuckableVariant(tucked, direction), new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[0]))));
                            break;
                        }
                        case SOUTH -> {
                            variantList.put(new TuckableVariant(tucked, direction), new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[1]))));
                            break;
                        }
                        case EAST ->  {
                            variantList.put(new TuckableVariant(tucked, direction), new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[2]))));
                            break;
                        }
                        case WEST -> {
                            variantList.put(new TuckableVariant(tucked, direction), new Variant(id).withYRot(Quadrant.valueOf('R'+String.valueOf(rotationArray[3]))));
                            break;
                        }
                    }
                }
            }
            return MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BasicChairBlock.TUCKED).generate((direction, aBoolean) -> {
                for (TuckableVariant tuckableVariant : variantList.keySet()){
                    if (tuckableVariant.direction.equals(direction) && tuckableVariant.tucked == aBoolean) {
                        return new MultiVariant(WeightedList.of(variantList.get(tuckableVariant)));
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
