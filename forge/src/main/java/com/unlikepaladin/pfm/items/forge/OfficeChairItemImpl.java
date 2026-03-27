package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.OfficeChairItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class OfficeChairItemImpl extends OfficeChairItem {
    public OfficeChairItemImpl(Properties settings) {
        super(settings);
    }

    public static Item getItemFactory(Item.Properties settings) {
        return new OfficeChairItemImpl(settings);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return PFMItemRendererForge.INSTANCE;
            }
        });
    }
}
