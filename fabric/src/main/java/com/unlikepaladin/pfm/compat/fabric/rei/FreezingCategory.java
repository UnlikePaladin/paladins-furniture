package com.unlikepaladin.pfm.compat.fabric.rei;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

public class FreezingCategory implements DisplayCategory<FreezingDisplay> {
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(PaladinFurnitureModBlocksItems.WHITE_FRIDGE);
    public static final TranslatableText TITLE = new TranslatableText("rei.pfm.freezer");

    @Override
    public Renderer getIcon() {
        return ICON;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public CategoryIdentifier<? extends FreezingDisplay> getCategoryIdentifier() {
        return FreezingDisplay.IDENTIFIER;
    }


    public static FreezingWidget createFreezing(Point point) {
        return createFreezing(new Rectangle(point, new Dimension(14, 14)));
    }

    public static FreezingWidget createFreezing(Rectangle rectangle) {
        return new FreezingWidget(rectangle);
    }

    public List<Widget> setupDisplay(FreezingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        double cookingTime = display.cookTime;
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(createFreezing(new Point(startPoint.x + 1, startPoint.y + 20)).animationDurationMS(10000.0D));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), new TranslatableText("category.rei.cooking.time&xp", new Object[]{df.format((double)display.getXp()), df.format(cookingTime / 20.0D)})).noShadow().rightAligned().color(-12566464, -4473925));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries((Collection)display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries((Collection)display.getInputEntries().get(0)).markInput());
        return widgets;
    }
}
