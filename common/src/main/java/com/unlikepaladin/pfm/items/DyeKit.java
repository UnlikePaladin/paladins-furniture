package com.unlikepaladin.pfm.items;

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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

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
            if(blockState.getBlock() instanceof DyeableFurniture) {
                if (stack.getItem() instanceof DyeKit) {
                    world.playSound(null, blockPos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    String newBlock= blockState.getBlock().toString();
                    newBlock = newBlock.replace(((DyeableFurniture) blockState.getBlock()).getPFMColor().toString(), getColor().toString()).replace("block.pfm.","").replace("Block{", "").replace("}", "");
                    BlockState blockState1 = Registry.BLOCK.get(new Identifier(newBlock)).getStateWithProperties(blockState);
                    world.setBlockState(blockPos, blockState1, 3);
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
