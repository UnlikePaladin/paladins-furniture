package com.unlikepaladin.pfm.entity;

import com.google.common.collect.UnmodifiableIterator;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicChair;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityChair extends MobEntity {



    public EntityChair(EntityType<? extends EntityChair> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    @Override
    public void tick() {
        if (!this.hasPassengers())
            this.discard();
        else if (this.world.getBlockState(this.getBlockPos()).getBlock() instanceof BasicChair)
            super.tick();
        else {
            this.removeAllPassengers();
           this.discard();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.setVelocity(Vec3d.ZERO);
    }

    @Override
    public boolean isAlive() {
        return !this.isRemoved();
    }


    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {

        return super.interactAt(player, hitPos, hand);
    }

    public static DefaultAttributeContainer getAttributeContainer() {
        return EntityChair.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 0).build();

    }


    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        } else {
            int[][] is = Dismounting.getDismountOffsets(direction);
            BlockPos blockPos = this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            UnmodifiableIterator var6 = passenger.getPoses().iterator();

            while(var6.hasNext()) {
                EntityPose entityPose = (EntityPose)var6.next();
                Box box = passenger.getBoundingBox(entityPose);
                int[][] var9 = is;
                int var10 = is.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    int[] js = var9[var11];
                    mutable.set(blockPos.getX() + js[0], blockPos.getY() + 0.3, blockPos.getZ() + js[1]);
                    double d = this.world.getDismountHeight(mutable);
                    if (Dismounting.canDismountInBlock(d)) {
                        Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                        if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
                            passenger.setPose(entityPose);
                            return vec3d;
                        }
                    }
                }
            }

            return super.updatePassengerForDismount(passenger);
        }
    }



}
