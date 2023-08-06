package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.PlateBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlateBlockImpl {
    public static void eatSandwich(ItemStack stack, World world, PlayerEntity player) {
        player.sendMessage(Text.of("You just ate a Sandwich in Forge?"), false);
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntityImpl(pos, state);
    }
}
