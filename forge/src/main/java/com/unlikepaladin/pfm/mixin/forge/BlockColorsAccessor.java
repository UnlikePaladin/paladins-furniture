package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.forge.BlockColorsExtension;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraftforge.registries.IRegistryDelegate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockColors.class)
public class BlockColorsAccessor implements BlockColorsExtension {

    @Shadow
    private final Map<IRegistryDelegate<Block>, BlockColor> blockColors = new HashMap<>();

    @Override
    public Map<IRegistryDelegate<Block>, BlockColor> getColorMap() {
        return blockColors;
    }
}
