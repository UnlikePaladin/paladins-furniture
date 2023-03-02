package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.TrashcanBlock;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class TrashcanBlockEntity extends LootableContainerBlockEntity {
    public TrashcanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TRASHCAN_BLOCK_ENTITY, pos, state);
    }
    public TrashcanBlockEntity(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity, BlockPos pos, BlockState state) {
        super(trashcanBlockEntity, pos, state);
    }

    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    protected void onContainerOpen(BlockState state) {
        if (state.getBlock() instanceof TrashcanBlock){
            this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
            this.setOpen(state, true);
        }
    }

    protected void onContainerClose(BlockState state) {
        if (state.getBlock() instanceof TrashcanBlock) {
            this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
            this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        if (state.contains(Properties.OPEN))
            this.world.setBlockState(this.getPos(), state.with(Properties.OPEN, open), 3);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        this.world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText( "container.pfm.trashcan");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new TrashcanScreenHandler(this, syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return 9;
    }


    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerOpen(this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerClose(this.getCachedState());
        }
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }
        return nbt;
    }

    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = super.removeStack(slot);
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = super.removeStack(slot, amount);
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
        return stack;
    }

    @Override
    public void clear() {
        super.clear();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }
}
