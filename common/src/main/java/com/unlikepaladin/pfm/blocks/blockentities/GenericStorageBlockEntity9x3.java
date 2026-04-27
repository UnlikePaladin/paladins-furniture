package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenDrawerBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;


public class GenericStorageBlockEntity9x3 extends RandomizableContainerBlockEntity {
    public GenericStorageBlockEntity9x3() {
        super(BlockEntities.DRAWER_BLOCK_ENTITY);
    }

    private NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);

    protected void onOpen(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof KitchenDrawerBlock || state.getBlock() instanceof KitchenCabinetBlock || state.getBlock() instanceof ClassicNightstandBlock){
            GenericStorageBlockEntity9x3.this.playSound(state, SoundEvents.BARREL_OPEN);
            GenericStorageBlockEntity9x3.this.setOpen(state, true);
        }
    }

    protected void onClose(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof KitchenDrawerBlock || state.getBlock() instanceof KitchenCabinetBlock || state.getBlock() instanceof ClassicNightstandBlock) {
            GenericStorageBlockEntity9x3.this.playSound(state, SoundEvents.BARREL_CLOSE);
            GenericStorageBlockEntity9x3.this.setOpen(state, false);
        }
    }


    @Override
    public int getContainerSize() {
        return 27;
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

    protected Component getDefaultName() {
        if (this.getBlockState().getBlock() instanceof KitchenWallDrawerBlock)
            return new TranslatableComponent("container.pfm.kitchen_cabinet");
        else if (this.getBlockState().getBlock() instanceof KitchenDrawerBlock)
            return new TranslatableComponent("container.pfm.drawer");
        else if (this.getBlockState().getBlock() instanceof ClassicNightstandBlock)
            return new TranslatableComponent("container.pfm.nightstand");
        else
            return new TranslatableComponent("container.pfm.cabinet");
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    @Override
    protected ChestMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.threeRows(containerId, playerInventory, this);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @ExpectPlatform
    public static Supplier<? extends GenericStorageBlockEntity9x3> getFactory() {
        throw new AssertionError();
    }
}

