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
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class OvenBlockEntityImpl extends OvenBlockEntity {

    public OvenBlockEntityImpl(BlockEntityType<? extends OvenBlockEntity> type) {
        super(type);
    }

    public OvenBlockEntityImpl() {
        super();
    }

    public static Supplier<? extends OvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? OvenBlockEntityBalm::new : OvenBlockEntityImpl::new;
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof OvenBlockEntity){
            OvenBlockEntity ovenBlockEntity = (OvenBlockEntity) world.getBlockEntity(pos);
            MenuProvider namedScreenHandlerFactory = state.getMenuProvider(world, pos);
            NetworkHooks.openGui((ServerPlayer) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(ovenBlockEntity.getBlockPos());
            } );
        }
    }
}
