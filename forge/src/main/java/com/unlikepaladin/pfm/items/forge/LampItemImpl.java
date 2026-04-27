package com.unlikepaladin.pfm.items.forge;

import com.unlikepaladin.pfm.items.LampItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;

import java.util.function.Consumer;

public class LampItemImpl extends LampItem {
    public LampItemImpl(Block block, Properties settings) {
        super(block, settings);
    }

    public static BlockItem getItemFactory(Block block, Properties settings) {
        return new LampItemImpl(block, settings);
    }
}
