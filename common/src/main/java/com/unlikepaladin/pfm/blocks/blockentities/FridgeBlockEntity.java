package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;


public class FridgeBlockEntity extends RandomizableContainerBlockEntity {
    @Override
    public int getContainerSize() {
            return 54;
        }

    public FridgeBlockEntity() {
        super(BlockEntities.FRIDGE_BLOCK_ENTITY);
    }
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);

    protected void onOpen(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof FridgeBlock) {
            FridgeBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            FridgeBlockEntity.this.setOpen(state, true);
        }
    }

    protected void onClose(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof FridgeBlock) {
            FridgeBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            FridgeBlockEntity.this.setOpen(state, false);
        }
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
        public void startOpen(Player player) {
            if (!this.remove && !player.isSpectator()) {
                this.onOpen(this.getLevel(), this.getBlockPos(), this.getBlockState());
            }
        }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.inventory);
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.inventory);
        }
        return nbt;
    }

    @Override
    protected Component getDefaultName() {
        String blockname = this.getBlockState().getBlock().getDescriptionId();
        blockname = blockname.replace("block.pfm", "");
        return new TranslatableComponent("container.pfm" + blockname);
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(FridgeBlock.OPEN, open), 2);
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
    public static Supplier<? extends FridgeBlockEntity> getFactory() {
        throw new AssertionError();
    }
}

