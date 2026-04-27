package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

public class BlockEntityRegistryImpl {

    public static <T extends BlockEntity>BlockEntityType<T> registerBlockEntity(String id, Block[] block, Supplier<T> factory) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(PaladinFurnitureMod.MOD_ID, id), BlockEntityType.Builder.of(factory, block).build(null));
    }
}
