package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class BlockEntityRegistryImpl {

    public static <T extends BlockEntity>BlockEntityType<T> registerBlockEntity(String id, Block[] block, Supplier<T> factory) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, id), BlockEntityType.Builder.create(factory, block).build(null));
    }
}
