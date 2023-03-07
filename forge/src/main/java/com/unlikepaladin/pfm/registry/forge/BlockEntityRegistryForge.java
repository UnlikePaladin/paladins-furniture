package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.blocks.BasicSinkBlock;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.forge.*;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryForge {

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        BlockEntities.DRAWER_BLOCK_ENTITY = BlockEntityType.Builder.create(GenericStorageBlockEntity9x3::new, PaladinFurnitureModBlocksItems.OAK_KITCHEN_DRAWER).build(null);
        BlockEntities.FRIDGE_BLOCK_ENTITY = BlockEntityType.Builder.create(FridgeBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FRIDGE).build(null);
        BlockEntities.IRON_FREEZER_BLOCK_ENTITY = BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.IRON_FREEZER).build(null);
        BlockEntities.FREEZER_BLOCK_ENTITY = BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FREEZER, PaladinFurnitureModBlocksItems.GRAY_FREEZER).build(null);
        BlockEntities.STOVE_BLOCK_ENTITY = BlockEntityType.Builder.create(StoveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE).build(null);
        BlockEntities.STOVE_TOP_BLOCK_ENTITY = BlockEntityType.Builder.create(StovetopBlockEntityImpl::new, PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP).build(null);
        Block[] counterOvens = Stream.concat(KitchenCounterOvenBlock.streamStoneCounterOvens(), KitchenCounterOvenBlock.streamWoodCounterOvens()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY = BlockEntityType.Builder.create(CounterOvenBlockEntity::new, counterOvens).build(null);
        BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY = BlockEntityType.Builder.create(LightSwitchBlockEntity::new, PaladinFurnitureModBlocksItems.LIGHT_SWITCH).build(null);
        BlockEntities.MICROWAVE_BLOCK_ENTITY = BlockEntityType.Builder.create(MicrowaveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.IRON_MICROWAVE).build(null);
        BlockEntities.PLATE_BLOCK_ENTITY = BlockEntityType.Builder.create(PlateBlockEntityImpl::new, PaladinFurnitureModBlocksItems.BASIC_PLATE).build(null);
        BlockEntities.TOILET_BLOCK_ENTITY = BlockEntityType.Builder.create(ToiletBlockEntity::new, PaladinFurnitureModBlocksItems.BASIC_TOILET).build(null);
        BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY = BlockEntityType.Builder.create(GenericStorageBlockEntity3x3::new, PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_SMALL_DRAWER).build(null);
        BlockEntities.TRASHCAN_BLOCK_ENTITY = BlockEntityType.Builder.create(TrashcanBlockEntityImpl::new, PaladinFurnitureModBlocksItems.MESH_TRASHCAN).build(null);
        Block[] sinks = Stream.concat(KitchenSinkBlock.streamStoneSinks(), KitchenSinkBlock.streamWoodSinks()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        sinks = Stream.concat(Arrays.stream(sinks), BasicSinkBlock.streamSinks()).toArray(Block[]::new);
        BlockEntities.SINK_BLOCK_ENTITY = BlockEntityType.Builder.create(SinkBlockEntity::new, sinks).build(null);
        BlockEntities.SHOWER_HEAD_BLOCK_ENTITY = BlockEntityType.Builder.create(ShowerHeadBlockEntityImpl::new, PaladinFurnitureModBlocksItems.BASIC_SHOWER_HEAD).build(null);
        BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY = BlockEntityType.Builder.create(ShowerHandleBlockEntity::new, PaladinFurnitureModBlocksItems.BASIC_SHOWER_HANDLE).build(null);
        BlockEntities.BATHTUB_BLOCK_ENTITY = BlockEntityType.Builder.create(BathtubBlockEntity::new, PaladinFurnitureModBlocksItems.BASIC_BATHTUB).build(null);

        event.getRegistry().registerAll(
                registerEntity("drawer_block_entity", BlockEntities.DRAWER_BLOCK_ENTITY),
                registerEntity("fridge_block_entity", BlockEntities.FRIDGE_BLOCK_ENTITY),
                registerEntity("iron_freezer_block_entity", BlockEntities.IRON_FREEZER_BLOCK_ENTITY),
                registerEntity("freezer_block_entity", BlockEntities.FREEZER_BLOCK_ENTITY),
                registerEntity("stove_block_entity", BlockEntities.STOVE_BLOCK_ENTITY),
                registerEntity("stovetop_block_entity", BlockEntities.STOVE_TOP_BLOCK_ENTITY),
                registerEntity("kitchen_counter_oven_block_entity", BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY),
                registerEntity("light_switch_block_entity", BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY),
                registerEntity("microwave_block_entity", BlockEntities.MICROWAVE_BLOCK_ENTITY),
                registerEntity("plate_block_entity", BlockEntities.PLATE_BLOCK_ENTITY),
                registerEntity("toilet_block_entity", BlockEntities.TOILET_BLOCK_ENTITY),
                registerEntity("small_storage_block_entity", BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY),
                registerEntity("trashcan_block_entity", BlockEntities.TRASHCAN_BLOCK_ENTITY),
                registerEntity("sink_block_entity", BlockEntities.SINK_BLOCK_ENTITY),
                registerEntity("shower_head_block_entity", BlockEntities.SHOWER_HEAD_BLOCK_ENTITY),
                registerEntity("shower_handle_block_entity", BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY),
                registerEntity("bathtub_block_entity", BlockEntities.BATHTUB_BLOCK_ENTITY)
        );
    }

    private static BlockEntityType<?> registerEntity(String name, BlockEntityType<?> entity) {
        return entity.setRegistryName(name);
    }
}
