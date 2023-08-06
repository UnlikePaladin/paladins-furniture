package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class BlockEntityRegistryImpl {

    public static <T extends BlockEntity>BlockEntityType<T> registerBlockEntity(String id, Block[] block, BlockEntityType.BlockEntityFactory<T> factory) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, id), BlockEntityType.Builder.create(factory, block).build(null));
    }
}
