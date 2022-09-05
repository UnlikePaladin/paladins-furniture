package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.Microwave;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MicrowaveBlockEntity extends LockableContainerBlockEntity implements NamedScreenHandlerFactory, SidedInventory, RecipeUnlocker, Tickable {
    public boolean isActive = false;
    public MicrowaveBlockEntity() {
        super(BlockEntities.MICROWAVE_BLOCK_ENTITY);
        this.recipeType = RecipeType.SMOKING;
        world = this.getWorld();
    }

    //Slot 0 = input, 2 = output, 1 = fuel

    private int viewerCount;

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    void playSound(BlockState state, SoundEvent soundEvent, int pitch) {
        Vec3i vec3i = state.get(Microwave.FACING).getVector();
        double d = (double) this.pos.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double e = (double) this.pos.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double f = (double) this.pos.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        float i = pitch == 0 ? (i = this.world.random.nextFloat() * 0.2f + 0.9f) : (i = pitch);
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, i);
    }

    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{1};
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    int cookTime;
    int cookTimeTotal;

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {

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
        public int size() {
            return 2;
        }
    };
    private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap();
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }
            ++this.viewerCount;
            MicrowaveBlockEntity.this.playSound(getCachedState(), SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 0);
            MicrowaveBlockEntity.this.setOpen(getCachedState(), true);
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            --this.viewerCount;
            MicrowaveBlockEntity.this.playSound(getCachedState(), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, 0);
            MicrowaveBlockEntity.this.setOpen(getCachedState(), false);
        }
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container.pfm.microwave");
    }

    private static int getCookTime(World world, RecipeType<? extends AbstractCookingRecipe> recipeType, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(recipeType, inventory, world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        this.isActive = nbt.getBoolean("isActive");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory);
        NbtCompound nbtCompound = new NbtCompound();
        nbt.putBoolean("isActive", this.isActive);
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), (int)integer));
        nbt.put("RecipesUsed", nbtCompound);
        return nbt;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        return null;
    }


    @Override
    protected Text getContainerName() {
        return getDisplayName();
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new MicrowaveScreenHandler(this,syncId, playerInventory, this, this.propertyDelegate);
    }


    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack =  Inventories.splitStack(this.inventory, slot, amount);
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
        this.markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack =  Inventories.removeStack(this.inventory, slot);
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
        this.markDirty();
        return stack;
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        if (slot == 0 && !bl) {
            this.cookTimeTotal = MicrowaveBlockEntity.getCookTime(this.world, this.recipeType, this);
            this.cookTime = 0;
            this.markDirty();
            world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        }
        return player.squaredDistanceTo((double) this.pos.getX() + 0.5, (double) this.pos.getY() + 0.5, (double) this.pos.getZ() + 0.5) <= 64.0;
    }

    @Nullable
    @Override
    public Recipe<?> getLastRecipe() {
        return null;
    }

    public Recipe<?> getRecipe() {
        return world.getRecipeManager().getFirstMatch(this.recipeType, this, world).orElse(null);
    }
    public static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getOutput();
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(0);
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxCount();
    }

    @Override
    public void clear() {
        this.inventory.clear();
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe == null || !MicrowaveBlockEntity.canAcceptRecipeOutput(recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack2 = recipe.getOutput();
        slots.set(0, itemStack2.copy());
        return true;
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(Microwave.OPEN, open), 3);
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }


    public void tick() {
        boolean bl = this.isActive;
        boolean bl2 = false;
        ItemStack itemStack = this.inventory.get(0);
        if (this.isActive || !itemStack.isEmpty()) {
            Recipe recipe = world.getRecipeManager().getFirstMatch(this.recipeType, this, world).orElse(null);
            int i = this.getMaxCountPerStack();
            if (this.isActive && canAcceptRecipeOutput(recipe, this.inventory, i)) {
                ++this.cookTime;
                if (this.cookTime == this.cookTimeTotal) {
                    this.cookTime = 0;
                    this.cookTimeTotal = getCookTime(world, this.recipeType, this);
                    if (craftRecipe(recipe, this.inventory, i)) {
                        this.setLastRecipe(recipe);
                        this.world.setBlockState(pos, getCachedState().with(Microwave.POWERED, false), 3);
                        this.playSound(getCachedState(), SoundIDs.MICROWAVE_BEEP_EVENT, 1);
                        this.setActiveonClient(this, false);
                        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                    }
                    bl2 = true;
                }
                else {
                    this.playSound(getCachedState(), SoundIDs.MICROWAVE_RUNNING_EVENT, 1);
                }
            } else {
                this.cookTime = 0;
                if(itemStack.isEmpty()) {
                    this.setActiveonClient(this,false);
                    world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                }
            }
        } else if (!this.isActive && this.cookTime > 0) {
            this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
        }
        if (bl != this.isActive) {
            bl2 = true;
        }
        if (bl2) {
            markDirty();
        }

    }

    public Direction getFacing() {
        return this.getCachedState().get(Microwave.FACING);
    }

    public void setActive(boolean active) {
        this.isActive = active;
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("isActive", active);
        this.writeNbt(nbtCompound);
        this.markDirty();
        world.setBlockState(pos, this.getCachedState().with(Microwave.POWERED, true), 3);
    }

    @ExpectPlatform
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        throw new AssertionError();
    }
}


