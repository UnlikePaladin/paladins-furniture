package com.unlikepaladin.pfm.compat.forge.imm_ptl.entity;


import com.unlikepaladin.pfm.compat.forge.imm_ptl.PFMImmPtlRegistry;
import com.unlikepaladin.pfm.compat.forge.imm_ptl.PFMMirrorBlockIP;
import com.unlikepaladin.pfm.compat.forge.imm_ptl.shape.BlockPortalShape;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.IntBox;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class PFMMirrorEntity extends Mirror {
    public static EntityType<PFMMirrorEntity> entityType = PFMImmPtlRegistry.MIRROR;
    @Nullable
    public IntBox wallArea;
    @Nullable
    public BlockPortalShape blockPortalShape;
    public boolean unbreakable = false;

    public PFMMirrorEntity(EntityType<PFMMirrorEntity> entityType, World world) {
        super(entityType, world);
        PFMMirrorEntity.entityType = entityType;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
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
    protected void writeCustomDataToNbt(NbtCompound tag) {
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
        if (!world.isClient) {
            if (!unbreakable) {
                if (world.getTime() % 10 == getId() % 10) {
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
        if (wallArea != null) {
            wallValid = wallArea.fastStream().allMatch(
                    blockPos -> isMirrorBlock(world, blockPos)
            );
        }
        else if (blockPortalShape != null) {
            wallValid = blockPortalShape.area.stream().allMatch(
                    blockPos -> isMirrorBlock(world, blockPos)
            );
        }
        else {
            wallValid = false;
        }
        if (!wallValid) {
            remove(RemovalReason.DISCARDED);
        }
    }

    public static boolean isMirrorBlock(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block instanceof PFMMirrorBlockIP;
    }

    public static PFMMirrorEntity createMirror(
            ServerWorld world,
            BlockPos glassPos,
            Direction facing
    ) {
        if (!isMirrorBlock(world, glassPos)) {
            return null;
        }

        BlockPortalShape shape = BlockPortalShape.findArea(
                glassPos, facing.getAxis(),
                blockPos -> isMirrorBlock(world, blockPos),
                blockPos -> !(isMirrorBlock(world, blockPos))
        );

        if (shape == null) {
            return null;
        }

        PFMMirrorEntity pfmMirrorEntity = PFMMirrorEntity.entityType.create(world);
        double distanceToCenter = -0.45;

        Box wallBox = getWallBox(world, shape.area.stream());
        if (wallBox == null) {
            return null;
        }

        Vec3d pos = Helper.getBoxSurfaceInversed(wallBox, facing.getOpposite()).getCenter();
        pos = Helper.putCoordinate(
                pos, facing.getAxis(),
                Helper.getCoordinate(
                        shape.innerAreaBox.getCenterVec().add(
                                Vec3d.of(facing.getVector()).multiply(distanceToCenter)
                        ),
                        facing.getAxis()
                )
        );
        pfmMirrorEntity.setPosition(pos.x, pos.y, pos.z);
        pfmMirrorEntity.setDestination(pos);
        pfmMirrorEntity.dimensionTo = world.getRegistryKey();

        shape.initPortalAxisShape(pfmMirrorEntity, pos, facing);

        pfmMirrorEntity.blockPortalShape = shape;
        world.spawnEntity(pfmMirrorEntity);

        return pfmMirrorEntity;
    }

    public IntBox getAreaBox() {
        if (wallArea != null) {
            return wallArea;
        }
        else if (blockPortalShape != null) {
            return blockPortalShape.innerAreaBox;
        }
        else {
            throw new RuntimeException();
        }
    }

    //it's a little bit incorrect with corner glass pane
    @Nullable
    public static Box getWallBox(World world, IntBox glassArea) {
        Stream<BlockPos> blockPosStream = glassArea.stream();
        return getWallBox(world, blockPosStream);
    }

    @Nullable
    public static Box getWallBox(World world, Stream<BlockPos> blockPosStream) {
        return blockPosStream.map(blockPos -> {
            VoxelShape collisionShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);

            if (collisionShape.isEmpty()) {
                return null;
            }

            return collisionShape.getBoundingBox().offset(Vec3d.of(blockPos));
        }).filter(b -> b != null).reduce(Box::union).orElse(null);
    }
}

