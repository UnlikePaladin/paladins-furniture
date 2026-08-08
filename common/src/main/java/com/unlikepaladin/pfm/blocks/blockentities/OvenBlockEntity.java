package com.unlikepaladin.pfm.blocks.blockentities;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class OvenBlockEntity extends BaseContainerBlockEntity implements Container, WorldlyContainer, StackedContentsCompatible {

    // slot layout: 0-2 inputs, 3-11 processing (9), 12-14 outputs, 15 fuel
    public static final int INPUT_COUNT = 3;
    public static final int PROCESSING_COUNT = 9;
    public static final int OUTPUT_COUNT = 3;
    public static final int FUEL_COUNT = 1;
    public static final int TOTAL_SLOTS = INPUT_COUNT + PROCESSING_COUNT + OUTPUT_COUNT + FUEL_COUNT; // 16
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;
    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);

    final ContainerData dataAccess = new ContainerData() {
        // layout:
        // 0 = furnaceBurnTime (current burn left)
        // 1 = currentItemBurnTime (burn duration of current fuel)
        // per-processing-slot pairs start at index 2:
        // 2 + slot*2     = slotCookTime[slot]
        // 2 + slot*2 + 1 = slotCookTimeTotal[slot]
        public int get(int id) {
            if (id == 0) return OvenBlockEntity.this.furnaceBurnTime;
            if (id == 1) return OvenBlockEntity.this.currentItemBurnTime;
            int idx = id - 2;
            if (idx >= 0) {
                int slot = idx / 2;
                boolean isTotal = (idx % 2) == 1;
                if (slot >= 0 && slot < OvenBlockEntity.this.slotCookTime.length) {
                    return isTotal ? OvenBlockEntity.this.slotCookTimeTotal[slot] : OvenBlockEntity.this.slotCookTime[slot];
                }
            }
            return 0;
        }

        public void set(int id, int value) {
            if (id == 0) {
                OvenBlockEntity.this.furnaceBurnTime = value;
                return;
            }
            if (id == 1) {
                OvenBlockEntity.this.currentItemBurnTime = value;
                return;
            }
            int idx = id - 2;
            if (idx >= 0) {
                int slot = idx / 2;
                boolean isTotal = (idx % 2) == 1;
                if (slot >= 0 && slot < OvenBlockEntity.this.slotCookTime.length) {
                    if (isTotal) OvenBlockEntity.this.slotCookTimeTotal[slot] = value;
                    else OvenBlockEntity.this.slotCookTime[slot] = value;
                }
            }
        }

        public int getCount() {
            return 2 + PROCESSING_COUNT * 2;
        }
    };

    protected int furnaceBurnTime;
    protected int currentItemBurnTime;
    protected int[] slotCookTime;
    protected int[] slotCookTimeTotal;
    protected double burnTimeRemainder = 0.0D;
    protected double[] slotCookTimeRemainder;
    private final ResourceKey<Recipe<?>>[] slotRecipes;
    private final Object2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Object2IntOpenHashMap<>();
    private SingleRecipeInput singleSlotRecipeWrapper;
    protected NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    public OvenBlockEntity(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.slotCookTime = new int[PROCESSING_COUNT];
        this.slotCookTimeTotal = new int[PROCESSING_COUNT];
        this.slotCookTimeRemainder = new double[PROCESSING_COUNT];
        this.slotRecipes = new ResourceKey[PROCESSING_COUNT];
        for (int i = 0; i < this.slotCookTimeTotal.length; i++) this.slotCookTimeTotal[i] = 200;
        this.singleSlotRecipeWrapper = new SingleRecipeInput(ItemStack.EMPTY);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMOKING);
    }

    public OvenBlockEntity(BlockPos blockPos, BlockState state) {
        this(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, blockPos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.pfm.oven");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        // pass both the block-entity (stoveBlockEntity parameter) and this as the backing Container
        return new OvenScreenHandler(ScreenHandlerIDs.OVEN_SCREEN_HANDLER, containerId, playerInventory, this, this.dataAccess);
    }


    protected void onContainerOpen(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof KitchenCounterOvenBlock){
            OvenBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            OvenBlockEntity.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof KitchenCounterOvenBlock) {
            OvenBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            OvenBlockEntity.this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            this.onContainerClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        return;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < this.items.size()) {
            return this.items.get(slotIndex);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.items, i, j);
        if (!itemStack.isEmpty() && i >= INPUT_COUNT && i < INPUT_COUNT + PROCESSING_COUNT) {
            int slotIdx = i - INPUT_COUNT;
            if (this.getItem(i).isEmpty()) {
                this.slotCookTime[slotIdx] = 0;
                this.slotCookTimeRemainder[slotIdx] = 0.0D;
                this.slotRecipes[slotIdx] = null;
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = ContainerHelper.takeItem(this.items, i);
        if (i >= INPUT_COUNT && i < INPUT_COUNT + PROCESSING_COUNT) {
            int slotIdx = i - INPUT_COUNT;
            this.slotCookTime[slotIdx] = 0;
            this.slotCookTimeRemainder[slotIdx] = 0.0D;
            this.slotRecipes[slotIdx] = null;
        }
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < this.items.size()) {
            this.items.set(i, itemStack);
            if (i >= INPUT_COUNT && i < INPUT_COUNT + PROCESSING_COUNT) {
                int slotIdx = i - INPUT_COUNT;
                if (itemStack.isEmpty()) {
                    this.slotCookTime[slotIdx] = 0;
                    this.slotCookTimeRemainder[slotIdx] = 0.0D;
                    this.slotRecipes[slotIdx] = null;
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) return false;
        if (this.level.getBlockEntity(this.worldPosition) != this) return false;
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void startOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
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

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return createMenu(containerId, inv);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        // Expose only inputs, outputs and fuel to automation. Processing slots are internal
        // and should never be accessible from hoppers / pipes.
        if (direction == Direction.UP) {
            // top -> inputs (0..2)
            int[] a = new int[INPUT_COUNT];
            for (int i = 0; i < INPUT_COUNT; i++) a[i] = i;
            return a;
        }

        if (direction == Direction.DOWN) {
            // bottom -> outputs only (12..14)
            int[] a = new int[OUTPUT_COUNT];
            for (int i = 0; i < OUTPUT_COUNT; i++) a[i] = INPUT_COUNT + PROCESSING_COUNT + i;
            return a;
        }

        // sides -> fuel only
        int[] a = new int[FUEL_COUNT];
        a[0] = TOTAL_SLOTS - 1; // fuel index
        return a;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        // only allow insertion into input slots or fuel slot via automation
        if (i >= 0 && i < INPUT_COUNT) return true;
        if (i == TOTAL_SLOTS - 1) return level.fuelValues().isFuel(itemStack);
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        // allow extraction only from output slots and possibly fuel
        int outputStart = INPUT_COUNT + PROCESSING_COUNT;
        if (i >= outputStart && i < outputStart + OUTPUT_COUNT) return true;
        return false;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        Arrays.fill(this.slotCookTime, 0);
        Arrays.fill(this.slotCookTimeRemainder, 0.0D);
        Arrays.fill(this.slotRecipes, null);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        // players may only place items into input slots or the fuel slot
        if (i >= 0 && i < INPUT_COUNT) return true;
        if (i == TOTAL_SLOTS - 1) return level.fuelValues().isFuel(itemStack);
        return false;
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            this.recipesUsed.addTo(recipe.id(), 1);
        }
    }

    public @Nullable Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedContents) {
        for (ItemStack itemStack : this.items) {
            stackedContents.accountStack(itemStack);
        }
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        this.items.clear();
        ContainerHelper.loadAllItems(valueInput, this.items);
        // load timers
        this.furnaceBurnTime = valueInput.getIntOr("BurnTime", 0);
        this.currentItemBurnTime = valueInput.getIntOr("CurrentItemBurnTime", 0);
        int[] cookTimes = valueInput.getIntArray("CookTimes").orElse(new int[0]);
        int[] cookTotals = valueInput.getIntArray("CookTimesTotal").orElse(new int[0]);
        if (cookTimes.length == PROCESSING_COUNT) this.slotCookTime = cookTimes;
        else this.slotCookTime = Arrays.copyOf(cookTimes, PROCESSING_COUNT);
        if (cookTotals.length == PROCESSING_COUNT) this.slotCookTimeTotal = cookTotals;
        else {
            this.slotCookTimeTotal = new int[PROCESSING_COUNT];
            Arrays.fill(this.slotCookTimeTotal, 200);
            System.arraycopy(cookTotals, 0, this.slotCookTimeTotal, 0, Math.min(cookTotals.length, this.slotCookTimeTotal.length));
        }
        this.recipesUsed.clear();
        this.recipesUsed.putAll((valueInput.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of())));
        super.loadAdditional(valueInput);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        ContainerHelper.saveAllItems(valueOutput, this.items);
        // save timers
        valueOutput.putInt("BurnTime", this.furnaceBurnTime);
        valueOutput.putInt("CurrentItemBurnTime", this.currentItemBurnTime);
        valueOutput.putIntArray("CookTimes", this.slotCookTime != null ? this.slotCookTime : new int[PROCESSING_COUNT]);
        valueOutput.putIntArray("CookTimesTotal", this.slotCookTimeTotal != null ? this.slotCookTimeTotal : new int[PROCESSING_COUNT]);
        valueOutput.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
        super.saveAdditional(valueOutput);
    }

    public AbstractCookingRecipe getSmokingRecipe(ItemStack itemStack, ServerLevel level) {
        RecipeHolder<SmokingRecipe> holder = (RecipeHolder<SmokingRecipe>) getSmokingRecipeHolder(itemStack, level);
        if (holder != null)
            return holder.value();
        return null;
    }

    public RecipeHolder<? extends AbstractCookingRecipe> getSmokingRecipeHolder(ItemStack itemStack, ServerLevel level) {
        this.singleSlotRecipeWrapper = new SingleRecipeInput( itemStack);
        Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe = quickCheck.getRecipeFor(this.singleSlotRecipeWrapper, level);
        if (recipe != null && recipe.isPresent()) {
            ItemStack result = recipe.get().value().result();
            if (!result.isEmpty() && result.has(DataComponents.FOOD)) {
                return recipe.get();
            }
        }
        return null;
    }

    public int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return level.fuelValues().burnDuration(itemStack);
        }
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer) {
        List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience((ServerLevel) serverPlayer.level(), serverPlayer.position());
        serverPlayer.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.object2IntEntrySet()) {
            serverLevel.recipeAccess().byKey(entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                createExperience(serverLevel, vec3, entry.getIntValue(), ((AbstractCookingRecipe) recipe.value()).experience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel serverLevel, Vec3 vec3, int i, float f) {
        int j = Mth.floor((float) i * f);
        float g = Mth.frac((float) i * f);
        if (g != 0.0F && Math.random() < (double) g) {
            ++j;
        }

        ExperienceOrb.award(serverLevel, vec3, j);
    }
    
    protected boolean shouldConsumeFuel() {
        int processingStart = INPUT_COUNT;
        int processingEnd = processingStart + PROCESSING_COUNT;
        for (int i = processingStart; i < processingEnd; i++) {
            ItemStack cookingStack = this.getItem(i);
            if (cookingStack.isEmpty() || cookingStack.is(Items.CHARCOAL)) {
                continue;
            }

            int slotIdx = i - processingStart;
            if (this.slotCookTime[slotIdx] >= this.slotCookTimeTotal[slotIdx]) {
                // finished items waiting for output can keep the oven burning, but charcoal should not
                return true;
            }

            if (level.recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT).test(cookingStack)) {
                return true;
            }
        }
        return false;
    }

    // Server tick handler: processes fuel consumption, cooking progress, transfers finished items to outputs,
    // and moves inputs into processing slots when space is available.
    public static void serverTick(Level level, BlockPos pos, BlockState state, OvenBlockEntity be) {
        if (be == null || level.isClientSide()) return;

        boolean hasChanged = false;

        // process cooking slots
        int processingStart = INPUT_COUNT;
        int processingEnd = processingStart + PROCESSING_COUNT;

        int activeCookingSlots = 0;
        for (int i = processingStart; i < processingEnd; i++) {
            int slotIdx = i - processingStart;
            if (!be.getItem(i).isEmpty() && !be.getItem(i).is(Items.CHARCOAL) && be.slotCookTime[slotIdx] < be.slotCookTimeTotal[slotIdx]) {
                activeCookingSlots++;
            }
        }

        int delayFactor = Math.max(1, (activeCookingSlots - 1) / 3 + 1);

        // decrement burn time if burning
        if (be.furnaceBurnTime > 0) {
            double totalFuelCost = 0.0D;
            if (activeCookingSlots > 0) {
                double speedMultiplier = PaladinFurnitureMod.getPFMConfig().getOvenSpeedMultiplier();
                double progressIncrement = (1.0D / delayFactor) * speedMultiplier;
                for (int i = processingStart; i < processingEnd; i++) {
                    int slotIdx = i - processingStart;
                    if (!be.getItem(i).isEmpty() && !be.getItem(i).is(Items.CHARCOAL) && be.slotCookTime[slotIdx] < be.slotCookTimeTotal[slotIdx]) {
                        double slotFuelFactor = be.slotCookTimeTotal[slotIdx] > 0 ? 200.0D / be.slotCookTimeTotal[slotIdx] : 1.0D;
                        totalFuelCost += progressIncrement * slotFuelFactor;
                    }
                }
            } else {
                totalFuelCost = 1.0D;
            }

            double fuelCost = totalFuelCost * PaladinFurnitureMod.getPFMConfig().getFuelConsumptionMultiplier();
            be.burnTimeRemainder += fuelCost;
            int intFuelCost = (int) be.burnTimeRemainder;
            be.burnTimeRemainder -= intFuelCost;
            be.furnaceBurnTime = Math.max(0, be.furnaceBurnTime - intFuelCost);
        }

        // try to consume fuel if not burning and there is something that needs burning
        if (be.furnaceBurnTime == 0 && be.shouldConsumeFuel()) {
            int fuelIndex = TOTAL_SLOTS - 1;
            ItemStack fuelStack = be.getItem(fuelIndex);
            if (!fuelStack.isEmpty() && level.fuelValues().isFuel(fuelStack)) {
                int burn = be.getBurnDuration(fuelStack);
                if (burn > 0) {
                    be.currentItemBurnTime = be.furnaceBurnTime = burn;
                    // handle container / recipe remainder (e.g. bucket)
                    Item item = fuelStack.getItem();
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        ItemStack containerItem = item.getCraftingRemainder();
                        be.setItem(fuelIndex, containerItem == null ? ItemStack.EMPTY : containerItem);
                    } else {
                        be.setItem(fuelIndex, fuelStack);
                    }
                    hasChanged = true;
                }
            }
        }


        // first, advance cooking timers and for slots that reach their total, prepare the result
        for (int i = processingStart; i < processingEnd; i++) {
            ItemStack procStack = be.getItem(i);
            int slotIdx = i - processingStart;
            if (procStack.isEmpty() || procStack.is(Items.CHARCOAL)) {
                if (procStack.isEmpty() && (be.slotCookTime[slotIdx] > 0 || be.slotCookTimeRemainder[slotIdx] > 0.0D)) {
                    be.slotCookTime[slotIdx] = 0;
                    be.slotCookTimeRemainder[slotIdx] = 0.0D;
                    be.slotRecipes[slotIdx] = null;
                    hasChanged = true;
                }
                continue;
            }

            if (be.slotCookTime[slotIdx] >= 0) {
                if (be.furnaceBurnTime > 0) {
                    // Instead of a flat ++, cook slower if the oven is packed
                    // If activeCookingSlots is 1-3 -> +1 tick progress
                    // If 4-6 -> +1 progress every 2 ticks
                    // If 7-9 -> +1 progress every 3 ticks
                    double progressIncrement = (1.0D / delayFactor) * PaladinFurnitureMod.getPFMConfig().getOvenSpeedMultiplier();
                    be.slotCookTimeRemainder[slotIdx] += progressIncrement;
                    int advance = (int) be.slotCookTimeRemainder[slotIdx];
                    be.slotCookTimeRemainder[slotIdx] -= advance;
                    if (advance > 0) {
                        be.slotCookTime[slotIdx] += advance;
                    }
                }
            }

            // when we reach or exceed the required cook time, replace processing slot with the recipe result
            if (be.slotCookTime[slotIdx] >= be.slotCookTimeTotal[slotIdx]) {
                // attempted to produce result
                RecipeHolder<? extends AbstractCookingRecipe> recipe = be.getSmokingRecipeHolder(procStack, (ServerLevel) level);
                if (recipe != null && !recipe.value().result().isEmpty()) {
                    // replace the processing input with the result item so transfer logic can move it
                    be.setItem(i, recipe.value().result().copy());
                    be.slotRecipes[slotIdx] = recipe.id();
                    hasChanged = true;
                }
            }
        }

        // transfer any finished processing items to outputs
        int outputStart = INPUT_COUNT + PROCESSING_COUNT;
        int outputEnd = outputStart + OUTPUT_COUNT;

        for (int i = processingStart; i < processingEnd; i++) {
            int slotIdx = i - processingStart;
            if (be.slotCookTime[slotIdx] < be.slotCookTimeTotal[slotIdx] && !be.getItem(i).is(Items.CHARCOAL)) continue; // not finished yet

            ItemStack toTransfer = be.getItem(i);
            if (toTransfer.isEmpty()) {
                be.slotCookTime[slotIdx] = 0;
                be.slotCookTimeRemainder[slotIdx] = 0.0D;
                continue;
            }

            ItemStack remaining = toTransfer.copy();
            // try merge into existing stacks
            for (int o = outputStart; o < outputEnd; o++) {
                ItemStack out = be.getItem(o);
                if (out.isEmpty()) continue;
                if (ItemStack.isSameItemSameComponents(out, remaining)) {
                    int space = Math.min(remaining.getMaxStackSize(), out.getMaxStackSize()) - out.getCount();
                    if (space > 0) {
                        int move = Math.min(space, remaining.getCount());
                        out.grow(move);
                        remaining.shrink(move);
                        be.setItem(o, out);
                        hasChanged = true;
                        if (remaining.isEmpty()) break;
                    }
                }
            }

            // place into empty output slot if still remaining
            if (!remaining.isEmpty()) {
                for (int o = outputStart; o < outputEnd; o++) {
                    ItemStack out = be.getItem(o);
                    if (out.isEmpty()) {
                        be.setItem(o, remaining.copy());
                        remaining = ItemStack.EMPTY;
                        hasChanged = true;
                        break;
                    }
                }
            }

            // update processing slot with leftover (or clear it)
            if (remaining.isEmpty()) {
                ResourceKey<Recipe<?>> usedRecipeId = be.slotRecipes[slotIdx];
                be.setItem(i, ItemStack.EMPTY);
                be.slotCookTime[slotIdx] = 0;
                be.slotCookTimeRemainder[slotIdx] = 0.0D;
                be.slotRecipes[slotIdx] = null;
                if (usedRecipeId != null) {
                    Optional<RecipeHolder<?>> usedRecipe = ((ServerLevel)level).recipeAccess().byKey(usedRecipeId);
                    if (usedRecipe != null && usedRecipe.isPresent() && toTransfer.getItem() != Items.CHARCOAL) {
                        be.setRecipeUsed(usedRecipe.get());
                    }
                }
            } else {
                // failed to fully move the result -> leave what's left in processing slot and allow burning to continue
                be.setItem(i, remaining);
                // if the furnace is still burning, the cook time will continue to advance above the total.
                // if it reaches double the required cook time, turn the item into charcoal
                if (be.slotCookTime[slotIdx] >= be.slotCookTimeTotal[slotIdx] * 2) {
                    int count = be.getItem(i).getCount();
                    be.setItem(i, new ItemStack(Items.CHARCOAL, Math.max(1, count)));
                    be.slotCookTime[slotIdx] = 0;
                    be.slotCookTimeRemainder[slotIdx] = 0.0D;
                    be.slotRecipes[slotIdx] = null;
                    hasChanged = true;
                }
            }
        }

        // cooling: if not burning, processing progress decays (like vanilla furnace/freezer)
        if (be.furnaceBurnTime == 0) {
            for (int i = processingStart; i < processingEnd; i++) {
                int slotIdx = i - processingStart;
                if (be.slotCookTime[slotIdx] > 0) {
                    be.slotCookTime[slotIdx] = Mth.clamp(be.slotCookTime[slotIdx] - 2, 0, be.slotCookTimeTotal[slotIdx]);
                    hasChanged = true;
                }
            }
        }

        // move one item from inputs into the first empty processing slot
        int firstEmptyProcessing = -1;
        for (int i = processingStart; i < processingEnd; i++) {
            if (be.getItem(i).isEmpty()) { firstEmptyProcessing = i; break; }
        }
        if (firstEmptyProcessing != -1) {
            for (int in = 0; in < INPUT_COUNT; in++) {
                ItemStack inStack = be.getItem(in);
                if (!inStack.isEmpty()) {
                    // determine cook time from recipe if available
                    RecipeHolder<? extends AbstractCookingRecipe> recipe = be.getSmokingRecipeHolder(inStack, (ServerLevel) level);
                    int slotIdx = firstEmptyProcessing - processingStart;
                    if (recipe == null) {
                        continue;
                    }
                    ItemStack moved = inStack.split(1);
                    be.slotCookTimeTotal[slotIdx] = recipe.value().cookingTime();
                    be.slotRecipes[slotIdx] = recipe.id();
                    be.setItem(firstEmptyProcessing, moved);
                    be.slotCookTime[slotIdx] = 0;
                    be.slotCookTimeRemainder[slotIdx] = 0.0D;
                    be.setItem(in, inStack.isEmpty() ? ItemStack.EMPTY : inStack);
                    hasChanged = true;
                    break;
                }
            }
        }

        boolean prevLit = state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT);
        boolean nowLit = be.furnaceBurnTime > 0;
        if (state.hasProperty(BlockStateProperties.LIT) && prevLit != nowLit) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, nowLit), 3);
            hasChanged = true;
        }

        if (hasChanged) {
            be.setChanged();
        }
    }

    public boolean isCookable(Level level, ItemStack heldItem) {
        return level.recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT).test(heldItem);
    }
}
