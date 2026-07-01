package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.compat.PFMClientModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.client.PFMCookingForBlockheadsClient;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.networking.ClientStoveResultsPacket;
import com.unlikepaladin.pfm.data.PFMTag;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import com.unlikepaladin.pfm.runtime.data.SimpleFurnitureRecipeJsonFactory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProcessorHolder;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PFMCookingForBlockheadsImpl extends PFMCookingForBlockheads {
    private PFMClientModCompatibility clientModCompatibility;

    public PFMCookingForBlockheadsImpl() {
    }

    public static void registerPackets(PayloadRegistrar registrar) {
        registrar.playToClient(ClientStoveResultsPacket.TYPE, ClientStoveResultsPacket.STREAM_CODEC, (payload, context) -> ClientStoveResultsPacket.handle(context.player(), payload));
    }

    @Override
    public void generateTags() {
        super.generateTags();

        PFMTagProvider.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK);

        PFMTagProvider.getOrCreateTagBuilder(ModBlockTags.COOKING_TABLES)
                .add(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK);

        List<Block> storageBlocks = new ArrayList<>(PaladinFurnitureMod.furnitureEntryMap.get(KitchenDrawerBlock.class).getAllBlocks());
        storageBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerBlock.class).getAllBlocks());
        storageBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCabinetBlock.class).getAllBlocks());
        storageBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(ClassicNightstandBlock.class).getAllBlocks());
        storageBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallDrawerSmallBlock.class).getAllBlocks());
        storageBlocks.addAll(PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterOvenBlock.class).getAllBlocks());
        storageBlocks.addAll(List.of(PaladinFurnitureModBlocksItems.WHITE_FRIDGE, PaladinFurnitureModBlocksItems.XBOX_FRIDGE, PaladinFurnitureModBlocksItems.GRAY_FRIDGE, PaladinFurnitureModBlocksItems.IRON_FRIDGE));

        PFMTagProvider.getOrCreateTagBuilder(ModBlockTags.KITCHEN_ITEM_PROVIDERS)
                .add(storageBlocks.toArray(new Block[0]));

        Block[] ovens = {PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE};
        Block[] freezers = {PaladinFurnitureModBlocksItems.GRAY_FREEZER, PaladinFurnitureModBlocksItems.IRON_FREEZER, PaladinFurnitureModBlocksItems.WHITE_FREEZER};

        PFMTag<Block> builder = PFMTagProvider.getOrCreateTagBuilder(ModBlockTags.KITCHEN_CONNECTORS);
        PaladinFurnitureMod.furnitureEntryMap.get(KitchenCounterBlock.class).getAllBlocks().forEach(builder::add);
        PaladinFurnitureMod.furnitureEntryMap.get(KitchenWallCounterBlock.class).getAllBlocks().forEach(builder::add);
        PaladinFurnitureMod.furnitureEntryMap.get(KitchenSinkBlock.class).getAllBlocks().forEach(builder::add);
        builder.add(storageBlocks.toArray(new Block[0]));
        builder.add(ovens);
        builder.add(freezers);
    }

    @Override
    public void generateRecipes(RecipeOutput exporter) {
        SimpleFurnitureRecipeJsonFactory.create(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK, 4).group("kitchen").unlockedBy(PFMRecipeProvider.getunlockedByNameFromOutput(PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK), PFMRecipeProvider.conditionsFromItem(ModItems.recipeBook)).input(ModItems.recipeBook).input(Blocks.WHITE_CONCRETE, 2).input(Blocks.GRAY_CONCRETE).save(exporter, ResourceLocation.fromNamespaceAndPath("pfm", PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK.asItem().getDescriptionId().replace("block.pfm.", "")));
    }

    @Override
    public String getModId() {
        return CookingForBlockheads.MOD_ID;
    }

    @Override
    public void registerBlocks() {
        LateBlockRegistry.registerLateBlockClassic("cooking_table", PFMCookingForBlockHeadsCompat.COOKING_TABLE_BLOCK, true, PaladinFurnitureMod.FURNITURE_GROUP);
    }

    @Override
    public void registerBlockEntityTypes() {
        initCapabilities(Balm.getCapabilities());
    }

    public static PFMCookingForBlockheads getInstance() {
        return new PFMCookingForBlockheadsImpl();
    }

    @Override
    public Optional<PFMClientModCompatibility> getClientModCompatiblity() {
        if (clientModCompatibility == null)
            clientModCompatibility = new PFMCookingForBlockheadsClient(this);
        return Optional.of(clientModCompatibility);
    }

    public void initCapabilities(BalmCapabilities balmCapabilities) {
        balmCapabilities.registerProvider(CookingForBlockheads.id("kitchen_item_provider"), ModCapabilities.KITCHEN_ITEM_PROVIDER, ((blockEntity, unused) -> {
            if (blockEntity instanceof KitchenItemProviderHolder provider) {
                return provider.getKitchenItemProvider();
            }
            return null;
        }),  () -> List.of(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, BlockEntities.DRAWER_BLOCK_ENTITY, BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY, BlockEntities.FRIDGE_BLOCK_ENTITY, BlockEntities.FREEZER_BLOCK_ENTITY, BlockEntities.STOVE_BLOCK_ENTITY));
        balmCapabilities.registerProvider(CookingForBlockheads.id("kitchen_item_processor"), ModCapabilities.KITCHEN_ITEM_PROCESSOR, (blockEntity, context) -> {
            if (blockEntity instanceof KitchenItemProcessorHolder holder) {
                return holder.getKitchenItemProcessor();
            }
            return null;
        }, () -> List.of(BlockEntities.STOVE_BLOCK_ENTITY, BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, BlockEntities.STOVE_TOP_BLOCK_ENTITY));
    }
}
