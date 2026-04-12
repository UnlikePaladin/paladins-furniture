package com.unlikepaladin.pfm.blocks.blockentities;

import com.google.common.collect.Maps;
import com.unlikepaladin.pfm.blocks.FreezerBlock;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
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
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class FreezerBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible {
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.FREEZER_BLOCK_ENTITY, pos, state);
        this.recipeType = RecipeTypes.FREEZING_RECIPE;
        this.matchGetter = RecipeManager.createCheck(recipeType);
    }
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {


        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FreezerBlock) {
                FreezerBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
                FreezerBlockEntity.this.setOpen(state, true);
            }
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            if (state.getBlock() instanceof FreezerBlock) {
                FreezerBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
                FreezerBlockEntity.this.setOpen(state, false);
            }
        }


        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {

        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof FreezerScreenHandler) {
                Container inventory = ((FreezerScreenHandler)player.containerMenu).getContainer();
                return inventory == FreezerBlockEntity.this;
            }
            return false;
        }
    };


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

    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (Holder<Item> registryEntry : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            fuelTimes.put(registryEntry.value(), fuelTime);
        }
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemLike item, int fuelTime) {
        Item item2 = item.asItem();
        fuelTimes.put(item2, fuelTime);
    }
    private static int getFreezeTime(Level world, RecipeType<? extends AbstractCookingRecipe> recipeType, Container inventory) {
        return world.getRecipeManager().getRecipeFor(recipeType, new SingleRecipeInput(inventory.getItem(0)), world).map(RecipeHolder::value).map(AbstractCookingRecipe::getCookingTime).orElse(200);
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
            return stack.is(Items.BUCKET) || stack.is(Items.GLASS_BOTTLE);
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
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, stack);
        this.inventory.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (slot == 0 && !bl) {
            this.freezeTimeTotal = getFreezeTime(this.level, this.recipeType, this);
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
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceLocation identifier = recipe.id();
            this.recipesUsed.addTo(identifier, 1);
        }
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
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
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
        this.fuelTime = nbt.getShort("FuelTimeLeft");
        this.freezeTime = nbt.getShort("FreezeTime");
        this.freezeTimeTotal = nbt.getShort("FreezeTimeTotal");
        this.fuelTimeTotal = this.getFuelTime(this.inventory.get(1));
        CompoundTag nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.parse(string), nbtCompound.getInt(string));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
        nbt.putShort("FuelTimeLeft", (short)this.fuelTime);
        nbt.putShort("FreezeTime", (short)this.freezeTime);
        nbt.putShort("FreezeTimeTotal", (short)this.freezeTimeTotal);
        CompoundTag nbtCompound = new CompoundTag();
        this.recipesUsed.forEach((identifier, integer) -> nbtCompound.putInt(identifier.toString(), integer));
        nbt.put("RecipesUsed", nbtCompound);
    }


    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(FreezerBlock.OPEN, open), Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
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
        return Component.translatable("container.pfm.freezer");
    }

    @Override
    protected Component getDefaultName() {
        return getDisplayName();
    }


    private static boolean canAcceptRecipeOutput(RegistryAccess registryManager, @Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (slots.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getResultItem(registryManager);
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = slots.get(2);
        if (itemStack2.isEmpty()) {
            return true;
        }
        if (!itemStack2.is(itemStack.getItem())) {
            return false;
        }
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxStackSize();
    }

    private static boolean craftRecipe(RegistryAccess registryManager, @Nullable Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        if (recipe == null || !FreezerBlockEntity.canAcceptRecipeOutput(registryManager,recipe, slots, count)) {
            return false;
        }
        ItemStack itemStack = slots.get(0);
        ItemStack itemStack2 = recipe.getResultItem(registryManager);
        ItemStack itemStack3 = slots.get(2);
        if (itemStack2.is(Items.OBSIDIAN) || itemStack2.is(Items.ICE) || itemStack2.is(Items.BLUE_ICE)) {
            slots.set(0, new ItemStack(Items.BUCKET));
        }
        if (itemStack2.getItem() == (Items.SNOWBALL)) {
            slots.set(0, new ItemStack(Items.GLASS_BOTTLE));
        }
        if (itemStack3.isEmpty()) {
            slots.set(2, itemStack2.copy());
        } else if (itemStack3.is(itemStack2.getItem())) {
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, FreezerBlockEntity blockEntity) {
        boolean bl = blockEntity.isActive();
        boolean bl2 = false;
        if (blockEntity.isActive()) {
            --blockEntity.fuelTime;
        }
        ItemStack itemStack = blockEntity.inventory.get(1);
        if (blockEntity.isActive() || !itemStack.isEmpty() && !blockEntity.inventory.get(0).isEmpty()) {
            RecipeHolder<?> recipEntry = blockEntity.matchGetter.getRecipeFor(new SingleRecipeInput(blockEntity.getItem(0)), level).orElse(null);
            Recipe recipe = recipEntry != null ? recipEntry.value() : null;
            int i = blockEntity.getMaxStackSize();
            if (!blockEntity.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(level.registryAccess(), recipe, blockEntity.inventory, i)) {
                blockEntity.fuelTimeTotal = blockEntity.fuelTime = blockEntity.getFuelTime(itemStack);
                if (blockEntity.isActive()) {
                    bl2 = true;
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getCraftingRemainingItem();
                            blockEntity.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }
            if (blockEntity.isActive() && FreezerBlockEntity.canAcceptRecipeOutput(level.registryAccess(), recipe, blockEntity.inventory, i)) {
                ++blockEntity.freezeTime;
                if (blockEntity.freezeTime == blockEntity.freezeTimeTotal) {
                    blockEntity.freezeTime = 0;
                    blockEntity.freezeTimeTotal = FreezerBlockEntity.getFreezeTime(level, blockEntity.recipeType, blockEntity);
                    if (FreezerBlockEntity.craftRecipe(level.registryAccess(),recipe, blockEntity.inventory, i)) {
                        blockEntity.setRecipeUsed(recipEntry);
                    }
                    bl2 = true;
                }
            } else {
                blockEntity.freezeTime = 0;
            }
        } else if (!blockEntity.isActive() && blockEntity.freezeTime > 0) {
            blockEntity.freezeTime = Mth.clamp(blockEntity.freezeTime - 2, 0, blockEntity.freezeTimeTotal);
        }
        if (bl != blockEntity.isActive()) {
            bl2 = true;
        }
        if (bl2) {
            FreezerBlockEntity.setChanged(level, pos, state);
        }
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends FreezerBlockEntity> getFactory() {
        throw new AssertionError();
    }
}

