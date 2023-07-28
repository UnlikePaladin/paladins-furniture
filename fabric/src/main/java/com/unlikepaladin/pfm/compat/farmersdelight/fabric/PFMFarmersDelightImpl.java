package com.unlikepaladin.pfm.compat.farmersdelight.fabric;

import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import com.unlikepaladin.pfm.blocks.IronStoveBlock;
import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.farmersdelight.PFMFarmersDelight;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PFMFarmersDelightImpl extends PFMFarmersDelight {
    public static PFMFarmersDelight getInstance() {
        return new PFMFarmersDelightImpl();
    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerBlockEntities() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerScreenHandlers() {

    }

    @Override
    public void registerScreens() {

    }

    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {

    }

    @Override
    public void generateTags() {
        List<Block> stoves = new ArrayList<>(StoveBlock.streamStoves().map(FurnitureBlock::getBlock).toList());
        stoves.addAll(IronStoveBlock.streamIronStoves().map(FurnitureBlock::getBlock).toList());
        stoves.add(PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP);
        PFMTagProvider.getOrCreateTagBuilder(TagsRegistry.HEAT_SOURCES)
                .add(stoves.toArray(new Block[0]));
    }

    @Override
    public String getModId() {
        return FarmersDelightMod.MOD_ID;
    }
}
