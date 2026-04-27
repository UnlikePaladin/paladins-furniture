package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.TrashcanBlockEntityImpl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.level.Level;

public class InnerTrashcanBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new TrashcanBlockEntityImpl();
    }


    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);
        if (screenHandlerFactory != null) {
            // With this call the server will request the client to open the appropriate Screenhandler
            player.openMenu(screenHandlerFactory);
        }
    }
}
