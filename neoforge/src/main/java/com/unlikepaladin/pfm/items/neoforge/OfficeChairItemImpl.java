package com.unlikepaladin.pfm.items.neoforge;

import com.unlikepaladin.pfm.client.neoforge.PFMItemRendererNeoForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Properties settings) {
        super(settings);
    }

    public static Item getItemFactory(Properties settings) {
        return new OfficeChairItemImpl(settings);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return PFMItemRendererNeoForge.INSTANCE;
            }
        });
    }
}
