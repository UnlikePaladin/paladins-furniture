package com.unlikepaladin.pfm.registry.forge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistryForge {

    @SubscribeEvent
    public static void registerItemGroups(CreativeModeTabEvent.Register creativeModeTabEvent){
        PaladinFurnitureMod.DYE_KITS.setB(creativeModeTabEvent.registerCreativeModeTab(new ResourceLocation(MOD_ID, "dye_kits"), builder -> {
                builder.title(Component.translatable("itemGroup.pfm.dye_kits"))
                        .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED))
                        .displayItems((enabledFeatures, stacks) -> {
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_ORANGE));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_YELLOW));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GREEN));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIME));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_CYAN));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLUE));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_BLUE));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PURPLE));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_MAGENTA));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_PINK));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BROWN));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_WHITE));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_GRAY));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_LIGHT_GRAY));
                            stacks.accept(new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_BLACK));
                        })
                        .build();
            }));
        PaladinFurnitureMod.FURNITURE_GROUP.setB(creativeModeTabEvent.registerCreativeModeTab(new ResourceLocation(MOD_ID, "furniture"), builder -> {
                    builder.title(Component.translatable("itemGroup.pfm.furniture"))
                            .icon(() -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultInstance())
                            .displayItems((enabledFeatures, entries) -> {
                            })
                            .build();
                }
        ));
    }
    @SubscribeEvent
    public static void addToVanillaItemGroups(CreativeModeTabEvent.BuildContents creativeModeTabEvent){
        for (Map.Entry<Tuple<String, CreativeModeTab>, List<Item>> itemGroupListEntry : PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.entrySet()) {
            if (creativeModeTabEvent.getTab() == itemGroupListEntry.getKey().getB()) {
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
                                CompoundTag beTag = new CompoundTag();
                                beTag.putString("color", color.getSerializedName());
                                beTag.putString("variant", variant.getIdentifier().toString());
                                stack.addTagElement("BlockEntityTag", beTag);
                                stacks.add(stack);
                            }
                        }
                        stacks.forEach(creativeModeTabEvent::accept);
                    } else if (item == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
                        List<ItemStack> stacks = new ArrayList<>();
                        for (DyeColor color : DyeColor.values()) {
                            ItemStack stack = new ItemStack(item);
                            CompoundTag beTag = new CompoundTag();
                            beTag.putString("Color", color.getSerializedName());
                            stack.setTag(beTag);
                            stacks.add(stack);
                        }
                        stacks.forEach(creativeModeTabEvent::accept);
                    } else {
                        creativeModeTabEvent.accept(item);
                    }
                });
            }
        }
    }
}
