package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.widget.PFMOptionListWidget;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PFMConfigScreen extends Screen {
    private final Screen parent;
    private PFMOptionListWidget optionListWidget;
    public AbstractConfigOption<?> focusedConfigOption;
    private ButtonWidget resetButton;
    private final HashMap<String, AbstractConfigOption> options;
    private final MinecraftClient client;
    public static boolean isOnServer = false;
    private final MutableText TITLE;
    public PFMConfigScreen(MinecraftClient client, Screen parent) {
        super(Text.translatable("pfm.config.title"));
        this.parent = parent;
        this.client = client;
        TITLE = Text.translatable("pfm.config.title");
        this.options = PaladinFurnitureMod.getPFMConfig().options;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && !optionListWidget.hasChanges.isEmpty()) {
            client.setScreen(new ConfirmScreen(t -> {
                if (t){
                    this.optionListWidget.save();
                    try {
                        PaladinFurnitureMod.getPFMConfig().save();
                    } catch (IOException e) {
                        PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to save config options!");
                        throw new RuntimeException(e);
                    }
                }
                MinecraftClient.getInstance().setScreen(parent);
            }, Text.translatable("gui.pfm.changesMightNotBeSaved").setStyle(Style.EMPTY.withColor(0xf77f34).withBold(true)), Text.translatable("gui.pfm.saveChanges")));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        this.optionListWidget.save();
        MinecraftClient.getInstance().setScreen(parent);
        try {
            PaladinFurnitureMod.getPFMConfig().save();
        } catch (IOException e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to save config options!");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.optionListWidget = new PFMOptionListWidget(this, this.client);
        this.addSelectableChild(this.optionListWidget);
        this.resetButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("pfm.option.resetAll"), button -> {
            options.forEach((title, option) -> {
                if (option.getSide() == Side.CLIENT){
                    if (option.getType() == Boolean.class) {
                        if (option.getDefaultValue() != this.optionListWidget.newConfigValues.get(option)) {
                            this.optionListWidget.hasChanges.set(this.optionListWidget.configOptionToIndexForHasChanges.get(option), true);
                        }
                        this.optionListWidget.newConfigValues.put(option, (Boolean) option.getDefaultValue());
                    }
                } else if (!isOnServer && option.getSide() == Side.SERVER) {
                    if (option.getType() == Boolean.class) {
                        if (option.getDefaultValue() != this.optionListWidget.newConfigValues.get(option)) {
                            this.optionListWidget.hasChanges.set(this.optionListWidget.configOptionToIndexForHasChanges.get(option), true);
                        }
                        this.optionListWidget.newConfigValues.put(option, (Boolean) option.getDefaultValue());
                    }
                }
            });
        }).dimensions(this.width/2 - 155, this.height -29, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.optionListWidget.save();
            this.client.setScreen(this.parent);
            try {
                PaladinFurnitureMod.getPFMConfig().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).dimensions(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.optionListWidget != null)
            this.optionListWidget.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, TITLE.setStyle(Style.EMPTY.withColor(0xf77f34).withBold(true)), this.width / 2, 8, 0xFFFFFF);
        boolean bl = false;
        for (Map.Entry<AbstractConfigOption, Boolean> optionEntry : optionListWidget.newConfigValues.entrySet()) {
            if (optionEntry.getValue() == optionEntry.getKey().getDefaultValue()) continue;
            bl = true;
            break;
        }
        this.resetButton.active = bl;
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }
}
