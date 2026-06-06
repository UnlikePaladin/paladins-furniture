package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.OvenBlockEntityBalm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class OvenBlockEntityImpl extends OvenBlockEntity {

    public OvenBlockEntityImpl(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OvenBlockEntityImpl(BlockPos blockPos, BlockState state) {
        super(blockPos, state);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? OvenBlockEntityBalm::new : OvenBlockEntityImpl::new;
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof OvenBlockEntity ovenBlockEntity){
            MenuProvider namedScreenHandlerFactory = state.getMenuProvider(world, pos);
            NetworkHooks.openGui((ServerPlayer) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(ovenBlockEntity.getBlockPos());
            } );
        }
    }
}
