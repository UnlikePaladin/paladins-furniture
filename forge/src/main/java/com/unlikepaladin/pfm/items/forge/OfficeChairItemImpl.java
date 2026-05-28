package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Properties settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Properties settings) {
        return new OfficeChairItemImpl(settings);
    }

}
