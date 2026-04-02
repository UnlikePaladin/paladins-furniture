package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.MicrowaveBlock;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.SoundIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MicrowaveBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer, RecipeHolder {
    public boolean isActive = false;

    public MicrowaveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MICROWAVE_BLOCK_ENTITY, pos, state);
        this.recipeType = RecipeType.SMOKING;
        level = this.getLevel();
    }

    //Slot 0 = input, 2 = output, 1 = fuel
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof MicrowaveBlock) {
                MicrowaveBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN, 0);
                MicrowaveBlockEntity.this.setOpen(state, true);
            }
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof MicrowaveBlock) {
                MicrowaveBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE, 0);
                MicrowaveBlockEntity.this.setOpen(state, false);
            }
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof MicrowaveScreenHandler) {
                Container inventory = ((MicrowaveScreenHandler) player.containerMenu).getContainer();
                return inventory == MicrowaveBlockEntity.this;
            }
            return false;
        }
    };

    void playSound(BlockState state, SoundEvent soundEvent, int pitch) {
        Vec3i vec3i = state.getValue(MicrowaveBlock.FACING).getNormal();
        double d = (double) this.worldPosition.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double e = (double) this.worldPosition.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double f = (double) this.worldPosition.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        float i = pitch == 0 ? (i = this.level.random.nextFloat() * 0.2f + 0.9f) : (i = pitch);
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, i);
    }

    private static final int[] TOP_SLOTS = new int[]{0};
    public NonNullList<ItemStack> container = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    int cookTime;
    int cookTimeTotal;

    public ContainerData getPropertyDelegate() {
        return dataAccess;
    }

    protected final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int index) {
            switch (index) {
                case 0: {
                    return MicrowaveBlockEntity.this.cookTime;
                }
                case 1: {
                    return MicrowaveBlockEntity.this.cookTimeTotal;
                }
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0: {
                    MicrowaveBlockEntity.this.cookTime = value;
                    break;
                }
                case 1: {
                    MicrowaveBlockEntity.this.cookTimeTotal = value;
                    break;
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap();
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.pfm.microwave");
    }

    private static int getCookingTime(Level level, RecipeType<? extends AbstractCookingRecipe> recipeType, Container inventory) {
        return level.getRecipeManager().getRecipeFor(recipeType, inventory, level).map(RecipeEntry::value).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.container = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.container);
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        CompoundTag nbtCompound = nbt.getCompound("RecipesUsed");
        this.isActive = nbt.getBoolean("isActive");
        for (String string : nbtCompound.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(string), nbtCompound.getInt(string));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        ContainerHelper.saveAllItems(nbt, this.container);
        CompoundTag nbtCompound = new CompoundTag();
        nbt.putBoolean("isActive", this.isActive);
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), (int)integer));
        nbt.put("RecipesUsed", nbtCompound);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP || side == Direction.DOWN) {
            return TOP_SLOTS;
        }
        return null;
    }


    @Override
    protected Component getDefaultName() {
        return getDisplayName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new MicrowaveScreenHandler(this, containerId, playerInventory, this, this.dataAccess);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && getRecipe(new SimpleContainer(stack)) == null;
    }

    public Recipe<?> getRecipe(Container inventory) {
        return this.level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, inventory, level).orElse(null).value();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.container) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.container.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack =  ContainerHelper.removeItem(this.container, slot, amount);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack =  ContainerHelper.takeItem(this.container, slot);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        this.setChanged();
        return stack;
    }

    public void fillStackedContents(StackedContents finder) {
        for (ItemStack itemStack : this.container) {
            finder.accountStack(itemStack);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = this.container.get(slot);
        boolean bl = !stack.isEmpty() && stack.is(itemStack.getItem()) && ItemStack.isSameItemSameTags(stack, itemStack);
        this.container.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == 0 && !bl) {
            this.cookTimeTotal = MicrowaveBlockEntity.getCookingTime(this.level, this.recipeType, this);
            this.cookTime = 0;
            this.setChanged();
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public RecipeEntry<?> getRecipeUsed() {
        return null;
    }

    public RecipeEntry<?> getRecipe() {
        return level.getRecipeManager().getRecipeFor(this.recipeType, this, level).orElse(null);
    }
    public static boolean canAcceptRecipeOutput(RegistryAccess registryManager, @Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getResultItem(registryManager);
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(0);
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxStackSize();
    }

    @Override
    public void clearContent() {
        this.container.clear();
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeEntry<?> recipe) {
        if (recipe != null) {
            ResourceLocation identifier = recipe.id();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    private static boolean craftRecipe(RegistryAccess recipeManager, @Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (recipe == null || !MicrowaveBlockEntity.canAcceptRecipeOutput(recipeManager, recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack2 = recipe.getResultItem(recipeManager);
        slots.set(0, itemStack2.copy());
        return true;
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(MicrowaveBlock.OPEN, open), Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, MicrowaveBlockEntity blockEntity) {
        boolean bl = blockEntity.isActive;
        boolean bl2 = false;
        ItemStack itemStack = blockEntity.container.get(0);
        if (blockEntity.isActive || !itemStack.isEmpty()) {
            RecipeEntry<? extends AbstractCookingRecipe> recipeEntry = level.getRecipeManager().getRecipeFor(blockEntity.recipeType, blockEntity, level).orElse(null);
            Recipe recipe = recipeEntry != null ? recipeEntry.value() : null;
            int i = blockEntity.getMaxStackSize();
            if (blockEntity.isActive && canAcceptRecipeOutput(level.registryAccess(), recipe, blockEntity.container, i)) {
                ++blockEntity.cookTime;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = getCookingTime(level, blockEntity.recipeType, blockEntity);
                    if (craftRecipe(level.registryAccess(),recipe, blockEntity.container, i)) {
                        blockEntity.setRecipeUsed(recipeEntry);
                        blockEntity.level.setBlock(pos, state = state.setValue(MicrowaveBlock.POWERED, false), Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
                        blockEntity.playSound(state, SoundIDs.MICROWAVE_BEEP_EVENT, 1);
                        blockEntity.setActiveonClient(blockEntity, false);
                        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                    }
                    bl2 = true;
                }
                else {
                    blockEntity.playSound(state, SoundIDs.MICROWAVE_RUNNING_EVENT, 1);
                }
            } else {
                blockEntity.cookTime = 0;
                if(itemStack.isEmpty()) {
                    blockEntity.setActiveonClient(blockEntity,false);
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                }
            }
        } else if (!blockEntity.isActive && blockEntity.cookTime > 0) {
            blockEntity.cookTime = Mth.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        }
        if (bl != blockEntity.isActive) {
            bl2 = true;
        }
        if (bl2) {
            setChanged(level, pos, state);
        }

    }

    public Direction getFacing() {
        return this.getBlockState().getValue(MicrowaveBlock.FACING);
    }

    public void setActive(boolean active) {
        this.isActive = active;
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putBoolean("isActive", active);
        this.saveAdditional(nbtCompound);
        this.setChanged();
        level.setBlock(getBlockPos(), this.getBlockState().setValue(MicrowaveBlock.POWERED, true), Block.UPDATE_CLIENTS);
    }

    @ExpectPlatform
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends MicrowaveBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}


