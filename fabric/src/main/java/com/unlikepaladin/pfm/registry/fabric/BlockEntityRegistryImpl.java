package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.Set;

public class BlockEntityRegistryImpl {

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, Block[] block, BlockEntityType.BlockEntitySupplier<T> factory) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, id), new BlockEntityType<>(factory, Set.of(block)));
    }
}
