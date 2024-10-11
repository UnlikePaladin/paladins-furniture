package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.fabric.menus.slots.SizeableSlot;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class AbstractMicrowaveScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookCategory category;
    public boolean isActive;
    public MicrowaveBlockEntity microwaveBlockEntity;
    // Client Constructor
    protected AbstractMicrowaveScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookCategory category, int syncId, PlayerInventory playerInventory, MicrowaveData packet) {
        this((MicrowaveBlockEntity) playerInventory.player.getWorld().getBlockEntity(packet.pos()), type, recipeType, category, syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(2));
        this.isActive = packet.isActive();
    }

    // Server Constructor
    protected AbstractMicrowaveScreenHandler(MicrowaveBlockEntity microwaveBlockEntity, ScreenHandlerType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookCategory category, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(type, syncId);
        this.microwaveBlockEntity = microwaveBlockEntity;
        int i;
        this.recipeType = recipeType;
        this.category = category;
        AbstractMicrowaveScreenHandler.checkSize(inventory, 1);
        AbstractMicrowaveScreenHandler.checkDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getEntityWorld();
        this.addSlot(new SizeableSlot(playerInventory.player, inventory, 0, 78, 40));

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperties(propertyDelegate);

    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.inventory).provideRecipeInputs(finder);
        }
    }

    public boolean getActive() {
        return isActive;
    }

    @ExpectPlatform
    public static void setActive(MicrowaveBlockEntity blockEntity, boolean isActive){
        blockEntity.isActive = isActive;
    }

    @Override
    public void clearCraftingSlots() {
        this.getSlot(0).setStack(ItemStack.EMPTY);
    }

    @Override
    public boolean matches(RecipeEntry<? extends Recipe<Inventory>> recipe) {
        return recipe != null && recipe.value() != null && recipe.value().matches(this.inventory, this.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 1;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0 ? !this.insertItem(itemStack2, 3, 37, false) : (this.isCookable(itemStack2) ? !this.insertItemToSlot(itemStack2, 0, 1, false) : (index >= 3 && index < 30 ? !this.insertItem(itemStack2, 30, 37, false) : index >= 30 && index < 37 && !this.insertItem(itemStack2, 3, 30, false)))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    protected boolean insertItemToSlot(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }
        if (!stack.isEmpty()) {
            i = fromLast ? endIndex - 1 : startIndex;
            while (fromLast ? i >= startIndex : i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }
                    slot.markDirty();
                    bl = true;
                    break;
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return bl;
    }

    protected boolean isCookable(ItemStack itemStack) {
        Optional<? extends RecipeEntry<? extends AbstractCookingRecipe>> optionalRecipeEntry = this.world.getRecipeManager().getFirstMatch(this.recipeType, new SimpleInventory(itemStack), this.world);
        return optionalRecipeEntry != null && optionalRecipeEntry.isPresent() && optionalRecipeEntry.get().value() != null;
    }

    public int getCookProgress() {
        int i = this.propertyDelegate.get(0);
        int j = this.propertyDelegate.get(1);
        if (j == 0 || i == 0) {
            return 0;
        }
        return i * 24 / j;
    }


    public boolean isActive() {
        return this.propertyDelegate.get(0) != 0;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    public static final PacketCodec<RegistryByteBuf, MicrowaveData> PACKET_CODEC = PacketCodec.of(MicrowaveData::write, MicrowaveData::new);
    public record MicrowaveData(BlockPos pos, Boolean isActive) {
        public MicrowaveData(RegistryByteBuf buf) {
            this(buf.readBlockPos(), buf.readBoolean());
        }
        public void write(RegistryByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeBoolean(isActive);
        }
    }
}

