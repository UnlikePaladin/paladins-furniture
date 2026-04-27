package com.unlikepaladin.pfm.entity;

import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OfficeChairEntity extends Mob implements DyeableFurnitureEntity<OfficeChairEntity> {
    private float yawVelocity = 0.0F;
    private int rotationInputTicks = 0;  // Track how long player has been pressing same direction
    private float lastRotationDirection = 0.0F;  // Track last rotation direction
    private float wheelSpinAngle = 0.0F;  // Accumulated wheel spin angle
    private static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(OfficeChairEntity.class, EntityDataSerializers.BYTE);

    public OfficeChairEntity(EntityType<? extends OfficeChairEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (isAlive() && invulnerableTime == 0) {
            heal(0.1f);
        }
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isVehicle() && this.canBeControlledByRider()) {
            LivingEntity livingEntity = (LivingEntity)this.getControllingPassenger();
            this.yRotO = this.yRot;
            this.xRot = livingEntity.xRot * 0.5F;
            this.setRot(this.yRot, this.xRot);
            this.yBodyRot = this.yRot;
            this.yHeadRot = this.yBodyRot;

            float forwardsSpeed = livingEntity.zza;
            float rotationInput = livingEntity.xxa;

            this.flyingSpeed = this.getSpeed() * 0.1F;
            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                final float baseRotationSensitivity = 0.5F;
                final float maxRotationSensitivity = 2.0F;

                final int maxAccelerationTicks = 30;  // Ticks to reach max speed
                final float decay = 0.85F;

                if (Math.abs(rotationInput) > 1.0E-5F) {
                    float currentDirection = Math.signum(rotationInput);

                    // Check if same direction as last tick
                    if (currentDirection == lastRotationDirection) {
                        rotationInputTicks = Math.min(rotationInputTicks + 1, maxAccelerationTicks);
                    } else {
                        rotationInputTicks = 1;
                    }
                    lastRotationDirection = currentDirection;

                    // Calculate ramped sensitivity based on how long they've been pressing
                    float accelerationFactor = (float) rotationInputTicks / maxAccelerationTicks;
                    float currentSensitivity = baseRotationSensitivity + (maxRotationSensitivity - baseRotationSensitivity) * accelerationFactor;

                    // negated to match player controls
                    yawVelocity += -rotationInput * currentSensitivity;
                } else {
                    rotationInputTicks = 0;
                    lastRotationDirection = 0.0F;
                }

                yawVelocity *= decay;
                this.yRot = (this.yRot + yawVelocity);

                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;

                super.travel(new Vec3(0, movementInput.y, forwardsSpeed));

            } else if (livingEntity instanceof Player) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            // Accumulate wheel spin based on distance traveled this tick
            double speed = horizontalLength(this.getDeltaMovement());
            wheelSpinAngle += (float)(speed * 200);  // Adjust multiplier to control spin speed

            this.tryCheckInsideBlocks();
        } else {
            this.flyingSpeed = 0.02F;
            super.travel(movementInput);
        }
    }

    public static double horizontalLength(Vec3 v) {
        return Math.sqrt(v.x * v.x + v.z * v.z);
    }

    public float getWheelSpinAngle() {
        return wheelSpinAngle;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("Color", (byte)this.getPFMColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setPFMColor(DyeColor.byId(nbt.getByte("Color")));
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float g = (float)((this.removed ? 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());

            Vec3 offset = new Vec3(0.0, 0.0, 0.0).yRot(-this.getYRot() * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));
            passenger.setPos(this.getX() + offset.x, this.getY() + (double)g, this.getZ() + offset.z);
            passenger.yRot = (passenger.getYRot() + this.yawVelocity);
            passenger.setYHeadRot(passenger.getYHeadRot() + this.yawVelocity);
            this.copyEntityData(passenger);
        }
    }

    protected void copyEntityData(Entity entity) {
        entity.setYBodyRot(this.yRot);
        float f = Mth.wrapDegrees(entity.yRot - this.yRot);
        float g = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += g - f;
        entity.setYRot(entity.yRot + g - f);
        entity.setYHeadRot(entity.yRot);
    }

    @Override
    public void onPassengerTurned(Entity passenger) {
        this.copyEntityData(passenger);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if (player.isSpectator() || player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        }


        if (!this.isVehicle() && !player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            player.startRiding(this, true);
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public @Nullable Entity getControllingPassenger() {
        List<Entity> list = this.getPassengerList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Direction direction = this.getMotionDirection();

        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] dismountingOffsets = DismountHelper.offsetsForDirection(direction);
            BlockPos chairPos = this.getOnPos();
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1f);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.55;
    }

    @Override
    public void setPFMColor(DyeColor color) {
        byte b = this.entityData.get(COLOR);
        this.entityData.set(COLOR, (byte)(b & 240 | color.getId() & 15));
    }

    @Override
    public DyeColor getPFMColor() {
        return DyeColor.byId(this.entityData.get(COLOR) & 15);
    }

    @Override
    public CompoundTag writeColor(CompoundTag nbt) {
        nbt.putByte("Color", (byte)this.getPFMColor().getId());
        return nbt;
    }

    @Override
    public OfficeChairEntity getEntity() {
        return this;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        super.dropAllDeathLoot(source);
        if (!source.isCreativePlayer()) {
            ItemStack stack = PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM.getDefaultInstance();
            stack.getOrCreateTag().putString("Color", this.getPFMColor().getSerializedName());

            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), stack);
            this.level.addFreshEntity(itemEntity);
        }
    }

    @Override
    public void knockback(float strength, double x, double z) {

    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.STONE_HIT;
    }
}
