package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.widget.PFMOptionListWidget;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.client.MinecraftClient;
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
    public void close() {
        try {
            PaladinFurnitureMod.getPFMConfig().save();
        } catch (IOException e) {
            PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to save config options!");
            throw new RuntimeException(e);
        }
        this.client.setScreen(parent);
    }

    @Override
    protected void init() {
        super.init();
        this.optionListWidget = new PFMOptionListWidget(this, this.client);
        this.addSelectableChild(this.optionListWidget);
        this.resetButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("pfm.option.resetAll"), button -> {
            options.forEach((title, option) -> {
                if (option.getSide() == Side.CLIENT){
                    option.setValue(option.getDefaultValue());
                } else if (!isOnServer && option.getSide() == Side.SERVER) {
                    option.setValue(option.getDefaultValue());
                }
            });
        }).dimensions(this.width/2 - 155, this.height -29, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.client.setScreen(this.parent);
            try {
                PaladinFurnitureMod.getPFMConfig().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).dimensions(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (this.optionListWidget != null)
            this.optionListWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, TITLE.setStyle(Style.EMPTY.withColor(0xf77f34).withBold(true)), this.width / 2, 8, 0xFFFFFF);
        boolean bl = false;
        for (Map.Entry<String, AbstractConfigOption> optionEntry : options.entrySet()) {
            if (optionEntry.getValue().isDefault()) continue;
            bl = true;
            break;
        }
        this.resetButton.active = bl;
        super.render(matrices, mouseX, mouseY, delta);
    }
}
