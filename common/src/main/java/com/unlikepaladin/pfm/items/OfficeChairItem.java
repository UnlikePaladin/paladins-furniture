package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import com.unlikepaladin.pfm.entity.OfficeChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class OfficeChairItem extends Item implements PFMBuiltinItemRendererExtension {
    public OfficeChairItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        DyeColor color = stack.getComponents().getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
        return Component.translatable(String.format("block.pfm.%s_office_chair", color.getSerializedName()));
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);
        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BLOCK;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else {
            Vec3 vec3d = user.getViewVector(1.0F);
            double boxSize = 5.0F;
            List<Entity> list = world.getEntities(user, user.getBoundingBox().expandTowards(vec3d.scale(boxSize)).inflate(1.0F),
                    EntitySelector.NO_SPECTATORS.and(Entity::isPickable));
            if (!list.isEmpty()) {
                Vec3 eyePos = user.getEyePosition();

                for(Entity entity : list) {
                    AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (box.contains(eyePos)) {
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                OfficeChairEntity chair = Entities.OFFICE_CHAIR.create(world, EntitySpawnReason.SPAWN_ITEM_USE);

                DyeColor color = itemStack.getComponents().getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE);

                chair.snapTo(hitResult.getLocation().x, hitResult.getLocation().y+0.1f,
                        hitResult.getLocation().z, user.getYRot(), 0);
                chair.setPersistenceRequired();
                chair.setPFMColor(color);
                chair.setYRot(user.getYRot());
                BlockPos pos = new BlockPos((int) hitResult.getLocation().x, (int) hitResult.getLocation().y, (int) hitResult.getLocation().z);
                world.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!world.isClientSide()) {
                    world.addFreshEntity(chair);
                    world.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
                    if (!user.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }

                user.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);

            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @ExpectPlatform
    public static Item getItemFactory(Properties settings) {
        throw new AssertionError();
    }

}
