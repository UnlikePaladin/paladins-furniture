package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMGenerator;
import com.unlikepaladin.pfm.runtime.PFMProvider;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.data.loot.BlockLoot.createSinglePropConditionTable;

public class PFMLootTableProvider extends PFMProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTypeGenerators = ImmutableList.of(Pair.of(PFMLootTableGenerator::new, LootContextParamSets.BLOCK));

    public PFMLootTableProvider(PFMGenerator parent) {
        super(parent, "PFM Drops");
        parent.setProgress("Generating Loot Tables");
    }

    @Override
    public void run() {
        startProviderRun();
        createWriter();

        Path path = getParent().getResultItem();
        Set<ResourceLocation> identifiers = new HashSet<>();
        this.lootTypeGenerators.forEach((pair) -> pair.getFirst().get().accept((identifier, builder) -> {
            if (!identifiers.add(identifier)) {
                throw new IllegalStateException("Duplicate loot table " + identifier);
            } else {
                Path path2 = getResultItem(path, identifier);
                enqueueJsonWrite(getWriteQueue(), path2, LootTables.serialize(builder.setParamSet(pair.getSecond()).build()));
            }
        }));

        waitForWrite();
        endProviderRun();
    }

    private static Path getResultItem(Path rootOutput, ResourceLocation lootTableId) {
        return rootOutput.resolve("data/" + lootTableId.getNamespace() + "/loot_tables/" + lootTableId.getPath() + ".json");
    }

    private static LootTable.Builder lampDrop(Block drop) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(drop)
                                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("variant", "BlockEntityTag.variant").copy("color", "BlockEntityTag.color"))
                                )
                );
    }

    static class PFMLootTableGenerator implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
        private final Map<ResourceLocation, LootTable.Builder> lootTables = Maps.newHashMap();
        private final List<Block> pfmBlocks = new ArrayList<>();
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
            List<Block> blocks = PaladinFurnitureModBlocksItems.BLOCKS;
            blocks.forEach(this::addDrop);
            Block[] beds = PaladinFurnitureModBlocksItems.getBeds();
            Arrays.stream(beds).forEach(bed -> this.addDrop(bed, (Block block) -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD)));
            BasicBathtubBlock.basicBathtubBlockStream().forEach(basicBathtubBlock -> this.addDrop(basicBathtubBlock, (Block block) -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD)));
            this.addDrop(PaladinFurnitureModBlocksItems.BASIC_LAMP, PFMLootTableProvider::lampDrop);

            HashSet<ResourceLocation> set = Sets.newHashSet();
            for (Block block : pfmBlocks) {
                ResourceLocation identifier = block.getLootTable();
                if (identifier == BuiltInLootTables.EMPTY || !set.add(identifier)) continue;
                LootTable.Builder builder5 = this.lootTables.remove(identifier);
                if (builder5 == null) {
                    throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", identifier, Registry.BLOCK.getKey(block)));
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
            this.addDrop(block, BlockLoot.createSingleItemTable(drop));
        }

        public void addDrop(Block block) {
            this.addDrop(block, block);
        }

        public final void addDrop(Block block, LootTable.Builder lootTable) {
            this.lootTables.put(block.getLootTable(), lootTable);
            this.pfmBlocks.add(block);
        }
    }
}

