package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.neoforge.BlockColorsExtension;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockColors.class)
public class BlockColorsAccessor implements BlockColorsExtension {

    @Shadow
    private final Map<Block, BlockColorProvider> providers = new HashMap<>();

    @Override
    public Map<Block, BlockColorProvider> getColorMap() {
        return providers;
    }
}
