package com.unlikepaladin.pfm.entity;

import com.unlikepaladin.pfm.items.PFMComponents;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OfficeChairEntity extends MobEntity implements DyeableFurnitureEntity<OfficeChairEntity> {
    private float yawVelocity = 0.0F;
    private int rotationInputTicks = 0;  // Track how long player has been pressing same direction
    private float lastRotationDirection = 0.0F;  // Track last rotation direction
    private float wheelSpinAngle = 0.0F;  // Accumulated wheel spin angle
    private static final TrackedData<Byte> COLOR = DataTracker.registerData(OfficeChairEntity.class, TrackedDataHandlerRegistry.BYTE);

    public OfficeChairEntity(EntityType<? extends OfficeChairEntity> type, World world) {
        super(type, world);
    }

    public OfficeChairEntity(World world, double x, double y, double z) {
        super(Entities.OFFICE_CHAIR, world);
        this.setPos(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, (byte)0);
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
        if (this.hasPassengers() && this.getControllingPassenger() instanceof LivingEntity) {
            LivingEntity livingEntity = this.getControllingPassenger();
            this.prevYaw = this.getYaw();
            this.setPitch(livingEntity.getPitch() * 0.5F);
            this.setRotation(this.getYaw(), this.getPitch());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.bodyYaw;

            float forwardsSpeed = livingEntity.forwardSpeed;
            float rotationInput = livingEntity.sidewaysSpeed;

            if (this.isLogicalSideForUpdatingMovement()) {
                this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.MOVEMENT_SPEED));
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
                this.setYaw(this.getYaw() + yawVelocity);

                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;

                super.travel(new Vec3d(0, movementInput.y, forwardsSpeed));

            } else if (livingEntity instanceof PlayerEntity) {
                this.setVelocity(Vec3d.ZERO);
            }

            // Accumulate wheel spin based on distance traveled this tick
            double speed = this.getVelocity().horizontalLength();
            wheelSpinAngle += (float)(speed * 200);  // Adjust multiplier to control spin speed

            this.tickBlockCollision();
        } else {
            super.travel(movementInput);
        }
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
    public void updatePassengerPosition(Entity passenger, PositionUpdater updater) {
        if (this.hasPassenger(passenger)) {
            float g = (float)((this.isRemoved() ? 0.01F : this.getPassengerRidingPos(passenger).y));

            Vec3d offset = new Vec3d(0.0, 0.0, 0.0).rotateY(-this.getYaw() * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));
            updater.accept(passenger, this.getX() + offset.x, this.getY() + (double)g, this.getZ() + offset.z);
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.copyEntityData(passenger);
        }
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.setYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
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

        if (getWorld().isClient) {
            return ActionResult.CONSUME;
        }


        if (!this.hasPassengers() && !player.isSneaking() && hand == Hand.MAIN_HAND) {
            player.startRiding(this, true);
            return ActionResult.SUCCESS;
        }
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        if (this.hasPassengers()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return super.getControllingPassenger();
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
                    double dismountHeight = this.getWorld().getDismountHeight(dismountPos);
                    if (Dismounting.canDismountInBlock(dismountHeight)) {
                        Vec3d vec3d = Vec3d.ofCenter(dismountPos, dismountHeight);
                        if (Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) {
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
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 10.0D)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.1f);
    }

    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        return new Vec3d(0, 0.15, 0);
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
    protected void drop(ServerWorld world, DamageSource source) {
        super.drop(world, source);
        if (!source.isSourceCreativePlayer()) {
            ItemStack stack = PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM.getDefaultStack();
            stack.set(PFMComponents.COLOR_COMPONENT, this.getPFMColor());

            ItemEntity itemEntity = new ItemEntity(getWorld(), this.getX(), this.getY(), this.getZ(), stack);
            this.getWorld().spawnEntity(itemEntity);
        }
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {

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
