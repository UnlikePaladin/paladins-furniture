package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Settings settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Settings settings) {
        return new OfficeChairItemImpl(settings);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return PFMItemRendererForge.INSTANCE;
            }
        });
    }
}
