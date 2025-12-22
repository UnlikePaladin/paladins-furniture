package com.unlikepaladin.pfm.entity;

import com.unlikepaladin.pfm.registry.Entities;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OfficeChairEntity extends MobEntity implements DyeableFurnitureEntity<OfficeChairEntity> {
    private float yawVelocity = 0.0F;
    private int rotationInputTicks = 0;  // Track how long player has been pressing same direction
    private float lastRotationDirection = 0.0F;  // Track last rotation direction
    private float wheelSpinAngle = 0.0F;  // Accumulated wheel spin angle
    private static final TrackedData<Byte> COLOR = DataTracker.registerData(OfficeChairEntity.class, TrackedDataHandlerRegistry.BYTE);

    public OfficeChairEntity(EntityType<? extends OfficeChairEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COLOR, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (isAlive() && timeUntilRegen == 0) {
            heal(0.1f);
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.hasPassengers() && this.canBeControlledByRider()) {
            LivingEntity livingEntity = (LivingEntity)this.getPrimaryPassenger();
            this.prevYaw = this.yaw;
            this.pitch = livingEntity.pitch * 0.5F;
            this.setRotation(this.yaw, this.pitch);
            this.bodyYaw = this.yaw;
            this.headYaw = this.bodyYaw;

            float forwardsSpeed = livingEntity.forwardSpeed;
            float rotationInput = livingEntity.sidewaysSpeed;

            this.flyingSpeed = this.getMovementSpeed() * 0.1F;
            if (this.isLogicalSideForUpdatingMovement()) {
                this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
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
                this.yaw = (this.yaw + yawVelocity);

                this.setRotation(this.yaw, this.pitch);
                this.bodyYaw = this.yaw;
                this.headYaw = this.bodyYaw;

                super.travel(new Vec3d(0, movementInput.y, forwardsSpeed));

            } else if (livingEntity instanceof PlayerEntity) {
                this.setVelocity(Vec3d.ZERO);
            }

            // Accumulate wheel spin based on distance traveled this tick
            double speed = horizontalLength(this.getVelocity());
            wheelSpinAngle += (float)(speed * 200);  // Adjust multiplier to control spin speed

            this.checkBlockCollision();
        } else {
            this.flyingSpeed = 0.02F;
            super.travel(movementInput);
        }
    }

    public static double horizontalLength(Vec3d v) {
        return Math.sqrt(v.x * v.x + v.z * v.z);
    }

    public float getWheelSpinAngle() {
        return wheelSpinAngle;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Color", (byte)this.getPFMColor().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setPFMColor(DyeColor.byId(nbt.getByte("Color")));
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float g = (float)((this.removed ? 0.01F : this.getMountedHeightOffset()) + passenger.getHeightOffset());

            Vec3d offset = new Vec3d(0.0, 0.0, -0.1).rotateY(-this.yaw * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));
            passenger.setPosition(this.getX() + offset.x, this.getY() + (double)g, this.getZ() + offset.z);
            passenger.yaw = (passenger.yaw + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.copyEntityData(passenger);
        }
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.yaw);
        float f = MathHelper.wrapDegrees(entity.yaw - this.yaw);
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.yaw = (entity.yaw + g - f);
        entity.setHeadYaw(entity.yaw);
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
        this.copyEntityData(passenger);
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (player.isSpectator() || player.isSneaking()) {
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.CONSUME;
        }


        if (!this.hasPassengers() && !player.isSneaking() && hand == Hand.MAIN_HAND) {
            player.startRiding(this, true);
            return ActionResult.SUCCESS;
        }
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public @Nullable Entity getPrimaryPassenger() {
        List<Entity> list = this.getPassengerList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getPrimaryPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();

        if (direction.getAxis() != Direction.Axis.Y) {
            int[][] dismountingOffsets = Dismounting.getDismountOffsets(direction);
            BlockPos chairPos = this.getBlockPos();
            BlockPos.Mutable dismountPos = new BlockPos.Mutable();

            for (EntityPose entityPose : passenger.getPoses()) {
                Box box = passenger.getBoundingBox(entityPose);
                for (int[] dismountingOffset : dismountingOffsets) {
                    dismountPos.set(chairPos.getX() + dismountingOffset[0], chairPos.getY() + 0.3, chairPos.getZ() + dismountingOffset[1]);
                    double dismountHeight = this.world.getDismountHeight(dismountPos);
                    if (Dismounting.canDismountInBlock(dismountHeight)) {
                        Vec3d vec3d = Vec3d.ofCenter(dismountPos, dismountHeight);
                        if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
                            passenger.setPose(entityPose);
                            return vec3d;
                        }
                    }
                }
            }
        }
        return super.updatePassengerForDismount(passenger);
    }


    public static DefaultAttributeContainer.Builder createMobAttributes(){
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f);
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.55;
    }

    @Override
    public void setPFMColor(DyeColor color) {
        byte b = this.dataTracker.get(COLOR);
        this.dataTracker.set(COLOR, (byte)(b & 240 | color.getId() & 15));
    }

    @Override
    public DyeColor getPFMColor() {
        return DyeColor.byId(this.dataTracker.get(COLOR) & 15);
    }

    @Override
    public NbtCompound writeColor(NbtCompound nbt) {
        nbt.putByte("Color", (byte)this.getPFMColor().getId());
        return nbt;
    }

    @Override
    public OfficeChairEntity getEntity() {
        return this;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
    }

    @Override
    protected void drop(DamageSource source) {
        super.drop(source);
        if (!source.isSourceCreativePlayer()) {
            ItemStack stack = PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM.getDefaultStack();
            stack.getOrCreateTag().putString("Color", this.getPFMColor().asString());

            ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), stack);
            this.world.spawnEntity(itemEntity);
        }
    }

    @Override
    public void takeKnockback(float strength, double x, double z) {

    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_STONE_HIT;
    }
}
