package com.unlikepaladin.pfm.registry.forge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistry {

    public static void registerItemGroups(){
        PaladinFurnitureMod.FURNITURE_GROUP = new ItemGroup(MOD_ID + ".furniture") {
            @Override
            public ItemStack createIcon() {
                return  new ItemStack(PaladinFurnitureModBlocksItems.OAK_CHAIR);
            }
        };
        PaladinFurnitureMod.DYE_KITS = new ItemGroup(MOD_ID + ".dye_kits") {
            @Override
            public ItemStack createIcon() {
                return  new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED);
            }
            @Override
            public void appendStacks(DefaultedList<ItemStack> stacks) {
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GREEN));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIME));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_CYAN));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLUE));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PINK));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BROWN));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_WHITE));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GRAY));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY));
                stacks.add(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLACK));
                super.appendStacks(stacks);
            }
        };
    }
}
