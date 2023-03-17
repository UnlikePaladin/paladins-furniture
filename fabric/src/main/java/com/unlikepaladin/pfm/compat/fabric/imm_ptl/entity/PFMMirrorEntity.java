package com.unlikepaladin.pfm.compat.fabric.imm_ptl.entity;


import com.unlikepaladin.pfm.compat.fabric.imm_ptl.PFMImmPtlRegistry;
import com.unlikepaladin.pfm.compat.fabric.imm_ptl.PFMMirrorBlockIP;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.q_misc_util.my_util.IntBox;
import qouteall.q_misc_util.Helper;

public class PFMMirrorEntity extends Mirror {
    public static EntityType<PFMMirrorEntity> entityType = PFMImmPtlRegistry.MIRROR;

    public IntBox wallArea;
    public boolean unbreakable = false;

    public PFMMirrorEntity(EntityType<PFMMirrorEntity> entityType, World world) {
        super(entityType, world);
        PFMMirrorEntity.entityType = entityType;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
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
        if (tag.contains("unbreakable")) {
            unbreakable = tag.getBoolean("unbreakable");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("boxXL", wallArea.l.getX());
        tag.putInt("boxYL", wallArea.l.getY());
        tag.putInt("boxZL", wallArea.l.getZ());
        tag.putInt("boxXH", wallArea.h.getX());
        tag.putInt("boxYH", wallArea.h.getY());
        tag.putInt("boxZH", wallArea.h.getZ());

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
        return super.isPortalValid() && wallArea != null;
    }

    private void checkWallIntegrity() {
        boolean wallValid = wallArea.fastStream().allMatch(
                blockPos ->
                        isMirrorBlock(world, blockPos)
        );
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

        //IntBox wallArea =  new IntBox(glassPos, glassPos);
        IntBox wallArea = Helper.expandRectangle(
                glassPos,
                blockPos -> isMirrorBlock(world, blockPos),
                facing.getAxis()
        );
        PFMMirrorEntity pfmMirrorEntity = PFMMirrorEntity.entityType.create(world);
        double distanceToCenter = -0.45;

        Box wallBox = getWallBox(world, wallArea);

        Vec3d pos = Helper.getBoxSurfaceInversed(wallBox, facing.getOpposite()).getCenter();
        pos = Helper.putCoordinate(
                pos, facing.getAxis(),
                Helper.getCoordinate(
                        wallArea.getCenterVec().add(
                                Vec3d.of(facing.getVector()).multiply(distanceToCenter)
                        ),
                        facing.getAxis()
                )
        );
        pfmMirrorEntity.setPosition(pos.x, pos.y, pos.z);
        pfmMirrorEntity.setDestination(pos);
        pfmMirrorEntity.dimensionTo = world.getRegistryKey();

        Pair<Direction, Direction> dirs =
                Helper.getPerpendicularDirections(facing);

        Vec3d boxSize = Helper.getBoxSize(wallBox);
        double width = Helper.getCoordinate(boxSize, dirs.getLeft().getAxis());
        double height = Helper.getCoordinate(boxSize, dirs.getRight().getAxis());

        pfmMirrorEntity.axisW =  Vec3d.of(dirs.getLeft().getVector());
        pfmMirrorEntity.axisH =  Vec3d.of(dirs.getRight().getVector());
        pfmMirrorEntity.width = width;
        pfmMirrorEntity.height = height;

        pfmMirrorEntity.wallArea = wallArea;

        world.spawnEntity(pfmMirrorEntity);

        return pfmMirrorEntity;
    }


    //it's a little bit incorrect with corner glass pane
    private static Box getWallBox(ServerWorld world, IntBox glassArea) {
        return glassArea.stream().map(blockPos ->
                world.getBlockState(blockPos).getCollisionShape(world, blockPos).getBoundingBox()
                        .offset( Vec3d.of(blockPos))
        ).reduce(Box::union).orElse(null);
    }
}

