package com.unlikepaladin.pfm.compat.imm_ptl.neoforge.entity;

/*
import com.unlikepaladin.pfm.compat.imm_ptl.forge.PFMImmersivePortalsImpl;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.PFMMirrorBlockIP;
import com.unlikepaladin.pfm.compat.imm_ptl.forge.shape.BlockPortalShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.Level;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.IntBox;

import org.jetbrains.annotations.Nullable;
import java.util.stream.Stream;

public class PFMMirrorEntity extends Mirror {
    public static EntityType<PFMMirrorEntity> entityType = PFMImmersivePortalsImpl.MIRROR;
    @Nullable
    public IntBox wallArea;
    @Nullable
    public BlockPortalShape blockPortalShape;
    public boolean unbreakable = false;
    private Direction facing;

    public PFMMirrorEntity(EntityType<PFMMirrorEntity> entityType, Level world) {
        super(entityType, world);
        PFMMirrorEntity.entityType = entityType;
    }

    @Override
    protected void readCustomDataFromNbt(CompoundTag tag) {
        super.readCustomDataFromNbt(tag);
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
    protected void writeCustomDataToNbt(CompoundTag tag) {
        super.writeCustomDataToNbt(tag);
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
        if (!getEntityWorld().isClient) {
            if (!unbreakable) {
                if (getEntityWorld().getTime() % 10 == getId() % 10) {
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
        if (this.facing == null && this.getEntityWorld().getBlockState(getBlockPos()).contains(BlockStateProperties.HORIZONTAL_FACING))
            this.facing = this.getEntityWorld().getBlockState(getBlockPos()).get(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        else if (this.facing == null){
            this.facing = Direction.NORTH;
        }
        if (wallArea != null) {
            wallValid = wallArea.fastStream().allMatch(
                    blockPos -> isMirrorBlock(getEntityWorld(), blockPos, this.facing.getOpposite())
            );
        }
        else if (blockPortalShape != null) {
            wallValid = blockPortalShape.area.stream().allMatch(
                    blockPos -> isMirrorBlock(getEntityWorld(), blockPos, this.facing.getOpposite())
            );
        }
        else {
            wallValid = false;
        }
        if (!wallValid) {
            ((Entity)this).remove(RemovalReason.DISCARDED);
        }
    }

    public static boolean isMirrorBlock(Level world, BlockPos blockPos, Direction facing) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return blockState.getBlock() instanceof PFMMirrorBlockIP && blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).equals(facing);
        }
        return false;
    }

    public static void createMirror(ServerLevel world, BlockPos glassPos, Direction facing) {
        if (!isMirrorBlock(world, glassPos, facing.getOpposite())) {
            return;
        }

        BlockPortalShape shape = BlockPortalShape.findArea(
                glassPos, facing.getAxis(),
                blockPos -> isMirrorBlock(world, blockPos, facing.getOpposite()),
                blockPos -> !(isMirrorBlock(world, blockPos, facing.getOpposite()))
        );

        if (shape == null) {
            return;
        }

        PFMMirrorEntity pfmMirrorEntity = PFMMirrorEntity.entityType.create(world);
        double distanceToCenter = -0.452;

        Box wallBox = getWallBox(world, shape.area.stream());
        if (wallBox == null) {
            return;
        }
        pfmMirrorEntity.facing = facing;
        Vec3 pos = Helper.getBoxSurfaceInversed(wallBox, facing.getOpposite()).getCenter();
        pos = Helper.putCoordinate(
                pos, facing.getAxis(),
                Helper.getCoordinate(
                        shape.innerAreaBox.getCenterVec().add(
                                Vec3.of(facing.getVector()).multiply(distanceToCenter)
                        ),
                        facing.getAxis()
                )
        );
        ((Entity)pfmMirrorEntity).setPos(pos.x, pos.y, pos.z);
        pfmMirrorEntity.setDestination(pos);
        pfmMirrorEntity.dimensionTo = world.getRegistryKey();

        shape.initPortalAxisShape(pfmMirrorEntity, pos, facing);

        pfmMirrorEntity.blockPortalShape = shape;
        world.spawnEntity(pfmMirrorEntity);

    }

    @Nullable
    public static Box getWallBox(Level world, Stream<BlockPos> blockPosStream) {
        return blockPosStream.map(blockPos -> {
            VoxelShape collisionShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);

            if (collisionShape.isEmpty()) {
                return null;
            }

            return collisionShape.getBoundingBox().offset(Vec3.of(blockPos));
        }).filter(b -> b != null).reduce(Box::union).orElse(null);
    }
}
 */