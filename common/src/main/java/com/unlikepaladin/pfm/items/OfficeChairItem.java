package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.NonNullList;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class OfficeChairItem extends Item implements PFMBuiltinItemRendererExtension {
    public OfficeChairItem(Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        DyeColor color = DyeColor.WHITE;
        if (stack.hasTag()) {
            if (stack.getTag().contains("Color")) {
                color = DyeColor.byName(stack.getTag().getString("Color"), DyeColor.WHITE);
            }
        }
        return String.format("block.pfm.%s_office_chair", color.getSerializedName());
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putString("Color", DyeColor.WHITE.getSerializedName());
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (DyeColor color : DyeColor.values()) {
                ItemStack stack = new ItemStack(this);
                stack.getOrCreateTag().putString("Color", color.getSerializedName());
                stacks.add(stack);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            Vec3 vec3d = user.getViewVector(1.0F);
            double boxSize = 5.0F;
            List<Entity> list = world.getEntities(user, user.getBoundingBox().expandTowards(vec3d.scale(boxSize)).inflate(1.0F),
                    EntitySelector.NO_SPECTATORS.and(Entity::isPickable));
            if (!list.isEmpty()) {
                Vec3 eyePos = user.getEyePosition(1.0f);

                for(Entity entity : list) {
                    AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (box.contains(eyePos)) {
                        return InteractionResultHolder.pass(itemStack);
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                OfficeChairEntity chair = Entities.OFFICE_CHAIR.create(world);
                DyeColor color = DyeColor.WHITE;
                if (itemStack.hasTag()) {
                    CompoundTag nbt = itemStack.getTag();
                    if (nbt.contains("Color")) {
                        color = DyeColor.byName(nbt.getString("Color"), DyeColor.WHITE);
                    }
                }

                chair.setPersistenceRequired();
                chair.moveTo(hitResult.getLocation().x, hitResult.getLocation().y+0.1f, hitResult.getLocation().z, user.yRot, 0);
                chair.setPFMColor(color);
                chair.yRot = (user.yRot);
                world.playSound(null, new BlockPos(hitResult.getLocation()), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!world.isClientSide) {
                    world.addFreshEntity(chair);
                    if (!user.isCreative()) {
                        itemStack.shrink(1);
                    }
                }

                user.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());

            } else {
                return InteractionResultHolder.pass(itemStack);
            }
        }
    }

    @ExpectPlatform
    public static Item getItemFactory(Properties settings) {
        throw new AssertionError();
    }

}
