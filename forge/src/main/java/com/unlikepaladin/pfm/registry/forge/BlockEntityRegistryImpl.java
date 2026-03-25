package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityRegistryImpl {
    public static List<BlockEntityType<?>> blockEntityTypes = new ArrayList<>();

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String blockEntityId, Block[] block, BlockEntityType.BlockEntitySupplier<T> factory) {
        BlockEntityType<T> blockEntityType = BlockEntityType.Builder.of(factory, block).build(null);
        blockEntityType.setRegistryName(blockEntityId);
        blockEntityTypes.add(blockEntityType);
        return blockEntityType;
    }
}
