package com.unlikepaladin.pfm.registry.fabric;

import com.mojang.datafixers.types.Type;
import com.unlikepaladin.pfm.blocks.KitchenCounterOven;
import com.unlikepaladin.pfm.blocks.KitchenSink;
import com.unlikepaladin.pfm.blocks.KitchenSink;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

public class BlockEntityRegistryFabric {

    public static void registerBlockEntities() {
        BlockEntities.DRAWER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":drawer_block_entity", BlockEntityType.Builder.create(GenericStorageBlockEntity9x3::new, PaladinFurnitureModBlocksItems.OAK_KITCHEN_DRAWER).build(null));
        BlockEntities.FRIDGE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":fridge_block_entity", BlockEntityType.Builder.create(FridgeBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FRIDGE).build(null));
        BlockEntities.IRON_FREEZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":iron_freezer_block_entity", BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.IRON_FREEZER).build(null));
        BlockEntities.FREEZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":freezer_block_entity", BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FREEZER, PaladinFurnitureModBlocksItems.GRAY_FREEZER).build(null));
        BlockEntities.STOVE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":stove_block_entity", BlockEntityType.Builder.create(StoveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE).build(null));
        BlockEntities.STOVE_TOP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":stovetop_block_entity", BlockEntityType.Builder.create(StovetopBlockEntityImpl::new, PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP).build(null));
        Block[] counterOvens = Stream.concat(KitchenCounterOven.streamStoneCounterOvens(), KitchenCounterOven.streamWoodCounterOvens()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":kitchen_counter_oven_block_entity", BlockEntityType.Builder.create(CounterOvenBlockEntity::new, counterOvens).build(null));
        BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":light_switch_block_entity", BlockEntityType.Builder.create(LightSwitchBlockEntity::new, PaladinFurnitureModBlocksItems.LIGHT_SWITCH).build(null));
        BlockEntities.MICROWAVE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":microwave_block_entity", BlockEntityType.Builder.create(MicrowaveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.IRON_MICROWAVE).build(null));
        BlockEntities.PLATE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":plate_block_entity", BlockEntityType.Builder.create(PlateBlockEntityImpl::new, PaladinFurnitureModBlocksItems.BASIC_PLATE).build(null));
        BlockEntities.TOILET_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":toilet_block_entity", BlockEntityType.Builder.create(ToiletBlockEntity::new, PaladinFurnitureModBlocksItems.BASIC_TOILET).build(null));
        BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":small_storage_block_entity", BlockEntityType.Builder.create(GenericStorageBlockEntity3x3::new, PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_SMALL_DRAWER).build(null));
        BlockEntities.TRASHCAN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":trashcan_block_entity", BlockEntityType.Builder.create(TrashcanBlockEntityImpl::new, PaladinFurnitureModBlocksItems.TRASHCAN, PaladinFurnitureModBlocksItems.MESH_TRASHCAN).build(null));
        Block[] sinks = Stream.concat(KitchenSink.streamStoneSinks(), KitchenSink.streamWoodSinks()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        BlockEntities.SINK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PaladinFurnitureMod.MOD_ID + ":sink_block_entity", BlockEntityType.Builder.create(SinkBlockEntity::new, sinks).build(null));

    }

    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, builder.build(type));
    }
}
