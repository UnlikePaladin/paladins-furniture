package com.unlikepaladin.pfm.blocks.blockentities;

import com.google.common.collect.Maps;
import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class FreezerBlockEntity extends LockableContainerBlockEntity implements NamedScreenHandlerFactory, SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {
    public FreezerBlockEntity() {
        super(BlockEntities.FREEZER_BLOCK_ENTITY);
        this.recipeType = RecipeTypes.FREEZING_RECIPE;
    }

    public static int countViewers(World world, LockableContainerBlockEntity inventory, int x, int y, int z) {
        int i = 0;
        float f = 5.0f;
        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, new Box((float)x - 5.0f, (float)y - 5.0f, (float)z - 5.0f, (float)(x + 1) + 5.0f, (float)(y + 1) + 5.0f, (float)(z + 1) + 5.0f));
        for (PlayerEntity playerEntity : list) {
            Inventory inventory2;
            if (!(playerEntity.currentScreenHandler instanceof GenericContainerScreenHandler) || (inventory2 = ((GenericContainerScreenHandler)playerEntity.currentScreenHandler).getInventory()) != inventory && (!(inventory2 instanceof DoubleInventory) || !((DoubleInventory)inventory2).isPart(inventory))) continue;
            ++i;
        }
        return i;
    }

    private int viewerCount;
    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }
            ++this.viewerCount;
            FreezerBlockEntity.this.playSound(getCachedState(), SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
            FreezerBlockEntity.this.setOpen(getCachedState(), true);
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            --this.viewerCount;
            FreezerBlockEntity.this.playSound(this.getCachedState(), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
            FreezerBlockEntity.this.setOpen(this.getCachedState(), false);
        }
    }


    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1, 0};
    private static final int[] SIDE_SLOTS = new int[]{1};
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    int fuelTime;
    int fuelTimeTotal;
    int freezeTime;
    int freezeTimeTotal;
    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {

        @Override
        public int get(int index) {
            switch (index) {
                case 0: {
                    return FreezerBlockEntity.this.fuelTime;
                }
                case 1: {
                    return FreezerBlockEntity.this.fuelTimeTotal;
                }
                case 2: {
                    return FreezerBlockEntity.this.freezeTime;
                }
                case 3: {
                    return FreezerBlockEntity.this.freezeTimeTotal;
                }
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0: {
                    FreezerBlockEntity.this.fuelTime = value;
                    break;
                }
                case 1: {
                    FreezerBlockEntity.this.fuelTimeTotal = value;
                    break;
                }
                case 2: {
                    FreezerBlockEntity.this.freezeTime = value;
                    break;
                }
                case 3: {
                    FreezerBlockEntity.this.freezeTimeTotal = value;
                    break;
                }
            }
        }


        @Override
        public int size() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap();
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    public static Map<Item, Integer> createFuelTimeMap() {
        LinkedHashMap<Item, Integer> map = Maps.newLinkedHashMap();
        FreezerBlockEntity.addFuel(map, Items.SNOWBALL, 50);
        FreezerBlockEntity.addFuel(map, Items.SNOW, 62);
        FreezerBlockEntity.addFuel(map, Items.SNOW_BLOCK, 400);
        FreezerBlockEntity.addFuel(map, Items.ICE, 1600);
        FreezerBlockEntity.addFuel(map, Items.PACKED_ICE, 14400);
        FreezerBlockEntity.addFuel(map, Items.BLUE_ICE, 129600);
        return map;
    }
    private boolean isActive() {
        return this.fuelTime > 0;
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, Tag<Item> tag, int fuelTime) {
        for (Item item : tag.values()) {
            fuelTimes.put(item, fuelTime);
        }
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        fuelTimes.put(item2, fuelTime);
    }
    private static int getFreezeTime(World world, RecipeType<? extends AbstractCookingRecipe> recipeType, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(recipeType, inventory, world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return FreezerBlockEntity.createFuelTimeMap().containsKey(stack.getItem());
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        }
        if (slot == 1) {
            ItemStack itemStack = this.inventory.get(1);
            return FreezerBlockEntity.canUseAsFuel(stack);
        }
        return true;
    }
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot != 2) {
            return stack.getItem() == (Items.BUCKET);
        }
        return true;
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
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
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
            this.freezeTimeTotal = FreezerBlockEntity.getFreezeTime(this.world, this.recipeType, this);
            this.freezeTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        }
        return player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    @Override
    @Nullable
    public Recipe<?> getLastRecipe() {
        return null;
    }


    @Override
        public int size() {
            return 3;
        }


    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        Item item = fuel.getItem();
        return FreezerBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.fuelTime = nbt.getShort("FuelTimeLeft");
        this.freezeTime = nbt.getShort("FreezeTime");
        this.freezeTimeTotal = nbt.getShort("FreezeTimeTotal");
        this.fuelTimeTotal = this.getFuelTime(this.inventory.get(1));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putShort("FuelTimeLeft", (short)this.fuelTime);
        nbt.putShort("FreezeTime", (short)this.freezeTime);
        nbt.putShort("FreezeTimeTotal", (short)this.freezeTimeTotal);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), integer));
        nbt.put("RecipesUsed", nbtCompound);
        return nbt;
    }


    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(FreezerBlock.OPEN, open), 3);
    }


    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(FreezerBlock.FACING).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container.pfm.freezer");
    }

    @Override
    protected Text getContainerName() {
        return getDisplayName();
    }


    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getOutput();
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(2);
        if (itemStack2.isEmpty()) {
            return true;
        }
        if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
            return false;
        }
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxCount();
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe == null || !FreezerBlockEntity.canAcceptRecipeOutput(recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack = slots.get(0);
        ItemStack itemStack2 = recipe.getOutput();
        ItemStack itemStack3 = slots.get(2);
        if (itemStack2.getItem() == (Items.OBSIDIAN) || itemStack2.getItem() == (Items.ICE) || itemStack2.getItem() == (Items.BLUE_ICE)) {
            slots.set(0, new ItemStack(Items.BUCKET));
        }
        if (itemStack2.getItem() == (Items.SNOWBALL)) {
            slots.set(0, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (itemStack3.isEmpty()) {
            slots.set(2, itemStack2.copy());
        } else if (itemStack3.isItemEqual(itemStack2)) {
            itemStack3.increment(1);
        }
        itemStack.decrement(1);
        return true;
    }
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return createScreenHandler(syncId, inv);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FreezerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void tick() {
        boolean bl = this.isActive();
        boolean bl2 = false;
        if (this.isActive()) {
            --this.fuelTime;
        }
        ItemStack itemStack = this.inventory.get(1);
        if (this.isActive() || !itemStack.isEmpty() && !this.inventory.get(0).isEmpty()) {
            Recipe recipe = world.getRecipeManager().getFirstMatch(this.recipeType, this, world).orElse(null);
            int i = this.getMaxCountPerStack();
            if (!this.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(recipe, this.inventory, i)) {
                this.fuelTimeTotal = this.fuelTime = this.getFuelTime(itemStack);
                if (this.isActive()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            this.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
            if (this.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(recipe, this.inventory, i)) {
                ++this.freezeTime;
                if (this.freezeTime == this.freezeTimeTotal) {
                    this.freezeTime = 0;
                    this.freezeTimeTotal = FreezerBlockEntity.getFreezeTime(world, this.recipeType, this);
                    if (FreezerBlockEntity.craftRecipe(recipe, this.inventory, i)) {
                        this.setLastRecipe(recipe);
                    }
                    bl2 = true;
                }
            } else {
                this.freezeTime = 0;
            }
        } else if (!this.isActive() && this.freezeTime > 0) {
            this.freezeTime = MathHelper.clamp(this.freezeTime - 2, 0, this.freezeTimeTotal);
        }
        if (bl != this.isActive()) {
            bl2 = true;
        }
        if (bl2) {
            this.markDirty();
        }
    }

    @ExpectPlatform
    public static Supplier<? extends FreezerBlockEntity> getFactory() {
        throw new AssertionError();
    }
}

