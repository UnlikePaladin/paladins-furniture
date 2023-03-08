package com.unlikepaladin.pfm.config.option;

import net.minecraft.text.Text;

public class BooleanConfigOption extends AbstractConfigOption<Boolean>{
    private final Text title;
    private final Text tooltip;
    private final String category;
    private boolean value;
    private final boolean defaultValue;
    public BooleanConfigOption(Text title, Text tooltip, String category, boolean value) {
        this.title = title;
        this.category = category;
        this.tooltip = tooltip;
        this.value = value;
        this.defaultValue = value;
    }

    @Override
    public Text getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Text getToolTip() {
        return tooltip;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public boolean isDefault() {
        return value == defaultValue;
    }
}
