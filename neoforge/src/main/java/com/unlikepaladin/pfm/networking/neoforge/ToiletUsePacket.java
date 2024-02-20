package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ToiletUsePacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_use");
    private final BlockPos blockPos;
    public ToiletUsePacket(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }

    public ToiletUsePacket(BlockPos pos) {
        this.blockPos = pos;
    }

    public static void handle(ToiletUsePacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            Optional<PlayerEntity> optionalPlayerEntity = ctx.player();
            if (optionalPlayerEntity.isPresent()) {
                PlayerEntity player = optionalPlayerEntity.get();
                BlockPos blockPos = msg.blockPos;
                World world = player.getEntityWorld();
                if (world.isChunkLoaded(blockPos)) {
                    world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                }
                else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
