package com.unlikepaladin.pfm.blocks;


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.IronStoveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class IronStove extends Stove {
    public IronStove(Settings settings) {
        super(settings);
    }

    protected static final VoxelShape IRON_STOVE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 14),createCuboidShape(0, 1, 14, 16, 16, 15),createCuboidShape(1.8, 12.2, 15.5375, 14.3, 12.799, 16.1375),createCuboidShape(2.5, 12.2, 14.07, 3.1, 12.79, 15.56),createCuboidShape(12.6, 12.2, 14.07, 13.2, 12.79, 15.57),createCuboidShape(1.8, 2.89, 15.437, 14.3, 3.49, 16.037),createCuboidShape(2.5, 2.89, 13.47, 3.1, 3.49, 15.47),createCuboidShape(12.6, 2.89, 13.47, 13.2, 3.49, 15.47));
    protected static final VoxelShape IRON_STOVE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_EAST = rotateShape(Direction.NORTH, Direction.EAST, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_WEST = rotateShape(Direction.NORTH, Direction.WEST, IRON_STOVE);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> IRON_STOVE_EAST;
            case NORTH -> IRON_STOVE_SOUTH;
            case SOUTH -> IRON_STOVE;
            default -> IRON_STOVE_WEST;
        };
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        double x = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double z = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            world.playSound(x, y, z, SoundEvents.BLOCK_SMOKER_SMOKE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
        int min = 0;
        int max = 3;
        int griddleChosen = (int)Math.floor(Math.random()*(max-min+1)+min);
        switch (griddleChosen) {
            case 0 -> world.addParticle(ParticleTypes.SMOKE, x - 0.25, y + 1.1, z - 0.2, 0.0, 0.0, 0.0);
            case 1 -> world.addParticle(ParticleTypes.SMOKE, x + 0.25, y + 1.1, z - 0.2, 0.0, 0.0, 0.0);
            case 2 -> world.addParticle(ParticleTypes.SMOKE, x + 0.25, y + 1.1, z + 0.2, 0.0, 0.0, 0.0);
            case 3 -> world.addParticle(ParticleTypes.SMOKE, x - 0.25, y + 1.1, z + 0.2, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IronStoveBlockEntity(pos, state);
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, PaladinFurnitureMod.IRON_STOVE_BLOCK_ENTITY);
    }

    @Override
    public void openScreen(World world, BlockPos pos, PlayerEntity player) {
        //This is called by the onUse method inside AbstractFurnaceBlock so
        //it is a little bit different of how you open the screen for normal container
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IronStoveBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            // Optional: increment player's stat
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

}
