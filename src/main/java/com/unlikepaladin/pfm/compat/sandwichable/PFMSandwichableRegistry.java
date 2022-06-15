package com.unlikepaladin.pfm.compat.sandwichable;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.sandwichable.blocks.PFMToaster;
import com.unlikepaladin.pfm.compat.sandwichable.blocks.blockentities.PFMToasterBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class PFMSandwichableRegistry {
    public static BlockEntityType<PFMToasterBlockEntity> IRON_TOASTER_BLOCKENTITY;

    public static void registerFurniture(String blockName, Block block, Boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
        }
    }
    public static final PFMToaster IRON_TOASTER = new PFMToaster(FabricBlockSettings.copy(Blocks.STONECUTTER));
    public static void register() {
            registerFurniture("iron_toaster", IRON_TOASTER, true);
            IRON_TOASTER_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("pfm","iron_toaster_ent"), FabricBlockEntityTypeBuilder.create(PFMToasterBlockEntity::new, new Block[]{IRON_TOASTER}).build(null));
    }
    }
