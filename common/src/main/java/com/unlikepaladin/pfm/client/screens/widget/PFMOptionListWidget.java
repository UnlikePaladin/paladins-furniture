package com.unlikepaladin.pfm.client.screens.widget;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.BooleanConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.config.option.DoubleConfigOption;
import net.minecraft.client.gui.components.EditBox;
import com.unlikepaladin.pfm.runtime.PFMAssetGenerator;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.*;
import java.util.function.Consumer;

public class PFMOptionListWidget extends ContainerObjectSelectionList<PFMOptionListWidget.Entry> {
    final PFMConfigScreen parent;
    int maxKeyNameLength;
    public BitSet hasChanges;
    public Map<AbstractConfigOption, Object> newConfigValues;
    public Map<AbstractConfigOption, Integer> configOptionToIndexForHasChanges;
    public PFMOptionListWidget(PFMConfigScreen parent, Minecraft client) {
        super(client, parent.width + 125, parent.height, 43, parent.height - 32, 20);
        this.parent = parent;
        String string = null;
        int index = 0;
        hasChanges = new BitSet(PaladinFurnitureMod.getPFMConfig().options.size());
        newConfigValues = new HashMap<>(PaladinFurnitureMod.getPFMConfig().options.size());
        configOptionToIndexForHasChanges = new HashMap<>(PaladinFurnitureMod.getPFMConfig().options.size());
        for(Map.Entry<String, AbstractConfigOption> configOptionEntry : PaladinFurnitureMod.getPFMConfig().options.entrySet()) {
            Component text;
            int i;
            String configOptionCategory = configOptionEntry.getValue().getCategory();
            if (!configOptionCategory.equals(string)) {
                string = configOptionCategory;
                this.addEntry(new CategoryEntry(Component.translatable(configOptionCategory)));
            }
            if ((i = client.font.width(text = configOptionEntry.getValue().getTitle())) > this.maxKeyNameLength) {
                this.maxKeyNameLength = i;
            }
            if (configOptionEntry.getValue().getType() == Boolean.class) {
                PFMOptionListWidget.this.newConfigValues.put(configOptionEntry.getValue(), configOptionEntry.getValue().getValue());
                this.addEntry(new BooleanEntry((BooleanConfigOption)configOptionEntry.getValue(), text, index));
            } else if (configOptionEntry.getValue().getType() == Double.class) {
                PFMOptionListWidget.this.newConfigValues.put(configOptionEntry.getValue(), configOptionEntry.getValue().getValue());
                this.addEntry(new DoubleEntry((DoubleConfigOption)configOptionEntry.getValue(), text, index));
            } else {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Unsupported Config Type!");
            }
            configOptionToIndexForHasChanges.put(configOptionEntry.getValue(), index);
            index++;
        }
        this.addEntry(new CategoryEntry(Component.literal("")));
        this.addEntry(new ButtonEntry(Side.CLIENT, Component.translatable("pfm.option.regenAssets"), Component.translatable("pfm.config.regen"), Component.translatable("pfm.option.regenAssets.tooltip"), button -> {
            PFMFileUtil.deleteDir(PFMRuntimeResources.getAssetPackDirectory().toFile());
            PFMAssetGenerator.FROZEN = false;
            PFMRuntimeResources.prepareAndRunAssetGen(true);
            Minecraft.getInstance().delayTextureReload();
        }));
        ButtonEntry entry = new ButtonEntry(Side.SERVER, Component.translatable("pfm.option.regenData"), Component.translatable("pfm.config.regen"), Component.translatable("pfm.option.regenData.tooltip"), button -> {
            PFMFileUtil.deleteDir(PFMRuntimeResources.getDataPackDirectory().toFile());
            PFMDataGenerator.FROZEN = false;
            PFMRuntimeResources.prepareAndRunDataGen(true);
        });
        entry.button.active = !PFMConfigScreen.isOnServer;
        this.addEntry(entry);
    }

    public void save() {
        for (Map.Entry<AbstractConfigOption, Object> entry : newConfigValues.entrySet()) {
            if (entry.getKey().getType() == Boolean.class)
                entry.getKey().setValue(entry.getValue());
            else if (entry.getKey().getType() == Double.class)
                entry.getKey().setValue(entry.getValue());
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    @Environment(value= EnvType.CLIENT)
    public class CategoryEntry
            extends Entry {
        final MutableComponent text;
        private final int textWidth;

        public CategoryEntry(MutableComponent text) {
            this.text = text;
            this.textWidth = PFMOptionListWidget.this.minecraft.font.width(this.text);
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.minecraft.font.draw(matrices, this.text.setStyle(Style.EMPTY.withBold(true)), (float)((PFMOptionListWidget.this.minecraft.screen.width / 2 - this.textWidth / 2)), (float)(y + entryHeight - (PFMOptionListWidget.this).minecraft.font.lineHeight - 1), 0xFFFFFF);
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            return false;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry(){

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput builder) {
                    builder.add(NarratedElementType.TITLE, PFMOptionListWidget.CategoryEntry.this.text);
                }
            });
        }
    }

    @Environment(value=EnvType.CLIENT)
    public class BooleanEntry
            extends Entry {
        private final BooleanConfigOption configOption;
        private final Component optionName;
        private final Button valueButton;
        private final Button resetButton;

        private final Button.OnTooltip supplier;
        int index;
        boolean hasChanges = false;
        BooleanEntry(final BooleanConfigOption configOption, final Component optionName, int index) {
            this.configOption = configOption;
            this.optionName = optionName;
            this.index = index;
            this.supplier = new Button.OnTooltip() {
                final MutableComponent sideText = configOption.getSide() == Side.CLIENT ? Component.translatable("pfm.option.client").setStyle(Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)) : Component.translatable("pfm.option.server").setStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)));
                final MutableComponent styledTooltip = ((MutableComponent)configOption.getToolTip()).setStyle(Style.EMPTY.withItalic(true));
                final MutableComponent combinedText = Component.literal("").append(sideText).append(Component.literal("\n")).append(styledTooltip);
                @Override
                public void onTooltip(Button button, PoseStack matrices, int mouseX, int mouseY) {
                    PFMOptionListWidget.this.parent.renderTooltip(matrices, PFMOptionListWidget.this.minecraft.font.split(combinedText, Math.max(PFMOptionListWidget.this.width / 2 - 43, 170)), mouseX, mouseY);
                }
                @Override
                public void narrateTooltip(Consumer<Component> consumer) {
                    consumer.accept(combinedText);
                    Button.OnTooltip.super.narrateTooltip(consumer);
                }
            };

            this.valueButton = new Button(0, 0, 75, 20, optionName, button -> {
                PFMOptionListWidget.this.parent.focusedConfigOption = configOption;
                PFMOptionListWidget.this.newConfigValues.put(configOption, !(Boolean) PFMOptionListWidget.this.newConfigValues.get(configOption));
                hasChanges = !hasChanges;
                PFMOptionListWidget.this.hasChanges.set(index, hasChanges);
            }, this.supplier);

            this.resetButton = new Button(0, 0, 50, 20, Component.translatable("controls.reset"), button -> {
                PFMOptionListWidget.this.newConfigValues.put(configOption, configOption.getDefaultValue());
                hasChanges = true;
                PFMOptionListWidget.this.hasChanges.set(index, true);
            }){

                @Override
                protected MutableComponent createNarrationMessage() {
                    return Component.translatable("narrator.controls.reset", optionName);
                }
            };
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.minecraft.font.draw(matrices, this.optionName, (float)(x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (float)(y + entryHeight / 2 - PFMOptionListWidget.this.minecraft.font.lineHeight / 2), 0xFFFFFF);
            this.resetButton.x = x + 190;
            this.resetButton.y = y;
            this.resetButton.active = this.configOption.getSide() == Side.SERVER ? !PFMConfigScreen.isOnServer && !this.configOption.getDefaultValue().equals(PFMOptionListWidget.this.newConfigValues.get(configOption)) : !this.configOption.getDefaultValue().equals(PFMOptionListWidget.this.newConfigValues.get(configOption));
            this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
            this.valueButton.x = x + 105;
            this.valueButton.y = y;
            this.valueButton.setMessage((Boolean) PFMOptionListWidget.this.newConfigValues.get(configOption) ? CommonComponents.GUI_YES : CommonComponents.GUI_NO);
            this.valueButton.active = this.configOption.getSide() != Side.SERVER || !PFMConfigScreen.isOnServer;
            this.valueButton.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.valueButton, this.resetButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
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
    public class DoubleEntry
            extends Entry {
        private final DoubleConfigOption configOption;
        private final Component optionName;
        public final EditBox valueEditBox;
        private final Button resetButton;
        private final Button.OnTooltip supplier;
        int index;
        boolean hasChanges = false;
        DoubleEntry(final DoubleConfigOption configOption, final Component optionName, int index) {
            this.configOption = configOption;
            this.optionName = optionName;
            this.index = index;
            this.supplier = new Button.OnTooltip() {
                final MutableComponent sideText = configOption.getSide() == Side.CLIENT ? Component.translatable("pfm.option.client").setStyle(Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)) : Component.translatable("pfm.option.server").setStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)));
                final MutableComponent styledTooltip = ((MutableComponent)configOption.getToolTip()).setStyle(Style.EMPTY.withItalic(true));
                final MutableComponent combinedText = Component.literal("").append(sideText).append(Component.literal("\n")).append(styledTooltip);
                @Override
                public void onTooltip(Button button, PoseStack matrices, int mouseX, int mouseY) {
                    PFMOptionListWidget.this.parent.renderTooltip(matrices, PFMOptionListWidget.this.minecraft.font.split(combinedText, Math.max(PFMOptionListWidget.this.width / 2 - 43, 170)), mouseX, mouseY);
                }
                @Override
                public void narrateTooltip(Consumer<Component> consumer) {
                    consumer.accept(combinedText);
                    Button.OnTooltip.super.narrateTooltip(consumer);
                }
            };

            this.valueEditBox = new EditBox(PFMOptionListWidget.this.minecraft.font, 0, 0, 70, 16, optionName);
            this.valueEditBox.setValue(String.valueOf(PFMOptionListWidget.this.newConfigValues.get(configOption)));
            this.valueEditBox.setResponder(text -> {
                try {
                    double val = Double.parseDouble(text);
                    PFMOptionListWidget.this.newConfigValues.put(configOption, val);
                    this.hasChanges = (val != configOption.getValue());
                    PFMOptionListWidget.this.hasChanges.set(index, this.hasChanges);
                } catch (NumberFormatException e) {
                    // ignore invalid inputs in value map, but keep hasChanges
                }
            });
            this.valueEditBox.setEditable(this.configOption.getSide() != Side.SERVER || !PFMConfigScreen.isOnServer);

            this.resetButton = new Button(0, 0, 50, 20, Component.translatable("controls.reset"), button -> {
                PFMOptionListWidget.this.newConfigValues.put(configOption, configOption.getDefaultValue());
                this.valueEditBox.setValue(String.valueOf(configOption.getDefaultValue()));
                hasChanges = true;
                PFMOptionListWidget.this.hasChanges.set(index, true);
            }){

                @Override
                protected MutableComponent createNarrationMessage() {
                    return Component.translatable("narrator.controls.reset", optionName);
                }
            };
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.minecraft.font.draw(matrices, this.optionName, (float)(x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (float)(y + entryHeight / 2 - PFMOptionListWidget.this.minecraft.font.lineHeight / 2), 0xFFFFFF);
            this.resetButton.x = x + 190;
            this.resetButton.y = y;
            this.resetButton.active = this.configOption.getSide() == Side.SERVER ? !PFMConfigScreen.isOnServer && !this.configOption.getDefaultValue().equals(PFMOptionListWidget.this.newConfigValues.get(configOption)) : !this.configOption.getDefaultValue().equals(PFMOptionListWidget.this.newConfigValues.get(configOption));
            this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
            this.valueEditBox.x = x + 107;
            this.valueEditBox.y = y;
            this.valueEditBox.render(matrices, mouseX, mouseY, tickDelta);
            if (mouseX >= this.valueEditBox.x && mouseX < this.valueEditBox.x + this.valueEditBox.getWidth() && mouseY >= this.valueEditBox.y && mouseY < this.valueEditBox.y + this.valueEditBox.getHeight()) {
                this.supplier.onTooltip(null, matrices, mouseX, mouseY);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.valueEditBox, this.resetButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.valueEditBox, this.resetButton);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean editable = this.configOption.getSide() != Side.SERVER || !PFMConfigScreen.isOnServer;
            if (editable && this.valueEditBox.mouseClicked(mouseX, mouseY, button)) {
                this.setFocused(this.valueEditBox);
                return true;
            }
            this.setFocused(null);
            return this.resetButton.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.valueEditBox.mouseReleased(mouseX, mouseY, button) || this.resetButton.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            return this.valueEditBox.charTyped(chr, modifiers);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return this.valueEditBox.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void tick() {
        for (Entry entry : this.children()) {
            if (entry instanceof DoubleEntry doubleEntry) {
                doubleEntry.valueEditBox.tick();
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public class ButtonEntry
            extends Entry {
        private final Component optionName;
        private final Button button;

        private final Button.OnTooltip supplier;
        private final Side side;
        ButtonEntry(Side side, final Component optionName, Component buttonText, Component tooltip, Button.OnPress pressAction) {
            this.optionName = optionName;
            this.side = side;
            this.supplier = new Button.OnTooltip() {
                final MutableComponent sideText = side == Side.CLIENT ? Component.translatable("pfm.option.client").setStyle(Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)) : Component.translatable("pfm.option.server").setStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)));
                final MutableComponent styledTooltip = ((MutableComponent)tooltip).setStyle(Style.EMPTY.withItalic(true));
                final MutableComponent combinedText = Component.literal("").append(sideText).append(Component.literal("\n")).append(styledTooltip);
                @Override
                public void onTooltip(Button button, PoseStack matrices, int mouseX, int mouseY) {
                    PFMOptionListWidget.this.parent.renderTooltip(matrices, PFMOptionListWidget.this.minecraft.font.split(combinedText, Math.max(PFMOptionListWidget.this.width / 2 - 43, 170)), mouseX, mouseY);
                }
                @Override
                public void narrateTooltip(Consumer<Component> consumer) {
                    consumer.accept(combinedText);
                    Button.OnTooltip.super.narrateTooltip(consumer);
                }
            };

            this.button = new Button(0, 0, 135, 20, buttonText, pressAction, supplier){
                @Override
                protected MutableComponent createNarrationMessage() {
                    return (MutableComponent) optionName;
                }
            };
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            PFMOptionListWidget.this.minecraft.font.draw(matrices, this.optionName, (float)(x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (float)(y + entryHeight / 2 - PFMOptionListWidget.this.minecraft.font.lineHeight / 2), 0xFFFFFF);
            this.button.x = x + 105;
            this.button.y = y;
            this.button.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.button);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.button);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.button.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.button.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static abstract class Entry extends ContainerObjectSelectionList.Entry<PFMOptionListWidget.Entry> {
    }
}

