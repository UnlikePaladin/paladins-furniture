package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.KitchenCounterOven;
import com.unlikepaladin.pfm.blocks.blockentities.*;
import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.blocks.blockentities.forge.PlateBlockEntityImpl;
import com.unlikepaladin.pfm.blocks.blockentities.forge.StoveBlockEntityImpl;
import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntityRegistryForge {

    @SubscribeEvent
    public static void registerBlockEntities(RegisterEvent event){
        BlockEntities.DRAWER_BLOCK_ENTITY = BlockEntityType.Builder.create(GenericStorageBlockEntity9x3::new, PaladinFurnitureModBlocksItems.OAK_CLASSIC_NIGHTSTAND).build(null);
        BlockEntities.FRIDGE_BLOCK_ENTITY = BlockEntityType.Builder.create(FridgeBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FRIDGE).build(null);
        BlockEntities.IRON_FREEZER_BLOCK_ENTITY = BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.IRON_FREEZER).build(null);
        BlockEntities.FREEZER_BLOCK_ENTITY = BlockEntityType.Builder.create(FreezerBlockEntity::new, PaladinFurnitureModBlocksItems.WHITE_FREEZER, PaladinFurnitureModBlocksItems.GRAY_FREEZER).build(null);
        BlockEntities.STOVE_BLOCK_ENTITY = BlockEntityType.Builder.create(StoveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.WHITE_STOVE, PaladinFurnitureModBlocksItems.GRAY_STOVE, PaladinFurnitureModBlocksItems.IRON_STOVE).build(null);
        BlockEntities.STOVE_TOP_BLOCK_ENTITY = BlockEntityType.Builder.create(StovetopBlockEntityImpl::new, PaladinFurnitureModBlocksItems.KITCHEN_STOVETOP).build(null);
        Block[] counterOvens = Stream.concat(KitchenCounterOven.streamStoneCounterOvens(), KitchenCounterOven.streamWoodCounterOvens()).map(FurnitureBlock::getBlock).toArray(Block[]::new);
        BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY = BlockEntityType.Builder.create(CounterOvenBlockEntity::new, counterOvens).build(null);
        BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY = BlockEntityType.Builder.create(LightSwitchBlockEntity::new, PaladinFurnitureModBlocksItems.LIGHT_SWITCH).build(null);
        BlockEntities.MICROWAVE_BLOCK_ENTITY = BlockEntityType.Builder.create(MicrowaveBlockEntityImpl::new, PaladinFurnitureModBlocksItems.IRON_MICROWAVE).build(null);
        BlockEntities.PLATE_BLOCK_ENTITY = BlockEntityType.Builder.create(PlateBlockEntityImpl::new, PaladinFurnitureModBlocksItems.BASIC_PLATE).build(null);
        BlockEntities.TOILET_BLOCK_ENTITY = BlockEntityType.Builder.create(ToiletBlockEntity::new, PaladinFurnitureModBlocksItems.BASIC_TOILET).build(null);
        BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY = BlockEntityType.Builder.create(GenericStorageBlockEntity3x3::new, PaladinFurnitureModBlocksItems.OAK_KITCHEN_WALL_SMALL_DRAWER).build(null);

        registerBlockEntity(event, "drawer_block_entity", BlockEntities.DRAWER_BLOCK_ENTITY);
        registerBlockEntity(event, "fridge_block_entity", BlockEntities.FRIDGE_BLOCK_ENTITY);
        registerBlockEntity(event, "iron_freezer_block_entity", BlockEntities.IRON_FREEZER_BLOCK_ENTITY);
        registerBlockEntity(event, "freezer_block_entity", BlockEntities.FREEZER_BLOCK_ENTITY);
        registerBlockEntity(event, "stove_block_entity", BlockEntities.STOVE_BLOCK_ENTITY);
        registerBlockEntity(event, "stovetop_block_entity", BlockEntities.STOVE_TOP_BLOCK_ENTITY);
        registerBlockEntity(event, "kitchen_counter_oven_block_entity", BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY);
        registerBlockEntity(event, "light_switch_block_entity", BlockEntities.LIGHT_SWITCH_BLOCK_ENTITY);
        registerBlockEntity(event, "microwave_block_entity", BlockEntities.MICROWAVE_BLOCK_ENTITY);
        registerBlockEntity(event, "plate_block_entity", BlockEntities.PLATE_BLOCK_ENTITY);
        registerBlockEntity(event, "toilet_block_entity", BlockEntities.TOILET_BLOCK_ENTITY);
        registerBlockEntity(event, "small_storage_block_entity", BlockEntities.KITCHEN_DRAWER_SMALL_BLOCK_ENTITY);
    }

    private static BlockEntityType<?> registerBlockEntity(RegisterEvent event, String name, BlockEntityType<?> entity) {
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, new Identifier(PaladinFurnitureMod.MOD_ID), () -> entity);
        return entity;
    }
}
