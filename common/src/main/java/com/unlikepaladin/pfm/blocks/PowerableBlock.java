package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PowerableBlock extends Block {
    protected PowerableBlock(Settings settings) {
        super(settings);
    }
    public abstract void setPowered(World world, BlockPos lightPos, boolean powered);
    public static BooleanProperty POWERLOCKED = BooleanProperty.of("powerlocked");
}
