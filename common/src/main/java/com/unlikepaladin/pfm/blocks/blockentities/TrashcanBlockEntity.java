package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.TrashcanBlock;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;

import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TrashcanBlockEntity extends RandomizableContainerBlockEntity {
    public TrashcanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TRASHCAN_BLOCK_ENTITY, pos, state);
    }
    public TrashcanBlockEntity(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity, BlockPos pos, BlockState state) {
        super(trashcanBlockEntity, pos, state);
    }

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

    protected void onContainerOpen(BlockState state) {
        if (state.getBlock() instanceof TrashcanBlock){
            this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            this.setOpen(state, true);
        }
    }

    protected void onClose(BlockState state) {
        if (state.getBlock() instanceof TrashcanBlock) {
            this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        if (state.hasProperty(BlockStateProperties.OPEN))
            this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        this.level.playSound(null, getBlockPos(), soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable( "container.pfm.trashcan");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new TrashcanScreenHandler(this, containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }


    @Override
    public void startOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
            this.onContainerOpen(this.getBlockState());
        }
    }

    @Override
    public void stopOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
            this.onClose(this.getBlockState());
        }
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(view)) {
            ContainerHelper.loadAllItems(view, this.inventory);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        if (!this.trySaveLootTable(view)) {
            ContainerHelper.saveAllItems(view, this.inventory);
        }
    }

    public NonNullList<ItemStack> getContainer() {
        return this.inventory;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = super.removeItemNoUpdate(slot);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        return stack;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        return stack;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends TrashcanBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
