package com.unlikepaladin.pfm.compat.imm_ptl.forge.entity;


import com.unlikepaladin.pfm.compat.imm_ptl.forge.PFMImmersivePortalsImpl;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.PFMMirrorBlockIP;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.shape.BlockPortalShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.IntBox;

import org.jetbrains.annotations.Nullable;
import java.util.stream.Stream;

public class PFMMirrorEntity extends Mirror {
    @Nullable
    public IntBox wallArea;
    @Nullable
    public BlockPortalShape blockPortalShape;
    public boolean unbreakable = false;
    private Direction facing;

    public PFMMirrorEntity(EntityType<PFMMirrorEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("boxXL")) {
            wallArea = new IntBox(
                    new BlockPos(
                            tag.getInt("boxXL"),
                            tag.getInt("boxYL"),
                            tag.getInt("boxZL")
                    ),
                    new BlockPos(
                            tag.getInt("boxXH"),
                            tag.getInt("boxYH"),
                            tag.getInt("boxZH")
                    )
            );
        }
        else {
            wallArea = null;
        }
        if (tag.contains("blockPortalShape")) {
            blockPortalShape = BlockPortalShape.fromTag(tag.getCompound("blockPortalShape"));
        }
        else {
            blockPortalShape = null;
        }
        if (tag.contains("unbreakable")) {
            unbreakable = tag.getBoolean("unbreakable");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (wallArea != null) {
            tag.putInt("boxXL", wallArea.l.getX());
            tag.putInt("boxYL", wallArea.l.getY());
            tag.putInt("boxZL", wallArea.l.getZ());
            tag.putInt("boxXH", wallArea.h.getX());
            tag.putInt("boxYH", wallArea.h.getY());
            tag.putInt("boxZH", wallArea.h.getZ());
        }

        if (blockPortalShape != null) {
            tag.put("blockPortalShape", blockPortalShape.toTag());
        }
        tag.putBoolean("unbreakable", unbreakable);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (!unbreakable) {
                if (level().getDayTime() % 10 == getId() % 10) {
                    checkWallIntegrity();
                }
            }
        }
    }

    @Override
    public boolean isPortalValid() {
        return super.isPortalValid() && (wallArea != null || blockPortalShape != null);
    }

    private void checkWallIntegrity() {
        boolean wallValid;
        if (this.facing == null && this.level().getBlockState(getOnPos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            this.facing = this.level().getBlockState(getOnPos()).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        else if (this.facing == null){
            this.facing = Direction.NORTH;
        }
        if (wallArea != null) {
            wallValid = wallArea.fastStream().allMatch(
                    blockPos -> isMirrorBlock(level(), blockPos, this.facing.getOpposite())
            );
        }
        else if (blockPortalShape != null) {
            wallValid = blockPortalShape.area.stream().allMatch(
                    blockPos -> isMirrorBlock(level(), blockPos, this.facing.getOpposite())
            );
        }
        else {
            wallValid = false;
        }
        if (!wallValid) {
            ((Entity)this).remove(RemovalReason.DISCARDED);
        }
    }

    public static boolean isMirrorBlock(Level level, BlockPos blockPos, Direction facing) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return blockState.getBlock() instanceof PFMMirrorBlockIP && blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).equals(facing);
        }
        return false;
    }

    public static void createMirror(ServerLevel level, BlockPos glassPos, Direction facing) {
        if (!isMirrorBlock(level, glassPos, facing.getOpposite())) {
            return;
        }

        BlockPortalShape shape = BlockPortalShape.findArea(
                glassPos, facing.getAxis(),
                blockPos -> isMirrorBlock(level, blockPos, facing.getOpposite()),
                blockPos -> !(isMirrorBlock(level, blockPos, facing.getOpposite()))
        );

        if (shape == null) {
            return;
        }

        PFMMirrorEntity pfmMirrorEntity = PFMImmersivePortalsImpl.MIRROR.create(level);
        double distanceToCenter = -0.452;

        AABB wallBox = getWallBox(level, shape.area.stream());
        if (wallBox == null) {
            return;
        }
        pfmMirrorEntity.facing = facing;
        Vec3 pos = Helper.getBoxSurfaceInversed(wallBox, facing.getOpposite()).getCenter();
        pos = Helper.putCoordinate(
                pos, facing.getAxis(),
                Helper.getCoordinate(
                        shape.innerAreaBox.getCenterVec().add(
                                Vec3.atLowerCornerOf(facing.getNormal()).scale(distanceToCenter)
                        ),
                        facing.getAxis()
                )
        );
        ((Entity)pfmMirrorEntity).setPos(pos.x, pos.y, pos.z);
        pfmMirrorEntity.setDestination(pos);
        pfmMirrorEntity.dimensionTo = level.dimension();

        shape.initPortalAxisShape(pfmMirrorEntity, pos, facing);

        pfmMirrorEntity.blockPortalShape = shape;
        level.addFreshEntity(pfmMirrorEntity);

    }

    @Nullable
    public static AABB getWallBox(ServerLevel level, Stream<BlockPos> blockPosStream) {
        return blockPosStream.map(blockPos -> {
            VoxelShape collisionShape = level.getBlockState(blockPos).getCollisionShape(level, blockPos);

            if (collisionShape.isEmpty()) {
                return null;
            }

            return collisionShape.bounds().move(Vec3.atLowerCornerOf(blockPos));
        }).filter(b -> b != null).reduce(AABB::minmax).orElse(null);
    }
}