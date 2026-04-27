package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.Clearable;
import net.minecraft.world.Containers;
import net.minecraft.util.Tickable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;


public class StovetopBlockEntity extends BlockEntity implements Clearable, Tickable {

    public final NonNullList<ItemStack> itemsBeingCooked = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];
    public StovetopBlockEntity() {
        super(BlockEntities.STOVE_TOP_BLOCK_ENTITY);
    }
    public void litServerTick() {
        boolean bl = false;
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) continue;
            bl = true;
            int n = i;
        if (this.cookingTimes[n] < 600){
            this.cookingTimes[n] = this.cookingTimes[n] + 2;
        }
            if (this.cookingTimes[i] < this.cookingTotalTimes[i]) continue;
            SimpleContainer inventory = new SimpleContainer(itemStack);
            ItemStack itemStack2 = world.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, world).map(campfireCookingRecipe -> campfireCookingRecipe.assemble(inventory)).orElse(itemStack);
                if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    this.itemsBeingCooked.set(i, ItemStack.EMPTY);
                }
                else {
                    this.itemsBeingCooked.set(i, itemStack2);
                }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
        if (bl) {
            setChanged();
        }
    }

    public void unlitServerTick() {
        boolean bl = false;
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            if (this.cookingTimes[i] <= 0) continue;
            bl = true;
            this.cookingTimes[i] = Mth.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
        }
        if (bl) {
            setChanged();
        }
    }

    public void clientTick() {
        int i;
        Random random = level.random;
        i = getCachedState().getValue(KitchenStovetopBlock.FACING).getClockWise().get2DDataValue();
        for (int j = 0; j < this.itemsBeingCooked.size(); ++j) {
            ItemStack stack = this.itemsBeingCooked.get(j);
            if (stack.isEmpty() || !(random.nextFloat() < 0.2f) || !world.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(stack), world).isPresent()) continue;
            Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
            BlockPos pos = worldPosition;
            float f = 0.2125f;
            double x = pos.getX() + 0.5 - ((direction.getStepX() * f) + (direction.getClockWise().getStepX() * f));
            double y = pos.getY() + 0.2;
            double z = pos.getZ() + 0.5 - ((direction.getStepZ() * f) + (direction.getClockWise().getStepZ() * f));
            for (int k = 0; k < 4; ++k) {
                if (!(random.nextFloat() < 0.9f))
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public NonNullList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }

    public Container getContainer(){
        SimpleContainer inventory = new SimpleContainer(itemsBeingCooked.size());
        for (int i = 0; i < itemsBeingCooked.size(); i++) {
            inventory.setItem(i, itemsBeingCooked.get(i));
        }
        return inventory;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        int[] is;
        super.load(state, nbt);
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(nbt, this.itemsBeingCooked);
        if (nbt.contains("CookingTimes", 11)) {
            is = nbt.getIntArray("CookingTimes");
            System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }
        if (nbt.contains("CookingTotalTimes", 11)) {
            is = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        this.saveInitialChunkData(nbt);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return nbt;
    }

    protected CompoundTag saveInitialChunkData(CompoundTag nbt) {
        super.save(nbt);
        ContainerHelper.saveAllItems(nbt, this.itemsBeingCooked, true);
        return nbt;
    }

    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.itemsBeingCooked.get(slot).copy();
        this.itemsBeingCooked.set(slot, ItemStack.EMPTY);
        sendBlockUpdated();
        return stack;
    }

    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(item), this.level);
    }

    public boolean addItem(ItemStack item, int integer) {
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) continue;
            this.cookingTotalTimes[i] = integer;
            this.cookingTimes[i] = 0;
            this.itemsBeingCooked.set(i, item.split(1));
            this.sendBlockUpdated();
            return true;
        }
        return false;
    }

    private void sendBlockUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void clearContent() {
        this.itemsBeingCooked.clear();
        sendBlockUpdated();
    }

    @Override
    public void tick() {
        BlockState state = getCachedState();
        if (world.isClient) {
            if (getCachedState().get(KitchenStovetopBlock.LIT)) {
                clientTick();
            }
        } else {
            if (state.get(KitchenStovetopBlock.LIT)) {
                litServerTick();
            }
            else {
                unlitServerTick();
            }
        }
    }

    @ExpectPlatform
    public static Supplier<? extends StovetopBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}

