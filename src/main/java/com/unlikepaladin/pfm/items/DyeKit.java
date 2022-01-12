package com.unlikepaladin.pfm.items;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.DyeableFurniture;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.unlikepaladin.pfm.blocks.DyeableFurniture.COLORID;
import static com.unlikepaladin.pfm.blocks.DyeableFurniture.DYED;

public class DyeKit extends Item {
    private final DyeColor color;

    public DyeKit(Settings settings, DyeColor color) {
        super(settings);
        this.color = color;
    }

    public DyeColor getColor() {
        return this.color;
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack stack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if (playerEntity.isSneaking()) {
            System.out.println("sneaking");
            System.out.println(stack.getItem());
            System.out.println(stack.getItem() instanceof DyeKit);
            if(blockState.getBlock() instanceof DyeableFurniture) {
                if (stack.getItem() instanceof DyeKit && (blockState.get(COLORID) != ((DyeKit) stack.getItem()).getColor())) {
                    System.out.println("kit");

                    world.playSound(null, blockPos, PaladinFurnitureMod.FURNITURE_DYED_EVENT, SoundCategory.BLOCKS, 0.3f, 1f);
                    world.setBlockState(blockPos, blockState.with(COLORID, ((DyeKit) stack.getItem()).getColor()).with(DYED, true), 3);
                    stack.decrement(1);
                    return ActionResult.CONSUME;
                }
            }
        }
        return ActionResult.PASS;
    }


    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof SheepEntity) {
            SheepEntity sheepEntity = (SheepEntity)entity;
            if (sheepEntity.isAlive() && !sheepEntity.isSheared() && sheepEntity.getColor() != ((DyeKit) stack.getItem()).getColor()) {
                sheepEntity.world.playSoundFromEntity(user, sheepEntity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!user.world.isClient) {
                    sheepEntity.setColor(this.color);
                    stack.decrement(1);
                }

                return ActionResult.success(user.world.isClient);
            }
        }

        return ActionResult.PASS;
    }
}
