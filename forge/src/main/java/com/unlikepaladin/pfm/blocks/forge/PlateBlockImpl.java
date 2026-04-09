package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.PlateBlockEntityImpl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PlateBlockImpl {
    public static void eatSandwich(ItemStack stack, Level world, Player player) {
        player.displayClientMessage(Component.nullToEmpty("You just ate a Sandwich in Forge?"), false);
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntityImpl(pos, state);
    }
}
