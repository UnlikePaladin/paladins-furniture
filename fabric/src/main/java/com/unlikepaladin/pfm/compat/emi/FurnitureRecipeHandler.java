package com.unlikepaladin.pfm.compat.emi;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import dev.emi.emi.api.EmiFillAction;
import dev.emi.emi.api.EmiRecipeHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnitureRecipeHandler implements EmiRecipeHandler<WorkbenchScreenHandler> {

    @Override
    public List<Slot> getInputSources(WorkbenchScreenHandler handler) {
        List<Slot> list = Lists.newArrayList();
        int invStart = 1;
        for(int i = invStart; i < invStart + 36; ++i) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(WorkbenchScreenHandler handler) {
        return List.of();
    }

    @Override
    public @Nullable Slot getOutputSlot(WorkbenchScreenHandler handler) {
            return handler.slots.get(0);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == PaladinFurnitureModEMIPlugin.FURNITURE && recipe.supportsRecipeTree();
    }

    @Override
    public boolean performFill(EmiRecipe recipe, HandledScreen<WorkbenchScreenHandler> screen, EmiFillAction action, int amount) {
        return EmiRecipeHandler.super.performFill(recipe, screen, action, amount);
    }
}