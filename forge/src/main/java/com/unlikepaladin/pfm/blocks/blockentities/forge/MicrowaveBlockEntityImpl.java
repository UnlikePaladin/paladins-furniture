package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.forge.MicrowaveUpdatePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.Collection;
import java.util.Objects;

public class MicrowaveBlockEntityImpl  extends MicrowaveBlockEntity {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    //TODO: Implement Active on Client
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        BlockPos pos = microwaveBlockEntity.getPos();
        WorldChunk chunk = Objects.requireNonNull(microwaveBlockEntity.getWorld()).getWorldChunk(pos);
        
        PaladinFurnitureMod.GENERAL_LOGGER.warn("Sending Microwave Update to all players in chunk, this is really bad " + chunk);
        NetworkRegistryForge.PFM_SERVER_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MicrowaveUpdatePacket(pos, active));
    }
}
