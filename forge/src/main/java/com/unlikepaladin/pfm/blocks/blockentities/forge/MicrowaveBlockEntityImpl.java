package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.forge.MicrowaveUpdatePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class MicrowaveBlockEntityImpl  extends MicrowaveBlockEntity {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        BlockPos pos = microwaveBlockEntity.getPos();
        WorldChunk chunk = Objects.requireNonNull(microwaveBlockEntity.getWorld()).getWorldChunk(pos);
        NetworkRegistryForge.PFM_SERVER_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MicrowaveUpdatePacket(pos, active));
    }

}
