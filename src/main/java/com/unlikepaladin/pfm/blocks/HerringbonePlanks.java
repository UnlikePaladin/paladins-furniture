package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.state.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HerringbonePlanks extends HorizontalFacingBlock {
    private static final List<FurnitureBlock> PLANKS = new ArrayList<>();
    public HerringbonePlanks(Settings settings) {
        super(settings);
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(HerringbonePlanks.class)){
            PLANKS.add(new FurnitureBlock(this, "herringbone_planks"));
        }
    }

    public static Stream<FurnitureBlock> streamPlanks() {
        return PLANKS.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
