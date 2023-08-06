package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.PlateBlockEntityImpl;
import io.github.foundationgames.sandwichable.items.SandwichBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlateBlockImpl {
    public static void eatSandwich(ItemStack stack, World world, PlayerEntity player) {
        SandwichBlockItem item = (SandwichBlockItem)stack.getItem();
        item.finishUsing(stack, world, player);
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntityImpl(pos, state);
    }
}
