package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.CounterOvenBlockEntityBalm;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KitchenCounterOvenBlockImpl {
    public static BlockEntityType.BlockEntityFactory<? extends CounterOvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? CounterOvenBlockEntityBalm::new : CounterOvenBlockEntity::new;
    }

    public static void openMenuScreen(World world, BlockPos pos, PlayerEntity player) {
        NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(pos).createScreenHandlerFactory(world, pos);
        if (screenHandlerFactory != null && player instanceof ServerPlayerEntity) {
            // With this call the server will request the client to open the appropriate Screenhandler
            ((ServerPlayerEntity)player).openMenu(screenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(pos);
            } );
            player.incrementStat(Statistics.STOVE_OPENED);
        }
    }
}
