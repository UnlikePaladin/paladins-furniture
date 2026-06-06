package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OvenBlockEntityImpl extends OvenBlockEntity implements ExtendedScreenHandlerFactory {
    public OvenBlockEntityImpl(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OvenBlockEntityImpl(BlockPos blockPos, BlockState state) {
        super(blockPos, state);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        return OvenBlockEntityImpl::new;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(this.getBlockPos());
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);
        if (screenHandlerFactory != null) {
            // With this call the server will request the client to open the appropriate Screenhandler
            player.openMenu(screenHandlerFactory);
        }
    }
}
