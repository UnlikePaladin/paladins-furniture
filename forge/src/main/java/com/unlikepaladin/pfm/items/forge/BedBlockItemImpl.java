package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.client.forge.PFMItemRendererForge;
import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Properties settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Item.Properties settings) {
        return new BedBlockItemImpl(block, settings);
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
