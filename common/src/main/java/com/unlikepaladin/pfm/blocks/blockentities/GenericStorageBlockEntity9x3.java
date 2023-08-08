package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.ClassicNightstandBlock;
import com.unlikepaladin.pfm.blocks.KitchenCabinetBlock;
import com.unlikepaladin.pfm.blocks.KitchenDrawerBlock;
import com.unlikepaladin.pfm.blocks.KitchenWallDrawerBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.function.Supplier;


public class GenericStorageBlockEntity9x3 extends LootableContainerBlockEntity {
    public GenericStorageBlockEntity9x3() {
        super(BlockEntities.DRAWER_BLOCK_ENTITY);
    }

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof KitchenDrawerBlock || state.getBlock() instanceof KitchenCabinetBlock || state.getBlock() instanceof ClassicNightstandBlock){
                GenericStorageBlockEntity9x3.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                GenericStorageBlockEntity9x3.this.setOpen(state, true);
            }
        }

        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof KitchenDrawerBlock || state.getBlock() instanceof KitchenCabinetBlock || state.getBlock() instanceof ClassicNightstandBlock) {
                GenericStorageBlockEntity9x3.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                GenericStorageBlockEntity9x3.this.setOpen(state, false);
            }
        }


    @Override
    public int size() {
        return 27;
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
        public void onOpen(PlayerEntity player) {
            if (!this.removed && !player.isSpectator()) {
                this.onContainerOpen(this.getWorld(), this.getPos(), this.getCachedState());
            }
        }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerClose(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }


    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
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

    String blockname = this.getCachedState().getBlock().getTranslationKey();
    protected Text getContainerName() {
        if (this.getCachedState().getBlock() instanceof KitchenWallDrawerBlock)
            return new TranslatableText("container.pfm.kitchen_cabinet");
        else if (this.getCachedState().getBlock() instanceof KitchenDrawerBlock)
            return new TranslatableText("container.pfm.drawer");
        else if (this.getCachedState().getBlock() instanceof ClassicNightstandBlock)
            return new TranslatableText("container.pfm.nightstand");
        else
            return new TranslatableText("container.pfm.cabinet");
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(Properties.OPEN, open), 3);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(Properties.HORIZONTAL_FACING).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }

    @ExpectPlatform
    public static Supplier<? extends GenericStorageBlockEntity9x3> getFactory() {
        throw new AssertionError();
    }
}

