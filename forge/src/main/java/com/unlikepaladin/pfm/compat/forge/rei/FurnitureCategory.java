package com.unlikepaladin.pfm.compat.forge.rei;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplayMerger;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FurnitureCategory implements DisplayCategory<FurnitureDisplay> {
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(PaladinFurnitureModBlocksItems.WORKING_TABLE);
    public static final Text TITLE = Text.translatable("rei.pfm.furniture");

    public CategoryIdentifier<? extends FurnitureDisplay> getCategoryIdentifier() {
        return FurnitureDisplay.IDENTIFIER;
    }

    public Renderer getIcon() {
        return ICON;
    }

    public Text getTitle() {
        return TITLE;
    }

    public List<Widget> setupDisplay(FurnitureDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<InputIngredient<EntryStack<?>>> input = display.getInputIngredients(3, 3);
        List<Slot> slots = Lists.newArrayList();

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
            }
        }

        Iterator var9 = input.iterator();

        while (var9.hasNext()) {
            InputIngredient<EntryStack<?>> ingredient = (InputIngredient) var9.next();
            slots.get(ingredient.getIndex()).entries(ingredient.get());
        }

        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries((Collection) display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Nullable
    public DisplayMerger<FurnitureDisplay> getDisplayMerger() {
        return DisplayCategory.getContentMerger();
    }
}
