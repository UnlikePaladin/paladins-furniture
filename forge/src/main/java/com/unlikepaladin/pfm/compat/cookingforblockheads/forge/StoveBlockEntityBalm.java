package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
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
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.block.entity.IMutableNameable;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProcessorHolder;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StoveBlockEntityBalm extends BalmBlockEntity implements KitchenItemProcessor, BalmMenuProvider<StoveScreenHandler.StoveData>, IMutableNameable, BalmContainerProvider, BalmEnergyStorageProvider, KitchenItemProcessorHolder, KitchenItemProviderHolder {
    private static final int COOK_TIME = 200;
    private final DefaultContainer container = new DefaultContainer(20) {
        public boolean isValid(int slot, ItemStack itemStack) {
            if (slot < 3) {
                return !StoveBlockEntityBalm.this.getSmeltingResult(itemStack).isEmpty();
            } else {
                return slot != 3 || StoveBlockEntityBalm.isItemFuel(StoveBlockEntityBalm.this.world, itemStack);
            }
        }

        public void slotChanged(int slot) {
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
    private final EnergyStorage energyStorage = new DefaultEnergyStorage(10000) {
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.markDirty();
            }

            return super.fill(maxReceive, simulate);
        }

        public int drain(int maxExtract, boolean simulate) {
            if (!simulate) {
                StoveBlockEntityBalm.this.markDirty();
            }

            return super.drain(maxExtract, simulate);
        }
    };
    private final SubContainer inputContainer;
    private final SubContainer fuelContainer;
    final SubContainer outputContainer;
    private final SubContainer processingContainer;
    final SubContainer toolsContainer;
    private Text customName;
    private boolean isFirstTick;
    public int[] slotCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;
    private boolean hasPowerUpgrade;
    private Direction facing;
    private final Inventory singleSlotRecipeWrapper;
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

    public boolean onSyncedBlockEvent(int id, int type) {
        return super.onSyncedBlockEvent(id, type);
    }

    public static void clientTick(World level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntityBalm stoveBlockEntityBalm) {
            stoveBlockEntityBalm.clientTick(level, pos, state);
        }
    }

    public void clientTick(World level, BlockPos pos, BlockState state) {
    }

    public static void serverTick(World level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntityBalm stoveBlockEntityBalm) {
            stoveBlockEntityBalm.serverTick(level, pos, state);
        }
    }

    public void serverTick(World level, BlockPos pos, BlockState state) {
        if (this.isFirstTick && state.getBlock() instanceof StoveBlock) {
            this.facing = state.get(StoveBlock.FACING);
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

        if (!level.isClient()) {
            int firstEmptySlot;
            ItemStack containerItem;
            if (this.furnaceBurnTime == 0 && this.shouldConsumeFuel()) {
                for(firstEmptySlot = 0; firstEmptySlot < this.fuelContainer.size(); ++firstEmptySlot) {
                    ItemStack fuelItem = this.fuelContainer.getStack(firstEmptySlot);
                    if (!fuelItem.isEmpty()) {
                        this.currentItemBurnTime = this.furnaceBurnTime = (int)Math.max(1.0, (double)((float)getBurnTime(level, fuelItem)) * CookingForBlockheadsConfig.getActive().ovenFuelTimeMultiplier);
                        if (this.furnaceBurnTime != 0) {
                            containerItem = Balm.getHooks().getCraftingRemainingItem(fuelItem);
                            fuelItem.decrement(1);
                            if (fuelItem.isEmpty()) {
                                this.fuelContainer.setStack(firstEmptySlot, containerItem);
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
            for(i = 0; i < this.processingContainer.size(); ++i) {
                itemStack = this.processingContainer.getStack(i);
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
                                    this.processingContainer.setStack(i, resultStack);
                                    Balm.getEvents().fireEvent(new OvenCookedEvent(level, this.pos, resultStack));
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
                containerItem = this.processingContainer.getStack(firstTransferSlot);
                containerItem = ContainerUtils.insertItemStacked(this.outputContainer, containerItem, false);
                this.processingContainer.setStack(firstTransferSlot, containerItem);
                if (containerItem.isEmpty()) {
                    this.slotCookTime[firstTransferSlot] = 0;
                }

                hasChanged = true;
            }

            if (firstEmptySlot != -1) {
                for(i = 0; i < this.inputContainer.size(); ++i) {
                    itemStack = this.inputContainer.getStack(i);
                    if (!itemStack.isEmpty()) {
                        this.processingContainer.setStack(firstEmptySlot, itemStack.split(1));
                        if (itemStack.getCount() <= 0) {
                            this.inputContainer.setStack(i, ItemStack.EMPTY);
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
        SingleStackRecipeInput recipeInput = new SingleStackRecipeInput(itemStack);
        ItemStack ovenRecipeResult = this.getSmeltingResult(ModRecipes.ovenRecipeType, recipeInput);
        return !ovenRecipeResult.isEmpty() ? ovenRecipeResult : this.getSmeltingResult(RecipeType.SMELTING, recipeInput);
    }

    public <T extends RecipeInput> ItemStack getSmeltingResult(RecipeType<? extends Recipe<T>> recipeType, T recipeInput) {
        MinecraftServer server = this.world.getServer();
        if (server != null) {
            RecipeEntry<? extends Recipe<T>> recipe = server.getRecipeManager().getFirstMatch(recipeType, recipeInput, this.world).orElse(null);
            if (recipe != null) {
                ItemStack result = recipe.value().craft(recipeInput, this.world.getRegistryManager());
                if (!result.isEmpty() && result.get(DataComponentTypes.FOOD) != null) {
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public static boolean isItemFuel(World world, ItemStack itemStack) {
        if (CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil) {
            return itemStack.isIn(BalmItemTags.COOKING_OIL);
        } else {
            return getBurnTime(world, itemStack) > 0;
        }
    }

    protected static int getBurnTime(World world, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil && itemStack.isIn(BalmItemTags.COOKING_OIL) ? 800 : 800;
        }
    }

    private boolean shouldConsumeFuel() {
        for(int i = 0; i < this.processingContainer.size(); ++i) {
            ItemStack cookingStack = this.processingContainer.getStack(i);
            if (!cookingStack.isEmpty() && this.slotCookTime[i] != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        view.getOptionalReadView("ItemHandler").ifPresent((it) -> {
            Inventories.readData(it, this.container.getItems());
        });
        this.furnaceBurnTime = view.getShort("BurnTime", (short) 0);
        this.currentItemBurnTime = view.getShort("CurrentItemBurnTime", (short) 0);
        this.slotCookTime = view.getOptionalIntArray("CookTimes").orElse(new int[0]);
        if (this.slotCookTime.length != 9) {
            this.slotCookTime = new int[9];
        }

        this.hasPowerUpgrade = view.getBoolean("HasPowerUpgrade", false);
        this.energyStorage.setEnergy(view.getInt("EnergyStored", 0));
        this.customName = view.read("CustomNameV2", TextCodecs.CODEC).orElse(null);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view.get("ItemHandler"), this.container.getItems());
        view.putShort("BurnTime", (short)this.furnaceBurnTime);
        view.putShort("CurrentItemBurnTime", (short)this.currentItemBurnTime);
        view.putIntArray("CookTimes", ArrayUtils.clone(this.slotCookTime));
        view.putBoolean("HasPowerUpgrade", this.hasPowerUpgrade);
        view.putInt("EnergyStored", this.energyStorage.getEnergy());
        view.putNullable("CustomNameV2", TextCodecs.CODEC, this.customName);
    }

    @Override
    protected void writeUpdateTag(WriteView view) {
        this.writeData(view);
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
        return this.toolsContainer.getStack(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        this.toolsContainer.setStack(i, itemStack);
    }

    public Inventory getContainer(Direction side) {
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

    public Inventory getInputContainer() {
        return this.inputContainer;
    }

    public Inventory getFuelContainer() {
        return this.fuelContainer;
    }

    public Direction getFacing() {
        return this.facing == null ? Direction.NORTH : this.facing;
    }

    public @Nullable ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new StoveScreenHandlerBalm(i, playerInventory, this);
    }

    public Box balmGetRenderBoundingBox() {
        return new Box(this.pos.add(-1, 0, -1).toCenterPos(), this.pos.add(2, 1, 2).toCenterPos());
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
        return Text.translatable("container.cookingforblockheads.oven");
    }

    public Inventory getContainer() {
        return this.container;
    }

    public PropertyDelegate getContainerData() {
        return this.dataAccess;
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup provider) {
        return this.createNbt(provider);
    }

    @Override
    public void handleUpdateTag(ReadView tag, RegistryWrapper.WrapperLookup holders) {
        super.handleUpdateTag(tag, holders);
        this.readData(tag);
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
    public StoveScreenHandler.StoveData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new StoveScreenHandler.StoveData(this.pos);
    }

    @Override
    public PacketCodec<RegistryByteBuf, StoveScreenHandler.StoveData> getScreenStreamCodec() {
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
