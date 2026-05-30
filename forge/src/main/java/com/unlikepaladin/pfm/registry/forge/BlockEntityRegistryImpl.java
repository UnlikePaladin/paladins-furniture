package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.Identifier;

import java.util.*;

public class BlockEntityRegistryImpl {
    public static Map<Identifier, BlockEntityType<?>> blockEntityTypes = new LinkedHashMap<>();

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String blockEntityId, Block[] block, BlockEntityType.BlockEntitySupplier<T> factory) {
        BlockEntityType<T> blockEntityType = new BlockEntityType<>(factory, Set.of(block));
        blockEntityTypes.put(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockEntityId), blockEntityType);
        return blockEntityType;
    }
}
