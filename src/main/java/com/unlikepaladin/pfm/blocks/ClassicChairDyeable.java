package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class ClassicChairDyeable extends ClassicChair implements DyeableFurniture {


    public ClassicChairDyeable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(COLORID, DyeColor.WHITE).with(DYED, false));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
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
    }


    }


