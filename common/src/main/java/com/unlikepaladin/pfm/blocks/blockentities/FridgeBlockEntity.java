package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.FridgeBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;


public class FridgeBlockEntity extends LootableContainerBlockEntity {
    @Override
    public int size() {
            return 54;
        }

    public FridgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FRIDGE_BLOCK_ENTITY, pos, state);
    }
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);
    private final ViewerCountManager stateManager = new ViewerCountManager() {


        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FridgeBlock) {
                FridgeBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
                FridgeBlockEntity.this.setOpen(state, true);
            }
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FridgeBlock) {
                FridgeBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
                FridgeBlockEntity.this.setOpen(state, false);
            }
        }


        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == FridgeBlockEntity.this;
            }
            return false;
        }
    };

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
                this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
            }
        }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
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
    String blockname = this.getCachedState().getBlock().getTranslationKey();

    protected Text getContainerName() {
        blockname = blockname.replace("block.pfm", "");
        return new TranslatableText("container.pfm" + blockname);
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(FridgeBlock.OPEN, open), Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(FridgeBlock.FACING).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }
}

