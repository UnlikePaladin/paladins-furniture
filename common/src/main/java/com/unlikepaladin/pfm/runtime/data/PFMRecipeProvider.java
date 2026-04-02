package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
// TODO : Remake EMI screen to work with the new recipe system
public class PFMRecipeProvider extends PFMProvider {

    public PFMRecipeProvider(PFMGenerator parent) {
        super(parent, "PFM Recipes");
        parent.setProgress("Generating Recipes");
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();

        Path path = getParent().getResultItem();
        Set<ResourceLocation> set = Sets.newHashSet();
        WorkbenchScreenHandler.ALL_RECIPES.clear();
        generateRecipes(new RecipeOutput() {
            @Override
            public void accept(ResourceLocation recipeId, Recipe<?> recipe, @Nullable AdvancementHolder advancementEntry) {
                if (!set.add(recipeId)) {
                    getParent().getLogger().error("Duplicate recipe " + recipeId);
                    throw new IllegalStateException("Duplicate recipe " + recipeId);
                }
                if (recipe == null) {
                    getParent().getLogger().error("Recipe Json Provider is null");
                    throw new IllegalStateException("Recipe Json Provider is null");
                }
                Path recipePath = path.resolve("data/" + recipeId.getNamespace() + "/recipes/" + recipeId.getPath() + ".json");
                enqueueJsonWrite(getWriteQueue(), recipePath, Util.getResult(Recipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe), IllegalStateException::new));
                if (advancementEntry != null) {
                    Path advancementPath = path.resolve("data/" + recipeId.getNamespace() + "/advancements/" + advancementEntry.id().getPath() + ".json");
                    enqueueJsonWrite(getWriteQueue(), advancementPath, Util.getResult(Advancement.CODEC.encodeStart(JsonOps.INSTANCE, advancementEntry.value()), IllegalStateException::new));
                }
            }

            @Override
            public Advancement.Builder advancement() {
                return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
            }
        });

        enqueueJsonWrite(getWriteQueue(), path.resolve("data/pfm/advancements/recipes/root.json"), Util.getResult(Advancement.CODEC.encodeStart(JsonOps.INSTANCE, Advancement.Builder.create().criterion("has_planks", conditionsFromTag(ItemTags.PLANKS)).build(new ResourceLocation("root")).value()), IllegalAccessError::new));
        waitForWrite();
        endProviderRun();
    }

    @ExpectPlatform
    protected static ResourceLocation getId(Block block) {
        throw new AssertionError();    
    }

    @ExpectPlatform
    protected static ResourceLocation getId(Item item) {
        throw new AssertionError();
    }

    protected void generateRecipes(RecipeOutput exporter) {
        List<ResourceLocation> generatedRecipes = new ArrayList<>();

        offerBasicChairRecipe(BasicChairBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariants(), exporter);
        offerDinnerChairRecipe(DinnerChairBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(DinnerChairBlock.class).getVariants(), exporter);
        offerClassicChairRecipe(ClassicChairBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicChairBlock.class).getVariants(), exporter);

        FurnitureBlock[] froggyChairs = FroggyChairBlock.streamFroggyChair().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock froggyChair : froggyChairs) {
            if (!generatedRecipes.contains(getId(froggyChair.getBlock()))) {
                offerFroggyChairRecipe(froggyChair.getBlock(), Ingredient.of(froggyChair.getFroggyChairMaterial().asItem()), exporter);
                generatedRecipes.add(getId(froggyChair.getBlock()));
            }
        }
        FurnitureBlock[] woolClassicChairs = ClassicChairDyeableBlock.streamWoodDyeableChair().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock classicChair : woolClassicChairs) {
            if (!generatedRecipes.contains(getId(classicChair.getBlock()))) {
                offerClassicChairDyedRecipe(classicChair.getBlock(), Ingredient.of(Items.OAK_LOG), Ingredient.of(classicChair.getArmChairMaterial()), exporter);
                generatedRecipes.add(getId(classicChair.getBlock()));
            }
        }
        offerModernChairRecipe(ModernChairBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ModernChairBlock.class).getVariants(), exporter);

        FurnitureBlock[] armChairs = ArmChairColoredBlock.streamArmChairColored().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock armChair : armChairs) {
            if (!generatedRecipes.contains(getId(armChair.getBlock()))) {
                offerArmChairRecipe(armChair.getBlock(), Ingredient.of(Items.OAK_LOG), Ingredient.of(armChair.getArmChairMaterial().asItem()), exporter);
                generatedRecipes.add(getId(armChair.getBlock()));
            }
        }
        FurnitureBlock[] simpleSofas = SimpleSofaBlock.streamSimpleSofas().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock sofa : simpleSofas) {
            if (!generatedRecipes.contains(getId(sofa.getBlock()))) {
                offerSimpleSofaRecipe(sofa.getBlock(), Ingredient.of(Items.OAK_LOG), Ingredient.of(sofa.getArmChairMaterial().asItem()), exporter);
                generatedRecipes.add(getId(sofa.getBlock()));
            }
        }
        FurnitureBlock[] leatherArmChairs =  ArmChairBlock.streamArmChairs().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock armChair : leatherArmChairs) {
            if (!generatedRecipes.contains(getId(armChair.getBlock()))) {
                offerArmChairRecipe(armChair.getBlock(), Ingredient.of(Items.OAK_LOG), Ingredient.of(armChair.getArmChairMaterial().asItem()), exporter);
                generatedRecipes.add(getId(armChair.getBlock()));
            }
        }

        offerBasicTableRecipe(BasicTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(BasicTableBlock.class).getVariants(), exporter);
        offerClassicTableRecipe(ClassicTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicTableBlock.class).getVariants(), exporter);
        offerLogTableRecipe(LogTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(LogTableBlock.class).getVariants(), exporter);
        offerLogTableRecipe(RawLogTableBlock.class, "secondary", "secondary", PaladinFurnitureMod.furnitureEntryMap.get(RawLogTableBlock.class).getVariants(), exporter);
        offerDinnerTableRecipe(DinnerTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(DinnerTableBlock.class).getVariants(), exporter);
        offerModernDinnerTableRecipe(ModernDinnerTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ModernDinnerTableBlock.class).getVariants(), exporter);
        offerClassicNightStandRecipe(ClassicNightstandBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicNightstandBlock.class).getVariants(), exporter);


        for (Block block : getVanillaBeds()) {
            offerSimpleBedRecipe(SimpleBedBlock.class, "base", PaladinFurnitureMod.furnitureEntryMap.get(SimpleBedBlock.class).getVariants(), Ingredient.of(block), exporter);
            offerClassicBedRecipe(ClassicBedBlock.class, "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicBedBlock.class).getVariants(), Ingredient.of(block), "fence", exporter);
        }

        offerSimpleBunkLadderRecipe(SimpleBunkLadderBlock.class, "base", PaladinFurnitureMod.furnitureEntryMap.get(SimpleBunkLadderBlock.class).getVariants(), exporter);

        offerLogStoolRecipe(LogStoolBlock.class, "secondary", PaladinFurnitureMod.furnitureEntryMap.get(LogStoolBlock.class).getVariants(), exporter);
        offerSimpleStoolRecipe(SimpleStoolBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(SimpleStoolBlock.class).getVariants(), exporter);
        offerClassicStoolRecipe(ClassicStoolBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicStoolBlock.class).getVariants(), exporter);
        offerModernStoolRecipe(ModernStoolBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ModernStoolBlock.class).getVariants(), exporter);
        offerCounterRecipe(KitchenCounterBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterBlock.class).getVariants(), exporter);
        offerKitchenSinkRecipe(KitchenSinkBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).getVariants(), Ingredient.of(Items.BUCKET), Ingredient.of(Items.IRON_INGOT), exporter);
        offerCounterApplianceRecipe(KitchenDrawerBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).getVariants(), Ingredient.of(Items.CHEST), exporter);
        offerCounterApplianceRecipe(KitchenCounterOvenBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getVariants(), Ingredient.of(Items.FURNACE), exporter);
        offerCounterRecipe(KitchenWallCounterBlock.class, "base", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).getVariants(), exporter);
        offerWallDrawerRecipe(KitchenWallDrawerBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getVariants(), Ingredient.of(Items.CHEST), exporter);
        offerWallDrawerSmallRecipe(KitchenWallDrawerSmallBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getVariants(), Ingredient.of(Items.CHEST), exporter);
        offerCabinetRecipe(KitchenCabinetBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).getVariants(), Ingredient.of(Items.CHEST), exporter);

        for (Map.Entry<VariantBase<?>, ? extends Block> herringbonePlank : PaladinFurnitureMod.furnitureEntryMap.get(HerringbonePlankBlock.class).entrySet()) {
            if (!generatedRecipes.contains(getId(herringbonePlank.getValue()))) {
                Item material = herringbonePlank.getKey().getItemOfThis("slab") != null
                        ? herringbonePlank.getKey().getItemOfThis("slab") :
                        herringbonePlank.getKey().getBaseBlock().asItem();

                offerHerringbonePlanks(herringbonePlank.getValue(), material, exporter);
            }
        }
        FurnitureBlock[] fridges = FridgeBlock.streamFridges().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock fridge : fridges) {
            if (!generatedRecipes.contains(getId(fridge.getBlock()))) {
                offerFridgeRecipe(fridge.getBlock(), Ingredient.of(fridge.getFridgeMaterial().asItem()), Ingredient.of(Items.CHEST), exporter);
                generatedRecipes.add(getId(fridge.getBlock()));
            }
        }
        FurnitureBlock[] freezers = FreezerBlock.streamFreezers().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock freezer : freezers) {
            if (!generatedRecipes.contains(getId(freezer.getBlock()))) {
                offerFreezerRecipe(freezer.getBlock(), Ingredient.of(freezer.getFridgeMaterial().asItem()), exporter);
                generatedRecipes.add(getId(freezer.getBlock()));
            }
        }
        FurnitureBlock[] microwaves = MicrowaveBlock.streamMicrowaves().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock microwave : microwaves) {
            if (!generatedRecipes.contains(getId(microwave.getBlock()))) {
                offerMicrowaveRecipe(microwave.getBlock(),  Ingredient.of(microwave.getFridgeMaterial().asItem()), Ingredient.of(Items.FURNACE), exporter);
                generatedRecipes.add(getId(microwave.getBlock()));
            }
        }
        FurnitureBlock[] rangeHoods = KitchenRangeHoodBlock.streamOvenRangeHoods().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock rangeHood : rangeHoods) {
            if (!generatedRecipes.contains(getId(rangeHood.getBlock()))) {
                offerRangeHoodRecipe(rangeHood.getBlock(),  Ingredient.of(rangeHood.getFridgeMaterial().asItem()), Ingredient.of(Items.REDSTONE_LAMP), exporter);
                generatedRecipes.add(getId(rangeHood.getBlock()));
            }
        }
        FurnitureBlock[] stoves = StoveBlock.streamStoves().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock stove : stoves) {
            if (!generatedRecipes.contains(getId(stove.getBlock()))) {
                offerStoveRecipe(stove.getBlock(),  Ingredient.of(stove.getFridgeMaterial().asItem()), Ingredient.of(Items.FURNACE), exporter);
                generatedRecipes.add(getId(stove.getBlock()));
            }
        }
        FurnitureBlock[] ironStove = IronStoveBlock.streamIronStoves().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock stove : ironStove) {
            if (!generatedRecipes.contains(getId(stove.getBlock()))) {
                offerStoveRecipe(stove.getBlock(),  Ingredient.of(stove.getFridgeMaterial().asItem()), Ingredient.of(Items.FURNACE), exporter);
                generatedRecipes.add(getId(stove.getBlock()));
            }
        }
        KitchenStovetopBlock[] stovetopBlocks = KitchenStovetopBlock.streamKitchenStovetop().toList().toArray(new KitchenStovetopBlock[0]);
        for (KitchenStovetopBlock stove : stovetopBlocks) {
            if (!generatedRecipes.contains(getId(stove))) {
                offerStovetopRecipe(stove, Ingredient.of(Items.IRON_INGOT), Ingredient.of(Blocks.GRAY_CONCRETE), exporter);
                generatedRecipes.add(getId(stove));
            }
        }
        FurnitureBlock[] plates = PlateBlock.streamPlates().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock plate : plates) {
            if (!generatedRecipes.contains(getId(plate.getBlock()))) {
                offerPlateRecipe(plate.getBlock(), Ingredient.of(plate.getPlateMaterial()), Ingredient.of(Items.ITEM_FRAME), Ingredient.of(plate.getPlateDecoration()), exporter);
                generatedRecipes.add(getId(plate.getBlock()));
            }
        }
        FurnitureBlock[] cutleries = CutleryBlock.streamCutlery().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock cutlery : cutleries) {
            if (!generatedRecipes.contains(getId(cutlery.getBlock()))) {
                offerCutleryRecipe(cutlery.getBlock(), Ingredient.of(cutlery.getCutleryMaterial()), exporter);
                generatedRecipes.add(getId(cutlery.getBlock()));
            }
        }
        PaladinFurnitureMod.furnitureEntryMap.get(PendantBlock.class).getAllBlocks().forEach((block) -> {
            if (!generatedRecipes.contains(getId(block))) {
                Block hang = Blocks.LIGHT_GRAY_CONCRETE;
                Block base;
                if (block == PaladinFurnitureModBlocksItems.GLASS_MODERN_PENDANT)
                    base = Blocks.WHITE_STAINED_GLASS;
                else if (block == PaladinFurnitureModBlocksItems.WHITE_MODERN_PENDANT)
                    base = Blocks.WHITE_CONCRETE;
                else
                    base = Blocks.GRAY_CONCRETE;
                offerPendantRecipe(block.asItem(), Ingredient.of(base), Ingredient.of(hang), exporter);
                generatedRecipes.add(getId(block));
            }
        });
        if (!generatedRecipes.contains(BuiltInRegistries.ITEM.getKey(PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM))) {
            SimpleFurnitureRecipeJsonFactory.create(PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM, 6).input(Blocks.WHITE_CONCRETE, 6).input(Blocks.LIGHT_GRAY_CONCRETE, 2).input(Items.REDSTONE).save(exporter, new ResourceLocation("pfm", PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM.getDescriptionId().replace("block.pfm.", "")));
            generatedRecipes.add(BuiltInRegistries.ITEM.getKey(PaladinFurnitureModBlocksItems.LIGHT_SWITCH_ITEM));
        }

        FurnitureBlock[] basicToilets = BasicToiletBlock.streamBasicToilet().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock toilet : basicToilets) {
            if (!generatedRecipes.contains(getId(toilet.getBlock()))) {
                saveiletRecipe(toilet.getBlock(), Ingredient.of(Items.STONE_BUTTON), Ingredient.of(Blocks.QUARTZ_BLOCK), exporter);
                generatedRecipes.add(getId(toilet.getBlock()));
            }
        }

        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER))) {
            offerWallToiletPaperRecipe(PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER,  Ingredient.of(Blocks.STONE), exporter);
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.WALL_TOILET_PAPER));
        }

        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.BASIC_SINK))) {
            offerSinkRecipe(PaladinFurnitureModBlocksItems.BASIC_SINK, Ingredient.of(Blocks.QUARTZ_BLOCK), exporter);
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.BASIC_SINK));
        }

        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.BASIC_BATHTUB))) {
            offerBathtubRecipe(PaladinFurnitureModBlocksItems.BASIC_BATHTUB, Ingredient.of(Blocks.QUARTZ_BLOCK), exporter);
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.BASIC_BATHTUB));
        }

        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD))) {
            offerShowerHeadRecipe(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD, Ingredient.of(Items.WATER_BUCKET), exporter);
            offerShowerHandleRecipe(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM, Ingredient.of(Blocks.LEVER), exporter);
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD));
            generatedRecipes.add(BuiltInRegistries.ITEM.getKey(PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE_ITEM));
        }

        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.MESH_TRASHCAN))) {
            SimpleFurnitureRecipeJsonFactory.create(PaladinFurnitureModBlocksItems.MESH_TRASHCAN, 1).input(Items.IRON_INGOT, 1).input(Items.ENDER_PEARL, 1).input(Blocks.IRON_BARS, 4).save(exporter, new ResourceLocation("pfm", PaladinFurnitureModBlocksItems.MESH_TRASHCAN.asItem().getDescriptionId().replace("block.pfm.", "")));
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.MESH_TRASHCAN));
        }
        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.TRASHCAN))) {
            SimpleFurnitureRecipeJsonFactory.create(PaladinFurnitureModBlocksItems.TRASHCAN, 1).input(Items.IRON_INGOT, 1).input(Items.ENDER_PEARL, 1).input(Blocks.IRON_BARS, 4).save(exporter, new ResourceLocation("pfm", PaladinFurnitureModBlocksItems.TRASHCAN.asItem().getDescriptionId().replace("block.pfm.", "")));
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.TRASHCAN));
        }
        if (!generatedRecipes.contains(getId(PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM))) {
            offerOfficeChairRecipes(exporter);
            generatedRecipes.add(getId(PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM));
        }

        FurnitureBlock[] showerTowels = ShowerTowelBlock.streamShowerTowels().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock towel : showerTowels) {
            if (!generatedRecipes.contains(getId(towel.getBlock()))) {
                offerShowerTowelRecipe(towel.getBlock(),  Ingredient.of(towel.getWoolColor()), exporter);
                generatedRecipes.add(getId(towel.getBlock()));
            }
        }

        FurnitureBlock[] mirrors = MirrorBlock.streamMirrorBlocks().toList().toArray(new FurnitureBlock[0]);
        for (FurnitureBlock mirror : mirrors) {
            if (!generatedRecipes.contains(getId(mirror.getBlock()))) {
                offerMirrorRecipe(mirror.getBlock(), Ingredient.of(mirror.getBaseMaterial()), exporter);
                generatedRecipes.add(getId(mirror.getBlock()));
            }
        }

        offerLampRecipes(exporter);

        offerBasicCoffeeTableRecipe(BasicCoffeeTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(BasicCoffeeTableBlock.class).getVariants(), exporter);
        offerModernCoffeeTableRecipe(ModernCoffeeTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ModernCoffeeTableBlock.class).getVariants(), exporter);
        offerClassicCoffeeTableRecipe(ClassicCoffeeTableBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicCoffeeTableBlock.class).getVariants(), exporter);
        offerBasicDeskRecipe(BasicDeskBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskBlock.class).getVariants(), exporter);
        offerBasicDeskCabinetRecipe(BasicDeskCabinetBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(BasicDeskCabinetBlock.class).getVariants(), exporter);
        offerClassicDeskRecipe(ClassicDeskBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskBlock.class).getVariants(), exporter);
        offerClassicDeskCabinetRecipe(ClassicDeskCabinetBlock.class, "secondary", "base", PaladinFurnitureMod.furnitureEntryMap.get(ClassicDeskCabinetBlock.class).getVariants(), exporter);

        PaladinFurnitureMod.pfmModCompatibilities.forEach(pfmModCompatibility -> pfmModCompatibility.generateRecipes(exporter));
    }

    public static void offerLampRecipes(RecipeOutput exporter) {
        for (DyeColor color : DyeColor.values()) {
            CompoundTag beTag = new CompoundTag();
            beTag.putString("color", color.getSerializedName());
            CompoundTag tag = new CompoundTag();
            tag.put("BlockEntityTag", beTag);
            tag.putBoolean("variantInNbt", true);

            DynamicFurnitureRecipeJsonFactory.create(BasicLampBlock.class, 1,  WoodVariantRegistry.getVariants().stream().map(woodVariant -> woodVariant.identifier).toList(), tag).vanillaInput(ModelHelper.getWoolColor(color.getSerializedName()), 3).vanillaInput(Items.TORCH).vanillaInput(Items.REDSTONE).childInput("stripped_log", 2).save(exporter, new ResourceLocation("pfm", String.format("basic_%s_lamp", color.getSerializedName())));
        }
    }

    public static void offerOfficeChairRecipes(RecipeOutput exporter) {
        for (DyeColor color : DyeColor.values()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("Color", color.getSerializedName());
            SimpleFurnitureRecipeJsonFactory.create(PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM, tag).input(ModelHelper.getWoolColor(color.getSerializedName()), 3).input(Items.IRON_INGOT, 2).input(Items.IRON_NUGGET).input(Items.STONE_BUTTON, 4).save(exporter, new ResourceLocation("pfm", String.format("%s_office_chair", color.getSerializedName())));
        }
    }

    public static Tuple<Block, Block> getCounterMaterials(VariantBase<?> variantBase) {
        Block counterTop = variantBase.getSecondaryBlock();
        Block counterBase = variantBase.getBaseBlock();

        if (variantBase.identifier.getPath().equals("calcite") || variantBase.identifier.getPath().equals("netherite")) {
            Block temp = counterBase;
            counterBase = counterTop;
            counterTop  = temp;
        }
        return new Tuple<>(counterBase,counterTop);
    }
    public Block getVanillaBed(Block block) {
        if (block instanceof SimpleBedBlock){
            String color = ((SimpleBedBlock) block).getPFMColor().getName();
            return BuiltInRegistries.BLOCK.get(new ResourceLocation("minecraft:" + color + "_bed"));
        }
        return null;
    }

    public List<Block> getVanillaBeds() {
        List<Block> beds = new ArrayList<>();
        BuiltInRegistries.BLOCK.stream().forEach(block -> {
            if (block instanceof BedBlock && BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("minecraft"))
                beds.add(block);
        });
        return beds;
    }

    public static void offerBasicChairRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("chairs").childInput(legMaterial, 2).childInput(baseMaterial, 4).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerFroggyChairRecipe(ItemLike output, Ingredient baseMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 4).group("chairs").unlockedBy("has_concrete", conditionsFromIngredient(baseMaterial)).input(baseMaterial, 6).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerDinnerChairRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("chairs").childInput(legMaterial, 3).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicChairDyedRecipe(ItemLike output, Ingredient legMaterial, Ingredient baseMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 4).group("chairs").unlockedBy(getunlockedByNameFromOutput(output), conditionsFromIngredient(baseMaterial)).input(legMaterial, 4).input(baseMaterial, 2).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerClassicChairRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("chairs").childInput(legMaterial, 4).childInput(baseMaterial, 2).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerModernChairRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("chairs").childInput(legMaterial, 3).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));

    }
    public static void offerArmChairRecipe(ItemLike output, Ingredient baseMaterial, Ingredient legMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 2).group("chairs").unlockedBy("has_wool", conditionsFromIngredient(baseMaterial)).input(legMaterial, 4).input(baseMaterial, 2).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerSimpleSofaRecipe(ItemLike output, Ingredient baseMaterial, Ingredient legMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 2).group("chairs").unlockedBy("has_wool", conditionsFromIngredient(baseMaterial)).input(legMaterial, 2).input(baseMaterial, 4).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerBasicTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 5).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerBasicDeskRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("desks").childInput(legMaterial, 4).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerBasicDeskCabinetRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("desks").childInput(legMaterial, 4).childInput(baseMaterial, 3).vanillaInput(Ingredient.of(Items.CHEST)).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicDeskRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("desks").childInput(legMaterial, 5).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicDeskCabinetRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("desks").vanillaInput(Ingredient.of(Items.CHEST)).childInput(legMaterial, 5).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerBasicCoffeeTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 3).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerModernCoffeeTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 4).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicCoffeeTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 2).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 4).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerLogTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 2).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerHerringbonePlanks(ItemLike output, Item baseMaterial, RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4).define('X', baseMaterial).pattern("XX").pattern("XX").unlockedBy("has_wood_slabs", conditionsFromItem(baseMaterial)).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerDinnerTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 3).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerModernDinnerTableRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("tables").childInput(legMaterial, 5).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicNightStandRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("bedroom").childInput(legMaterial, 6).childInput(baseMaterial, 1).vanillaInput(Blocks.CHEST, 1).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static String getunlockedByNameFromOutput(ItemLike output) {
        return getunlockedByNameFromOutput(output, "");
    }

    public static String getEmptyCriteria() {
        return "";
    }

    public static String getunlockedByNameFromOutput(ItemLike output, String type) {
        if (Block.byItem(output.asItem()) == null || Block.byItem(output.asItem()) == Blocks.AIR || !PaladinFurnitureMod.furnitureEntryMap.containsKey(Block.byItem(output.asItem()).getClass())) {
            return getItemPath(output);
        }
        if (PaladinFurnitureMod.furnitureEntryMap.get(Block.byItem(output.asItem()).getClass()).getVariantFromEntry(Block.byItem(output.asItem())) instanceof WoodVariant) {
            return type.isEmpty() ? "has_planks" : type;
        }
        else return getItemPath(output);
    }

    public static void offerSimpleBedRecipe(Class<? extends Block> output, String legMaterial, List<ResourceLocation> variants, Ingredient baseBed, RecipeOutput exporter) {
        DyeColor color = ((BedBlock)((BlockItem)Arrays.stream(baseBed.getItems()).findFirst().get().getItem()).getBlock()).getColor();
        CompoundTag tag = new CompoundTag();
        tag.putString("color", color.getSerializedName());
        DynamicFurnitureRecipeJsonFactory.create(output, 1, variants, tag).group("bedroom").childInput(legMaterial, 5).vanillaInput(baseBed, 1).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US) + "_"+ color.getSerializedName()));

    }

    public static void offerClassicBedRecipe(Class<? extends Block> output, String legMaterial, List<ResourceLocation> variants, Ingredient baseBed, String fence, RecipeOutput exporter) {
        DyeColor color = ((BedBlock)((BlockItem)Arrays.stream(baseBed.getItems()).findFirst().get().getItem()).getBlock()).getColor();
        CompoundTag tag = new CompoundTag();
        tag.putString("color", color.getSerializedName());
        DynamicFurnitureRecipeJsonFactory.create(output, 1, variants, tag).group("bedroom").childInput(legMaterial, 3).childInput(fence, 2).vanillaInput(baseBed, 1).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US) + "_"+ ((BedBlock)((BlockItem)Arrays.stream(baseBed.getItems()).findFirst().get().getItem()).getBlock()).getColor()));
    }

    public static void offerSimpleBunkLadderRecipe(Class<? extends Block> output, String base, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("bedroom").childInput(base, 1).vanillaInput(Ingredient.of(Items.STICK), 6).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerLogStoolRecipe(Class<? extends Block> output, String legMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("stools").childInput(legMaterial, 1).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerSimpleStoolRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("stools").childInput(legMaterial, 2).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerClassicStoolRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("stools").childInput(legMaterial, 3).childInput(baseMaterial, 2).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerModernStoolRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 4, variants).group("stools").childInput(legMaterial, 1).childInput(baseMaterial, 3).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerCounterRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 6, variants).group("kitchen").childInput(legMaterial, 3).childInput(baseMaterial, 6).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerCounterApplianceRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, Ingredient appliance, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 1, variants).group("kitchen").childInput(legMaterial, 3).childInput(baseMaterial, 5).vanillaInput(appliance).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerKitchenSinkRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, Ingredient center, Ingredient ingot, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 1, variants).group("kitchen").childInput(legMaterial, 2).childInput(baseMaterial, 5).vanillaInput(ingot).vanillaInput(center).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerWallDrawerRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, Ingredient appliace, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 1, variants).group("kitchen").childInput(legMaterial, 6).childInput(baseMaterial, 2).vanillaInput(appliace).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerWallDrawerSmallRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, Ingredient appliance, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 3, variants).group("kitchen").childInput(legMaterial, 3).childInput(baseMaterial, 2).vanillaInput(appliance).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerCabinetRecipe(Class<? extends Block> output, String legMaterial, String baseMaterial, List<ResourceLocation> variants, Ingredient chest, RecipeOutput exporter) {
        DynamicFurnitureRecipeJsonFactory.create(output, 3, variants).group("kitchen").childInput(legMaterial, 6).childInput(baseMaterial, 2).vanillaInput(chest).save(exporter, new ResourceLocation("pfm", output.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US)));
    }

    public static void offerFridgeRecipe(ItemLike output, Ingredient legMaterial, Ingredient storage, RecipeOutput exporter) {
        if (output.asItem().toString().contains("xbox")) {
            SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial, 6).input(storage, 1).input(Ingredient.of(Items.REDSTONE)).input(Ingredient.of(Items.WHITE_CONCRETE)).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
        }
        else {
            SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial, 7).input(storage).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
        }
    }

    public static void offerFreezerRecipe(ItemLike output, Ingredient legMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial, 7).input(Ingredient.of(Items.REDSTONE), 2).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerMicrowaveRecipe(ItemLike output, Ingredient legMaterial, Ingredient storage, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial ,5).input(storage).input(Ingredient.of(Items.REDSTONE)).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerRangeHoodRecipe(ItemLike output, Ingredient legMaterial, Ingredient secondMaterial, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial, 4).input(secondMaterial).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerStoveRecipe(ItemLike output, Ingredient legMaterial, Ingredient storage, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(legMaterial), conditionsFromIngredient(legMaterial)).input(legMaterial, 8).input(storage).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerStovetopRecipe(ItemLike output, Ingredient base, Ingredient material, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("kitchen").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 6).input(material, 2).input(Ingredient.of(Items.FLINT_AND_STEEL)).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerPlateRecipe(ItemLike output, Ingredient base, Ingredient frame, Ingredient decoration, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 4).group("kitchen").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 4).input(frame).input(decoration, 4).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }
    public static void offerCutleryRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 4).group("kitchen").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 4).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerPendantRecipe(ItemLike output, Ingredient base, Ingredient hang, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 4).group("lighting").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 2).input(hang, 2).input(PaladinFurnitureModBlocksItems.SIMPLE_LIGHT).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void saveiletRecipe(ItemLike output, Ingredient base, Ingredient material, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(material), conditionsFromIngredient(material)).input(base).input(material, 4).input(Ingredient.of(Items.BUCKET)).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerWallToiletPaperRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(Items.PAPER), conditionsFromItem(Items.PAPER)).input(base, 1).input(Items.PAPER, 8).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerSinkRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 3).input(Items.STONE_BUTTON, 2).input(Items.IRON_INGOT, 1).input(Items.BUCKET, 1).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerBathtubRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 5).input(Items.STONE_BUTTON, 2).input(Items.BUCKET, 1).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerShowerHeadRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 1).input(Items.REDSTONE, 1).input(Items.IRON_INGOT, 1).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerShowerHandleRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 1).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base, 2).input(Items.REDSTONE, 1).input(Items.IRON_INGOT, 1).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerShowerTowelRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 2).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base,4).input(Ingredient.of(Items.LIGHT_GRAY_CONCRETE), 2).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    public static void offerMirrorRecipe(ItemLike output, Ingredient base, RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(output, 2).group("bathroom").unlockedBy("has_" + getItemPath(base), conditionsFromIngredient(base)).input(base,3).input(Ingredient.of(Items.GLASS), 2).save(exporter, new ResourceLocation("pfm", output.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(MinMaxBounds.Ints count, ItemLike item) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.item().of(item).withCount(count).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(ItemLike item) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.item().of(item).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromIngredient(Ingredient item) {
        List<Item> items = new ArrayList<>();
        for (ItemStack item1:
                item.getItems()) {
            if (items.contains(item1.getItem()))
                continue;
            items.add(item1.getItem());
        }
        return conditionsFromItemPredicates(ItemPredicate.Builder.item().of(items.toArray(new Item[0])).build());
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromTag(TagKey<Item> tag) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.item().of(tag).build());
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromPredicates(ItemPredicate.Builder... predicates) {
        return conditionsFromItemPredicates(Arrays.stream(predicates).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItemPredicates(ItemPredicate... predicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
    }

    private static String getItemPath(Ingredient item) {
        ItemStack[] n = item.getItems();
        if (n.length > 0) {
            return BuiltInRegistries.ITEM.getKey(n[0].getItem()).getPath();
        } else {
            return item.toString();
        }
    }
    private static String getItemPath(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
