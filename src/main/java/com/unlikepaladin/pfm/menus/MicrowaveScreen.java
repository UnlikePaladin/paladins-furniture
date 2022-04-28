package com.unlikepaladin.pfm.menus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MicrowaveScreen extends HandledScreen<MicrowaveScreenHandler> {
    private final Identifier background = new Identifier(PaladinFurnitureMod.MOD_ID,"textures/gui/container/microwave.png");
    private boolean narrow;
    public boolean isActive;
    private MicrowaveBlockEntity microwaveBlockEntity;

    private final TranslatableText startButtonText = new TranslatableText("gui.pfm.microwave.start_button");
    public MicrowaveScreen(MicrowaveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    public ButtonWidget startButton;

    @Override
    public void init() {
        super.init();
        this.microwaveBlockEntity = handler.microwaveBlockEntity;
        isActive = handler.getActive();
        this.narrow = this.width < 379;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.startButton = this.addDrawableChild(new ButtonWidget(this.x, this.y, 40, 20, startButtonText, button -> {
            handler.setActive(true);
            System.out.println(" button");
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        // if (this.recipeBook.isOpen() && this.narrow) {
        if (this.narrow) {
            this.drawBackground(matrices, delta, mouseX, mouseY);
            //this.recipeBook.render(matrices, mouseX, mouseY, delta);
        } else {
            //this.recipeBook.render(matrices, mouseX, mouseY, delta);
            super.render(matrices, mouseX, mouseY, delta);
            //this.recipeBook.drawGhostSlots(matrices, this.x, this.y, true, delta);
        }
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        //this.recipeBook.drawTooltip(matrices, this.x, this.y, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int k;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, this.background);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (((AbstractMicrowaveScreenHandler)this.handler).isActive()) {
            k = ((AbstractMicrowaveScreenHandler)this.handler).getCookProgress();
            this.drawTexture(matrices, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
        k = ((AbstractMicrowaveScreenHandler)this.handler).getCookProgress();
        this.drawTexture(matrices, i + 79, j + 34, 176, 14, k + 1, 16);
    }

    public Recipe<?> getRecipe(World world, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(RecipeType.SMOKING, inventory, world).orElse(null);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.isActive = handler.isActive;
        //MicrowaveBlockEntity.canAcceptRecipeOutput(microwaveBlockEntity.getRecipe(), microwaveBlockEntity.inventory ,microwaveBlockEntity.getMaxCountPerStack())
       //this.handler.getSlot(0).getStack().isEmpty()
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1,handler.getInventory().getStack(0));
        Recipe<?> recipe = getRecipe(handler.world, handler.getInventory());
        //System.out.println(inventory);
        if(!MicrowaveBlockEntity.canAcceptRecipeOutput(recipe, inventory ,microwaveBlockEntity.getMaxCountPerStack())) {
            this.startButton.active = false;
        }
        else {
            this.startButton.active = !this.handler.isActive();
        }
    }
}
