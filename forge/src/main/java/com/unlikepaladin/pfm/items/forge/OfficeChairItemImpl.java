package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.item.Item;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Settings settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Settings settings) {
        return new OfficeChairItemImpl(settings);
    }

}
