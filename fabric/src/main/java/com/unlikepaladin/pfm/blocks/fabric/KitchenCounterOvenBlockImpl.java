package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.CounterOvenBlockEntityImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.CounterOvenBlockEntityBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntityImpl::new;
    }

    public static void openMenuScreen(Level world, BlockPos pos, Player player) {
        MenuProvider screenHandlerFactory = world.getBlockState(pos).getMenuProvider(world, pos);
        if (screenHandlerFactory != null) {
            // With this call the server will request the client to open the appropriate Screenhandler
            player.openMenu(screenHandlerFactory);
            player.awardStat(Statistics.STOVE_OPENED);
        }
    }
}
