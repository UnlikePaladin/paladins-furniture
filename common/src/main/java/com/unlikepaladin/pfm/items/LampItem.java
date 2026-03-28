package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;

public class LampItem extends BlockItem {
    public LampItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        DyeColor color = DyeColor.WHITE;
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.hasTag()) {
            if (stack.getTagElement("BlockEntityTag").contains("color")) {
                color = DyeColor.byName(stack.getTagElement("BlockEntityTag").getString("color"), DyeColor.WHITE);
            }
            if (stack.getTagElement("BlockEntityTag").contains("variant")) {
                variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(stack.getTagElement("BlockEntityTag").getString("variant")));
            }
        }
        return String.format("block.pfm.basic_%s_%s_lamp", color.getSerializedName(), variant.getSerializedName());
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("color", DyeColor.WHITE.getSerializedName());
        tag.putString("variant", WoodVariantRegistry.OAK.getSerializedName());
        stack.addTagElement("BlockEntityTag", tag);
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowedIn(group)) {
            for (WoodVariant variant : WoodVariantRegistry.getVariants()) {
                for (DyeColor color : DyeColor.values()) {
                    ItemStack stack = new ItemStack(this);
                    CompoundTag beTag = new CompoundTag();
                    beTag.putString("color", color.getSerializedName());
                    beTag.putString("variant", variant.getIdentifier().toString());
                    stack.addTagElement("BlockEntityTag", beTag);
                    stacks.add(stack);
                }
            }
        }
    }

    @ExpectPlatform
    public static BlockItem getItemFactory(Block block, Properties settings) {
        throw new UnsupportedOperationException();
    }
}
