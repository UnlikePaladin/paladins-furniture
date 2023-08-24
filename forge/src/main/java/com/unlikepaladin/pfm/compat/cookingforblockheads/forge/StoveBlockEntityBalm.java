package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.api.event.OvenCookedEvent;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.IMutableNameable;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.blay09.mods.cookingforblockheads.tile.util.EnergyStorageModifiable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoveBlockEntityBalm extends BlockEntity implements IKitchenSmeltingProvider, IMutableNameable, Tickable, NamedScreenHandlerFactory {
    private static final int COOK_TIME = 200;
    private final ItemStackHandler container = new ItemStackHandler(20) {
        @Override
        public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot < 3) {
                if (StoveBlockEntityBalm.this.getSmeltingResult(stack).isEmpty()) {
                    return stack;
                }
            } else if (slot == 3 && !OvenTileEntity.isItemFuel(stack)) {
                return stack;
            }

            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public void onContentsChanged(int slot) {
            if (slot >= 7 && slot < 16) {
                StoveBlockEntityBalm.this.slotCookTime[slot - 7] = 0;
            }

            StoveBlockEntityBalm.this.isDirty = true;
            StoveBlockEntityBalm.this.markDirty();
        }
    };
    private final PropertyDelegate dataAccess = new PropertyDelegate() {
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

        public int size() {
            return 11;
        }
    };
    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(10000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.markDirty();
            }

            return super.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.markDirty();
            }

            return super.extractEnergy(maxExtract, simulate);
        }
    };
    private final RangedWrapper inputContainer;
    private final RangedWrapper fuelContainer;
    private final RangedWrapper outputContainer;
    private final RangedWrapper processingContainer;
    private final RangedWrapper toolsContainer;
    private final DefaultKitchenItemProvider itemProvider;
    private final ItemStackHandler singleSlotItemHandler;
    private Text customName;
    private boolean isFirstTick;
    public int[] slotCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private final Inventory singleSlotRecipeWrapper;

    private final LazyOptional<IKitchenItemProvider> itemProviderCap;
    private final LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap;
    private final LazyOptional<IEnergyStorage> energyStorageCap;
    private final LazyOptional<IItemHandler> itemHandlerCap;
    private final LazyOptional<IItemHandler> itemHandlerInputCap;
    private final LazyOptional<IItemHandler> itemHandlerFuelCap;
    private final LazyOptional<IItemHandler> itemHandlerOutputCap;

    public StoveBlockEntityBalm() {
        super(BlockEntities.STOVE_BLOCK_ENTITY);
        this.inputContainer = new RangedWrapper(this.container, 0, 3);
        this.fuelContainer = new RangedWrapper(this.container, 3, 4);
        this.outputContainer = new RangedWrapper(this.container, 4, 7);
        this.processingContainer = new RangedWrapper(this.container, 7, 16);
        this.toolsContainer = new RangedWrapper(this.container, 16, 20);
        this.itemProvider = new KitchenItemProvider(new CombinedInvWrapper(this.toolsContainer, this.outputContainer));
        this.isFirstTick = true;
        this.slotCookTime = new int[9];
        this.singleSlotItemHandler = new ItemStackHandler(1);
        this.singleSlotRecipeWrapper = new RecipeWrapper(singleSlotItemHandler);

        this.itemProviderCap = LazyOptional.of(() -> {
            return this.itemProvider;
        });
        this.smeltingProviderCap = LazyOptional.of(() -> {
            return this;
        });
        this.energyStorageCap = LazyOptional.of(() -> {
            return this.energyStorage;
        });
        this.itemHandlerCap = LazyOptional.of(() -> {
            return this.container;
        });
        this.itemHandlerInputCap = LazyOptional.of(() -> {
            return this.inputContainer;
        });
        this.itemHandlerFuelCap = LazyOptional.of(() -> {
            return this.fuelContainer;
        });
        this.itemHandlerOutputCap = LazyOptional.of(() -> {
            return this.outputContainer;
        });
    }

    public boolean onSyncedBlockEvent(int id, int type) {
        return super.onSyncedBlockEvent(id, type);
    }

    public void clientTick() {
    }


    public void serverTick() {
        World world = this.world;
        if (this.isFirstTick && getCachedState().getBlock() instanceof StoveBlock) {
            this.facing = getCachedState().get(StoveBlock.FACING);
            this.isFirstTick = false;
        }

        if (this.isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            this.isDirty = false;
        }

        boolean hasChanged = false;
        int burnPotential = 200 - this.furnaceBurnTime;
        if (this.hasPowerUpgrade && burnPotential > 0 && this.shouldConsumeFuel()) {
            this.furnaceBurnTime += this.energyStorage.extractEnergy(burnPotential, false);
        }

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!world.isClient) {
            int firstEmptySlot;
            ItemStack containerItem;
            if (this.furnaceBurnTime == 0 && this.shouldConsumeFuel()) {
                for(firstEmptySlot = 0; firstEmptySlot < this.fuelContainer.getSlots(); ++firstEmptySlot) {
                    ItemStack fuelItem = this.fuelContainer.getStackInSlot(firstEmptySlot);
                    if (!fuelItem.isEmpty()) {
                        this.currentItemBurnTime = this.furnaceBurnTime = (int)Math.max(1.0, (double)((float)getBurnTime(fuelItem)) * CookingForBlockheadsConfig.COMMON.ovenFuelTimeMultiplier.get());
                        if (this.furnaceBurnTime != 0) {
                            containerItem = fuelItem.getItem().getContainerItem(fuelItem);
                            fuelItem.decrement(1);
                            if (fuelItem.isEmpty()) {
                                this.fuelContainer.setStackInSlot(firstEmptySlot, containerItem);
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
            for(i = 0; i < this.processingContainer.getSlots(); ++i) {
                itemStack = this.processingContainer.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    if (this.slotCookTime[i] != -1) {
                        double maxCookTime = 200.0 * CookingForBlockheadsConfig.COMMON.ovenCookTimeMultiplier.get();
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
                                    this.processingContainer.setStackInSlot(i, resultStack);
                                    MinecraftForge.EVENT_BUS.post(new OvenCookedEvent(this.world, this.pos, resultStack));
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
                containerItem = this.processingContainer.getStackInSlot(firstTransferSlot);
                containerItem = ItemHandlerHelper.insertItemStacked(this.outputContainer, containerItem, false);
                this.processingContainer.setStackInSlot(firstTransferSlot, containerItem);
                if (containerItem.isEmpty()) {
                    this.slotCookTime[firstTransferSlot] = 0;
                }

                hasChanged = true;
            }

            if (firstEmptySlot != -1) {
                for(i = 0; i < this.inputContainer.getSlots(); ++i) {
                    itemStack = this.inputContainer.getStackInSlot(i);
                    if (!itemStack.isEmpty()) {
                        this.processingContainer.setStackInSlot(firstEmptySlot, itemStack.split(1));
                        if (itemStack.getCount() <= 0) {
                            this.inputContainer.setStackInSlot(i, ItemStack.EMPTY);
                        }
                        break;
                    }
                }
            }
        }

        if (hasChanged) {
            this.markDirty();
        }

    }

    public ItemStack getSmeltingResult(ItemStack itemStack) {
        ItemStack result = CookingRegistry.getSmeltingResult(itemStack);
        if (!result.isEmpty()) {
            return result;
        } else {
            this.singleSlotRecipeWrapper.setStack(0, itemStack);
            Recipe<?> recipe = this.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this.singleSlotRecipeWrapper, this.world).orElse(null);
            if (recipe != null) {
                result = recipe.getOutput();
                if (!result.isEmpty() && result.getItem().isFood()) {
                    return result;
                }
            }

            return !result.isEmpty() && CookingRegistry.isNonFoodRecipe(result) ? result : ItemStack.EMPTY;
        }
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        if (CookingForBlockheadsConfig.COMMON.ovenRequiresCookingOil.get()) {
            return itemStack.getItem().getTags().contains(Compat.cookingOilTag);
        } else {
            return getBurnTime(itemStack) > 0;
        }
    }

    protected static int getBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.COMMON.ovenRequiresCookingOil.get() && itemStack.getItem().getTags().contains(Compat.cookingOilTag) ? 800 : ForgeEventFactory.getItemBurnTime(itemStack, itemStack.getBurnTime() == -1 ? AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(itemStack.getItem(), 0) : itemStack.getBurnTime());
        }
    }

    private boolean shouldConsumeFuel() {
        for(int i = 0; i < this.processingContainer.getSlots(); ++i) {
            ItemStack cookingStack = this.processingContainer.getStackInSlot(i);
            if (!cookingStack.isEmpty() && this.slotCookTime[i] != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound tagCompound) {
        super.fromTag(state, tagCompound);
        this.container.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
        this.currentItemBurnTime = tagCompound.getShort("CurrentItemBurnTime");
        this.slotCookTime = tagCompound.getIntArray("CookTimes");
        if (this.slotCookTime.length != 9) {
            this.slotCookTime = new int[9];
        }

        this.hasPowerUpgrade = tagCompound.getBoolean("HasPowerUpgrade");
        this.energyStorage.setEnergyStored(tagCompound.getInt("EnergyStored"));
        if (tagCompound.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(tagCompound.getString("CustomName"));
        }

    }

    public NbtCompound writeNbt(NbtCompound tagCompound) {
        super.writeNbt(tagCompound);
        tagCompound.put("ItemHandler", this.container.serializeNBT());
        tagCompound.putShort("BurnTime", (short)this.furnaceBurnTime);
        tagCompound.putShort("CurrentItemBurnTime", (short)this.currentItemBurnTime);
        tagCompound.putIntArray("CookTimes", ArrayUtils.clone(this.slotCookTime));
        tagCompound.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        tagCompound.putInt("EnergyStored", this.energyStorage.getEnergyStored());
        if (this.customName != null) {
            tagCompound.putString("CustomName", Text.Serializer.toJson(this.customName));
        }

        return tagCompound;
    }

    public boolean hasPowerUpgrade() {
        return this.hasPowerUpgrade;
    }

    public void setHasPowerUpgrade(boolean hasPowerUpgrade) {
        this.hasPowerUpgrade = hasPowerUpgrade;
        BlockState state = this.world.getBlockState(this.pos);
        this.world.setBlockState(this.pos, state.with(OvenBlock.POWERED, hasPowerUpgrade));
        this.markDirty();
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        return this.currentItemBurnTime == 0 && this.furnaceBurnTime > 0 ? 1.0F : (float)this.furnaceBurnTime / (float)this.currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        return (float)this.slotCookTime[i] / (float)(200.0 * CookingForBlockheadsConfig.COMMON.ovenCookTimeMultiplier.get());
    }

    public ItemStack smeltItem(ItemStack itemStack) {
        return ItemHandlerHelper.insertItemStacked(this.inputContainer, itemStack, false);
    }

    public ItemStack getToolItem(int i) {
        return this.toolsContainer.getStackInSlot(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        this.toolsContainer.setStackInSlot(i, itemStack);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @javax.annotation.Nullable Direction facing) {
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

    public @Nullable ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new StoveScreenHandlerBalm(i, playerInventory, this);
    }

    public Text getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
        this.markDirty();
    }

    public boolean hasCustomName() {
        return this.customName != null;
    }

    public @Nullable Text getCustomName() {
        return this.customName;
    }

    public Text getDisplayName() {
        return this.getName();
    }

    public Text getDefaultName() {
        return new TranslatableText("container.cookingforblockheads.oven");
    }

    public IItemHandler getContainer() {
        return this.container;
    }

    public PropertyDelegate getContainerData() {
        return this.dataAccess;
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return this.energyStorage;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataNbt());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        nbt.put("ItemHandler", this.container.serializeNBT());
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, NbtCompound tag) {
        this.fromTag(state, tag);
        super.handleUpdateTag(state, tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.container.deserializeNBT(pkt.getNbt().getCompound("ItemHandler"));
    }

    protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock){
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
            StoveBlockEntityBalm.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
            StoveBlockEntityBalm.this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(Properties.OPEN, open), 3);
    }

    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerClose(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerOpen(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(Properties.HORIZONTAL_FACING).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void tick() {
        if (world.isClient) {
            clientTick();
        } else {
            serverTick();
        }
    }


}
