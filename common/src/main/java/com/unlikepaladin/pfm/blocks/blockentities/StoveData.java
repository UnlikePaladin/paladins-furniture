package com.unlikepaladin.pfm.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record StoveData(BlockPos pos) implements StovePacket {
    public static final StreamCodec<RegistryFriendlyByteBuf, StoveData> PACKET_CODEC = StreamCodec.ofMember(StoveData::write, StoveData::new);

        public StoveData(RegistryFriendlyByteBuf buf) {
            this(buf.readBlockPos());
        }
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }