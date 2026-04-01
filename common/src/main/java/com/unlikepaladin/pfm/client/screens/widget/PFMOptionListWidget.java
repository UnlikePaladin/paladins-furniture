package com.unlikepaladin.pfm.client.screens.widget;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.BooleanConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.runtime.PFMDataGenerator;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.runtime.PFMAssetGenerator;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.*;
import java.util.function.Supplier;

public class PFMOptionListWidget extends ContainerObjectSelectionList<PFMOptionListWidget.Entry> {
    final PFMConfigScreen parent;
    int maxKeyNameLength;
    public BitSet hasChanges;
    public Map<AbstractConfigOption, Boolean> newConfigValues;
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
                PFMOptionListWidget.this.newConfigValues.put(configOptionEntry.getValue(), (Boolean) configOptionEntry.getValue().getValue());
                this.addEntry(new BooleanEntry((BooleanConfigOption)configOptionEntry.getValue(), text, index));
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
        for (Map.Entry<AbstractConfigOption, Boolean> entry : newConfigValues.entrySet()) {
            if (entry.getKey().getType() == Boolean.class)
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
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(PFMOptionListWidget.this.client.textRenderer, this.text.setStyle(Style.EMPTY.withBold(true)), (PFMOptionListWidget.this.client.currentScreen.width / 2 - this.textWidth / 2), y + entryHeight - (PFMOptionListWidget.this).client.textRenderer.fontHeight - 1, 0xFFFFFF, true);
        }

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

        private final Tooltip supplier;
        int index;
        boolean hasChanges = false;
        BooleanEntry(final BooleanConfigOption configOption, final Component optionName, int index) {
            this.configOption = configOption;
            this.optionName = optionName;
            this.index = index;
            final MutableComponent sideText = configOption.getSide() == Side.CLIENT ? Component.translatable("pfm.option.client").setStyle(Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)) : Component.translatable("pfm.option.server").setStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)));
            final MutableComponent styledTooltip = ((MutableComponent)configOption.getToolTip()).setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(ChatFormatting.WHITE));
            final Component tooltipText = sideText.append(Component.literal("\n")).append(styledTooltip);
            this.supplier = Tooltip.create(tooltipText);

            this.valueButton = Button.builder(optionName, button -> {
                PFMOptionListWidget.this.parent.focusedConfigOption = configOption;
                PFMOptionListWidget.this.newConfigValues.put(configOption, !PFMOptionListWidget.this.newConfigValues.get(configOption));
                hasChanges = !hasChanges;
                PFMOptionListWidget.this.hasChanges.set(index, hasChanges);
            }).tooltip(supplier).bounds(0,0,75,20).createNarration(Supplier::get).build();

            this.resetButton = Button.builder(Component.translatable("controls.reset"), button -> {
                PFMOptionListWidget.this.newConfigValues.put(configOption, configOption.getDefaultValue());
                hasChanges = true;
                PFMOptionListWidget.this.hasChanges.set(index, true);
            }).bounds(0,0,50,20).createNarration(textSupplier -> Component.translatable("narrator.controls.reset", optionName)).build();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(PFMOptionListWidget.this.minecraft.font, this.optionName, (x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (y + entryHeight / 2 - PFMOptionListWidget.this.client.textRenderer.fontHeight / 2), 0xFFFFFF, false);
            this.resetButton.setX(x + 190);
            this.resetButton.setY(y);
            this.resetButton.active = this.configOption.getSide() == Side.SERVER ? !PFMConfigScreen.isOnServer && !(this.configOption.getDefaultValue() == PFMOptionListWidget.this.newConfigValues.get(configOption)) : !(this.configOption.getDefaultValue() == PFMOptionListWidget.this.newConfigValues.get(configOption));;
            this.resetButton.render(context, mouseX, mouseY, tickDelta);
            this.valueButton.setX(x + 105);
            this.valueButton.setY(y);
            this.valueButton.setMessage(PFMOptionListWidget.this.newConfigValues.get(configOption) ? CommonComponents.GUI_YES : CommonComponents.GUI_NO);
            this.valueButton.active = this.configOption.getSide() != Side.SERVER || !PFMConfigScreen.isOnServer;
            this.valueButton.render(context, mouseX, mouseY, tickDelta);
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
    public class ButtonEntry
            extends Entry {
        private final Component optionName;
        private final Button button;

        private final Tooltip supplier;
        private final Side side;
        ButtonEntry(Side side, final Component optionName, Component buttonText, Component tooltip, Button.OnPress pressAction) {
            this.optionName = optionName;
            this.side = side;
            final MutableComponent sideText = side == Side.CLIENT ? Component.translatable("pfm.option.client").setStyle(Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)) : Component.translatable("pfm.option.server").setStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(0xf77f34)));
            final MutableComponent styledTooltip = ((MutableComponent)tooltip).setStyle(Style.EMPTY.withItalic(true).withBold(false).withColor(ChatFormatting.WHITE));
            final Component tooltipText = sideText.append(Component.literal("\n")).append(styledTooltip);
            this.supplier = Tooltip.create(tooltipText);

            this.button = new Button(0, 0, 135, 20, buttonText, pressAction, Supplier::get){
                @Override
                protected MutableComponent createNarrationMessage() {
                    return (MutableComponent) optionName;
                }
            };
            button.setTooltip(supplier);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(PFMOptionListWidget.this.minecraft.font, this.optionName, (x + 90 - PFMOptionListWidget.this.maxKeyNameLength), (y + entryHeight / 2 - PFMOptionListWidget.this.client.textRenderer.fontHeight / 2), 0xFFFFFF, false);
            this.button.setX(x+105);
            this.button.setY(y);
            this.button.render(context, mouseX, mouseY, tickDelta);
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

