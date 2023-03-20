package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.stream.Stream;

public class BlockEntityRegistry {

    public static void registerBlockEntities() {
        BlockEntities.DRAWER_BLOCK_ENTITY = (BlockEntityType<GenericStorageBlockEntity9x3>) registerBlockEntity("drawer_block_entity", new Block[]{PaladinFurnitureModBlocksItems.OAK_KITCHEN_DRAWER}, GenericStorageBlockEntity9x3::new);
        BlockEntities.FRIDGE_BLOCK_ENTITY = (BlockEntityType<FridgeBlockEntity>) registerBlockEntity("fridge_block_entity", new Block[]{PaladinFurnitureModBlocksItems.WHITE_FRIDGE}, FridgeBlockEntity::new);
        BlockEntities.FREEZER_BLOCK_ENTITY = (BlockEntityType<FreezerBlockEntity>) registerBlockEntity("freezer_block_entity", new Block[]{PaladinFurnitureModBlocksItems.WHITE_FREEZER, PaladinFurnitureModBlocksItems.GRAY_FREEZER, PaladinFurnitureModBlocksItems.IRON_FREEZER}, FreezerBlockEntity::new);
        Block[] counterOvens = Stream.concat(KitchenCounterOvenBlock.streamStoneCounterOvens(), KitchenCounterOvenBlock.streamWoodCounterOvens()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY = (BlockEntityType<CounterOvenBlockEntity>) registerBlockEntity("kitchen_counter_oven_block_entity", counterOvens, CounterOvenBlockEntity::new);
        BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY = (BlockEntityType<LightSwitchBlockEntity>) registerBlockEntity("light_switch_block_entity", new Block[]{PaladinFurnitureModBlocksItems.LIGHT_SWITCH}, LightSwitchBlockEntity::new);
        BlockEntities.STOVE_BLOCK_ENTITY = (BlockEntityType<? extends StoveBlockEntity>) BlockEntityRegistry.registerBlockEntity("stove_block_entity", new Block[]{PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE}, StoveBlockEntity.getFactory());
        BlockEntities.STOVE_TOP_BLOCK_ENTITY = (BlockEntityType<? extends StovetopBlockEntity>) BlockEntityRegistry.registerBlockEntity("stovetop_block_entity", new Block[]{PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP}, StovetopBlockEntity.getFactory());
        BlockEntities.MICROWAVE_BLOCK_ENTITY = (BlockEntityType<? extends MicrowaveBlockEntity>) BlockEntityRegistry.registerBlockEntity("microwave_block_entity", new Block[]{PaladinFurnitureModBlocksItems.IRON_MICROWAVE}, MicrowaveBlockEntity.getFactory());
        BlockEntities.PLATE_BLOCK_ENTITY = (BlockEntityType<? extends PlateBlockEntity>) BlockEntityRegistry.registerBlockEntity("plate_block_entity", new Block[]{PaladinFurnitureModBlocksItems.BASIC_PLATE}, PlateBlockEntity.getFactory());
        BlockEntities.TOILET_BLOCK_ENTITY = (BlockEntityType<ToiletBlockEntity>) BlockEntityRegistry.registerBlockEntity("toilet_block_entity", new Block[]{PaladinFurnitureModBlocksItems.BASIC_TOILET}, ToiletBlockEntity::new);
        BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY = (BlockEntityType<GenericStorageBlockEntity3x3>) BlockEntityRegistry.registerBlockEntity("small_storage_block_entity", new Block[]{PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_SMALL_DRAWER}, GenericStorageBlockEntity3x3::new);
        BlockEntities.TRASHCAN_BLOCK_ENTITY = (BlockEntityType< ? extends TrashcanBlockEntity>) BlockEntityRegistry.registerBlockEntity("trashcan_block_entity", new Block[]{PaladinFurnitureModBlocksItems.TRASHCAN, PaladinFurnitureModBlocksItems.MESH_TRASHCAN}, TrashcanBlockEntity.getFactory());
        Block[] sinks = Stream.concat(KitchenSinkBlock.streamStoneSinks(), KitchenSinkBlock.streamWoodSinks()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        sinks = Stream.concat(Arrays.stream(sinks), BasicSinkBlock.streamSinks()).toArray(Block[]::new);
        BlockEntities.SINK_BLOCK_ENTITY = (BlockEntityType<SinkBlockEntity>) BlockEntityRegistry.registerBlockEntity("sink_block_entity", sinks, SinkBlockEntity::new);
        BlockEntities.SHOWER_HEAD_BLOCK_ENTITY = (BlockEntityType<? extends ShowerHeadBlockEntity>) BlockEntityRegistry.registerBlockEntity("shower_head_block_entity", new Block[]{PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD}, ShowerHeadBlockEntity.getFactory());
        BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY = (BlockEntityType<ShowerHandleBlockEntity>) BlockEntityRegistry.registerBlockEntity("shower_handle_block_entity", new Block[]{PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE}, ShowerHandleBlockEntity::new);
        BlockEntities.BATHTUB_BLOCK_ENTITY = (BlockEntityType<BathtubBlockEntity>) BlockEntityRegistry.registerBlockEntity("bathtub_block_entity", new Block[]{PaladinFurnitureModBlocksItems.BASIC_BATHTUB}, BathtubBlockEntity::new);
    }

    @ExpectPlatform
    public static BlockEntityType<? extends BlockEntity> registerBlockEntity(String id, Block[] block, BlockEntityType.BlockEntityFactory<? extends BlockEntity> factory) {
        throw new RuntimeException();
    }
}
