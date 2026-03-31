package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.PlateBlockEntityImpl;
import io.github.foundationgames.sandwichable.items.SandwichBlockItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PlateBlockImpl {
    public static void eatSandwich(ItemStack stack, Level world, Player player) {
        SandwichBlockItem item = (SandwichBlockItem)stack.getItem();
        item.finishUsingItem(stack, world, player);
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntityImpl(pos, state);
    }
}
