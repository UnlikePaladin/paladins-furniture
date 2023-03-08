package com.unlikepaladin.pfm.config.option;

import net.minecraft.text.Text;

public abstract class AbstractConfigOption<T> {

    public abstract Text getTitle();

    public abstract String getCategory();

    public abstract T getValue();

    public abstract T getDefaultValue();

    public abstract Text getToolTip();

    public abstract void setValue(T value);

    public abstract Class<T> getType();

    public abstract boolean isDefault();

    @Override
    public String toString() {
        return "{Type: " + getType() + ", Title: " + getTitle().getString() + ", Category: " + getCategory() +  ", Value: " + getValue() + "}";
    }
}
