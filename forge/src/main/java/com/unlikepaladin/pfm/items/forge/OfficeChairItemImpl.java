package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.item.Item;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Settings settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Settings settings) {
        return new OfficeChairItemImpl(settings.setISTER(() -> PFMItemRendererForge::new));
    }
}
