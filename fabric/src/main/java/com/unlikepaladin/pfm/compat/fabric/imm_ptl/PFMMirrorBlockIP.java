package com.unlikepaladin.pfm.compat.fabric.imm_ptl;

import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.compat.fabric.imm_ptl.entity.PFMMirrorEntity;
import com.unlikepaladin.pfm.entity.ChairEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PFMMirrorBlockIP extends MirrorBlock {
    protected PFMMirrorBlockIP(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient && world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos)).isEmpty()) {
            PFMMirrorEntity.createMirror((ServerWorld) world, pos, state.get(FACING).getOpposite());
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        List<PFMMirrorEntity> mirrorBlockEntities;
        if (!world.isClient && !(mirrorBlockEntities = world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos))).isEmpty()) {
            mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                pfmMirrorEntity.remove(Entity.RemovalReason.KILLED);
            });
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient() && neighborState.getBlock() instanceof PFMMirrorBlockIP && world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(neighborPos)).isEmpty()) {
            List<PFMMirrorEntity> mirrorBlockEntities;
            if (!(mirrorBlockEntities = world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos))).isEmpty()) {
                mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                    pfmMirrorEntity.remove(Entity.RemovalReason.KILLED);
                });
            }
            PFMMirrorEntity.createMirror((ServerWorld) world, neighborPos, neighborState.get(FACING).getOpposite());
            world.updateNeighbors(neighborPos, neighborState.getBlock());
        }
        System.out.println("Called from: " + pos);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
