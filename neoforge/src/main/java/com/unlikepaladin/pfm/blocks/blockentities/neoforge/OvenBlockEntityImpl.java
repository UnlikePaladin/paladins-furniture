package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.OvenBlockEntityBalm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OvenBlockEntityImpl extends OvenBlockEntity {

    public OvenBlockEntityImpl(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OvenBlockEntityImpl(BlockPos blockPos, BlockState state) {
        super(blockPos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    @Override
    public void handleUpdateTag(ValueInput tag) {
        super.handleUpdateTag(tag);
        this.loadAdditional(tag);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput data) {
        super.onDataPacket(connection, data);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? OvenBlockEntityBalm::new : OvenBlockEntityImpl::new;
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof OvenBlockEntity ovenBlockEntity){
            MenuProvider namedScreenHandlerFactory = state.getMenuProvider(world, pos);
            player.openMenu(namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(ovenBlockEntity.getBlockPos());
            } );
        }
    }
}
