package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.blocks.DyeableFurnitureBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BedBlock.class)
public class PFMBedBlockAccessor implements DyeableFurnitureBlock {

    @Shadow
    @Final
    private DyeColor color;

    @Override
    public DyeColor getPFMColor() {
        return this.color;
    }
}
