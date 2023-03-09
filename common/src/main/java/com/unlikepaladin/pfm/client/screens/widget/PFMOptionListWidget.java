package com.unlikepaladin.pfm.client.screens.widget;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.BooleanConfigOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PFMOptionListWidget extends ElementListWidget<PFMOptionListWidget.Entry> {
    final PFMConfigScreen parent;
    int maxKeyNameLength;

    public PFMOptionListWidget(PFMConfigScreen parent, MinecraftClient client) {
        super(client, parent.width + 125, parent.height, 43, parent.height - 32, 20);
        this.parent = parent;
        String string = null;
        for(Map.Entry<String, AbstractConfigOption> configOptionEntry : PaladinFurnitureMod.getPFMConfig().options.entrySet()) {
            Text text;
            int i;
            String configOptionCategory = configOptionEntry.getValue().getCategory();
            if (!configOptionCategory.equals(string)) {
                string = configOptionCategory;
                this.addEntry(new CategoryEntry(new TranslatableText(configOptionCategory)));
            }
            if ((i = client.textRenderer.getWidth(text = configOptionEntry.getValue().getTitle())) > this.maxKeyNameLength) {
                this.maxKeyNameLength = i;
            }
            if (configOptionEntry.getValue().getType() == Boolean.class) {
                this.addEntry(new BooleanEntry((BooleanConfigOption)configOptionEntry.getValue(), text));
            } else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Unsupported Config Type!");
            }
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 15;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    @Environment(value= EnvType.CLIENT)
    public class CategoryEntry
            extends Entry {
        final MutableText text;
        private final int textWidth;

        public CategoryEntry(MutableText text) {
            this.text = text;
            this.textWidth = PFMOptionListWidget.this.client.textRenderer.getWidth(this.text);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.client.textRenderer.draw(matrices, this.text.setStyle(Style.EMPTY.withBold(true)), (float)((PFMOptionListWidget.this.client.currentScreen.width / 2 - this.textWidth / 2)), (float)(y + entryHeight - (PFMOptionListWidget.this).client.textRenderer.fontHeight - 1), 0xFFFFFF);
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            return false;
        }

        @Override
        public List<? extends Element> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable(){

                @Override
                public Selectable.SelectionType getType() {
                    return Selectable.SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, PFMOptionListWidget.CategoryEntry.this.text);
                }
            });
        }
    }

    @Environment(value=EnvType.CLIENT)
    public class BooleanEntry
            extends Entry {
        private final BooleanConfigOption configOption;
        private final Text optionName;
        private final ButtonWidget valueButton;
        private final ButtonWidget resetButton;

        BooleanEntry(final BooleanConfigOption configOption, final Text optionName) {
            this.configOption = configOption;
            this.optionName = optionName;
            this.valueButton = new ButtonWidget(0, 0, 75, 20, optionName, button -> {
                PFMOptionListWidget.this.parent.focusedConfigOption = configOption;
                configOption.setValue(!configOption.getValue());
            });

            this.resetButton = new ButtonWidget(0, 0, 50, 20, new TranslatableText("controls.reset"), button -> {
                configOption.setValue(configOption.getDefaultValue());
            }){

                @Override
                protected MutableText getNarrationMessage() {
                    return new TranslatableText("narrator.controls.reset", optionName);
                }
            };
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.client.textRenderer.draw(matrices, this.optionName, (float)(x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (float)(y + entryHeight / 2 - PFMOptionListWidget.this.client.textRenderer.fontHeight / 2), 0xFFFFFF);
            this.resetButton.x = x + 190;
            this.resetButton.y = y;
            this.resetButton.active = !this.configOption.isDefault();
            this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
            this.valueButton.x = x + 105;
            this.valueButton.y = y;
            this.valueButton.setMessage(this.configOption.getValue() ? ScreenTexts.YES : ScreenTexts.NO);
            this.valueButton.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.valueButton, this.resetButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.valueButton, this.resetButton);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.valueButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return this.resetButton.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.valueButton.mouseReleased(mouseX, mouseY, button) || this.resetButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static abstract class Entry extends ElementListWidget.Entry<Entry> {
    }
}

