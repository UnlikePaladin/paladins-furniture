package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.blocks.BasicChairBlock;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public abstract class PFMItemGroup extends ItemGroup {
    public PFMItemGroup(int index, String id) {
        super(index, id);
    }

    @Override
    public ItemStack createIcon() {
        return PaladinFurnitureModBlocksItems.furnitureEntryMap.get(BasicChairBlock.class).getVariantToBlockMap().get(WoodVariantRegistry.OAK).asItem().getDefaultStack();
    }

    @Override
    public void appendStacks(DefaultedList<ItemStack> stacks) {
        for (Item item : Registry.ITEM) {
            item.appendStacks(this, stacks);
        }
    }
}
