package com.unlikepaladin.pfm.compat.imm_ptl.forge;
/*
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.MirrorBlock;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.entity.PFMMirrorEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PFMMirrorBlockIP extends MirrorBlock {
    protected PFMMirrorBlockIP(Properties settings) {
        super(settings);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn()) {
            if (!world.isClientSide && world.getEntitiesOfClass(PFMMirrorEntity.class, new AABB(pos)).isEmpty()) {
                PFMMirrorEntity.createMirror((ServerLevel) world, pos, state.getValue(FACING).getOpposite());
            }
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(world, pos, state, player);
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn()) {
        List<PFMMirrorEntity> mirrorBlockEntities;
            if (!world.isClientSide && !(mirrorBlockEntities = world.getEntitiesOfClass(PFMMirrorEntity.class, new AABB(pos))).isEmpty()) {
                mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                    ((PFMMirrorEntity)pfmMirrorEntity).remove(Entity.RemovalReason.KILLED);
                });
                world.updateNeighborsAt(pos, state.getBlock());
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (PaladinFurnitureMod.getPFMConfig().doImmersivePortalsMirrorsSpawn() && !world.isClientSide()) {
            List<PFMMirrorEntity> mirrorBlockEntities = new ArrayList<>();
            if (canConnect(neighborState, state)) {
                mirrorBlockEntities.addAll(world.getEntitiesOfClass(PFMMirrorEntity.class, new AABB(neighborPos)));
            }
            if (!(world.getEntitiesOfClass(PFMMirrorEntity.class, new AABB(pos)).isEmpty())) {
                mirrorBlockEntities.addAll(world.getEntitiesOfClass(PFMMirrorEntity.class, new AABB(pos)));
                mirrorBlockEntities.forEach(pfmMirrorEntity -> {
                    ((Entity)pfmMirrorEntity).remove(Entity.RemovalReason.KILLED);
                });
            }
            PFMMirrorEntity.createMirror((ServerLevel) world, pos, state.getValue(FACING).getOpposite());
            world.blockUpdated(pos, state.getBlock());
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
}
*/