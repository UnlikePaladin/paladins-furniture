package com.unlikepaladin.pfm.blockentities;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;


import java.util.UUID;


public class PlayerChairBlockEntity extends SkullBlockEntity {
    private BlockState cachedState;



    public PlayerChairBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        this.cachedState = state;
    }

    public BlockRenderType getRenderType(BlockState state) {
        // When inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.INVISIBLE;
    }
    private GameProfile owner;
    public BlockState getCachedState() {
        return this.cachedState;
    }


    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }
     @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) {
            NbtCompound nbtCompound = new NbtCompound();
            NbtHelper.writeGameProfile(nbtCompound, this.owner);
            nbt.put("SkullOwner", nbtCompound);
            System.out.println("Writing NBT: " + nbt );
        }
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        System.out.println("Reading NBT");
        if (nbt.contains("SkullOwner", 10)) {
            this.setOwner(NbtHelper.toGameProfile(nbt.getCompound("SkullOwner")));
            System.out.println("NBT Contains SkullOwner: " + nbt.getCompound("SkullOwner"));
        } else if (nbt.contains("ExtraType", 8)) {
            String string = nbt.getString("ExtraType");
            if (!StringHelper.isEmpty(string)) {
                System.out.println("Contains Extra Type: " + string);
                this.setOwner(new GameProfile((UUID)null, string));
            }
        }
    }
    public void setOwner(@Nullable GameProfile owner) {
        synchronized(this) {
            System.out.println("Setting Owner");
            this.owner = owner;
            System.out.println("setOwner owner: " + owner);
        }
        this.loadOwnerProperties();
    }
    public void loadOwnerProperties() {
        System.out.println("Loading Properties");
        loadProperties(this.owner, (owner) -> {
            this.owner = owner;
            System.out.println("from Load properties: " + owner);
            this.markDirty();

        });
    }


    @Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

}
