package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.*;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;

import net.minecraft.world.Containers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StoveBlockEntity extends OvenBlockEntity {
    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
    }
    public StoveBlockEntity(BlockEntityType<? extends OvenBlockEntity> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

     String blockname = this.getBlockState().getBlock().getDescriptionId();

    protected void onContainerOpen(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock){
            StoveBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_OPEN);
            StoveBlockEntity.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StoveBlock) {
            StoveBlockEntity.this.playSound(state, SoundEvents.IRON_TRAPDOOR_CLOSE);
            StoveBlockEntity.this.setOpen(state, false);
        }
    }

    void setOpen(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.OPEN, open), 3);
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.onContainerClose(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
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

    @Override
    protected Component getDefaultName() {
        blockname = blockname.replace("block.pfm", "");
        if (this.getBlockState().getBlock() instanceof KitchenCounterOvenBlock) {
            return Component.translatable("container.pfm.kitchen_counter_oven");
        }
        return Component.translatable("container.pfm" + blockname);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new OvenScreenHandler(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, containerId, playerInventory, this, this.dataAccess);
    }

    protected final NonNullList<ItemStack> itemsBeingCooked = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];

    public NonNullList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }
    public Optional<RecipeHolder<CampfireCookingRecipe>> getRecipeFor(ItemStack item) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return ((ServerLevel)this.level).recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(item), this.level);
    }

    @Override
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        int[] is;
        this.itemsBeingCooked.clear();
        readData(view, this.itemsBeingCooked);
        is = view.getIntArray("CookingTimes").orElse(new int[0]);
        System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        is = view.getIntArray("CookingTotalTimes").orElse(new int[0]);
        System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        writeData(view, this.itemsBeingCooked, true);
        view.putIntArray("CookingTimes", this.cookingTimes);
        view.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
    }

    public static void writeData(ValueOutput view, NonNullList<ItemStack> stacks, boolean setIfEmpty) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> listAppender = view.list("CookTopItems", ItemStackWithSlot.CODEC);

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack itemStack = stacks.get(i);
            if (!itemStack.isEmpty()) {
                listAppender.add(new ItemStackWithSlot(i, itemStack));
            }
        }

        if (listAppender.isEmpty() && !setIfEmpty) {
            view.discard("CookTopItems");
        }
    }

    public static void readData(ValueInput view, NonNullList<ItemStack> stacks) {
        for (ItemStackWithSlot stackWithSlot : view.listOrEmpty("CookTopItems", ItemStackWithSlot.CODEC)) {
            if (stackWithSlot.isValidInContainer(stacks.size())) {
                stacks.set(stackWithSlot.slot(), stackWithSlot.stack());
            }
        }
    }

    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.itemsBeingCooked.get(slot).copy();
        this.itemsBeingCooked.set(slot, ItemStack.EMPTY);
        sendBlockUpdated();
        return stack;
    }

    @Override
    public void clearContent() {
        this.itemsBeingCooked.clear();
    }


    private void sendBlockUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public static void litServerTick(Level level1, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        ServerLevel level = (ServerLevel) level1;
        if (blockEntity instanceof StoveBlockEntity) {
            StoveBlockEntity stoveBlockEntity = (StoveBlockEntity) blockEntity;
            boolean bl = false;
            for (int i = 0; i < stoveBlockEntity.itemsBeingCooked.size(); ++i) {
                ItemStack itemStack = stoveBlockEntity.itemsBeingCooked.get(i);
                if (itemStack.isEmpty()) continue;
                bl = true;
                int n = i;
                if (stoveBlockEntity.cookingTimes[n] < 600){
                    stoveBlockEntity.cookingTimes[n] = stoveBlockEntity.cookingTimes[n] + 2;
                }
                if (stoveBlockEntity.cookingTimes[i] < stoveBlockEntity.cookingTotalTimes[i]) continue;
                SingleRecipeInput inventory = new SingleRecipeInput(itemStack);
                ItemStack itemStack2 = level.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, level).map(campfireCookingRecipe -> campfireCookingRecipe.value().assemble(inventory, level.registryAccess())).orElse(itemStack);
                    if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                        Containers.dropItemStack(level, pos.getX(), pos.above().getY(), pos.getZ(), itemStack2);
                        stoveBlockEntity.itemsBeingCooked.set(i, ItemStack.EMPTY);
                    }
                    else {
                        stoveBlockEntity.itemsBeingCooked.set(i, itemStack2);
                    }
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            }
            if (bl) {
                setChanged(level, pos, state);
            }
            serverTick(level, pos, state, stoveBlockEntity);
        }
    }

    public static void unlitServerTick(ServerLevel level, BlockPos pos, BlockState state, StoveBlockEntity stoveBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stoveBlockEntity.itemsBeingCooked.size(); ++i) {
            if (stoveBlockEntity.cookingTimes[i] <= 0) continue;
            bl = true;
            stoveBlockEntity.cookingTimes[i] = Mth.clamp(stoveBlockEntity.cookingTimes[i] - 2, 0, stoveBlockEntity.cookingTotalTimes[i]);
        }
        if (bl) {
            setChanged(level, pos, state);
        }
        serverTick(level, pos, state, stoveBlockEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof StoveBlockEntity) {
            StoveBlockEntity stoveBlockEntity = (StoveBlockEntity) blockEntity;
            int i;
            RandomSource random = level.random;
            i = state.getValue(StoveBlock.FACING).getClockWise().get2DDataValue();
            for (int j = 0; j < stoveBlockEntity.itemsBeingCooked.size(); ++j) {
                ItemStack stack = stoveBlockEntity.itemsBeingCooked.get(j);
                if (stack.isEmpty() || !(random.nextFloat() < 0.2f) ) continue;
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
                float f = 0.2125f;
                double x = pos.getX() + 0.5 - ((direction.getStepX() * f) + (direction.getClockWise().getStepX() * f));
                double y = pos.getY() + 1.1;
                double z = pos.getZ() + 0.5 - ((direction.getStepZ() * f) + (direction.getClockWise().getStepZ() * f));
                for (int k = 0; k < 4; ++k) {
                    if (!(random.nextFloat() < 0.9f))
                        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }

    public boolean addItem(ServerLevel world, @Nullable LivingEntity entity, ItemStack stack) {
        for (int i = 0; i < this.itemsBeingCooked.size(); i++) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) {
                Optional<RecipeHolder<CampfireCookingRecipe>> optional = world.recipeAccess()
                        .getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), world);
                if (optional.isEmpty()) {
                    return false;
                }

                this.cookingTotalTimes[i] = ((CampfireCookingRecipe)((RecipeHolder<?>)optional.get()).value()).cookingTime();
                this.cookingTimes[i] = 0;
                this.itemsBeingCooked.set(i, stack.consumeAndReturn(1, entity));
                world.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
                this.sendBlockUpdated();
                return true;
            }
        }

        return false;
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
