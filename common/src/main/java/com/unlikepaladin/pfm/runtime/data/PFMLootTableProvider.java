package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.EmptyTagLookupWrapper;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class PFMLootTableProvider extends PFMProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, ContextKeySet>> lootTypeGenerators = ImmutableList.of(Pair.of(PFMLootTableGenerator::new, LootContextParamSets.BLOCK));

    public PFMLootTableProvider(PFMGenerator parent) {
        super(parent, "PFM Drops");
        parent.setProgress("Generating Loot Tables");
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();

        Path path = getParent().getOutput();
        Set<Identifier> identifiers = new HashSet<>();
        HolderLookup.Provider lookup = EmptyTagLookupWrapper.wrap(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
        RegistryOps<JsonElement> ops = lookup.createSerializationContext(JsonOps.INSTANCE);
        this.lootTypeGenerators.forEach((pair) -> pair.getFirst().get().accept((identifier, builder) -> {
            if (!identifiers.add(identifier)) {
                throw new IllegalStateException("Duplicate loot table " + identifier);
            } else {
                Path path2 = getResultItem(path, identifier);
                enqueueJsonWrite(getWriteQueue(), path2, LootTable.DIRECT_CODEC.encodeStart(ops, builder.build()).getOrThrow(IllegalStateException::new));
            }
        }));

        waitForWrite();
        endProviderRun();
    }

    public String getName() {
        return "PFM Loot Tables";
    }

    private static Path getResultItem(Path rootOutput, Identifier lootTableId) {
        return rootOutput.resolve("data/" + lootTableId.getNamespace() + "/loot_table/" + lootTableId.getPath() + ".json");
    }

    private static LootTable.Builder lampDrop(Block drop) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setBonusRolls(ContextFloatProviders.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(drop)
                                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(PFMComponents.VARIANT_COMPONENT).include(PFMComponents.COLOR_COMPONENT))
                                )
                );
    }

    static class PFMLootTableGenerator implements Consumer<BiConsumer<Identifier, LootTable.Builder>> {
        private final Map<Identifier, LootTable.Builder> lootTables = Maps.newHashMap();
        private final List<Block> pfmBlocks = new ArrayList<>();
        public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
            List<Block> blocks = PaladinFurnitureModBlocksItems.BLOCKS;
            blocks.forEach(this::addDrop);
            Block[] beds = PaladinFurnitureModBlocksItems.getBeds();
            Arrays.stream(beds).forEach(bed -> this.addDrop(bed, (Block block) -> dropsWithProperty(block, BedBlock.PART, BedPart.HEAD)));
            BasicBathtubBlock.basicBathtubBlockStream().forEach(basicBathtubBlock -> this.addDrop(basicBathtubBlock, (Block block) -> dropsWithProperty(block, BedBlock.PART, BedPart.HEAD)));
            this.addDrop(PaladinFurnitureModBlocksItems.BASIC_LAMP, PFMLootTableProvider::lampDrop);

            HashSet<Identifier> set = Sets.newHashSet();
            for (Block block : pfmBlocks) {
                if (block.getLootTable().isEmpty()) continue;

                Identifier identifier = block.getLootTable().get().identifier();
                if (!set.add(identifier)) continue;
                LootTable.Builder builder5 = this.lootTables.remove(identifier);
                if (builder5 == null) {
                    throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", identifier, BuiltInRegistries.BLOCK.getKey(block)));
                }
                biConsumer.accept(identifier, builder5);
            }
            if (!this.lootTables.isEmpty()) {
                throw new IllegalStateException("Created block loot tables for non-blocks: " + this.lootTables.keySet());
            }
        }

        private void addDrop(Block block, Function<Block, LootTable.Builder> lootTableFunction) {
            this.addDrop(block, lootTableFunction.apply(block));
        }

        public void addDrop(Block block, Block drop) {
            this.addDrop(block, drops(drop));
        }

        public LootTable.Builder drops(ItemLike drop) {
            return LootTable.lootTable().withPool(this.addSurvivesExplosionCondition(drop, LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(drop))));
        }

        public void addDrop(Block block) {
            this.addDrop(block, block);
        }

        public final void addDrop(Block block, LootTable.Builder lootTable) {
            this.lootTables.put(block.getLootTable().get().identifier(), lootTable);
            this.pfmBlocks.add(block);
        }

        public <T extends Comparable<T> & StringRepresentable> LootTable.Builder dropsWithProperty(Block drop, Property<T> property, T value) {
            return LootTable.lootTable().withPool(
                this.addSurvivesExplosionCondition(
                    drop,
                    LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(
                            LootItem.lootTableItem(drop)
                                .when(
                                    MatchBlock.blockMatches(
                                        BuiltInRegistries.BLOCK,
                                        drop,
                                        StatePropertiesPredicate.Builder.properties().hasProperty(property, value)
                                    )
                                )
                        )
                )
            );
        }

        protected <T extends ConditionUserBuilder<T>> T addSurvivesExplosionCondition(ItemLike drop, ConditionUserBuilder<T> builder) {
            return builder.unwrap();
        }
    }
}

