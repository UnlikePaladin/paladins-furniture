package com.unlikepaladin.pfm.registry.forge;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class ItemGroupRegistryForge {

    @SubscribeEvent
    public static void registerItemGroups(RegisterEvent event){
        event.register(RegistryKeys.ITEM_GROUP, helper -> {
            ItemGroup dyeGroup = ItemGroup.builder().displayName(Text.translatable("itemGroup.pfm.dye_kits"))
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
                        }).build();
            helper.register(Identifier.of(MOD_ID, "dye_kits"), dyeGroup);
            PaladinFurnitureMod.DYE_KITS.setRight(dyeGroup);

            ItemGroup furnitureGroup = ItemGroup.builder().displayName(Text.translatable("itemGroup.pfm.furniture"))
                    .icon(() -> PaladinFurnitureMod.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultStack())
                    .entries((enabledFeatures, entries) -> {
                    }).build();
            helper.register(Identifier.of(MOD_ID, "furniture"), furnitureGroup);
            PaladinFurnitureMod.FURNITURE_GROUP.setRight(furnitureGroup);
        });
    }

    @SubscribeEvent
    public static void addToVanillaItemGroups(BuildCreativeModeTabContentsEvent creativeModeTabEvent){
        for (Map.Entry<Pair<String, ItemGroup>, Set<Item>> itemGroupListEntry : PaladinFurnitureModBlocksItems.ITEM_GROUP_LIST_MAP.entrySet()) {
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
                                stack.set(PFMComponents.VARIANT_COMPONENT, variant.getIdentifier());
                                stack.set(PFMComponents.COLOR_COMPONENT, color);
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
