package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SmokerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class Stove extends SmokerBlock {

    public Stove(Settings settings) {
        super(settings);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(StatisticsRegistry.STOVE_OPENED);
        }
    }

    protected static final VoxelShape STOVE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 14),createCuboidShape(0, 1, 14, 16, 16, 15),createCuboidShape(1.8, 12.2, 15.5375, 14.3, 12.799, 16.1375),createCuboidShape(2.5, 12.2, 14.07, 3.1, 12.79, 15.56),createCuboidShape(12.6, 12.2, 14.07, 13.2, 12.79, 15.57),createCuboidShape(1.8, 2.89, 15.437, 14.3, 3.49, 16.037),createCuboidShape(2.5, 2.89, 13.47, 3.1, 3.49, 15.47),createCuboidShape(12.6, 2.89, 13.47, 13.2, 3.49, 15.47),createCuboidShape(0, 16, 0, 16, 19, 1));
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return rotateShape(Direction.NORTH, Direction.EAST, STOVE);
            case NORTH:
                return rotateShape(Direction.NORTH, Direction.SOUTH, STOVE);
            case SOUTH:
                return STOVE;
            case EAST:
            default:
                return rotateShape(Direction.NORTH, Direction.WEST, STOVE);
        }
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
        return new StoveBlockEntity(PaladinFurnitureMod.STOVE_BLOCK_ENTITY, pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return Stove.checkType(world, type, PaladinFurnitureMod.STOVE_BLOCK_ENTITY);
    }

}
