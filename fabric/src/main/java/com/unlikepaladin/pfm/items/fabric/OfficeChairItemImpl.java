package com.unlikepaladin.pfm.items.fabric;

import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.world.item.Item;

public class OfficeChairItemImpl {
    public static Item getItemFactory(Item.Properties settings) {
        return new OfficeChairItem(settings);
    }
}
