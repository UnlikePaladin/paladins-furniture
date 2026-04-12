package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;

import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;


public class FridgeBlockEntity extends RandomizableContainerBlockEntity {
    @Override
    public int getContainerSize() {
            return 54;
        }

    public FridgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FRIDGE_BLOCK_ENTITY, pos, state);
    }
    private NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {


        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FridgeBlock) {
                FridgeBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
                FridgeBlockEntity.this.setOpen(state, true);
            }
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FridgeBlock) {
                FridgeBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
                FridgeBlockEntity.this.setOpen(state, false);
            }
        }


        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu) {
                Container inventory = ((ChestMenu)player.containerMenu).getContainer();
                return inventory == FridgeBlockEntity.this;
            }
            return false;
        }
    };

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
        public void startOpen(Player player) {
            if (!this.remove && !player.isSpectator()) {
                this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
            }
        }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
        }
    }
    String blockname = this.getBlockState().getBlock().getDescriptionId();

    protected Component getDefaultName() {
        blockname = blockname.replace("block.pfm", "");
        return Component.translatable("container.pfm" + blockname);
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(FridgeBlock.OPEN, open), Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
    }

    @Override
    protected ChestMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.sixRows(containerId, playerInventory, this);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(FridgeBlock.FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends FridgeBlockEntity> getFactory() {
        throw new AssertionError();
    }
}

