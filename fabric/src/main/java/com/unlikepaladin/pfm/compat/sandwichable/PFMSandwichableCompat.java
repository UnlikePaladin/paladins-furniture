package com.unlikepaladin.pfm.compat.sandwichable;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import io.github.foundationgames.sandwichable.Sandwichable;
import io.github.foundationgames.sandwichable.blocks.BlocksRegistry;
import io.github.foundationgames.sandwichable.items.ItemsRegistry;
import io.github.foundationgames.sandwichable.recipe.ToastingRecipe;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class PFMSandwichableCompat {

    public static void toastSandwich(PFMToasterBlockEntity pfmToasterBlockEntity) {
        World world = pfmToasterBlockEntity.getWorld();
        List<ItemStack> items = pfmToasterBlockEntity.getItems();
        for (int i = 0; i < 2; i++) {
            SimpleInventory inv = new SimpleInventory(pfmToasterBlockEntity.getItems().get(i));
            Optional<ToastingRecipe> match = world.getRecipeManager().getFirstMatch(ToastingRecipe.Type.INSTANCE, inv, world);

            boolean changed = false;
            if(match.isPresent()) {
                pfmToasterBlockEntity.setItem(i, match.get().getOutput().copy());
                changed = true;
            } else {
                if(items.get(i).isFood()) {
                    Item item = Sandwichable.SMALL_FOODS.contains(items.get(i).getItem()) ? ItemsRegistry.BURNT_MORSEL : ItemsRegistry.BURNT_FOOD;
                    items.set(i, new ItemStack(item, 1));
                    changed = true;
                }
            }

            if (!world.isClient() && changed) {
                ItemStack advStack = items.get(i);
                pfmToasterBlockEntity.getLastUser().ifPresent(player -> {
                    if (player instanceof ServerPlayerEntity) {
                        Sandwichable.TOAST_ITEM.trigger((ServerPlayerEntity) player, advStack);
                    }
                });
            }
        }
    }

    public static boolean isMetal(ItemStack stack) {
        return Sandwichable.METAL_ITEMS.contains(stack.getItem());
    }

    public static boolean isSandwich(ItemStack stack) {
        return stack.getItem().equals(BlocksRegistry.SANDWICH.asItem());
    }
}
