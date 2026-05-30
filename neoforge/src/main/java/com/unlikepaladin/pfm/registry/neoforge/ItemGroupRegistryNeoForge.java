package com.unlikepaladin.pfm.registry.neoforge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistryNeoForge {

    @SubscribeEvent
    public static void registerItemGroups(RegisterEvent event){
        event.register(Registries.CREATIVE_MODE_TAB, helper -> {
            CreativeModeTab dyeGroup = CreativeModeTab.builder().title(Component.translatable("itemGroup.pfm.dye_kits"))
                        .icon(() -> new ItemStack(PaladinFurnitureModBlocksItems.DYE_KIT_RED))
                        .displayItems((enabledFeatures, stacks) -> {}).build();
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "dye_kits"), dyeGroup);
            PaladinFurnitureMod.DYE_KITS.setB(dyeGroup);

            CreativeModeTab furnitureGroup = CreativeModeTab.builder().title(Component.translatable("itemGroup.pfm.furniture"))
                    .icon(() -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultInstance())
                    .displayItems((enabledFeatures, entries) -> {
                    }).build();
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "furniture"), furnitureGroup);
            PaladinFurnitureMod.FURNITURE_GROUP.setB(furnitureGroup);
            PaladinFurnitureMod.BUILDING_BLOCKS.setB(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.BUILDING_BLOCKS));
        });
    }

    @SubscribeEvent
    public static void addToVanillaItemGroups(BuildCreativeModeTabContentsEvent creativeModeTabEvent){
        for (Map.Entry<Tuple<String, CreativeModeTab>, Set<Item>> itemGroupListEntry : PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.entrySet()) {
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
                                stack.set(PFMComponents.VARIANT_COMPONENT, variant.getIdentifier());
                                stack.set(PFMComponents.COLOR_COMPONENT, color);
                                stacks.add(stack);
                            }
                        }
                        stacks.forEach(creativeModeTabEvent::accept);
                    } else if (item == PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM) {
                        List<ItemStack> stacks = new ArrayList<>();
                        for (DyeColor color : DyeColor.values()) {
                            ItemStack stack = new ItemStack(item);
                            stack.set(PFMComponents.COLOR_COMPONENT, color);
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
