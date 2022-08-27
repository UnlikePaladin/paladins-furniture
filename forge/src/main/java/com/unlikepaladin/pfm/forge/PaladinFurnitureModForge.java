package com.unlikepaladin.pfm.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.forge.TagsImpl;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.forge.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.FURNITURE_GROUP;
import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

@Mod(PaladinFurnitureMod.MOD_ID)
public class PaladinFurnitureModForge {
    public PaladinFurnitureModForge() {
        ItemGroupRegistry.registerItemGroups();
        TagsImpl.TUCKABLE_BLOCKS = BlockTags.createOptional(new Identifier("pfm", "tuckable_blocks"));
        MinecraftForge.EVENT_BUS.register(EntityRegistryForge.class);
        PaladinFurnitureMod.commonInit();
        MinecraftForge.EVENT_BUS.register(BlockItemRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(StatisticsRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(ScreenHandlerRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(RecipeRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(BlockEntityRegistryForge.class);
        MinecraftForge.EVENT_BUS.register(SoundRegistryForge.class);
        NetworkRegistryForge.registerPackets();
    }

}
