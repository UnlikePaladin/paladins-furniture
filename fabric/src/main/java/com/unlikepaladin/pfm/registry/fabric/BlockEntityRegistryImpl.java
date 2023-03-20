package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntityRegistryImpl {

    public static BlockEntityType<? extends BlockEntity> registerBlockEntity(String id, Block[] block, BlockEntityType.BlockEntityFactory<? extends BlockEntity> factory) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PaladinFurnitureMod.MOD_ID, id), BlockEntityType.Builder.create(factory, block).build(null));
    }
}
