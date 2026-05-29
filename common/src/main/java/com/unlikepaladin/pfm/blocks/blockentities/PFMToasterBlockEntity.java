package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PFMToasterBlockEntity extends BlockEntity implements WorldlyContainer {
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private @Nullable UUID lastUser;
    private static int toastTime = 240;
    private int toastProgress = 0;
    private boolean toasting = false;
    private boolean smoking = false;
    private int smokeProgress = 0;

    private boolean currentlyPowered = false;
    private boolean previouslyPowered = false;
    private boolean updateNeighbors = false;

    public PFMToasterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TOASTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        items = NonNullList.withSize(2, ItemStack.EMPTY);
        toastProgress = view.getIntOr("toastProgress", 0);
        toasting = view.getBooleanOr("toasting", false);
        smokeProgress = view.getIntOr("smokeProgress", 0);
        smoking = view.getBooleanOr("smoking", false);
        view.getString("lastUser").ifPresent((str) -> {
            this.lastUser = UUID.fromString(str);
        });
        ContainerHelper.loadAllItems(view, items);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        view.putInt("toastProgress", toastProgress);
        view.putBoolean("toasting", toasting);
        view.putInt("smokeProgress", smokeProgress);
        view.putBoolean("smoking", smoking);
        if (this.lastUser == null) {
            view.discard("lastUser");
        } else view.putString("lastUser", this.lastUser.toString());
        ContainerHelper.saveAllItems(view, items);
        super.saveAdditional(view);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    private void explode() {
        if(!level.isClientSide()) {
            level.removeBlock(getBlockPos(), true);
            Player player = level.getNearestPlayer(getBlockPos().getX(), getBlockPos().getZ(), 8, 10, false);
            level.explode(player, level.damageSources().explosion(player, player), null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2.2f, true, Level.ExplosionInteraction.BLOCK);
        }
    }

    public Direction getToasterFacing() {
        if(this.level.getBlockState(this.worldPosition).getBlock() instanceof PFMToasterBlock) {
            return this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        return Direction.NORTH;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void setCurrentItem(int i, ItemStack stack) {
        items.set(i, stack);
        sync(this, getLevel());
    }
    public Optional<Player> getLastUser() {
        return Optional.ofNullable(this.lastUser).map(this.getLevel()::getPlayerByUUID);
    }

    public void setLastUser(@Nullable Player player) {
        this.lastUser = (player == null ? null : player.getUUID());
    }

    public ItemStack takeItem(@Nullable Player player) {
        int index = !items.get(1).isEmpty() ? 1 : 0;
        ItemStack stack = items.get(index);
        items.set(index, ItemStack.EMPTY);
        updateNeighbors = true;
        this.setLastUser(player);
        return stack;
    }

    public boolean addItem(InteractionHand hand, Player player) {
        if(!toasting) {
            ItemStack playerItem = player.getItemInHand(hand).copy();
            playerItem.setCount(1);
            if (!items.get(0).isEmpty() && !items.get(1).isEmpty()) {
                return false;
            }
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            int index = !items.get(0).isEmpty() ? 1 : 0;
            items.set(index, playerItem);
            updateNeighbors = true;
            this.setLastUser(player);
            return true;
        } return false;
    }

    @ExpectPlatform
    public static boolean isMetal(ItemStack stack) {
        throw new AssertionError();
    }

    public boolean hasMetalInside() {
        if (PaladinFurnitureMod.getModList().contains("sandwichable"))
            return isMetal(items.get(0)) || isMetal(items.get(1));
        return items.get(0).getItem().getDescriptionId().contains("iron") || items.get(1).getItem().getDescriptionId().contains("iron");
    }

    @ExpectPlatform
    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {

    }

    private void toastItems(ServerLevel world) {
        if (PaladinFurnitureMod.getModList().contains("sandwichable")) {
            sandwichableToast(this);
        }
        else {
            for (int i = 0; i < 2; i++) {
                SingleRecipeInput inv = new SingleRecipeInput(items.get(i));
                Optional<RecipeHolder<CampfireCookingRecipe>> match = world.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inv, level);

                boolean changed = false;
                if(match.isPresent()) {
                    items.set(i, match.get().value().result().copy());
                    changed = true;
                } else {
                    if(items.get(i).has(DataComponents.FOOD)) {
                        Item item = Items.COAL;
                        items.set(i, new ItemStack(item, 1));
                        changed = true;
                    }
                }
            }
        }
    }

    public void startToasting(@Nullable Player player) {
        if(this.level.getBlockState(this.worldPosition).getBlock() instanceof PFMToasterBlock) {
            this.level.setBlockAndUpdate(getBlockPos(), this.level.getBlockState(this.worldPosition).setValue(PFMToasterBlock.ON, true));
        }
        level.playSound(null, getBlockPos(), SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.5F, 0.8F);
        toastProgress = 0;
        toasting = true;
        updateNeighbors = true;
        if (player != null) this.setLastUser(player);
    }

    public void stopToasting(@Nullable Player player) {
        if(this.level.getBlockState(this.worldPosition).getBlock() instanceof PFMToasterBlock) {
            this.level.setBlockAndUpdate(getBlockPos(), this.level.getBlockState(this.worldPosition).setValue(PFMToasterBlock.ON, false));
        }
        level.playSound(null, getBlockPos(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 0.8F, 4);
        toastProgress = 0;
        toasting = false;
        updateNeighbors = true;
        if (player != null) this.setLastUser(player);
    }

    public int getAnalogOutputSignal() {
        int r = 0;
        for (int i = 0; i < 2; i++) {
            r += items.get(i).isEmpty() ? 0 : 1;
        }
        r = (int)Math.round(r * 7.5);
        return r;
    }

    public boolean isToasting() {
        return toasting;
    }

    public int getToastingProgress() {
        return this.toastProgress;
    }

    private boolean tickPitch = false;

    public static void serverTick(Level level, BlockPos pos, BlockState state, PFMToasterBlockEntity blockEntity) {
        int smokeTime = 80;
        if(blockEntity.updateNeighbors) {
            level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());
            blockEntity.updateNeighbors = false;
        }
        blockEntity.previouslyPowered = blockEntity.currentlyPowered;
        blockEntity.currentlyPowered = level.hasNeighborSignal(pos);
        if(blockEntity.toasting) {
            blockEntity.toastProgress++;
            if(blockEntity.toastProgress % 4 == 0 && blockEntity.toastProgress != toastTime) {
                level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.05F, blockEntity.tickPitch ? 2.0F : 1.9F);
                blockEntity.tickPitch = !blockEntity.tickPitch;
            }
            if(blockEntity.hasMetalInside()){
                blockEntity.explode();
            }
        }
        if(blockEntity.toastProgress == toastTime && level instanceof ServerLevel serverWorld) {
            blockEntity.stopToasting(null);
            blockEntity.toastItems(serverWorld);
            blockEntity.smoking = true;
        }
        if(blockEntity.smoking) {
            if(blockEntity.smokeProgress % 3 == 0) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0, 0.03, 0);
            }
            blockEntity.smokeProgress++;
        } if (blockEntity.smokeProgress == smokeTime) { blockEntity.smoking = false; blockEntity.smokeProgress = 0; }
        if(blockEntity.currentlyPowered && !blockEntity.previouslyPowered) {
            if(!blockEntity.toasting) { blockEntity.startToasting(null); }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return items.get(slot).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return !level.getBlockState(getBlockPos()).getValue(PFMToasterBlock.ON);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return removeItemNoUpdate(slot);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = items.get(slot).copy();
        items.set(slot, ItemStack.EMPTY);
        setLastUser(null);
        sync(this, level);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        setLastUser(null);
        sync(this, level);
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        items.clear();
        sync(this, level);
    }

    public static void sync(PFMToasterBlockEntity blockEntity, Level level) {
        if (!level.isClientSide())
            ((ServerLevel) level).getChunkSource().blockChanged(blockEntity.getBlockPos());
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends PFMToasterBlockEntity> getFactory() {
        throw new AssertionError();
    }
}