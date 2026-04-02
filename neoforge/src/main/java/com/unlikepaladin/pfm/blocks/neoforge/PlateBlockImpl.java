package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.PlateBlockEntityImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlateBlockImpl {
    public static void eatSandwich(ItemStack stack, Level world, Player player) {
        player.displayClientMessage(Component.literal("You just ate a Sandwich in NeoForge?"), false);
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntityImpl(pos, state);
    }
}
