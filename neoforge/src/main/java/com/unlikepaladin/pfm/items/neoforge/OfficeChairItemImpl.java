package com.unlikepaladin.pfm.items.neoforge;

import com.unlikepaladin.pfm.client.neoforge.PFMItemRendererNeoForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Settings settings) {
        super(settings);
    }

    public static Item getItemFactory(Settings settings) {
        return new OfficeChairItemImpl(settings);
    }
}
