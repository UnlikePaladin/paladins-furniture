package com.unlikepaladin.pfm.blocks.blockentities;

import com.google.common.collect.Maps;
import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class FreezerBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer, RecipeHolder, StackedContentsCompatible, TickableBlockEntity {
    public FreezerBlockEntity() {
        super(BlockEntities.FREEZER_BLOCK_ENTITY, pos, state);
        this.recipeType = RecipeTypes.FREEZING_RECIPE;
    }

    public static int countViewers(Level world, BaseContainerBlockEntity inventory, int x, int y, int z) {
        int i = 0;
        float f = 5.0f;
        List<Player> list = world.getNonSpectatingEntities(PlayerEntity.class, new Box((float)x - 5.0f, (float)y - 5.0f, (float)z - 5.0f, (float)(x + 1) + 5.0f, (float)(y + 1) + 5.0f, (float)(z + 1) + 5.0f));
        for (Player playerEntity : list) {
            Inventory inventory2;
            if (!(playerEntity.containerMenu instanceof ChestMenu) || (inventory2 = ((ChestMenu)playerEntity.containerMenu).getInventory()) != inventory && (!(inventory2 instanceof CompoundContainer) || !((CompoundContainer)inventory2).contains(inventory))) continue;
            ++i;
        }
        return i;
    }

    private int viewerCount;
    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            return true;
        }
        return super.triggerEvent(type, data);
    }

    @Override
    public void startOpen(Player player) {
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
    public void stopOpen(Player player) {
        if (!this.removed && !player.isSpectator()) {
            --this.viewerCount;
            FreezerBlockEntity.this.playSound(this.getCachedState(), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
            FreezerBlockEntity.this.setOpen(this.getCachedState(), false);
        }
    }


    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1, 0};
    private static final int[] SIDE_SLOTS = new int[]{1};
    private NonNullList<ItemStack> inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    int fuelTime;
    int fuelTimeTotal;
    int freezeTime;
    int freezeTimeTotal;
    protected final ContainerData dataAccess = new ContainerData() {

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
        public int getCount() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap();
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
        for (Item item : tag.getValues()) {
            fuelTimes.put(item, fuelTime);
        }
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemLike item, int fuelTime) {
        Item item2 = item.asItem();
        fuelTimes.put(item2, fuelTime);
    }
    private static int getFreezeTime(Level world, RecipeType<? extends AbstractCookingRecipe> recipeType, Container inventory) {
        return world.getRecipeManager().getRecipeFor(recipeType, inventory, world).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return FreezerBlockEntity.createFuelTimeMap().containsKey(stack.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        }
        if (slot == 1) {
            ItemStack itemStack = this.inventory.get(1);
            return FreezerBlockEntity.canUseAsFuel(stack);
        }
        return true;
    }
    public void fillStackedContents(StackedContents finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.accountStack(itemStack);
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot != 2) {
            return stack.getItem() == (Items.BUCKET) || stack.getItem() == (Items.GLASS_BOTTLE);
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

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.inventory, slot);
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.sameItem(itemStack) && ItemStack.tagMatches(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == 0 && !bl) {
            this.freezeTimeTotal = FreezerBlockEntity.getFreezeTime(this.level, this.recipeType, this);
            this.freezeTime = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }


    @Override
    public int getContainerSize() {
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
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
        this.fuelTime = nbt.getShort("FuelTimeLeft");
        this.freezeTime = nbt.getShort("FreezeTime");
        this.freezeTimeTotal = nbt.getShort("FreezeTimeTotal");
        this.fuelTimeTotal = this.getFuelTime(this.inventory.get(1));
        CompoundTag nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(string), nbtCompound.getInt(string));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putShort("FuelTimeLeft", (short)this.fuelTime);
        nbt.putShort("FreezeTime", (short)this.freezeTime);
        nbt.putShort("FreezeTimeTotal", (short)this.freezeTimeTotal);
        CompoundTag nbtCompound = new CompoundTag();
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), integer));
        nbt.put("RecipesUsed", nbtCompound);
        return nbt;
    }


    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(FreezerBlock.OPEN, open), 3);
    }


    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.getValue(FreezerBlock.FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.pfm.freezer");
    }

    @Override
    protected Component getDefaultName() {
        return getDisplayName();
    }


    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getResultItem();
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(2);
        if (itemStack2.isEmpty()) {
            return true;
        }
        if (!itemStack2.sameItem(itemStack)) {
            return false;
        }
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxStackSize();
    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (recipe == null || !FreezerBlockEntity.canAcceptRecipeOutput(recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack = slots.get(0);
        ItemStack itemStack2 = recipe.getResultItem();
        ItemStack itemStack3 = slots.get(2);
        if (itemStack2.getItem() == (Items.OBSIDIAN) || itemStack2.getItem() == (Items.ICE) || itemStack2.getItem() == (Items.BLUE_ICE)) {
            slots.set(0, new ItemStack(Items.BUCKET));
        }
        if (itemStack2.getItem() == (Items.SNOWBALL)) {
            slots.set(0, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (itemStack3.isEmpty()) {
            slots.set(2, itemStack2.copy());
        } else if (itemStack3.sameItemStackIgnoreDurability(itemStack2)) {
            itemStack3.grow(1);
        }
        itemStack.shrink(1);
        return true;
    }
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return createMenu(containerId, inv);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new FreezerScreenHandler(containerId, playerInventory, this, this.dataAccess);
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
            Recipe recipe = world.getRecipeManager().getRecipeFor(this.recipeType, this, world).orElse(null);
            int i = this.getMaxStackSize();
            if (!this.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(recipe, this.inventory, i)) {
                this.fuelTimeTotal = this.fuelTime = this.getFuelTime(itemStack);
                if (this.isActive()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getCraftingRemainingItem();
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
                        this.setRecipeUsed(recipe);
                    }
                    bl2 = true;
                }
            } else {
                this.freezeTime = 0;
            }
        } else if (!this.isActive() && this.freezeTime > 0) {
            this.freezeTime = Mth.clamp(this.freezeTime - 2, 0, this.freezeTimeTotal);
        }
        if (bl != this.isActive()) {
            bl2 = true;
        }
        if (bl2) {
            this.setChanged();
        }
    }

    @ExpectPlatform
    public static Supplier<? extends FreezerBlockEntity> getFactory() {
        throw new AssertionError();
    }
}

