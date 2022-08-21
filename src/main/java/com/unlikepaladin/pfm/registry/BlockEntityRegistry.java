package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.blocks.KitchenCounterOven;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.stream.Stream;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class BlockEntityRegistry {
    public static BlockEntityType<GenericStorageBlockEntity9x3> DRAWER_BLOCK_ENTITY;
    public static BlockEntityType<FridgeBlockEntity> FRIDGE_BLOCK_ENTITY;
    public static BlockEntityType<FreezerBlockEntity> FREEZER_BLOCK_ENTITY;
    public static BlockEntityType<FreezerBlockEntity> IRON_FREEZER_BLOCK_ENTITY;
    public static BlockEntityType<StoveBlockEntity> STOVE_BLOCK_ENTITY;
    public static BlockEntityType<CounterOvenBlockEntity> KITCHEN_COUNTER_OVEN_BLOCK_ENTITY;
    public static BlockEntityType<LightSwitchBlockEntity> LIGHT_SWITCH_BLOCK_ENTITY;
    public static BlockEntityType<MicrowaveBlockEntity> MICROWAVE_BLOCK_ENTITY;
    public static BlockEntityType<StovetopBlockEntity> STOVE_TOP_BLOCK_ENTITY;
    public static BlockEntityType<PlateBlockEntity> PLATE_BLOCK_ENTITY;
    public static BlockEntityType<ToiletBlockEntity> TOILET_BLOCK_ENTITY;
    public static BlockEntityType<GenericStorageBlockEntity3x3> KITCHEN_DRAWER_SMALL_BLOCK_ENTITY;


    public static void registerBlockEntities() {
        DRAWER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":drawer_block_entity", FabricBlockEntityTypeBuilder.create(GenericStorageBlockEntity9x3::new, BlockItemRegistry.OAK_KITCHEN_DRAWER).build(null));
        FRIDGE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":fridge_block_entity", FabricBlockEntityTypeBuilder.create(FridgeBlockEntity::new, BlockItemRegistry.WHITE_FRIDGE).build(null));
        IRON_FREEZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":iron_freezer_block_entity", FabricBlockEntityTypeBuilder.create(FreezerBlockEntity::new, BlockItemRegistry.IRON_FREEZER).build(null));
        FREEZER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":freezer_block_entity", FabricBlockEntityTypeBuilder.create(FreezerBlockEntity::new, BlockItemRegistry.WHITE_FREEZER, BlockItemRegistry.GRAY_FREEZER).build(null));
        STOVE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":stove_block_entity", FabricBlockEntityTypeBuilder.create(StoveBlockEntity::new, BlockItemRegistry.WHITE_STOVE, BlockItemRegistry.GRAY_STOVE, BlockItemRegistry.IRON_STOVE).build(null));
        STOVE_TOP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":stovetop_block_entity", FabricBlockEntityTypeBuilder.create(StovetopBlockEntity::new, BlockItemRegistry.KITCHEN_STOVETOP).build(null));
        Block[] counterOvens = Stream.concat(KitchenCounterOven.streamStoneCounterOvens(), KitchenCounterOven.streamWoodCounterOvens()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        KITCHEN_COUNTER_OVEN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":kitchen_counter_oven_block_entity", FabricBlockEntityTypeBuilder.create(CounterOvenBlockEntity::new, counterOvens).build(null));
        LIGHT_SWITCH_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":light_switch_block_entity", FabricBlockEntityTypeBuilder.create(LightSwitchBlockEntity::new, BlockItemRegistry.LIGHT_SWITCH).build(null));
        MICROWAVE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":microwave_block_entity", FabricBlockEntityTypeBuilder.create(MicrowaveBlockEntity::new, BlockItemRegistry.IRON_MICROWAVE).build(null));
        PLATE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":plate_block_entity", FabricBlockEntityTypeBuilder.create(PlateBlockEntity::new, BlockItemRegistry.BASIC_PLATE).build(null));
        TOILET_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":toilet_block_entity", FabricBlockEntityTypeBuilder.create(ToiletBlockEntity::new, BlockItemRegistry.BASIC_TOILET).build(null));
        KITCHEN_DRAWER_SMALL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":small_storage_block_entity", FabricBlockEntityTypeBuilder.create(GenericStorageBlockEntity3x3::new, BlockItemRegistry.OAK_KITCHEN_WALL_SMALL_DRAWER).build(null));

    }
}
