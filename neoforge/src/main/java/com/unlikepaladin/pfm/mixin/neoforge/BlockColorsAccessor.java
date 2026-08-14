package com.unlikepaladin.pfm.mixin.neoforge;

import com.unlikepaladin.pfm.client.neoforge.BlockColorsExtension;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(BlockColors.class)
public class BlockColorsAccessor implements BlockColorsExtension {

    @Shadow
    private final Map<Block, List<BlockTintSource>> blockColors = new HashMap<>();

    @Override
    public Map<Block, List<BlockTintSource>> getColorMap() {
        return blockColors;
    }
}
