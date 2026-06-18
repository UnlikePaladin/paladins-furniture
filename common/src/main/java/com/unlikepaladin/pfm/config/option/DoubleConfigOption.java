package com.unlikepaladin.pfm.config.option;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleConfigOption extends AbstractConfigOption<Double> {
    public static final ConfigOptionType<DoubleConfigOption> TYPE = new ConfigOptionType<>() {
        public DoubleConfigOption read(DataInput dataInput, int i, ConfigSizeTracker nbtTagSizeTracker) throws IOException {
            nbtTagSizeTracker.add(2400L);
            String title = dataInput.readUTF();
            String tooltip = dataInput.readUTF();
            String category = dataInput.readUTF();
            double value = dataInput.readDouble();
            Side side = getSide(dataInput.readUTF());
            nbtTagSizeTracker.add(224L + 16L * title.length());
            nbtTagSizeTracker.add(224L + 16L * tooltip.length());
            nbtTagSizeTracker.add(224L + 16L * category.length());
            nbtTagSizeTracker.add(64L);
            return new DoubleConfigOption(Component.translatable(title), Component.translatable(tooltip), category, value, side);
        }

        public String getCrashReportName() {
            return "DOUBLE";
        }

        public boolean isImmutable() {
            return true;
        }
    };

    private final Component title;
    private final Component tooltip;
    private final String category;
    private double value;
    private final double defaultValue;
    private final Side side;

    public DoubleConfigOption(Component title, Component tooltip, String category, double value, Side side) {
        this.title = title;
        this.category = category;
        this.tooltip = tooltip;
        this.value = value;
        this.defaultValue = value;
        this.side = side;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Double getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Component getToolTip() {
        return tooltip;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public boolean isDefault() {
        return value == defaultValue;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public byte getConfigType() {
        return DOUBLE_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(((TranslatableContents)title.getContents()).getKey());
        output.writeUTF(((TranslatableContents)tooltip.getContents()).getKey());
        output.writeUTF(category);
        output.writeDouble(value);
        output.writeUTF(side.getSerializedName());
    }
}
