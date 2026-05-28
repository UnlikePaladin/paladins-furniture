package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.*;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.world.Clearable;
import net.minecraft.world.Containers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class StovetopBlockEntity extends BlockEntity implements Clearable {

    public final NonNullList<ItemStack> itemsBeingCooked = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];
    public StovetopBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.STOVE_TOP_BLOCK_ENTITY, pos, state);
    }
    public static void litServerTick(Level world1, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        boolean bl = false;
        ServerLevel world = (ServerLevel) world1;
        for (int i = 0; i < stovetopBlockEntity.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = stovetopBlockEntity.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) continue;
            bl = true;
            if (stovetopBlockEntity.cookingTimes[i] < 600){
                stovetopBlockEntity.cookingTimes[i] = stovetopBlockEntity.cookingTimes[i] + 2;
            }
            if (stovetopBlockEntity.cookingTimes[i] < stovetopBlockEntity.cookingTotalTimes[i]) continue;
            SingleRecipeInput inventory = new SingleRecipeInput(itemStack);
            ItemStack itemStack2 = world.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, world).map(campfireCookingRecipe -> campfireCookingRecipe.value().assemble(inventory, world.registryAccess())).orElse(itemStack);
                if (PaladinFurnitureMod.getPFMConfig().doesFoodPopOffStove()) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    stovetopBlockEntity.itemsBeingCooked.set(i, ItemStack.EMPTY);
                }
                else {
                    stovetopBlockEntity.itemsBeingCooked.set(i, itemStack2);
                }
            world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
        }
        if (bl) {
            CampfireBlockEntity.setChanged(world, pos, state);
        }
    }

    public static void unlitServerTick(Level world, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        boolean bl = false;
        for (int i = 0; i < stovetopBlockEntity.itemsBeingCooked.size(); ++i) {
            if (stovetopBlockEntity.cookingTimes[i] <= 0) continue;
            bl = true;
            stovetopBlockEntity.cookingTimes[i] = Mth.clamp(stovetopBlockEntity.cookingTimes[i] - 2, 0, stovetopBlockEntity.cookingTotalTimes[i]);
        }
        if (bl) {
            CampfireBlockEntity.setChanged(world, pos, state);
        }
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, StovetopBlockEntity stovetopBlockEntity) {
        int i;
        RandomSource random = world.random;
        i = state.getValue(KitchenStovetopBlock.FACING).getClockWise().get2DDataValue();
        for (int j = 0; j < stovetopBlockEntity.itemsBeingCooked.size(); ++j) {
            ItemStack stack = stovetopBlockEntity.itemsBeingCooked.get(j);
            if (stack.isEmpty() || !(random.nextFloat() < 0.2f) || !world.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(stack)) continue;
            Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
            float f = 0.2125f;
            double x = pos.getX() + 0.5 - ((direction.getStepX() * f) + (direction.getClockWise().getStepX() * f));
            double y = pos.getY() + 0.2;
            double z = pos.getZ() + 0.5 - ((direction.getStepZ() * f) + (direction.getClockWise().getStepZ() * f));
            for (int k = 0; k < 4; ++k) {
                if (!(random.nextFloat() < 0.9f))
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
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
    protected void loadAdditional(ReadView view) {
        int[] is;
        super.loadAdditional(view);
        this.itemsBeingCooked.clear();
        Inventories.readData(view, this.itemsBeingCooked);
        is = view.getOptionalIntArray("CookingTimes").orElse(new int[0]);
        System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        is = view.getOptionalIntArray("CookingTotalTimes").orElse(new int[0]);
        System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
    }

    @Override
    protected void saveAdditional(WriteView view) {
        super.saveAdditional(view);
        view.putIntArray("CookingTimes", this.cookingTimes);
        view.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        Inventories.writeData(view, this.itemsBeingCooked, true);
    }

    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.itemsBeingCooked.get(slot).copy();
        this.itemsBeingCooked.set(slot, ItemStack.EMPTY);
        sendBlockUpdated();
        return stack;
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

    private void sendBlockUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void clearContent() {
        this.itemsBeingCooked.clear();
        sendBlockUpdated();
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends StovetopBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}

