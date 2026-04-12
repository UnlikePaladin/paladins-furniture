package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;

public class LampItem extends BlockItem {
    public LampItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        DyeColor color = DyeColor.WHITE;
        WoodVariant variant = WoodVariantRegistry.OAK;

        if (stack.get(PFMComponents.COLOR_COMPONENT) != null) {
            color = stack.get(PFMComponents.COLOR_COMPONENT);
        }
        if (stack.get(PFMComponents.VARIANT_COMPONENT) != null) {
            variant = WoodVariantRegistry.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT));
        }
        return String.format("block.pfm.basic_%s_%s_lamp", color.getSerializedName(), variant.getSerializedName());
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
        stack.set(PFMComponents.VARIANT_COMPONENT, WoodVariantRegistry.OAK.identifier);
        return stack;
    }

    @ExpectPlatform
    public static BlockItem getItemFactory(Block block, Properties settings) {
        throw new UnsupportedOperationException();
    }
}
