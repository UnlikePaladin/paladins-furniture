package com.unlikepaladin.pfm.registry.forge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistry {

    @SubscribeEvent
    public static void registerItemGroups(CreativeModeTabEvent.Register creativeModeTabEvent){
        PaladinFurnitureMod.DYE_KITS = creativeModeTabEvent.registerCreativeModeTab(new Identifier(MOD_ID, "dye_kits"), builder -> {
                builder.displayName(Text.translatable("itemGroup.pfm.dye_kits"))
                        .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED))
                        .entries((enabledFeatures, stacks) -> {
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
                        })
                        .build();
            }
        );


        PaladinFurnitureMod.FURNITURE_GROUP = creativeModeTabEvent.registerCreativeModeTab(new Identifier(MOD_ID, "furniture"), builder -> {
                    builder.displayName(Text.translatable("itemGroup.pfm.furniture"))
                            .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.OAK_CHAIR))
                            .entries((enabledFeatures, stacks) -> {
                                PaladinFurnitureModBlocksItems.PFM_TAB_ITEMS.forEach(stacks::add);
                            })
                            .build();
                }
        );
    }
    @SubscribeEvent
    public static void addToVanillaItemGroups(CreativeModeTabEvent.BuildContents creativeModeTabEvent){
        if (creativeModeTabEvent.getTab() == ItemGroups.BUILDING_BLOCKS) {
            PaladinFurnitureModBlocksItems.OTHER_TAB_ITEMS.forEach(creativeModeTabEvent::add);
        }
    }
}
