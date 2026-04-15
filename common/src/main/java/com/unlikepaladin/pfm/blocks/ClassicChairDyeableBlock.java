package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ClassicChairDyeableBlock extends ClassicChairBlock implements DyeableFurnitureBlock {

    private final DyeColor color;
    private static final List<FurnitureBlock> WOOD_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    public static MapCodec<ClassicChairDyeableBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(abstractSittableBlock -> abstractSittableBlock.color), propertiesCodec()).apply(instance, ClassicChairDyeableBlock::new));;;

    public ClassicChairDyeableBlock(DyeColor color, Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        if(isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ClassicChairDyeableBlock.class)){
            WOOD_DYEABLE_CLASSIC_CHAIRS.add(new FurnitureBlock(this, "oak_chair_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicChairDyeableBlock.class)){
            STONE_DYEABLE_CLASSIC_CHAIRS.add(new FurnitureBlock(this, "chair_classic"));
        }
        this.color = color;
    }

    public static Stream<FurnitureBlock> streamWoodDyeableChair() {
        return WOOD_DYEABLE_CLASSIC_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneDyeableChair() {
        return STONE_DYEABLE_CLASSIC_CHAIRS.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
    }

    @Override
    public DyeColor getPFMColor() {
        return this.color;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}


