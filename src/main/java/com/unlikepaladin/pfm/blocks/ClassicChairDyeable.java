package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ClassicChairDyeable extends ClassicChair implements DyeableFurniture {

    private static final List<FurnitureBlock> WOOD_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DYEABLE_CLASSIC_CHAIRS = new ArrayList<>();
    public ClassicChairDyeable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(COLORID, DyeColor.WHITE).with(DYED, false));
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
        stateManager.add(COLORID);
        stateManager.add(DYED);
    }
    protected DyeColor getColor (BlockState state){
        return state.get(this.COLORID);
    }
    public void dropKit(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DyeColor dyeColor = getColor(state);
        if (!player.getAbilities().creativeMode && !world.isClient && state.get(DYED)){
            switch (dyeColor){
                case RED:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_RED));     break;

                case ORANGE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_ORANGE));    break;

                case YELLOW:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_YELLOW));    break;

                case LIME:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIME));    break;

                case GREEN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_GREEN));    break;

                case CYAN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_CYAN));    break;

                case LIGHT_BLUE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_BLUE));    break;

                case BLUE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BLUE));    break;

                case PURPLE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_PURPLE));    break;

                case MAGENTA:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_MAGENTA));    break;

                case PINK:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_PINK));    break;

                case BROWN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BROWN));    break;

                case BLACK:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BLACK));    break;

                case GRAY:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_GRAY));    break;

                case LIGHT_GRAY:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_GRAY)); break;

                case WHITE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_WHITE));    break;

                default:
                    dropStack(world, pos, new ItemStack(Items.AIR));

            }
        }
    }


    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        dropKit(world, pos, state, player);
        super.onBreak(world, pos, state, player);
    }


    }


