package com.unlikepaladin.pfm.registry.forge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistryForge {

    @SubscribeEvent
    public static void registerItemGroups(CreativeModeTabEvent.Register creativeModeTabEvent){
        PaladinFurnitureMod.DYE_KITS.setRight(creativeModeTabEvent.registerCreativeModeTab(new Identifier(MOD_ID, "dye_kits"), builder -> {
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
            }));
        PaladinFurnitureMod.FURNITURE_GROUP.setRight(creativeModeTabEvent.registerCreativeModeTab(new Identifier(MOD_ID, "furniture"), builder -> {
                    builder.displayName(Text.translatable("itemGroup.pfm.furniture"))
                            .icon(() -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultStack())
                            .entries((enabledFeatures, entries) -> {
                            })
                            .build();
                }
        ));
    }
    @SubscribeEvent
    public static void addToVanillaItemGroups(CreativeModeTabEvent.BuildContents creativeModeTabEvent){
        for (Map.Entry<Pair<String, ItemGroup>, List<Item>> itemGroupListEntry : PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.entrySet()) {
            if (creativeModeTabEvent.getTab() == itemGroupListEntry.getKey().getRight()) {
                itemGroupListEntry.getValue().forEach(item -> {
                    if (item == PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM) {
                        List<ItemStack> stacks = new ArrayList<>();
                        for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                            boolean variantEnabled = true;
                            for (FeatureFlag flag : variant.getFeatureList()) {
                                if (!creativeModeTabEvent.getFlags().contains(flag)) {
                                    variantEnabled = false;
                                    break;
                                }
                            }
                            if (!variantEnabled) {
                                continue;
                            }
                            for (DyeColor color : DyeColor.values()) {
                                ItemStack stack = new ItemStack(item);
                                NbtCompound beTag = new NbtCompound();
                                beTag.putString("color", color.asString());
                                beTag.putString("variant", variant.getIdentifier().toString());
                                stack.setSubNbt("BlockEntityTag", beTag);
                                stacks.add(stack);
                            }
                        }
                        stacks.forEach(creativeModeTabEvent::add);
                    } else {
                        creativeModeTabEvent.add(item);
                    }
                });
            }
        }
    }
}
