package com.unlikepaladin.pfm.compat.farmersdelight.forge;

import com.unlikepaladin.pfm.blocks.IronStoveBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.farmersdelight.PFMFarmersDelight;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.runtime.data.PFMTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PFMFarmersDelightImpl extends PFMFarmersDelight {
    public static PFMFarmersDelight getInstance() {
        return new PFMFarmersDelightImpl();
    }

    @Override
    public void generateTags() {
        List<Block> stoves = new ArrayList<>(StoveBlock.streamStoves().map(FurnitureBlock::getBlock).toList());
        stoves.addAll(IronStoveBlock.streamIronStoves().map(FurnitureBlock::getBlock).toList());
        stoves.add(PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP);
        PFMTagProvider.getOrCreateTagBuilder(ModTags.HEAT_SOURCES)
                .add(stoves.stream().map(block -> Registries.BLOCK.getKey(block).or(null).get()).toArray(RegistryKey[]::new));
    }

    @Override
    public String getModId() {
        return FarmersDelight.MODID;
    }
}
