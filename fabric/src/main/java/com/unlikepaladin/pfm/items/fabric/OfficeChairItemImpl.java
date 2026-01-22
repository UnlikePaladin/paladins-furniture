package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.item.Item;

public class OfficeChairItemImpl {
    public static Item getItemFactory(Item.Settings settings) {
        return new OfficeChairItem(settings);
    }
}
