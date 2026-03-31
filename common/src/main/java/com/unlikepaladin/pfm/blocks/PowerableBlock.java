package com.unlikepaladin.pfm.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class PowerableBlock extends Block {
    protected PowerableBlock(Properties settings) {
        super(settings);
    }
    public abstract void setPowered(Level world, BlockPos lightPos, boolean powered);
    public static BooleanProperty POWERLOCKED = BooleanProperty.create("powerlocked");
}
