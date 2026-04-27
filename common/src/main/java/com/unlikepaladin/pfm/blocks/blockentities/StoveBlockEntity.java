package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.KitchenCounterOvenBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class StoveBlockEntity extends AbstractFurnaceBlockEntity implements TickableBlockEntity {
    public StoveBlockEntity() {
        super(BlockEntities.STOVE_BLOCK_ENTITY, RecipeType.SMOKING);
    }
    public StoveBlockEntity(BlockEntityType<?> entity) {
        super(entity, RecipeType.SMOKING);
    }

    @Override
    protected Component getDefaultName() {
        if (this.getBlockState().getBlock() instanceof KitchenCounterOvenBlock) {
            return new TranslatableComponent("container.pfm.kitchen_counter_oven");
        }
        String blockname = this.getBlockState().getBlock().getDescriptionId().replace("block.pfm", "");
        return new TranslatableComponent("container.pfm" + blockname);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new StoveScreenHandler(syncId, playerInventory, this, this.dataAccess);
    }

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
        Vec3i vec3i = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
    }

    protected final NonNullList<ItemStack> itemsBeingCooked = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];

    public NonNullList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }
    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(item), this.level);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        int[] is;
        this.itemsBeingCooked.clear();
        load(nbt, this.itemsBeingCooked);
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
        super.save(nbt);
        this.saveInitialChunkData(nbt);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return nbt;
    }

    protected CompoundTag saveInitialChunkData(CompoundTag nbt) {
        super.save(nbt);
        save(nbt, this.itemsBeingCooked, true);
        return nbt;
    }

    public static CompoundTag save(CompoundTag nbt, NonNullList<ItemStack> stacks, boolean setIfEmpty) {
        ListTag nbtList = new ListTag();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) continue;
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.save(nbtCompound);
            nbtList.add(nbtCompound);
        }
        if (!nbtList.isEmpty() || setIfEmpty) {
            nbt.put("CookTopItems", nbtList);
        }
        return nbt;
    }

    public static void load(CompoundTag nbt, NonNullList<ItemStack> stacks) {
        ListTag nbtList = nbt.getList("CookTopItems", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= stacks.size()) continue;
            stacks.set(j, ItemStack.of(nbtCompound));
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
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
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
            ItemStack itemStack2 = level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, level).map(campfireCookingRecipe -> campfireCookingRecipe.assemble(inventory)).orElse(itemStack);
                if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.above().getY(), worldPosition.getZ(), itemStack2);
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
        super.tick();
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            clientTick();
        } else {
            litServerTick();
        }
    }


    public void unlitServerTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stoveBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stoveBlockEntity.itemsBeingCooked.size(); ++i) {
            if (stoveBlockEntity.cookingTimes[i] <= 0) continue;
            bl = true;
            stoveBlockEntity.cookingTimes[i] = Mth.clamp(stoveBlockEntity.cookingTimes[i] - 2, 0, stoveBlockEntity.cookingTotalTimes[i]);
        }
        if (bl) {
            setChanged();
        }
        tick();
    }

    public void clientTick() {
        int i;
        Random random = level.random;
        i = getBlockState().getValue(StoveBlock.FACING).getClockWise().get2DDataValue();
        for (int j = 0; j < this.itemsBeingCooked.size(); ++j) {
            ItemStack stack = this.itemsBeingCooked.get(j);
            if (stack.isEmpty() || !(random.nextFloat() < 0.2f) || !level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(stack), level).isPresent()) continue;
            Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
            float f = 0.2125f;
            double x = worldPosition.getX() + 0.5 - ((direction.getStepX() * f) + (direction.getClockWise().getStepX() * f));
            double y = worldPosition.getY() + 1.1;
            double z = worldPosition.getZ() + 0.5 - ((direction.getStepZ() * f) + (direction.getClockWise().getStepZ() * f));
            for (int k = 0; k < 4; ++k) {
                if (!(random.nextFloat() < 0.9f))
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
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

    @ExpectPlatform
    public static Supplier<? extends BlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
