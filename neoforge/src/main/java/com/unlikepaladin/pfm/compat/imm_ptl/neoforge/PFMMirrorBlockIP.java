package com.unlikepaladin.pfm.compat.imm_ptl.neoforge;
/*
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.entity.PFMMirrorEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PFMMirrorBlockIP extends MirrorBlock {
    protected PFMMirrorBlockIP(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn()) {
            if (!world.isClient && world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos)).isEmpty()) {
                PFMMirrorEntity.createMirror((ServerWorld) world, pos, state.getValue(FACING).getOpposite());
            }
        }
    }

    @Override
    public void onBreak(Level world, BlockPos pos, BlockState state, Player player) {
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn()) {
        List<PFMMirrorEntity> mirrorBlockEntities;
            if (!world.isClient && !(mirrorBlockEntities = world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos))).isEmpty()) {
                mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                    ((PFMMirrorEntity)pfmMirrorEntity).remove(Entity.RemovalReason.KILLED);
                });
                world.updateNeighbors(pos, state.getBlock());
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn() && !world.isClient()) {
            List<PFMMirrorEntity> mirrorBlockEntities = new ArrayList<>();
            if (canConnect(neighborState, state)) {
                mirrorBlockEntities.addAll(world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(neighborPos)));
            }
            if (!(world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos)).isEmpty())) {
                mirrorBlockEntities.addAll(world.getNonSpectatingEntities(PFMMirrorEntity.class, new Box(pos)));
                mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                    ((Entity)pfmMirrorEntity).remove(Entity.RemovalReason.KILLED);
                });
            }
            PFMMirrorEntity.createMirror((ServerWorld) world, pos, state.getValue(FACING).getOpposite());
            world.updateNeighbors(pos, state.getBlock());
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
*/