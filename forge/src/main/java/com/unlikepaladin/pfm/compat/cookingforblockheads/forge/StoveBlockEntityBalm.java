package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.InventoryHandler;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.EnergyStorageModifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.Vec3i;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoveBlockEntityBalm extends OvenBlockEntityBalm implements MenuProvider {
    private final NonNullList<ItemStack> tools = NonNullList.withSize(4, ItemStack.EMPTY);
    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(10000) {
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }

            return super.receiveEnergy(maxReceive, simulate);
        }

        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }

            return super.extractEnergy(maxExtract, simulate);
        }
    };
    private ItemStack getMappedStack(int slot) {
        if (slot >= 0 && slot < 3) {
            return StoveBlockEntityBalm.this.getItem(slot);
        } else if (slot == 3) {
            return StoveBlockEntityBalm.this.getItem(15);
        } else if (slot >= 4 && slot < 7) {
            return StoveBlockEntityBalm.this.getItem(slot + 8);
        } else if (slot >= 7 && slot < 16) {
            return StoveBlockEntityBalm.this.getItem(slot - 4);
        } else if (slot >= 16 && slot < 20) {
            return StoveBlockEntityBalm.this.tools.get(slot - 16);
        }
        return ItemStack.EMPTY;
    }

    private void setMappedStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < 3) {
            StoveBlockEntityBalm.this.setItem(slot, stack);
        } else if (slot == 3) {
            StoveBlockEntityBalm.this.setItem(15, stack);
        } else if (slot >= 4 && slot < 7) {
            StoveBlockEntityBalm.this.setItem(slot + 8, stack);
        } else if (slot >= 7 && slot < 16) {
            StoveBlockEntityBalm.this.setItem(slot - 4, stack);
        } else if (slot >= 16 && slot < 20) {
            StoveBlockEntityBalm.this.tools.set(slot - 16, stack);
        }
        StoveBlockEntityBalm.this.setChanged();
    }

    private final IItemHandlerModifiable mappedContainer = new IItemHandlerModifiable() {
        @Override
        public int getSlots() {
            return 20;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return StoveBlockEntityBalm.this.getMappedStack(slot);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            StoveBlockEntityBalm.this.setMappedStack(slot, stack);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (!isItemValid(slot, stack)) {
                return stack;
            }
            ItemStack existing = StoveBlockEntityBalm.this.getMappedStack(slot);
            int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }
                limit -= existing.getCount();
            }
            if (limit <= 0) {
                return stack;
            }
            boolean reachedLimit = stack.getCount() > limit;
            if (!simulate) {
                if (existing.isEmpty()) {
                    StoveBlockEntityBalm.this.setMappedStack(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                } else {
                    existing.grow(reachedLimit ? limit : stack.getCount());
                    StoveBlockEntityBalm.this.setChanged();
                }
            }
            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount <= 0) {
                return ItemStack.EMPTY;
            }
            ItemStack existing = StoveBlockEntityBalm.this.getMappedStack(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            }
            int toExtract = Math.min(amount, existing.getCount());
            if (existing.getCount() <= toExtract) {
                if (!simulate) {
                    StoveBlockEntityBalm.this.setMappedStack(slot, ItemStack.EMPTY);
                }
                return existing.copy();
            } else {
                if (!simulate) {
                    StoveBlockEntityBalm.this.setMappedStack(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                }
                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot >= 0 && slot < 3) {
                return !StoveBlockEntityBalm.this.getSmeltingResult(stack).isEmpty();
            } else if (slot == 3) {
                return StoveBlockEntityBalm.this.isItemFuel(stack);
            }
            return true;
        }
    };

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int id) {
            if (id == 0) {
                return StoveBlockEntityBalm.this.furnaceBurnTime;
            } else if (id == 1) {
                return StoveBlockEntityBalm.this.currentItemBurnTime;
            } else if (id >= 2 && id <= 10) {
                return StoveBlockEntityBalm.this.slotCookTime[id - 2];
            }
            return 0;
        }

        @Override
        public void set(int id, int value) {
            if (id == 0) {
                StoveBlockEntityBalm.this.furnaceBurnTime = value;
            } else if (id == 1) {
                StoveBlockEntityBalm.this.currentItemBurnTime = value;
            } else if (id >= 2 && id <= 10) {
                StoveBlockEntityBalm.this.slotCookTime[id - 2] = value;
            }
        }

        @Override
        public int getCount() {
            return 11;
        }
    };

    private final RangedWrapper inputContainer;
    private final RangedWrapper fuelContainer;
    private final RangedWrapper outputContainer;
    private final RangedWrapper processingContainer;
    private final RangedWrapper toolsContainer;
    private final DefaultKitchenItemProvider itemProvider;
    private Component customName;
    private boolean isFirstTick = true;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private final Container singleSlotRecipeWrapper = new SimpleContainer(1);
    private final LazyOptional<IItemHandler> itemHandlerCap;
    private final LazyOptional<IItemHandler> itemHandlerInputCap;
    private final LazyOptional<IItemHandler> itemHandlerFuelCap;
    private final LazyOptional<IItemHandler> itemHandlerOutputCap;
    private final LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap;
    private final LazyOptional<IEnergyStorage> energyStorageCap;
    private final LazyOptional<IKitchenItemProvider> itemProviderCap;

    public StoveBlockEntityBalm() {
        super(BlockEntities.STOVE_BLOCK_ENTITY);
        this.inputContainer = new RangedWrapper(this.mappedContainer, 0, 3);
        this.fuelContainer = new RangedWrapper(this.mappedContainer, 3, 4);
        this.outputContainer = new RangedWrapper(this.mappedContainer, 4, 7);
        this.processingContainer = new RangedWrapper(this.mappedContainer, 7, 16);
        this.toolsContainer = new RangedWrapper(this.mappedContainer, 16, 20);
        this.itemProvider = new KitchenItemProvider(new CombinedInvWrapper(this.toolsContainer, this.outputContainer));
        this.itemHandlerCap = LazyOptional.of(() -> this.mappedContainer);
        this.itemHandlerInputCap = LazyOptional.of(() -> this.inputContainer);
        this.itemHandlerFuelCap = LazyOptional.of(() -> this.fuelContainer);
        this.itemHandlerOutputCap = LazyOptional.of(() -> this.outputContainer);
        this.energyStorageCap = LazyOptional.of(() -> this.energyStorage);
        this.smeltingProviderCap = LazyOptional.of(() -> this);
        this.itemProviderCap = LazyOptional.of(() -> this.itemProvider);
    }

    @Override
    public IItemHandler getContainer() {
        return this.mappedContainer;
    }

    public static void clientTick() {
    }

    public void serverTick() {
        if (this.isFirstTick && getBlockState().getBlock() instanceof StoveBlock) {
            this.facing = getBlockState().getValue(StoveBlock.FACING);
            this.isFirstTick = false;
        }

        int burnPotential = 200 - this.furnaceBurnTime;
        if (this.hasPowerUpgrade && burnPotential > 0 && this.shouldConsumeFuel()) {
            this.furnaceBurnTime += this.energyStorage.extractEnergy(burnPotential, false);
        }

        super.serverTick();
    }

    @Override
    public AbstractCookingRecipe getSmokingRecipe(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }
        this.singleSlotRecipeWrapper.setItem(0, itemStack);
        AbstractCookingRecipe recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this.singleSlotRecipeWrapper, this.level).orElse(null);
        if (recipe != null) {
            ItemStack result = recipe.getResultItem();
            if (!result.isEmpty() && (result.getItem().isEdible() || CookingRegistry.isNonFoodRecipe(result))) {
                return recipe;
            }
        }
        return null;
    }

    public ItemStack getSmeltingResult(ItemStack itemStack) {
        AbstractCookingRecipe recipe = this.getSmokingRecipe(itemStack);
        return recipe != null ? recipe.getResultItem() : ItemStack.EMPTY;
    }

    public boolean isItemFuel(ItemStack itemStack) {
        if (CookingForBlockheadsConfig.COMMON.ovenRequiresCookingOil.get()) {
            return itemStack.getItem().getTags().contains(Compat.cookingOilTag);
        } else {
            return getBurnDuration(itemStack) > 0;
        }
    }

    @Override
    public void load(BlockState state, CompoundTag tagCompound) {
        // NBT Migration
        if (tagCompound.contains("ItemHandler")) {
            ItemStackHandler oldContainer = new ItemStackHandler(20);
            oldContainer.deserializeNBT(tagCompound.getCompound("ItemHandler"));

            // Map old container slots to new standard slots
            for (int i = 0; i < 3; i++) {
                this.setItem(i, oldContainer.getStackInSlot(i));
            }
            this.setItem(15, oldContainer.getStackInSlot(3));
            for (int i = 0; i < 3; i++) {
                this.setItem(12 + i, oldContainer.getStackInSlot(4 + i));
            }
            for (int i = 0; i < 9; i++) {
                this.setItem(3 + i, oldContainer.getStackInSlot(7 + i));
            }
            for (int i = 0; i < 4; i++) {
                this.tools.set(i, oldContainer.getStackInSlot(16 + i));
            }
        }

        super.load(state, tagCompound);

        this.tools.clear();
        if (tagCompound.contains("Tools")) {
            ContainerHelper.loadAllItems(tagCompound.getCompound("Tools"), this.tools);
        }

        this.hasPowerUpgrade = tagCompound.getBoolean("HasPowerUpgrade");
        this.energyStorage.setEnergyStored(tagCompound.getInt("EnergyStored"));
        if (tagCompound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);

        CompoundTag toolsTag = new CompoundTag();
        ContainerHelper.saveAllItems(toolsTag, this.tools);
        tagCompound.put("Tools", toolsTag);

        tagCompound.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        tagCompound.putInt("EnergyStored", this.energyStorage.getEnergyStored());
        if (this.customName != null) {
            tagCompound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        return tagCompound;
    }

    public boolean hasPowerUpgrade() {
        return this.hasPowerUpgrade;
    }

    public void setHasPowerUpgrade(boolean hasPowerUpgrade) {
        this.hasPowerUpgrade = hasPowerUpgrade;
        BlockState state = this.level.getBlockState(this.worldPosition);
        this.level.setBlockAndUpdate(this.worldPosition, state.setValue(OvenBlock.POWERED, hasPowerUpgrade));
        this.setChanged();
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        return this.currentItemBurnTime == 0 && this.furnaceBurnTime > 0 ? 1.0F : (float) this.furnaceBurnTime / (float) this.currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        if (i < 0 || i >= this.slotCookTime.length) return 0.0F;
        return (float) this.slotCookTime[i] / (float) this.slotCookTimeTotal[i];
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        return ItemHandlerHelper.insertItemStacked(this.inputContainer, itemStack, false);
    }

    public ItemStack getToolItem(int i) {
        return this.tools.get(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        this.tools.set(i, itemStack);
        this.setChanged();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return this.itemHandlerCap.cast();
            }

            if (!(Boolean)CookingForBlockheadsConfig.COMMON.disallowOvenAutomation.get()) {
                switch (facing) {
                    case UP:
                        return this.itemHandlerInputCap.cast();
                    case DOWN:
                        return this.itemHandlerOutputCap.cast();
                    default:
                        return this.itemHandlerFuelCap.cast();
                }
            }
        }

        if (this.hasPowerUpgrade && capability == CapabilityEnergy.ENERGY) {
            return this.energyStorageCap.cast();
        } else if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
            return this.itemProviderCap.cast();
        } else {
            return capability == CapabilityKitchenSmeltingProvider.CAPABILITY ? this.smeltingProviderCap.cast() : super.getCapability(capability, facing);
        }
    }

    public IItemHandler getInputContainer() {
        return this.inputContainer;
    }

    public IItemHandler getFuelContainer() {
        return this.fuelContainer;
    }

    public Direction getFacing() {
        return this.facing == null ? Direction.NORTH : this.facing;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new StoveScreenHandlerBalm(i, playerInventory, this);
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    @Override
    public void setCustomName(Component customName) {
        this.customName = customName;
        this.setChanged();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.customName;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public Component getDefaultName() {
        return new TranslatableComponent("container.cookingforblockheads.oven");
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return this.energyStorage;
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 13, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        CompoundTag toolsTag = new CompoundTag();
        ContainerHelper.saveAllItems(toolsTag, this.tools);
        nbt.put("Tools", toolsTag);
        ContainerHelper.saveAllItems(nbt, this.items);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag) {
        this.load(state, tag);
        super.handleUpdateTag(state, tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.load(getBlockState(), pkt.getTag());
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new StoveScreenHandlerBalm(containerId, playerInventory, this);
    }

    protected void onContainerOpen(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            this.setOpen(state, true);
        }
    }

    protected void onContainerClose(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerOpen(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        double d = (double) this.worldPosition.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double e = (double) this.worldPosition.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double f = (double) this.worldPosition.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            clientTick();
        } else {
            serverTick();
        }
    }
}
