package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;


public class StovetopBlockEntity extends BlockEntity implements Clearable {

    protected final DefaultedList<ItemStack> itemsBeingCooked = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];
    public StovetopBlockEntity( BlockPos pos, BlockState state) {
        super(BlockEntities.STOVE_TOP_BLOCK_ENTITY, pos, state);
    }
    public static void litServerTick(World world, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stovetopBlockEntity.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = stovetopBlockEntity.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) continue;
            bl = true;
            int n = i;
        if (stovetopBlockEntity.cookingTimes[n] < 600){
            stovetopBlockEntity.cookingTimes[n] = stovetopBlockEntity.cookingTimes[n] + 2;
        }
            if (stovetopBlockEntity.cookingTimes[i] < stovetopBlockEntity.cookingTotalTimes[i]) continue;
            SimpleInventory inventory = new SimpleInventory(itemStack);
            ItemStack itemStack2 = world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, inventory, world).map(campfireCookingRecipe -> campfireCookingRecipe.craft(inventory)).orElse(itemStack);
                if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    stovetopBlockEntity.itemsBeingCooked.set(i, ItemStack.EMPTY);
                }
                else {
                    stovetopBlockEntity.itemsBeingCooked.set(i, itemStack2);
                }
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
        if (bl) {
            CampfireBlockEntity.markDirty(world, pos, state);
        }
    }

    public static void unlitServerTick(World world, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stovetopBlockEntity.itemsBeingCooked.size(); ++i) {
            if (stovetopBlockEntity.cookingTimes[i] <= 0) continue;
            bl = true;
            stovetopBlockEntity.cookingTimes[i] = MathHelper.clamp(stovetopBlockEntity.cookingTimes[i] - 2, 0, stovetopBlockEntity.cookingTotalTimes[i]);
        }
        if (bl) {
            CampfireBlockEntity.markDirty(world, pos, state);
        }
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        int i;
        Random random = world.random;
        i = state.get(KitchenStovetopBlock.FACING).rotateYClockwise().getHorizontal();
        for (int j = 0; j < stovetopBlockEntity.itemsBeingCooked.size(); ++j) {
            ItemStack stack = stovetopBlockEntity.itemsBeingCooked.get(j);
            if (stack.isEmpty() || !(random.nextFloat() < 0.2f) || world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(stack), world).isEmpty()) continue;
            Direction direction = Direction.fromHorizontal(Math.floorMod(j + i, 4));
            float f = 0.2125f;
            double x = pos.getX() + 0.5 - ((direction.getOffsetX() * f) + (direction.rotateYClockwise().getOffsetX() * f));
            double y = pos.getY() + 0.2;
            double z = pos.getZ() + 0.5 - ((direction.getOffsetZ() * f) + (direction.rotateYClockwise().getOffsetZ() * f));
            for (int k = 0; k < 4; ++k) {
                if (!(random.nextFloat() < 0.9f))
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public DefaultedList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }

    public Inventory getInventory(){
        SimpleInventory inventory = new SimpleInventory(itemsBeingCooked.size());
        for (int i = 0; i < itemsBeingCooked.size(); i++) {
            inventory.setStack(i, itemsBeingCooked.get(i));
        }
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int[] is;
        super.readNbt(nbt);
        this.itemsBeingCooked.clear();
        Inventories.readNbt(nbt, this.itemsBeingCooked);
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
    public NbtCompound writeNbt(NbtCompound nbt) {
        this.saveInitialChunkData(nbt);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return nbt;
    }

    protected NbtCompound saveInitialChunkData(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
        return nbt;
    }

    public ItemStack removeStack(int slot) {
        ItemStack stack = this.itemsBeingCooked.get(slot).copy();
        this.itemsBeingCooked.set(slot, ItemStack.EMPTY);
        updateListeners();
        return stack;
    }

    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(item), this.world);
    }

    public boolean addItem(ItemStack item, int integer) {
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) continue;
            this.cookingTotalTimes[i] = integer;
            this.cookingTimes[i] = 0;
            this.itemsBeingCooked.set(i, item.split(1));
            this.updateListeners();
            return true;
        }
        return false;
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    @Override
    public void clear() {
        this.itemsBeingCooked.clear();
        updateListeners();
    }

}

