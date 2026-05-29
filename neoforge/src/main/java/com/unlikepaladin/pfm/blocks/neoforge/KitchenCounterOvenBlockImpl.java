package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.CounterOvenBlockEntityBalm;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntitySupplier<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntity::new;
    }

    public static void openMenuScreen(Level world, BlockPos pos, Player player) {
        MenuProvider screenHandlerFactory = world.getBlockState(pos).getMenuProvider(world, pos);
        if (screenHandlerFactory != null && player instanceof ServerPlayer) {
            // With this call the server will request the client to open the appropriate Screenhandler
            ((ServerPlayer)player).openMenu(screenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(pos);
            } );
            player.awardStat(Statistics.STOVE_OPENED);
        }
    }
}
