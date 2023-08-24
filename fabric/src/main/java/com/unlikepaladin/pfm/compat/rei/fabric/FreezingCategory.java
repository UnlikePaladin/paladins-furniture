package com.unlikepaladin.pfm.compat.rei.fabric;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

public class FreezingCategory implements RecipeCategory<FreezingDisplay> {
    public static final EntryStack ICON = EntryStack.create(PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    public static final TranslatableText TITLE = new TranslatableText("rei.pfm.freezer");
    public static final EntryStack[] WORKSTATIONS = new EntryStack[]{EntryStack.create(PaladinFurnitureModBlocksItems.WHITE_FREEZER), EntryStack.create(PaladinFurnitureModBlocksItems.GRAY_FREEZER), EntryStack.create(PaladinFurnitureModBlocksItems.IRON_FREEZER)};

    @Override
    public @NotNull EntryStack getLogo() {
        return ICON;
    }

    @Override
    public @NotNull String getCategoryName() {
        return TITLE.getString();
    }


    public static FreezingWidget createFreezing(Point point) {
        return createFreezing(new Rectangle(point, new Dimension(14, 14)));
    }

    public static FreezingWidget createFreezing(Rectangle rectangle) {
        return new FreezingWidget(rectangle);
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return FreezingDisplay.IDENTIFIER;
    }

    public List<Widget> setupDisplay(FreezingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        double cookingTime = display.getCookingTime();
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(createFreezing(new Point(startPoint.x + 1, startPoint.y + 20)).animationDurationMS(10000.0D));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), new TranslatableText("category.rei.cooking.time&xp", new Object[]{df.format((double)display.getXp()), df.format(cookingTime / 20.0D)})).noShadow().rightAligned().color(-12566464, -4473925));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
        return widgets;
    }
}