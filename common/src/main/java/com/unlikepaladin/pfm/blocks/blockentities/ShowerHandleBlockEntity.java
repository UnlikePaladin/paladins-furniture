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
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity() {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY);
        this.showerOffset = null;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.showerOffset != null) {
            NbtLong showerHeadPos = NbtLong.of(this.showerOffset.asLong());
            nbt.put("showerHead", showerHeadPos);
        }
        return nbt;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
        if(nbt.contains("showerHead", 4)){
            this.showerOffset = BlockPos.fromLong(nbt.getLong("showerHead"));
        }
    }

    public void setState(boolean open)
    {
        if (this.showerOffset != null) {
            BlockPos showerHeadPos = this.pos.subtract(this.showerOffset);
            if(this.world.getBlockEntity(showerHeadPos) != null) {

                BlockState state = world.getBlockState(showerHeadPos);
                ((ShowerHeadBlockEntity)world.getBlockEntity(showerHeadPos)).setOpen(open);

                world.updateListeners(showerHeadPos, state, state, 3);
            } else if (this.world.getBlockEntity(showerHeadPos) == null) {
                this.showerOffset = null;
            }
        }
    }
}
