package com.unlikepaladin.pfm.compat.rei.fabric;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.I18n;
import com.mojang.blaze3d.vertex.PoseStack;
import com.unlikepaladin.pfm.mixin.fabric.PFMEntryWidgetAccessor;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.REIHelper;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FurnitureCategory implements TransferRecipeCategory<FurnitureDisplay> {
    public static final EntryStack ICON = EntryStack.create(PaladinFurnitureModBlocksItems.WORKING_TABLE);

    public static int getSlotWithSize(FurnitureDisplay recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
    }

    @Override
    public @NotNull ResourceLocation getIdentifier() {
        return FurnitureDisplay.IDENTIFIER;
    }


    @Override
    public @NotNull EntryStack getLogo() {
        return ICON;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.get("rei.pfm.furniture");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(FurnitureDisplay display, Rectangle bounds) {
        Map<EntryStack, Integer> stackToSlotIndex = new HashMap<>();
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<List<EntryStack>> input = display.getInputEntries();

        EntryWidget output = (EntryWidget) EntryWidget.create(new Point(startPoint.x + 95, startPoint.y + 19)).disableBackground().markOutput();
        int recipeIndex = 0;
        for (List<EntryStack> ingredient : display.getResultingEntries()) {
            output.entries(ingredient);
            stackToSlotIndex.put(ingredient.get(0), recipeIndex);
            recipeIndex++;
        }
        widgets.add(output);


        List<Slot> slots = Lists.newArrayList();
        int innerRecipeSize = display.itemsPerInnerRecipe();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                Slot inputSlot = Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput();
                slots.add(inputSlot);
            }
        }

        for (int i = 0; i < innerRecipeSize; i++) {
            int finalSlotIndex = i;
            // many thanks to shedaniel for helping me figure this out :)
            widgets.add(Widgets.createDrawableWidget((graphics, matrixStack, mouseX, mouseY, delta) -> {
                Integer currentRecipe = stackToSlotIndex.get(((PFMEntryWidgetAccessor)output).pfm$getCurrentShowingStack());
                if (currentRecipe != null && input.size() > finalSlotIndex+(currentRecipe*innerRecipeSize)) {
                    slots.get(finalSlotIndex).clearEntries().entries(input.get(finalSlotIndex+(currentRecipe*innerRecipeSize)));
                }
            }));
        }
        widgets.addAll(slots);
        return widgets;
    }

    @Override
    public void renderRedSlots(PoseStack matrices, List<Widget> widgets, Rectangle bounds, FurnitureDisplay display, IntList redSlots) {
        if (REIHelper.getInstance().getPreviousContainerScreen() == null) return;
        ContainerInfo<AbstractContainerMenu> info = (ContainerInfo<AbstractContainerMenu>) ContainerInfoHandler.getContainerInfo(getIdentifier(), REIHelper.getInstance().getPreviousContainerScreen().getMenu().getClass());
        if (info == null)
            return;
        matrices.pushPose();
        matrices.translate(0, 0, 400);
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        int width = info.getCraftingWidth(REIHelper.getInstance().getPreviousContainerScreen().getMenu());
        for (Integer slot : redSlots) {
            int i = slot;
            int x = i % width;
            int y = Mth.floor(i / (float) width);
            GuiComponent.fill(matrices, startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18, startPoint.x + 1 + x * 18 + 16, startPoint.y + 1 + y * 18 + 16, 0x60ff0000);
        }
        matrices.popPose();
    }
}