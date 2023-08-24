package com.unlikepaladin.pfm.compat.rei.forge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FurnitureCategory implements DisplayCategory<FurnitureDisplay>{
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(PaladinFurnitureModBlocksItems.WORKING_TABLE);

    public static int getSlotWithSize(FurnitureDisplay recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return FurnitureDisplay.IDENTIFIER.getIdentifier();
    }

    @Override
    public CategoryIdentifier<? extends FurnitureDisplay> getCategoryIdentifier() {
        return FurnitureDisplay.IDENTIFIER;
    }

    @Override
    public @NotNull Renderer getIcon() {
        return ICON;
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("rei.pfm.furniture");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(FurnitureDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<EntryIngredient> input = display.getInputEntries();
        List<Slot> slots = Lists.newArrayList();

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
            }
        }
        for (int z = 0; z < input.size(); z++)
            slots.get(z).entries(input.get(z));

        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }


}
