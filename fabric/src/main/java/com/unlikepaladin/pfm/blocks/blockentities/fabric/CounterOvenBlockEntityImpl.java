package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class CounterOvenBlockEntityImpl extends CounterOvenBlockEntity implements ExtendedScreenHandlerFactory<StoveScreenHandler.StoveData> {
    public CounterOvenBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public StoveScreenHandler.StoveData getScreenOpeningData(ServerPlayer player) {
        return super.getScreenOpeningData(player);
    }
}
