package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("pfm:textures/gui/container/working_table.png");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int RECIPE_LIST_COLUMNS = 6;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 16;
    private static final int RECIPE_ENTRY_HEIGHT = 18;
    private static final int SCROLLBAR_AREA_HEIGHT = 54;
    private static final int RECIPE_LIST_OFFSET_X = 20;
    private static final int RECIPE_LIST_OFFSET_Y = 30;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    private EditBox searchBox;

    public WorkbenchScreen(WorkbenchScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        menu.setContentsChangedListener(this::onInventoryChange);
        this.canCraft = menu.canCraft();
    }

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, this.leftPos + 20, this.topPos + 18, 110, this.font.lineHeight, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(0xFFFFFF);
        this.addWidget(this.searchBox);
        this.imageHeight = 180;
        this.imageWidth = 176;
        this.inventoryLabelY = this.imageHeight - 92;
    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        String string = this.searchBox.getValue();
        this.init(client, width, height);
        this.searchBox.setValue(string);
        if (!this.searchBox.getValue().isEmpty()) {
            this.search();
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        String string = this.searchBox.getValue();
        if (this.searchBox.charTyped(chr, modifiers)) {
            if (!Objects.equals(string, this.searchBox.getValue())) {
                this.search();
            }
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl2 = InputConstants.getKey(keyCode, scanCode).getNumericKeyValue().isPresent();
        if (bl2 && this.checkHotbarKeyPressed(keyCode, scanCode)) {
            return true;
        }
        String string = this.searchBox.getValue();
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            if (!Objects.equals(string, this.searchBox.getValue())) {
                this.search();
            }
            return true;
        }
        if (this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != GLFW.GLFW_KEY_ESCAPE) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    private final Set<TagKey<Item>> searchResultTags = new HashSet<>();

    private void search() {
        this.menu.getSearchableRecipes().clear();
        this.searchResultTags.clear();
        String string = this.searchBox.getValue();
        if (string.isEmpty()) {
            this.menu.updateInput();
            this.menu.searching = false;
        } else {
            this.menu.updateInput();
            List<FurnitureRecipe.CraftableFurnitureRecipe> filteredRecipes = menu.getSortedRecipes().stream()
                    .filter(recipe -> I18n.get(recipe.getResultItem(minecraft.level.registryAccess()).getDescriptionId())
                    .toLowerCase().contains(string.trim().toLowerCase())).toList();
            this.menu.getSearchableRecipes().addAll(filteredRecipes);
            this.menu.searching = true;
        }
        this.scrollAmount = 0.0f;
        this.scrollOffset = 0;
    }

    private void searchForTags(String id) {
        int i = id.indexOf(58);
        Predicate<ResourceLocation> predicate;
        if (i == -1) {
            predicate = (idx) -> idx.getPath().contains(id);
        } else {
            String string = id.substring(0, i).trim();
            String string2 = id.substring(i + 1).trim();
            predicate = (idx) -> idx.getNamespace().contains(string) && idx.getPath().contains(string2);
        }

        Stream<TagKey<Item>> keyStream = BuiltInRegistries.ITEM.getTagNames().filter((tagKey) -> predicate.test(tagKey.location()));
        keyStream.forEach(this.searchResultTags::add);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.searchBox != null) {
            //this.searchBox.tick();
        }
    }
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        context.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int)(41.0f * this.scrollAmount);
        context.blit(TEXTURE, x + 119, y + 31 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        int xOffSetForIcons = this.leftPos + RECIPE_LIST_OFFSET_X;
        int yOffsetForIcons = this.topPos + RECIPE_LIST_OFFSET_Y;
        int scrollOffsetForIcons = this.scrollOffset + 18;
        this.renderRecipeBackground(context, mouseX, mouseY, xOffSetForIcons, yOffsetForIcons, scrollOffsetForIcons);
        this.renderRecipeIcons(context, xOffSetForIcons, yOffsetForIcons, scrollOffsetForIcons);
        this.searchBox.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderTooltip(GuiGraphics context, int x, int y) {
        super.renderTooltip(context, x, y);
        int xOffsetForTooltip = this.leftPos + RECIPE_LIST_OFFSET_X;
        int yOffsetForTooltip = this.topPos + RECIPE_LIST_OFFSET_Y;
        int scrollOffsetForTooltip = this.scrollOffset + 18;
        for (int recipeIndex = this.scrollOffset; recipeIndex < scrollOffsetForTooltip && recipeIndex < this.menu.getVisibleRecipeCount(); ++recipeIndex) {
            int m = recipeIndex - this.scrollOffset;
            int n = xOffsetForTooltip + m % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int o = yOffsetForTooltip + m / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;
            if (x < n || x >= n + RECIPE_ENTRY_WIDTH || y < o || y >= o + RECIPE_ENTRY_HEIGHT) continue;
            List<Component> tooltip = new ArrayList<>();
            int iCopy = recipeIndex;
            if (this.menu.searching) {
                iCopy = this.menu.getSortedRecipes().indexOf(this.menu.getSearchableRecipes().get(iCopy));
            }
            tooltip.add(getTooltipFromContainerItem(this.menu.getSortedRecipes().get(iCopy).getResultItem(minecraft.level.registryAccess())).get(0));
            tooltip.add(Component.translatable("container.pfm.working_table.ingredient_required").setStyle(Style.EMPTY.withItalic(true)));
            HashMap<Item, Integer> itemStackCountMap = new HashMap<>();
            for (Ingredient ingredient : this.menu.getSortedRecipes().get(iCopy).getIngredients()) {
                for (ItemStack stack : ingredient.getItems()) {
                    if (!itemStackCountMap.containsKey(stack.getItem())) {
                        itemStackCountMap.put(stack.getItem(), stack.getCount());
                    } else {
                        itemStackCountMap.put(stack.getItem(), itemStackCountMap.get(stack.getItem()) + stack.getCount());
                    }
                }
            }
            itemStackCountMap.forEach((item, integer) -> {
                int itemCount = menu.getPlayerInventory().countItem(item);
                Style style = Style.EMPTY.withColor(ChatFormatting.GRAY);
                if (itemCount < integer) {
                    style = style.withColor(ChatFormatting.RED);
                }
                tooltip.add(Component.literal(integer + " ").append(Component.literal(getTooltipFromContainerItem(item.getDefaultInstance()).get(0).getString())).setStyle(style));
            });
            context.renderComponentTooltip(this.font, tooltip, x, y);
        }
    }

    private void renderRecipeBackground(GuiGraphics context, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < this.menu.getVisibleRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = j / RECIPE_LIST_COLUMNS;
            int m = y + l * RECIPE_ENTRY_HEIGHT + 2;
            int v = this.imageHeight;
            int iCopy = i;
            if (this.menu.searching) {
                iCopy = this.menu.getSortedRecipes().indexOf(this.menu.getSearchableRecipes().get(iCopy));
            }
            if (iCopy == this.menu.getSelectedRecipe()) {
                v += 55;
            }
            else if (!this.menu.getAvailableRecipes().contains(this.menu.getSortedRecipes().get(iCopy))) {
                v += 18;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
                v += 36;
            }
            context.blit(TEXTURE, k, m - 1, 0, v, 16, 18);
        }
    }

    private void renderRecipeIcons(GuiGraphics context, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < this.menu.getVisibleRecipeCount(); ++i) {
            int iMinusScrollOffset = i - this.scrollOffset;
            int xOffset = x + iMinusScrollOffset % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int l = iMinusScrollOffset / RECIPE_LIST_COLUMNS;
            int yOffset = y + l * RECIPE_ENTRY_HEIGHT + 2;
            int iCopy = i;
            if (this.menu.searching) {
                iCopy = this.menu.getSortedRecipes().indexOf(this.menu.getSearchableRecipes().get(iCopy));
            }
            context.renderItem(this.menu.getSortedRecipes().get(iCopy).getResultItem(minecraft.level.registryAccess()), xOffset, yOffset);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int xOffsetForMouseClick = this.leftPos + RECIPE_LIST_OFFSET_X;
            int yOffsetForMouseClick = this.topPos + RECIPE_LIST_OFFSET_Y;
            int scrollOffsetForMouseClick = this.scrollOffset + 18;
            for (int clickedRecipeId = this.scrollOffset; clickedRecipeId < scrollOffsetForMouseClick; ++clickedRecipeId) {
                int m = clickedRecipeId - this.scrollOffset;
                double d = mouseX - (double)(xOffsetForMouseClick + m % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH);
                double e = mouseY - (double)(yOffsetForMouseClick + m / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT);
                int clickedRecipeIdCopy = clickedRecipeId;
                if (this.menu.searching) {
                    if (clickedRecipeIdCopy < this.menu.getSearchableRecipes().size())
                        clickedRecipeIdCopy = this.menu.getSortedRecipes().indexOf(this.menu.getSearchableRecipes().get(clickedRecipeIdCopy));
                    else
                        clickedRecipeIdCopy = -1;
                }
                if (!(d >= 0.0) || !(e >= 0.0) || !(d < 16.0) || !(e < 18.0) || !this.menu.clickMenuButton(this.minecraft.player, clickedRecipeIdCopy)) continue;
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, clickedRecipeIdCopy);
                return true;
            }
            xOffsetForMouseClick = this.leftPos + 119;
            yOffsetForMouseClick = this.topPos + 9;
            if (mouseX >= (double)xOffsetForMouseClick && mouseX < (double)(xOffsetForMouseClick + SCROLLBAR_WIDTH) && mouseY >= (double)yOffsetForMouseClick && mouseY < (double)(yOffsetForMouseClick + SCROLLBAR_AREA_HEIGHT)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.topPos + 30;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5f) / ((float)(j - i) - SCROLLBAR_HEIGHT);
            this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * RECIPE_LIST_COLUMNS;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            this.scrollAmount = (float)((double)this.scrollAmount - verticalAmount / (double)i);
            this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * RECIPE_LIST_COLUMNS;
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.menu.getVisibleRecipeCount() > 18;
    }

    protected int getMaxScroll() {
        return (this.menu.getVisibleRecipeCount() + RECIPE_LIST_COLUMNS - 1) / RECIPE_LIST_COLUMNS - RECIPE_LIST_ROWS;
    }

    private void onInventoryChange() {
        this.canCraft = this.menu.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0f;
            this.scrollOffset = 0;
        }
    }
}