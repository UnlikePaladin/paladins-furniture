package com.unlikepaladin.pfm.blocks.blockentities;

import com.google.common.collect.Maps;
import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;


public class FreezerBlockEntity extends LockableContainerBlockEntity implements NamedScreenHandlerFactory, SidedInventory, RecipeUnlocker,
        RecipeInputProvider {
    public FreezerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.recipeType = RecipeTypes.FREEZING_RECIPE;
    }
    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FREEZER_BLOCK_ENTITY, pos, state);
        this.recipeType = RecipeTypes.FREEZING_RECIPE;
    }
    private final ViewerCountManager stateManager = new ViewerCountManager() {


        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FreezerBlock) {
                FreezerBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
                FreezerBlockEntity.this.setOpen(state, true);
            }
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FreezerBlock) {
                FreezerBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
                FreezerBlockEntity.this.setOpen(state, false);
            }
        }


        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof FreezerScreenHandler) {
                Inventory inventory = ((FreezerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == FreezerBlockEntity.this;
            }
            return false;
        }
    };


    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }


    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
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
        if (dir == Direction.DOWN && slot == 1) {
            return stack.isOf(Items.BUCKET);
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
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
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
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.fuelTime = nbt.getShort("BurnTime");
        this.freezeTime = nbt.getShort("CookTime");
        this.freezeTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTimeTotal = this.getFuelTime(this.inventory.get(1));
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("FuelTimeLeft", (short)this.fuelTime);
        nbt.putShort("FreezeTime", (short)this.freezeTime);
        nbt.putShort("FreezeTimeTotal", (short)this.freezeTimeTotal);
        Inventories.writeNbt(nbt, this.inventory);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), (int)integer));
        nbt.put("RecipesUsed", nbtCompound);
        return nbt;
    }


    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(FreezerBlock.OPEN, open), Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
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
        if (itemStack2.isOf(Items.OBSIDIAN) || itemStack2.isOf(Items.ICE) || itemStack2.isOf(Items.BLUE_ICE)) {
            slots.set(0, new ItemStack(Items.BUCKET));
        }
        if (itemStack2.getItem() == (Items.SNOWBALL)) {
            slots.set(0, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (itemStack3.isEmpty()) {
            slots.set(2, itemStack2.copy());
        } else if (itemStack3.isOf(itemStack2.getItem())) {
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

    public static void tick(World world, BlockPos pos, BlockState state, FreezerBlockEntity blockEntity) {
        boolean bl = blockEntity.isActive();
        boolean bl2 = false;
        if (blockEntity.isActive()) {
            --blockEntity.fuelTime;
        }
        ItemStack itemStack = blockEntity.inventory.get(1);
        if (blockEntity.isActive() || !itemStack.isEmpty() && !blockEntity.inventory.get(0).isEmpty()) {
            Recipe recipe = world.getRecipeManager().getFirstMatch(blockEntity.recipeType, blockEntity, world).orElse(null);
            int i = blockEntity.getMaxCountPerStack();
            if (!blockEntity.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                blockEntity.fuelTimeTotal = blockEntity.fuelTime = blockEntity.getFuelTime(itemStack);
                if (blockEntity.isActive()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getRecipeRemainder();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
            if (blockEntity.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(recipe, blockEntity.inventory, i)) {
                ++blockEntity.freezeTime;
                if (blockEntity.freezeTime == blockEntity.freezeTimeTotal) {
                    blockEntity.freezeTime = 0;
                    blockEntity.freezeTimeTotal = FreezerBlockEntity.getFreezeTime(world, blockEntity.recipeType, blockEntity);
                    if (FreezerBlockEntity.craftRecipe(recipe, blockEntity.inventory, i)) {
                        blockEntity.setLastRecipe(recipe);
                    }
                    bl2 = true;
                }
            } else {
                blockEntity.freezeTime = 0;
            }
        } else if (!blockEntity.isActive() && blockEntity.freezeTime > 0) {
            blockEntity.freezeTime = MathHelper.clamp(blockEntity.freezeTime - 2, 0, blockEntity.freezeTimeTotal);
        }
        if (bl != blockEntity.isActive()) {
            bl2 = true;
        }
        if (bl2) {
            FreezerBlockEntity.markDirty(world, pos, state);
        }
    }

}

