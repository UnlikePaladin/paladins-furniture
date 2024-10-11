package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY, pos, state);
        this.showerOffset = null;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (this.showerOffset != null) {
            NbtLong showerHeadPos = NbtLong.of(this.showerOffset.asLong());
            nbt.put("showerHead", showerHeadPos);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if(nbt.contains("showerHead", NbtElement.LONG_TYPE)){
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

                world.updateListeners(showerHeadPos, state, state, Block.NOTIFY_LISTENERS);
            } else if (this.world.getBlockEntity(showerHeadPos) == null) {
                this.showerOffset = null;
            }
        }
    }
}
