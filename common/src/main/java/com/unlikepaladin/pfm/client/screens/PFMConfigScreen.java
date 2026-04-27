package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.widget.PFMOptionListWidget;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.text.TextColor;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PFMConfigScreen extends Screen {
    private final Screen parent;
    private PFMOptionListWidget optionListWidget;
    public AbstractConfigOption<?> focusedConfigOption;
    private Button resetButton;
    private final HashMap<String, AbstractConfigOption> options;
    private final Minecraft minecraft;
    public static boolean isOnServer = false;
    private final MutableComponent TITLE;
    public PFMConfigScreen(Minecraft client, Screen parent) {
        super( new TranslatableComponent("pfm.config.title"));
        this.parent = parent;
        this.minecraft = client;
        TITLE = new TranslatableComponent("pfm.config.title");
        this.options = PaladinFurnitureMod.getPFMConfig().options;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && !optionListWidget.hasChanges.isEmpty()) {
            minecraft.openScreen(new ConfirmScreen(t -> {
                if (t){
                    this.optionListWidget.save();
                    try {
                        PaladinFurnitureMod.getPFMConfig().save();
                    } catch (IOException e) {
                        PaladinFurnitureMod.GENERAL_LOGGER.error("Failed to save config options!");
                        throw new RuntimeException(e);
                    }
                }
                Minecraft.getInstance().openScreen(parent);
            }, new TranslatableComponent("gui.pfm.changesMightNotBeSaved").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xf77f34)).withBold(true)), new TranslatableComponent("gui.pfm.saveChanges")));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.optionListWidget.save();
        Minecraft.getInstance().openScreen(parent);
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
        this.optionListWidget = new PFMOptionListWidget(this, this.minecraft);
        this.addWidget(this.optionListWidget);
        this.resetButton = this.addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TranslatableText("pfm.option.resetAll"), button -> {
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
        }));
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_DONE, button -> {
            this.optionListWidget.save();
            this.minecraft.openScreen(this.parent);
            try {
                PaladinFurnitureMod.getPFMConfig().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (this.optionListWidget != null)
            this.optionListWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredString(matrices, this.font, TITLE.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xf77f34)).withBold(true)), this.width / 2, 8, 0xFFFFFF);
        boolean bl = false;
        for (Map.Entry<AbstractConfigOption, Boolean> optionEntry : optionListWidget.newConfigValues.entrySet()) {
            if (optionEntry.getValue() == optionEntry.getKey().getDefaultValue()) continue;
            bl = true;
            break;
        }
        this.resetButton.active = bl;
        super.render(matrices, mouseX, mouseY, delta);
    }
}
