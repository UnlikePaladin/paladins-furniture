package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.BlockColorsExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockColors.class)
public class BlockColorsAccessor implements BlockColorsExtension {

    @Shadow
    private final Map<Holder.Reference<Block>, BlockColor> blockColors = new HashMap<>();

    @Override
    public Map<Holder.Reference<Block>, BlockColor> getColorMap() {
        return blockColors;
    }
}
