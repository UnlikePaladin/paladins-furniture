package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.*;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.api.event.OvenCookedEvent;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.IMutableNameable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoveBlockEntityBalm extends BalmBlockEntity implements IKitchenSmeltingProvider, BalmMenuProvider, IMutableNameable, BalmContainerProvider, BalmEnergyStorageProvider {
    private static final int COOK_TIME = 200;
    private final DefaultContainer container = new DefaultContainer(20) {
        public boolean isValid(int slot, ItemStack itemStack) {
            if (slot < 3) {
                return !StoveBlockEntityBalm.this.getSmeltingResult(itemStack).isEmpty();
            } else {
                return slot == 3 ? StoveBlockEntityBalm.isItemFuel(itemStack) : true;
            }
        }

        public void slotChanged(int slot) {
            if (slot >= 7 && slot < 16) {
                StoveBlockEntityBalm.this.slotCookTime[slot - 7] = 0;
            }

            StoveBlockEntityBalm.this.isDirty = true;
            StoveBlockEntityBalm.this.setChanged();
        }
    };
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int id) {
            if (id == 0) {
                return StoveBlockEntityBalm.this.furnaceBurnTime;
            } else if (id == 1) {
                return StoveBlockEntityBalm.this.currentItemBurnTime;
            } else {
                return id >= 2 && id <= 11 ? StoveBlockEntityBalm.this.slotCookTime[id - 2] : 0;
            }
        }

        public void set(int id, int value) {
            if (id == 0) {
                StoveBlockEntityBalm.this.furnaceBurnTime = value;
            } else if (id == 1) {
                StoveBlockEntityBalm.this.currentItemBurnTime = value;
            } else if (id >= 2 && id <= 11) {
                StoveBlockEntityBalm.this.slotCookTime[id - 2] = value;
            }

        }

        public int getCount() {
            return 11;
        }
    };
    private final EnergyStorage energyStorage = new EnergyStorage(10000) {
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }

            return super.fill(maxReceive, simulate);
        }

        public int drain(int maxExtract, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }

            return super.drain(maxExtract, simulate);
        }
    };
    private final SubContainer inputContainer;
    private final SubContainer fuelContainer;
    private final SubContainer outputContainer;
    private final SubContainer processingContainer;
    private final SubContainer toolsContainer;
    private final DefaultKitchenItemProvider itemProvider;
    private Component customName;
    private boolean isFirstTick;
    public int[] slotCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private final Container singleSlotRecipeWrapper;

    public StoveBlockEntityBalm(BlockPos pos, BlockState state) {
        super(BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
        this.inputContainer = new SubContainer(this.container, 0, 3);
        this.fuelContainer = new SubContainer(this.container, 3, 4);
        this.outputContainer = new SubContainer(this.container, 4, 7);
        this.processingContainer = new SubContainer(this.container, 7, 16);
        this.toolsContainer = new SubContainer(this.container, 16, 20);
        this.itemProvider = new DefaultKitchenItemProvider(new CombinedContainer(this.toolsContainer, this.outputContainer));
        this.isFirstTick = true;
        this.slotCookTime = new int[9];
        this.singleSlotRecipeWrapper = new DefaultContainer(1);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return super.triggerEvent(id, type);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntityBalm stoveBlockEntityBalm) {
            stoveBlockEntityBalm.clientTick(level, pos, state);
        }
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntityBalm stoveBlockEntityBalm) {
            stoveBlockEntityBalm.serverTick(level, pos, state);
        }
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (this.isFirstTick && state.getBlock() instanceof StoveBlock) {
            this.facing = state.getValue(StoveBlock.FACING);
            this.isFirstTick = false;
        }

        if (this.isDirty) {
            this.sync();
            this.isDirty = false;
        }

        boolean hasChanged = false;
        int burnPotential = 200 - this.furnaceBurnTime;
        if (this.hasPowerUpgrade && burnPotential > 0 && this.shouldConsumeFuel()) {
            this.furnaceBurnTime += this.energyStorage.drain(burnPotential, false);
        }

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!level.isClientSide) {
            int firstEmptySlot;
            ItemStack containerItem;
            if (this.furnaceBurnTime == 0 && this.shouldConsumeFuel()) {
                for(firstEmptySlot = 0; firstEmptySlot < this.fuelContainer.getContainerSize(); ++firstEmptySlot) {
                    ItemStack fuelItem = this.fuelContainer.getItem(firstEmptySlot);
                    if (!fuelItem.isEmpty()) {
                        this.currentItemBurnTime = this.furnaceBurnTime = (int)Math.max(1.0, (double)((float)getBurnTime(fuelItem)) * CookingForBlockheadsConfig.getActive().ovenFuelTimeMultiplier);
                        if (this.furnaceBurnTime != 0) {
                            containerItem = Balm.getHooks().getCraftingRemainingItem(fuelItem);
                            fuelItem.shrink(1);
                            if (fuelItem.isEmpty()) {
                                this.fuelContainer.setItem(firstEmptySlot, containerItem);
                            }

                            hasChanged = true;
                        }
                        break;
                    }
                }
            }

            firstEmptySlot = -1;
            int firstTransferSlot = -1;

            ItemStack itemStack;
            int i;
            for(i = 0; i < this.processingContainer.getContainerSize(); ++i) {
                itemStack = this.processingContainer.getItem(i);
                if (!itemStack.isEmpty()) {
                    if (this.slotCookTime[i] != -1) {
                        double maxCookTime = 200.0 * CookingForBlockheadsConfig.getActive().ovenCookTimeMultiplier;
                        if ((double)this.slotCookTime[i] >= maxCookTime && firstTransferSlot == -1) {
                            firstTransferSlot = i;
                        } else {
                            if (this.furnaceBurnTime > 0) {
                                this.slotCookTime[i]++;
                            }

                            if ((double)this.slotCookTime[i] >= maxCookTime) {
                                ItemStack smeltingResult = this.getSmeltingResult(itemStack);
                                if (!smeltingResult.isEmpty()) {
                                    ItemStack resultStack = smeltingResult.copy();
                                    this.processingContainer.setItem(i, resultStack);
                                    Balm.getEvents().fireEvent(new OvenCookedEvent(level, this.getBlockPos(), resultStack));
                                    this.slotCookTime[i] = -1;
                                    if (firstTransferSlot == -1) {
                                        firstTransferSlot = i;
                                    }
                                }
                            }
                        }
                    } else if (firstTransferSlot == -1) {
                        firstTransferSlot = i;
                    }
                } else if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            }

            if (firstTransferSlot != -1) {
                containerItem = this.processingContainer.getItem(firstTransferSlot);
                containerItem = ContainerUtils.insertItemStacked(this.outputContainer, containerItem, false);
                this.processingContainer.setItem(firstTransferSlot, containerItem);
                if (containerItem.isEmpty()) {
                    this.slotCookTime[firstTransferSlot] = 0;
                }

                hasChanged = true;
            }

            if (firstEmptySlot != -1) {
                for(i = 0; i < this.inputContainer.getContainerSize(); ++i) {
                    itemStack = this.inputContainer.getItem(i);
                    if (!itemStack.isEmpty()) {
                        this.processingContainer.setItem(firstEmptySlot, itemStack.split(1));
                        if (itemStack.getCount() <= 0) {
                            this.inputContainer.setItem(i, ItemStack.EMPTY);
                        }
                        break;
                    }
                }
            }
        }

        if (hasChanged) {
            this.setChanged();
        }

    }

    public ItemStack getSmeltingResult(ItemStack itemStack) {
        ItemStack result = CookingRegistry.getSmeltingResult(itemStack);
        if (!result.isEmpty()) {
            return result;
        } else {
            this.singleSlotRecipeWrapper.setItem(0, itemStack);
            Recipe<?> recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this.singleSlotRecipeWrapper, this.level).orElse(null);
            if (recipe != null) {
                result = recipe.getResultItem();
                if (!result.isEmpty() && result.getItem().isEdible()) {
                    return result;
                }
            }

            return !result.isEmpty() && CookingRegistry.isNonFoodRecipe(result) ? result : ItemStack.EMPTY;
        }
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        if (CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil) {
            return itemStack.is(Compat.getCookingOilTag());
        } else {
            return getBurnTime(itemStack) > 0;
        }
    }

    protected static int getBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil && itemStack.is(Compat.getCookingOilTag()) ? 800 : Balm.getHooks().getBurnTime(itemStack);
        }
    }

    private boolean shouldConsumeFuel() {
        for(int i = 0; i < this.processingContainer.getContainerSize(); ++i) {
            ItemStack cookingStack = this.processingContainer.getItem(i);
            if (!cookingStack.isEmpty() && this.slotCookTime[i] != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        this.container.deserialize(tagCompound.getCompound("ItemHandler"));
        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
        this.currentItemBurnTime = tagCompound.getShort("CurrentItemBurnTime");
        this.slotCookTime = tagCompound.getIntArray("CookTimes");
        if (this.slotCookTime.length != 9) {
            this.slotCookTime = new int[9];
        }

        this.hasPowerUpgrade = tagCompound.getBoolean("HasPowerUpgrade");
        this.energyStorage.setEnergy(tagCompound.getInt("EnergyStored"));
        if (tagCompound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }

    }

    @Override
    public void saveAdditional(CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        tagCompound.put("ItemHandler", this.container.serialize());
        tagCompound.putShort("BurnTime", (short)this.furnaceBurnTime);
        tagCompound.putShort("CurrentItemBurnTime", (short)this.currentItemBurnTime);
        tagCompound.putIntArray("CookTimes", ArrayUtils.clone(this.slotCookTime));
        tagCompound.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        tagCompound.putInt("EnergyStored", this.energyStorage.getEnergy());
        if (this.customName != null) {
            tagCompound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
    }

    public void balmFromClientTag(CompoundTag tag) {
    }

    public CompoundTag balmToClientTag(CompoundTag tag) {
        return tag;
    }

    public boolean hasPowerUpgrade() {
        return this.hasPowerUpgrade;
    }

    public void setHasPowerUpgrade(boolean hasPowerUpgrade) {
        this.hasPowerUpgrade = hasPowerUpgrade;
        BlockState state = this.level.getBlockState(this.getBlockPos());
        this.level.setBlockAndUpdate(this.getBlockPos(), (BlockState)state.setValue(OvenBlock.POWERED, hasPowerUpgrade));
        this.setChanged();
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        return this.currentItemBurnTime == 0 && this.furnaceBurnTime > 0 ? 1.0F : (float)this.furnaceBurnTime / (float)this.currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        return (float)this.slotCookTime[i] / (float)(200.0 * CookingForBlockheadsConfig.getActive().ovenCookTimeMultiplier);
    }

    public ItemStack smeltItem(ItemStack itemStack) {
        return ContainerUtils.insertItemStacked(this.inputContainer, itemStack, false);
    }

    public ItemStack getToolItem(int i) {
        return this.toolsContainer.getItem(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        this.toolsContainer.setItem(i, itemStack);
    }

    public Container getContainer(Direction side) {
        if (side == null) {
            return this.getContainer();
        } else {
            SubContainer subContainer;
            switch (side) {
                case UP -> subContainer = this.inputContainer;
                case DOWN -> subContainer = this.outputContainer;
                default -> subContainer = this.fuelContainer;
            }

            return subContainer;
        }
    }

    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(IKitchenItemProvider.class, this.itemProvider), new BalmProvider<>(IKitchenSmeltingProvider.class, this));
    }

    public Container getInputContainer() {
        return this.inputContainer;
    }

    public Container getFuelContainer() {
        return this.fuelContainer;
    }

    public Direction getFacing() {
        return this.facing == null ? Direction.NORTH : this.facing;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new StoveScreenHandlerBalm(i, playerInventory, this);
    }

    public AABB balmGetRenderBoundingBox() {
        return new AABB(this.getBlockPos().offset(-1, 0, -1), this.getBlockPos().offset(2, 1, 2));
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
        this.setChanged();
    }

    public boolean hasCustomName() {
        return this.customName != null;
    }

    public @Nullable Component getCustomName() {
        return this.customName;
    }

    public Component getDisplayName() {
        return this.getName();
    }

    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.oven");
    }

    public Container getContainer() {
        return this.container;
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("ItemHandler", this.container.serialize());
        return nbt;
    }

    protected void onContainerOpen(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock){
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            StoveBlockEntityBalm.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            StoveBlockEntityBalm.this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    public void onClose(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void onOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerOpen(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        double d = (double)this.getBlockPos().getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.getBlockPos().getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.getBlockPos().getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }
}
