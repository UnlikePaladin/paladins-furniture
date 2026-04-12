package com.unlikepaladin.pfm.items.neoforge;

import com.unlikepaladin.pfm.client.neoforge.PFMItemRendererNeoForge;
import com.unlikepaladin.pfm.items.BedBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BedBlockItemImpl extends BedBlockItem {

    public BedBlockItemImpl(Block block, Properties settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Properties settings) {
        return new BedBlockItemImpl(block, settings);
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
