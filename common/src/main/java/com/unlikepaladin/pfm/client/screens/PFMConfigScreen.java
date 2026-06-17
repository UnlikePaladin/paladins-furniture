package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.widget.PFMOptionListWidget;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PFMConfigScreen extends Screen {
    private final Screen parent;
    private PFMOptionListWidget optionListWidget;
    public AbstractConfigOption<?> focusedConfigOption;
    private Button resetButton;
    private final HashMap<String, AbstractConfigOption> options;
    private final Minecraft client;
    public static boolean isOnServer = false;
    private final MutableComponent TITLE;
    public PFMConfigScreen(Minecraft client, Screen parent) {
        super(Component.translatable("pfm.config.title"));
        this.parent = parent;
        this.client = client;
        TITLE = Component.translatable("pfm.config.title");
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
                Minecraft.getInstance().setScreen(parent);
            }, Component.translatable("gui.pfm.changesMightNotBeSaved").setStyle(Style.EMPTY.withColor(0xf77f34).withBold(true)), Component.translatable("gui.pfm.saveChanges")));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.optionListWidget.save();
        Minecraft.getInstance().setScreen(parent);
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
        this.addWidget(this.optionListWidget);
        this.resetButton = this.addRenderableWidget(Button.builder(Component.translatable("pfm.option.resetAll"), button -> {
            options.forEach((title, option) -> {
                if (option.getSide() == Side.CLIENT || (!isOnServer && option.getSide() == Side.SERVER)) {
                    if (!option.getDefaultValue().equals(this.optionListWidget.newConfigValues.get(option))) {
                        this.optionListWidget.hasChanges.set(this.optionListWidget.configOptionToIndexForHasChanges.get(option), true);
                    }
                    this.optionListWidget.newConfigValues.put(option, option.getDefaultValue());
                }
            });
        }).bounds(this.width/2 - 155, this.height -29, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.optionListWidget.save();
            this.client.setScreen(this.parent);
            try {
                PaladinFurnitureMod.getPFMConfig().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).bounds(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.optionListWidget != null) {
            this.optionListWidget.tick();
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (this.optionListWidget != null)
            this.optionListWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredString(matrices, this.font, TITLE.setStyle(Style.EMPTY.withColor(0xf77f34).withBold(true)), this.width / 2, 8, 0xFFFFFF);
        boolean bl = false;
        for (Map.Entry<AbstractConfigOption, Object> optionEntry : optionListWidget.newConfigValues.entrySet()) {
            if (optionEntry.getValue().equals(optionEntry.getKey().getDefaultValue())) continue;
            bl = true;
            break;
        }
        this.resetButton.active = bl;
        super.render(matrices, mouseX, mouseY, delta);
    }
}
