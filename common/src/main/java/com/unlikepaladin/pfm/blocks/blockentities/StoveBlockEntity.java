package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.KitchenCounterOven;
import com.unlikepaladin.pfm.blocks.Stove;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class StoveBlockEntity extends AbstractFurnaceBlockEntity implements Tickable {
    public StoveBlockEntity() {
        super(BlockEntities.STOVE_BLOCK_ENTITY, RecipeType.SMOKING);
    }
    public StoveBlockEntity(BlockEntityType<?> entity) {
        super(entity, RecipeType.SMOKING);
    }

    @Override
    protected Text getContainerName() {
        if (this.getCachedState().getBlock() instanceof KitchenCounterOven) {
            return new TranslatableText("container.pfm.kitchen_counter_oven");
        }
        String blockname = this.getCachedState().getBlock().getTranslationKey().replace("block.pfm", "");
        return new TranslatableText("container.pfm" + blockname);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new StoveScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof Stove){
            StoveBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
            StoveBlockEntity.this.setOpen(state, true);
        }
    }

    protected void onContainerClose(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof Stove) {
            StoveBlockEntity.this.playSound(state, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE);
            StoveBlockEntity.this.setOpen(state, false);
        }
    }
    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerClose(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }
    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = state.get(Properties.HORIZONTAL_FACING).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.onContainerOpen(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }
    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(Properties.OPEN, open), 3);
    }

    protected final DefaultedList<ItemStack> itemsBeingCooked = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];

    public DefaultedList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }
    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(item), this.world);
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        super.fromTag(state, nbt);
        int[] is;
        this.itemsBeingCooked.clear();
        readNbt(nbt, this.itemsBeingCooked);
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
        super.writeNbt(nbt);
        this.saveInitialChunkData(nbt);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return nbt;
    }

    protected NbtCompound saveInitialChunkData(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbt(nbt, this.itemsBeingCooked, true);
        return nbt;
    }

    public static NbtCompound writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, boolean setIfEmpty) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) continue;
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        if (!nbtList.isEmpty() || setIfEmpty) {
            nbt.put("CookTopItems", nbtList);
        }
        return nbt;
    }

    public static void readNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        NbtList nbtList = nbt.getList("CookTopItems", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= stacks.size()) continue;
            stacks.set(j, ItemStack.fromNbt(nbtCompound));
        }
    }

    public ItemStack removeStack(int slot) {
        ItemStack stack = this.itemsBeingCooked.get(slot).copy();
        this.itemsBeingCooked.set(slot, ItemStack.EMPTY);
        updateListeners();
        return stack;
    }

    @Override
    public void clear() {
        this.itemsBeingCooked.clear();
    }


    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
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
            SimpleInventory inventory = new SimpleInventory(itemStack);
            ItemStack itemStack2 = world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, inventory, world).map(campfireCookingRecipe -> campfireCookingRecipe.craft(inventory)).orElse(itemStack);
                if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                    ItemScatterer.spawn(world, pos.getX(), pos.up().getY(), pos.getZ(), itemStack2);
                    this.itemsBeingCooked.set(i, ItemStack.EMPTY);
                }
                else {
                    this.itemsBeingCooked.set(i, itemStack2);
                }
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
        if (bl) {
            markDirty();
        }
        super.tick();
    }

    @Override
    public void tick() {
        if (world.isClient) {
            clientTick();
        } else {
            litServerTick();
        }
    }


    public void unlitServerTick(World world, BlockPos pos, BlockState state, StoveBlockEntity stoveBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stoveBlockEntity.itemsBeingCooked.size(); ++i) {
            if (stoveBlockEntity.cookingTimes[i] <= 0) continue;
            bl = true;
            stoveBlockEntity.cookingTimes[i] = MathHelper.clamp(stoveBlockEntity.cookingTimes[i] - 2, 0, stoveBlockEntity.cookingTotalTimes[i]);
        }
        if (bl) {
            markDirty();
        }
        tick();
    }

    public void clientTick() {
        int i;
        Random random = world.random;
        i = getCachedState().get(Stove.FACING).rotateYClockwise().getHorizontal();
        for (int j = 0; j < this.itemsBeingCooked.size(); ++j) {
            ItemStack stack = this.itemsBeingCooked.get(j);
            if (stack.isEmpty() || !(random.nextFloat() < 0.2f) || !world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(stack), world).isPresent()) continue;
            Direction direction = Direction.fromHorizontal(Math.floorMod(j + i, 4));
            float f = 0.2125f;
            double x = pos.getX() + 0.5 - ((direction.getOffsetX() * f) + (direction.rotateYClockwise().getOffsetX() * f));
            double y = pos.getY() + 1.1;
            double z = pos.getZ() + 0.5 - ((direction.getOffsetZ() * f) + (direction.rotateYClockwise().getOffsetZ() * f));
            for (int k = 0; k < 4; ++k) {
                if (!(random.nextFloat() < 0.9f))
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
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
            this.updateListeners();
            return true;
        }
        return false;
    }

}
