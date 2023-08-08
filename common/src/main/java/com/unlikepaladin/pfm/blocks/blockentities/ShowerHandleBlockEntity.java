package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.util.math.BlockPos;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerHead;
    public ShowerHandleBlockEntity() {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY);
        this.showerHead = null;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.showerHead != null) {
            NbtLong showerHeadPos = NbtLong.of(this.showerHead.asLong());
            nbt.put("showerHead", showerHeadPos);
        }
        return nbt;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
        if(nbt.contains("showerHead", 4)){
            this.showerHead = BlockPos.fromLong(nbt.getLong("showerHead"));
        }
    }

    public void setState(boolean open)
    {
        if (this.showerHead != null) {
            if(this.world.getBlockEntity(this.showerHead) != null) {

                BlockState state = world.getBlockState(this.showerHead);
                ((ShowerHeadBlockEntity)world.getBlockEntity(this.showerHead)).setOpen(open);

                world.updateListeners(this.showerHead, state, state, 3);
            } else if (this.world.getBlockEntity(this.showerHead) == null) {
                this.showerHead = null;
            }
        }
    }
}
