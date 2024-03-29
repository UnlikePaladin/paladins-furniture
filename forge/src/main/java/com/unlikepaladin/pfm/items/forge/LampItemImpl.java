package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRenderer;
import com.unlikepaladin.pfm.items.LampItem;
import net.minecraft.block.Block;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class LampItemImpl extends LampItem {
    public LampItemImpl(Block block, Settings settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Settings settings) {
        return new LampItemImpl(block, settings);
    }


    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BuiltinModelItemRenderer getItemStackRenderer() {
                return PFMItemRenderer.INSTANCE;
            }
        });
    }
}
