package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class OfficeChairItem extends Item implements PFMBuiltinItemRendererExtension {
    public OfficeChairItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        DyeColor color = DyeColor.WHITE;
        if (stack.hasTag()) {
            if (stack.getTag().contains("Color")) {
                color = DyeColor.byName(stack.getTag().getString("Color"), DyeColor.WHITE);
            }
        }
        return String.format("block.pfm.%s_office_chair", color.asString());
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putString("Color", DyeColor.WHITE.asString());
        return stack;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                stack.getOrCreateTag().putString("Color", color.asString());
                stacks.add(stack);
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else {
            Vec3d vec3d = user.getRotationVec(1.0F);
            double boxSize = 5.0F;
            List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(boxSize)).expand(1.0F),
                    EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides));
            if (!list.isEmpty()) {
                Vec3d eyePos = user.getCameraPosVec(1.0f);

                for(Entity entity : list) {
                    Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                    if (box.contains(eyePos)) {
                        return TypedActionResult.pass(itemStack);
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                OfficeChairEntity chair = Entities.OFFICE_CHAIR.create(world);
                DyeColor color = DyeColor.WHITE;
                if (itemStack.hasTag()) {
                    NbtCompound nbt = itemStack.getTag();
                    if (nbt.contains("Color")) {
                        color = DyeColor.byName(nbt.getString("Color"), DyeColor.WHITE);
                    }
                }

                chair.setPersistent();
                chair.refreshPositionAndAngles(hitResult.getPos().x, hitResult.getPos().y+0.1f, hitResult.getPos().z, user.yaw, 0);
                chair.setPFMColor(color);
                chair.yaw = (user.yaw);
                world.playSound(null, new BlockPos(hitResult.getPos()), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!world.isClient) {
                    world.spawnEntity(chair);
                    if (!user.abilities.creativeMode) {
                        itemStack.decrement(1);
                    }
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());

            } else {
                return TypedActionResult.pass(itemStack);
            }
        }
    }

}
