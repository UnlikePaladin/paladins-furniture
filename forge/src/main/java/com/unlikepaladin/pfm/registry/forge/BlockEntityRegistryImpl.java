package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class BlockEntityRegistryImpl {
    public static Map<ResourceLocation, BlockEntityType<?>> blockEntityTypes = new LinkedHashMap<>();

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String blockEntityId, Block[] block, BlockEntityType.BlockEntitySupplier<T> factory) {
        BlockEntityType<T> blockEntityType = BlockEntityType.Builder.of(factory, block).build(null);
        blockEntityTypes.put(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, blockEntityId), blockEntityType);
        return blockEntityType;
    }
}
