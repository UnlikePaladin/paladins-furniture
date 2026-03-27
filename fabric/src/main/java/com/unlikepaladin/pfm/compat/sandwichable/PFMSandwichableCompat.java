package com.unlikepaladin.pfm.compat.sandwichable;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import io.github.foundationgames.sandwichable.Sandwichable;
import io.github.foundationgames.sandwichable.blocks.BlocksRegistry;
import io.github.foundationgames.sandwichable.items.ItemsRegistry;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class PFMSandwichableCompat {

    public static void toastSandwich(PFMToasterBlockEntity pfmToasterBlockEntity) {
        Level world = pfmToasterBlockEntity.getLevel();
        List<ItemStack> items = pfmToasterBlockEntity.getItems();
        for (int i = 0; i < 2; i++) {
            SimpleContainer inv = new SimpleContainer(pfmToasterBlockEntity.getItems().get(i));
            Optional<ToastingRecipe> match = world.getRecipeManager().getRecipeFor(ToastingRecipe.Type.INSTANCE, inv, world);

            boolean changed = false;
            if(match.isPresent()) {
                pfmToasterBlockEntity.setItem(i, match.get().getResultItem().copy());
                changed = true;
            } else {
                if(items.get(i).isEdible()) {
                    Item item = items.get(i).isIn(Sandwichable.SMALL_FOODS) ? ItemsRegistry.BURNT_MORSEL : ItemsRegistry.BURNT_FOOD;
                    items.set(i, new ItemStack(item, 1));
                    changed = true;
                }
            }

            if (!world.isClientSide() && changed) {
                ItemStack advStack = items.get(i);
                pfmToasterBlockEntity.getLastUser().ifPresent(player -> {
                    if (player instanceof ServerPlayer) {
                        Sandwichable.TOAST_ITEM.trigger((ServerPlayer) player, advStack);
                    }
                });
            }
        }
    }

    public static boolean isMetal(ItemStack stack) {
        return stack.isIn(Sandwichable.METAL_ITEMS);
    }

    public static boolean isSandwich(ItemStack stack) {
        return stack.getItem().equals(BlocksRegistry.SANDWICH.asItem());
    }
}
