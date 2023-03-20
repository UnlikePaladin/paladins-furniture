package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.BathtubBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityRegistryImpl {
    public static List<BlockEntityType<?>> blockEntityTypes = new ArrayList<>();

    public static <T extends BlockEntity>BlockEntityType<T> registerBlockEntity(String blockEntityId, Block[] block, BlockEntityType.BlockEntityFactory<T> factory) {
        BlockEntityType<T> blockEntityType = BlockEntityType.Builder.create(factory, block).build(null);
        blockEntityType.setRegistryName(blockEntityId);
        blockEntityTypes.add(blockEntityType);
        return blockEntityType;
    }
}
