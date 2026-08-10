package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.StoveData;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.api.container.CombinedContainer;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.energy.DefaultEnergyStorage;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.tag.BalmItemTags;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.minecraft.core.*;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StoveBlockEntityBalm extends OvenBlockEntityBalm implements BalmMenuProvider<StoveData> {
    private final NonNullList<ItemStack> tools = NonNullList.withSize(4, ItemStack.EMPTY);
    private final EnergyStorage energyStorage = new DefaultEnergyStorage(10000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }
            return super.fill(maxReceive, simulate);
        }

        @Override
        public int drain(int maxExtract, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.setChanged();
            }
            return super.drain(maxExtract, simulate);
        }
    };

    private final Container mappedContainer = new Container() {
        @Override
        public int getContainerSize() {
            return 20;
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < 16; i++) {
                if (!StoveBlockEntityBalm.this.getItem(i).isEmpty()) {
                    return false;
                }
            }
            for (ItemStack tool : StoveBlockEntityBalm.this.tools) {
                if (!tool.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
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

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = ItemStack.EMPTY;
            if (slot >= 0 && slot < 3) {
                stack = StoveBlockEntityBalm.this.removeItem(slot, amount);
            } else if (slot == 3) {
                stack = StoveBlockEntityBalm.this.removeItem(15, amount);
            } else if (slot >= 4 && slot < 7) {
                stack = StoveBlockEntityBalm.this.removeItem(slot + 8, amount);
            } else if (slot >= 7 && slot < 16) {
                stack = StoveBlockEntityBalm.this.removeItem(slot - 4, amount);
            } else if (slot >= 16 && slot < 20) {
                stack = ContainerHelper.removeItem(StoveBlockEntityBalm.this.tools, slot - 16, amount);
            }
            if (!stack.isEmpty()) {
                setChanged();
            }
            return stack;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack stack = ItemStack.EMPTY;
            if (slot >= 0 && slot < 3) {
                stack = StoveBlockEntityBalm.this.removeItemNoUpdate(slot);
            } else if (slot == 3) {
                stack = StoveBlockEntityBalm.this.removeItemNoUpdate(15);
            } else if (slot >= 4 && slot < 7) {
                stack = StoveBlockEntityBalm.this.removeItemNoUpdate(slot + 8);
            } else if (slot >= 7 && slot < 16) {
                stack = StoveBlockEntityBalm.this.removeItemNoUpdate(slot - 4);
            } else if (slot >= 16 && slot < 20) {
                stack = ContainerHelper.takeItem(StoveBlockEntityBalm.this.tools, slot - 16);
            }
            if (!stack.isEmpty()) {
                setChanged();
            }
            return stack;
        }

        @Override
        public void setItem(int slot, ItemStack itemStack) {
            if (slot >= 0 && slot < 3) {
                StoveBlockEntityBalm.this.setItem(slot, itemStack);
            } else if (slot == 3) {
                StoveBlockEntityBalm.this.setItem(15, itemStack);
            } else if (slot >= 4 && slot < 7) {
                StoveBlockEntityBalm.this.setItem(slot + 8, itemStack);
            } else if (slot >= 7 && slot < 16) {
                StoveBlockEntityBalm.this.setItem(slot - 4, itemStack);
            } else if (slot >= 16 && slot < 20) {
                StoveBlockEntityBalm.this.tools.set(slot - 16, itemStack);
            }
            setChanged();
        }

        @Override
        public void setChanged() {
            StoveBlockEntityBalm.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return StoveBlockEntityBalm.this.stillValid(player);
        }

        @Override
        public void clearContent() {
            for (int i = 0; i < 20; i++) {
                setItem(i, ItemStack.EMPTY);
            }
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

    private final SubContainer inputContainer;
    private final SubContainer fuelContainer;
    final SubContainer outputContainer;
    private final SubContainer processingContainer;
    final SubContainer toolsContainer;
    private Component customName;
    private boolean isFirstTick = true;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private SingleRecipeInput singleSlotRecipeWrapper = new SingleRecipeInput(ItemStack.EMPTY);
    private KitchenItemProvider itemProvider;
    public StoveBlockEntityBalm(BlockPos pos, BlockState state) {
        super(BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
        this.inputContainer = new SubContainer(this.mappedContainer, 0, 3);
        this.fuelContainer = new SubContainer(this.mappedContainer, 3, 4);
        this.outputContainer = new SubContainer(this.mappedContainer, 4, 7);
        this.processingContainer = new SubContainer(this.mappedContainer, 7, 16);
        this.toolsContainer = new SubContainer(this.mappedContainer, 16, 20);
        this.itemProvider = new ContainerKitchenItemProvider(new CombinedContainer(this.toolsContainer, this.outputContainer));
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, OvenBlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntityBalm stoveBlockEntityBalm) {
            stoveBlockEntityBalm.serverTick(level, pos, state);
        }
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (this.isFirstTick && state.getBlock() instanceof StoveBlock) {
            this.facing = state.getValue(StoveBlock.FACING);
            this.isFirstTick = false;
        }

        int burnPotential = 200 - this.furnaceBurnTime;
        if (this.hasPowerUpgrade && burnPotential > 0 && this.shouldConsumeFuel()) {
            this.furnaceBurnTime += this.energyStorage.drain(burnPotential, false);
        }

        OvenBlockEntity.serverTick(level, pos, state, this);
    }

    @Override
    public AbstractCookingRecipe getSmokingRecipe(ItemStack itemStack, ServerLevel level) {
        if (itemStack.isEmpty()) {
            return null;
        }
        this.singleSlotRecipeWrapper = new SingleRecipeInput(itemStack);
        Optional<RecipeHolder<SmeltingRecipe>> recipe = level.recipeAccess().getRecipeFor(RecipeType.SMELTING, this.singleSlotRecipeWrapper, this.level);
        if (recipe != null && recipe.isPresent()) {
            ItemStack result = recipe.get().value().result();
            if (!result.isEmpty() && (result.has(DataComponents.FOOD))) {
                return recipe.get().value();
            }
        }
        return super.getSmokingRecipe(itemStack, level);
    }

    public ItemStack getSmeltingResult(ItemStack itemStack, ServerLevel level) {
        AbstractCookingRecipe recipe = this.getSmokingRecipe(itemStack, level);
        return recipe != null ? recipe.result() : ItemStack.EMPTY;
    }

    public boolean isItemFuel(ItemStack itemStack) {
        if (CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil) {
            return itemStack.is(BalmItemTags.COOKING_OIL);
        } else {
            return getBurnDuration(itemStack) > 0;
        }
    }

    @Override
    public void loadAdditional(ValueInput view) {
        // NBT Migration
        NonNullList<ItemStack> oldContainer = NonNullList.withSize(20, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(view.childOrEmpty("ItemHandler"), oldContainer);

        // Map old container slots to new standard slots
        for (int i = 0; i < 3; i++) {
            this.setItem(i, oldContainer.get(i));
        }
        this.setItem(15, oldContainer.get(3));
        for (int i = 0; i < 3; i++) {
            this.setItem(12 + i, oldContainer.get(4 + i));
        }
        for (int i = 0; i < 9; i++) {
            this.setItem(3 + i, oldContainer.get(7 + i));
        }
        for (int i = 0; i < 4; i++) {
            this.tools.set(i, oldContainer.get(16 + i));
        }

        super.loadAdditional(view);

        this.tools.clear();
        ContainerHelper.loadAllItems(view.childOrEmpty("Tools"), this.tools);

        this.hasPowerUpgrade = view.getBooleanOr("HasPowerUpgrade", false);
        this.energyStorage.setEnergy(view.getIntOr("EnergyStored", 0));
        this.customName = view.read("CustomNameV2", ComponentSerialization.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);

        ContainerHelper.saveAllItems(view.child("Tools"), this.tools);
        view.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        view.putInt("EnergyStored", this.energyStorage.getEnergy());
        view.storeNullable("CustomNameV2", ComponentSerialization.CODEC, this.customName);
    }

    public boolean hasPowerUpgrade() {
        return this.hasPowerUpgrade;
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

    public ItemStack getToolItem(int i) {
        return this.tools.get(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        this.tools.set(i, itemStack);
        this.setChanged();
    }

    public Container getContainer(Direction side) {
        if (side == null) {
            return this.getContainer();
        } else {
            return switch (side) {
                case UP -> this.inputContainer;
                case DOWN -> this.outputContainer;
                default -> this.fuelContainer;
            };
        }
    }

    @Override
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

    @Override
    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
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

    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.oven");
    }

    @Override
    public Container getContainer() {
        return this.mappedContainer;
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
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
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getUnitVec3i();
        double d = (double) this.worldPosition.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double e = (double) this.worldPosition.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double f = (double) this.worldPosition.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    public boolean canProcess(RecipeType<?> recipeType) {
        return recipeType == RecipeType.SMELTING;
    }

    @Override
    public StoveData getScreenOpeningData(ServerPlayer serverPlayerEntity) {
        return new StoveData(this.getBlockPos());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, StoveData> getScreenStreamCodec() {
        return StoveData.PACKET_CODEC;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return this.itemProvider;
    }
}
