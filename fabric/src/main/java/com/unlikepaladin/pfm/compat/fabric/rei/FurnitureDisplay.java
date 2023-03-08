package com.unlikepaladin.pfm.compat.fabric.rei;

/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class FurnitureDisplay<C extends FurnitureRecipe> extends BasicDisplay implements SimpleGridMenuDisplay {
    protected Optional<C> recipe;
    public static final CategoryIdentifier<FurnitureDisplay> IDENTIFIER = CategoryIdentifier.of(new Identifier(PaladinFurnitureMod.MOD_ID, "crafting"));

    public FurnitureDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<C> recipe) {
        this(inputs, outputs, recipe.map(FurnitureRecipe::getId), recipe);
    }

    public FurnitureDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, Optional<C> recipe) {
        super(inputs, outputs, location);
        this.recipe = recipe;
    }

    private static final List<CraftingRecipeSizeProvider<?>> SIZE_PROVIDER = new ArrayList<>();



    /**
     * Registers a size provider for crafting recipes.
     * This is not reloadable, please statically registerBlocks your provider, and
     * do not repeatedly registerBlocks it.
     *
     * @param sizeProvider the provider to registerBlocks
     * @param <R>          the recipe type
     */
    public static <R extends FurnitureRecipe> void registerSizeProvider(CraftingRecipeSizeProvider<R> sizeProvider) {
        SIZE_PROVIDER.add(0, sizeProvider);

    }

    static {
        registerPlatformSizeProvider();
    }
    public static void registerPlatformSizeProvider() {
        registerSizeProvider(recipe -> {
            if (recipe instanceof FurnitureRecipe) {
                return new CraftingRecipeSizeProvider.Size(recipe.getWidth(), recipe.getHeight());
            }
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Registering Furniture REI Display as null");
            return null;
        });
    }

    @Nullable
    public static FurnitureDisplay<?> of(FurnitureRecipe recipe) {
            DefaultedList<Ingredient> ingredients = recipe.getIngredients();
            for (CraftingRecipeSizeProvider<?> pair : SIZE_PROVIDER) {
                CraftingRecipeSizeProvider.Size size = ((CraftingRecipeSizeProvider<FurnitureRecipe>) pair).getSize(recipe);

                if (size != null) {
                    return new DefaultCustomShapedDisplay(recipe, EntryIngredients.ofIngredients(recipe.getIngredients()),
                            Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                            size.getWidth(), size.getHeight());
                }


            }
           return null;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }

    public Optional<C> getOptionalRecipe() {
        return recipe;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return getOptionalRecipe().map(FurnitureRecipe::getId);
    }

    public <T extends ScreenHandler> List<List<ItemStack>> getOrganisedInputEntries(SimpleGridMenuInfo<T, FurnitureDisplay<?>> menuInfo, T container) {
        return CollectionUtils.map(getOrganisedInputEntries(menuInfo.getCraftingWidth(container), menuInfo.getCraftingHeight(container)), ingredient ->
                CollectionUtils.<EntryStack<?>, ItemStack>filterAndMap(ingredient, stack -> stack.getType() == VanillaEntryTypes.ITEM,
                        EntryStack::castValue));
    }

    public <T extends ScreenHandler> List<EntryIngredient> getOrganisedInputEntries(int menuWidth, int menuHeight) {
        List<EntryIngredient> list = new ArrayList<>(menuWidth * menuHeight);
        for (int i = 0; i < menuWidth * menuHeight; i++) {
            list.add(EntryIngredient.empty());
        }
        for (int i = 0; i < getInputEntries().size(); i++) {
            list.set(getSlotWithSize(this, i, menuWidth), getInputEntries().get(i));
        }
        return list;
    }

    public static int getSlotWithSize(FurnitureDisplay<?> display, int index, int craftingGridWidth) {
        return getSlotWithSize(display.getInputWidth(), index, craftingGridWidth);
    }

    public static int getSlotWithSize(int recipeWidth, int index, int craftingGridWidth) {
        int x = index % recipeWidth;
        int y = (index - x) / recipeWidth;
        return craftingGridWidth * y + x;
    }

    public static BasicDisplay.Serializer<FurnitureDisplay<?>> serializer() {
        return BasicDisplay.Serializer.<FurnitureDisplay<?>>ofSimple(DefaultCustomDisplay::simple)
                .inputProvider(display -> display.getOrganisedInputEntries(3, 3));
    }

    @Override
    public List<InputIngredient<EntryStack<?>>> getInputIngredients(MenuSerializationContext<?, ?, ?> context, MenuInfo<?, ?> info, boolean fill) {
        int craftingWidth = 3, craftingHeight = 3;

        if (info instanceof SimpleGridMenuInfo && fill) {
            craftingWidth = ((SimpleGridMenuInfo<ScreenHandler, ?>) info).getCraftingWidth(context.getMenu());
            craftingHeight = ((SimpleGridMenuInfo<ScreenHandler, ?>) info).getCraftingHeight(context.getMenu());
        }

        return getInputIngredients(craftingWidth, craftingHeight);
    }

    public List<InputIngredient<EntryStack<?>>> getInputIngredients(int craftingWidth, int craftingHeight) {
        int inputWidth = Math.max(3, getInputWidth());
        int inputHeight = Math.max(3, getInputHeight());

        InputIngredient<EntryStack<?>>[][] grid = new InputIngredient[Math.max(inputWidth, craftingWidth)][Math.max(inputHeight, craftingHeight)];

        List<EntryIngredient> inputEntries = getInputEntries();
        for (int i = 0; i < inputEntries.size(); i++) {
            grid[i % getInputWidth()][i / getInputWidth()] = InputIngredient.of(getSlotWithSize(getInputWidth(), i, craftingWidth), inputEntries.get(i));
        }

        List<InputIngredient<EntryStack<?>>> list = new ArrayList<>(craftingWidth * craftingHeight);
        for (int i = 0, n = craftingWidth * craftingHeight; i < n; i++) {
            list.add(InputIngredient.empty(i));
        }

        for (int x = 0; x < craftingWidth; x++) {
            for (int y = 0; y < craftingHeight; y++) {
                if (grid[x][y] != null) {
                    int index = craftingWidth * y + x;
                    list.set(index, grid[x][y]);
                }
            }
        }

        return list;
    }

    private static class DefaultCustomShapedDisplay extends FurnitureDisplay<FurnitureRecipe> {
        private int width;
        private int height;

        public DefaultCustomShapedDisplay(@Nullable FurnitureRecipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output, int width, int height) {
            this(null, possibleRecipe, input, output, width, height);
        }

        public DefaultCustomShapedDisplay(@Nullable Identifier location, @Nullable FurnitureRecipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output, int width, int height) {
            super(input, output, Optional.ofNullable(location == null && possibleRecipe != null ? possibleRecipe.getId() : location), Optional.ofNullable(possibleRecipe));
            this.width = width;
            this.height = height;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }
    }

    private static class DefaultCustomDisplay extends FurnitureDisplay<FurnitureRecipe> {
        private int width;
        private int height;

        public DefaultCustomDisplay(@Nullable FurnitureRecipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output) {
            this(null, possibleRecipe, input, output);
        }

        public DefaultCustomDisplay(@Nullable Identifier location, @Nullable FurnitureRecipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output) {
            super(input, output, Optional.ofNullable(location == null && possibleRecipe != null ? possibleRecipe.getId() : location), Optional.ofNullable(possibleRecipe));
            BitSet row = new BitSet(3);
            BitSet column = new BitSet(3);
            for (int i = 0; i < 9; i++)
                if (i < input.size()) {
                    EntryIngredient stacks = input.get(i);
                    if (stacks.stream().anyMatch(stack -> !stack.isEmpty())) {
                        row.set((i - (i % 3)) / 3);
                        column.set(i % 3);
                    }
                }
            this.width = column.cardinality();
            this.height = row.cardinality();
        }

        public static DefaultCustomDisplay simple(List<EntryIngredient> input, List<EntryIngredient> output, Optional<Identifier> location) {
            FurnitureRecipe optionalRecipe = (FurnitureRecipe) location.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation))
                    .orElse(null);
            return new DefaultCustomDisplay(location.orElse(null), optionalRecipe, input, output);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public int getInputWidth() {
            return 3;
        }

        @Override
        public int getInputHeight() {
            return 3;
        }
    }
}

