package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.MicrowaveBlock;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.SoundIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MicrowaveBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer, RecipeHolder, TickableBlockEntity {
    public boolean isActive = false;

    public MicrowaveBlockEntity() {
        super(BlockEntities.MICROWAVE_BLOCK_ENTITY);
        this.recipeType = RecipeType.SMOKING;
        level = this.getLevel();
    }

    //Slot 0 = input, 2 = output, 1 = fuel

    private int viewerCount;

    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            return true;
        }
        return super.triggerEvent(type, data);
    }

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
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }
            ++this.viewerCount;
            MicrowaveBlockEntity.this.playSound(getBlockState(), SoundEvents.IRON_TRAPDOOR_OPEN, 0);
            MicrowaveBlockEntity.this.setOpen(getBlockState(), true);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            --this.viewerCount;
            MicrowaveBlockEntity.this.playSound(getBlockState(), SoundEvents.IRON_TRAPDOOR_CLOSE, 0);
            MicrowaveBlockEntity.this.setOpen(getBlockState(), false);
        }
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.pfm.microwave");
    }

    private static int getCookingTime(Level level, RecipeType<? extends AbstractCookingRecipe> recipeType, Container inventory) {
        return level.getRecipeManager().getRecipeFor(recipeType, inventory, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
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
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        ContainerHelper.saveAllItems(nbt, this.container);
        CompoundTag nbtCompound = new CompoundTag();
        nbt.putBoolean("isActive", this.isActive);
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), (int)integer));
        nbt.put("RecipesUsed", nbtCompound);
        return nbt;
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
        return this.level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, inventory, level).orElse(null);
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
    public ItemStack getItem(int i) {
        return container.get(i);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack =  ContainerHelper.removeItem(this.container, slot, amount);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack =  ContainerHelper.takeItem(this.container, slot);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = this.container.get(slot);
        boolean bl = !stack.isEmpty() && stack.sameItem(itemStack) && ItemStack.tagMatches(stack, itemStack);
        this.container.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == 0 && !bl) {
            this.cookTimeTotal = MicrowaveBlockEntity.getCookingTime(this.level, this.recipeType, this);
            this.cookTime = 0;
            this.setChanged();
            level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public Recipe<?> getRecipe() {
        return level.getRecipeManager().getRecipeFor(this.recipeType, this, level).orElse(null);
    }
    public static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getResultItem();
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
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (recipe == null || !MicrowaveBlockEntity.canAcceptRecipeOutput(recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack2 = recipe.getResultItem();
        slots.set(0, itemStack2.copy());
        return true;
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(MicrowaveBlock.OPEN, open), 3);
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }


    public void tick() {
        boolean bl = this.isActive;
        boolean bl2 = false;
        ItemStack itemStack = this.container.get(0);
        if (this.isActive || !itemStack.isEmpty()) {
            Recipe recipe = level.getRecipeManager().getRecipeFor(this.recipeType, this, level).orElse(null);
            int i = this.getMaxStackSize();
            if (this.isActive && canAcceptRecipeOutput(recipe, this.container, i)) {
                ++this.cookTime;
                if (this.cookTime == this.cookTimeTotal) {
                    this.cookTime = 0;
                    this.cookTimeTotal = getCookingTime(level, this.recipeType, this);
                    if (craftRecipe(recipe, this.container, i)) {
                        this.setRecipeUsed(recipe);
                        this.level.setBlock(worldPosition, getBlockState().setValue(MicrowaveBlock.POWERED, false), 3);
                        this.playSound(getBlockState(), SoundIDs.MICROWAVE_BEEP_EVENT, 1);
                        this.setActiveonClient(this, false);
                        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                    }
                    bl2 = true;
                }
                else {
                    this.playSound(getBlockState(), SoundIDs.MICROWAVE_RUNNING_EVENT, 1);
                }
            } else {
                this.cookTime = 0;
                if(itemStack.isEmpty()) {
                    this.setActiveonClient(this,false);
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        } else if (!this.isActive && this.cookTime > 0) {
            this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
        }
        if (bl != this.isActive) {
            bl2 = true;
        }
        if (bl2) {
            setChanged();
        }

    }

    public Direction getFacing() {
        return this.getBlockState().getValue(MicrowaveBlock.FACING);
    }

    public void setActive(boolean active) {
        this.isActive = active;
        CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putBoolean("isActive", active);
        this.save(nbtCompound);
        this.setChanged();
        level.setBlock(getBlockPos(), this.getBlockState().setValue(MicrowaveBlock.POWERED, true), 3);
    }

    @ExpectPlatform
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Supplier<? extends MicrowaveBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}


