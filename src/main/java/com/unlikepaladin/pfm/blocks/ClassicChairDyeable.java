package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ClassicChairDyeable extends ClassicChair implements DyeableFurniture {

    private static final List<FurnitureBlock> WOOD_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    public ClassicChairDyeable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicChairDyeable.class)){
            WOOD_DYEABLE_CLASSIC_CHAIRS.add(new FurnitureBlock(this, "chair_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicChairDyeable.class)){
            STONE_DYEABLE_CLASSIC_CHAIRS.add(new FurnitureBlock(this, "chair_classic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodDyeableChair() {
        return WOOD_DYEABLE_CLASSIC_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneDyeableChair() {
        return STONE_DYEABLE_CLASSIC_CHAIRS.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
    }

    @Override
    public DyeColor getColor() {
        return null;
    }
}


