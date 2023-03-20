package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.blocks.BasicSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.stream.Stream;

public class BlockEntityRegistryFabric {

    public static void registerBlockEntities() {
        BlockEntityRegistry.registerBlockEntities();
    }
}
