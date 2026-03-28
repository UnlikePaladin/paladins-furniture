package com.unlikepaladin.pfm.entity;

import com.unlikepaladin.pfm.blocks.AbstractSittableBlock;
import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.client.PaladinFurnitureModClient;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class ChairEntity extends Mob {
    public ChairEntity(EntityType<? extends ChairEntity> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    @ExpectPlatform
    public static void fart(BlockPos pos) {
        throw new AssertionError();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.isVehicle()) {
            if (!this.level.isClientSide){
                this.discard();
            }
        }
        else if (this.level.getBlockState(this.blockPosition()).getBlock() instanceof BasicToiletBlock && level.isClientSide()){
            if (PaladinFurnitureModClient.USE_TOILET_KEYBIND.isDown() && this.level.getBlockState(this.blockPosition()).getValue(BasicToiletBlock.TOILET_STATE) == ToiletState.CLEAN) {
                fart(this.blockPosition());
            }
            super.tick();
        }
        else if (this.level.getBlockState(this.blockPosition()).getBlock() instanceof AbstractSittableBlock || this.level.getBlockState(this.blockPosition()).getBlock() instanceof BasicBathtubBlock){
            super.tick();
        }
        else {
            if (!this.level.isClientSide) {
                this.ejectPassengers();
                this.discard();
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public boolean isAlive() {
        return !this.isRemoved();
    }


    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Direction direction = this.getMotionDirection();
        if (this.level.getBlockState(this.blockPosition()).getBlock() instanceof AbstractSittableBlock) {
            direction = this.level.getBlockState(this.blockPosition()).getValue(AbstractSittableBlock.FACING).getOpposite();
        }
        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] dismountingOffsets = DismountHelper.offsetsForDirection(direction);
            BlockPos chairPos = this.blockPosition();
            BlockPos.MutableBlockPos dismountPos = new BlockPos.MutableBlockPos();

            for (Pose entityPose : passenger.getDismountPoses()) {
                AABB box = passenger.getLocalBoundsForPose(entityPose);
                for (int[] dismountingOffset : dismountingOffsets) {
                    dismountPos.set(chairPos.getX() + dismountingOffset[0], chairPos.getY() + 0.3, chairPos.getZ() + dismountingOffset[1]);
                    double dismountHeight = this.level.getBlockFloorHeight(dismountPos);
                    if (DismountHelper.isBlockFloorValid(dismountHeight)) {
                        Vec3 vec3d = Vec3.upFromBottomCenterOf(dismountPos, dismountHeight);
                        if (DismountHelper.canDismountTo(this.level, passenger, box.move(vec3d))) {
                            passenger.setPose(entityPose);
                            return vec3d;
                        }
                    }
                }
            }
        }
        return super.getDismountLocationForPassenger(passenger);
    }

    public static AttributeSupplier.Builder createMobAttributes(){
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.5f);
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }
}
