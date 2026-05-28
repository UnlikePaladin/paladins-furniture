package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.*;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.DefaultEnergyStorage;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.tag.BalmItemTags;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.blay09.mods.cookingforblockheads.api.event.OvenCookedEvent;
import net.blay09.mods.cookingforblockheads.block.entity.IMutableNameable;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProcessorHolder;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoveBlockEntityBalm extends BalmBlockEntity implements KitchenItemProcessor, BalmMenuProvider<StoveScreenHandler.StoveData>, IMutableNameable, BalmContainerProvider, BalmEnergyStorageProvider, KitchenItemProcessorHolder, KitchenItemProviderHolder {
    private static final int COOK_TIME = 200;
    private final DefaultContainer container = new DefaultContainer(20) {
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            if (slot < 3) {
                return !StoveBlockEntityBalm.this.getSmeltingResult(itemStack).isEmpty();
            } else {
                return slot != 3 || StoveBlockEntityBalm.isItemFuel(StoveBlockEntityBalm.this.level, itemStack);
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
    private final EnergyStorage energyStorage = new DefaultEnergyStorage(10000) {
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
    final SubContainer outputContainer;
    private final SubContainer processingContainer;
    final SubContainer toolsContainer;
    private Component customName;
    private boolean isFirstTick;
    public int[] slotCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private final Container singleSlotRecipeWrapper;
    private KitchenItemProvider itemProvider;
    public StoveBlockEntityBalm(BlockPos pos, BlockState state) {
        super(com.unlikepaladin.pfm.registry.BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
        this.inputContainer = new SubContainer(this.container, 0, 3);
        this.fuelContainer = new SubContainer(this.container, 3, 4);
        this.outputContainer = new SubContainer(this.container, 4, 7);
        this.processingContainer = new SubContainer(this.container, 7, 16);
        this.toolsContainer = new SubContainer(this.container, 16, 20);
        this.isFirstTick = true;
        this.slotCookTime = new int[9];
        this.singleSlotRecipeWrapper = new DefaultContainer(1);
        this.itemProvider = new ContainerKitchenItemProvider(new CombinedContainer(this.toolsContainer, this.outputContainer));
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
                        this.currentItemBurnTime = this.furnaceBurnTime = (int)Math.max(1.0, (double)((float)getBurnTime(level, fuelItem)) * CookingForBlockheadsConfig.getActive().ovenFuelTimeMultiplier);
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
                                    Balm.getEvents().fireEvent(new OvenCookedEvent(level, this.worldPosition, resultStack));
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
        SingleRecipeInput recipeInput = new SingleRecipeInput(itemStack);
        ItemStack ovenRecipeResult = this.getSmeltingResult(ModRecipes.ovenRecipeType, recipeInput);
        return !ovenRecipeResult.isEmpty() ? ovenRecipeResult : this.getSmeltingResult(RecipeType.SMELTING, recipeInput);
    }

    public <T extends RecipeInput> ItemStack getSmeltingResult(RecipeType<? extends Recipe<T>> recipeType, T recipeInput) {
        MinecraftServer server = this.level.getServer();
        if (server != null) {
            RecipeHolder<? extends Recipe<T>> recipe = server.getRecipeManager().getRecipeFor(recipeType, recipeInput, this.level).orElse(null);
            if (recipe != null) {
                ItemStack result = recipe.value().assemble(recipeInput, this.level.registryAccess());
                if (!result.isEmpty() && result.has(DataComponents.FOOD)) {
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public static boolean isItemFuel(Level world, ItemStack itemStack) {
        if (CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil) {
            return itemStack.is(BalmItemTags.COOKING_OIL);
        } else {
            return getBurnTime(world, itemStack) > 0;
        }
    }

    protected static int getBurnTime(Level world, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil && itemStack.is(BalmItemTags.COOKING_OIL) ? 800 : 800;
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
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        view.child("ItemHandler").ifPresent((it) -> {
            ContainerHelper.loadAllItems(it, this.container.getItems());
        });
        this.furnaceBurnTime = view.getShortOr("BurnTime", (short) 0);
        this.currentItemBurnTime = view.getShortOr("CurrentItemBurnTime", (short) 0);
        this.slotCookTime = view.getIntArray("CookTimes").orElse(new int[0]);
        if (this.slotCookTime.length != 9) {
            this.slotCookTime = new int[9];
        }

        this.hasPowerUpgrade = view.getBooleanOr("HasPowerUpgrade", false);
        this.energyStorage.setEnergy(view.getIntOr("EnergyStored", 0));
        this.customName = view.read("CustomNameV2", ComponentSerialization.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view.child("ItemHandler"), this.container.getItems());
        view.putShort("BurnTime", (short)this.furnaceBurnTime);
        view.putShort("CurrentItemBurnTime", (short)this.currentItemBurnTime);
        view.putIntArray("CookTimes", ArrayUtils.clone(this.slotCookTime));
        view.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        view.putInt("EnergyStored", this.energyStorage.getEnergy());
        view.storeNullable("CustomNameV2", ComponentSerialization.CODEC, this.customName);
    }

    @Override
    protected void writeUpdateTag(ValueOutput view) {
        this.saveAdditional(view);
        super.writeUpdateTag(view);
    }

    public boolean hasPowerUpgrade() {
        return this.hasPowerUpgrade;
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

    public Container getInputContainer() {
        return this.inputContainer;
    }

    public Container getFuelContainer() {
        return this.fuelContainer;
    }

    public Direction getFacing() {
        return this.facing == null ? Direction.NORTH : this.facing;
    }

    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new StoveScreenHandlerBalm(i, playerInventory, this);
    }

    public AABB balmGetRenderBoundingBox() {
        return new AABB(this.worldPosition.offset(-1, 0, -1).getCenter(), this.worldPosition.offset(2, 1, 2).getCenter());
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
        this.setChanged();
    }

    public boolean hasCustomHoverName() {
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
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public void handleUpdateTag(ValueInput tag, HolderLookup.Provider holders) {
        super.handleUpdateTag(tag, holders);
        this.loadAdditional(tag);
    }

    protected void onContainerOpen(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock){
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            StoveBlockEntityBalm.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            StoveBlockEntityBalm.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            StoveBlockEntityBalm.this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerOpen(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getUnitVec3i();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    public boolean canProcess(RecipeType<?> recipeType) {
        return recipeType == RecipeType.SMELTING;
    }

    public KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens) {
        for (IngredientToken ingredientToken : ingredientTokens) {
            ItemStack itemStack = ingredientToken.consume();
            ItemStack restStack = ContainerUtils.insertItemStacked(this.inputContainer, itemStack, false);
            if (!restStack.isEmpty()) {
                ingredientToken.restore(restStack);
            }
        }

        return KitchenOperation.EMPTY;
    }


    @Override
    public StoveScreenHandler.StoveData getScreenOpeningData(ServerPlayer serverPlayerEntity) {
        return new StoveScreenHandler.StoveData(this.getBlockPos());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, StoveScreenHandler.StoveData> getScreenStreamCodec() {
        return StoveScreenHandler.PACKET_CODEC;
    }

    @Override
    public KitchenItemProcessor getKitchenItemProcessor() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return this.itemProvider;
    }
}
