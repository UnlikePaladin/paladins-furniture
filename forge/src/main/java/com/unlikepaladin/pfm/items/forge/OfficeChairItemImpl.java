package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Properties settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Properties settings) {
        return new OfficeChairItemImpl(settings);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return PFMItemRendererForge.INSTANCE;
            }
        });
    }
}
