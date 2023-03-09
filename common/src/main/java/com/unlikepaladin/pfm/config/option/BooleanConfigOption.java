package com.unlikepaladin.pfm.config.option;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtNull;
import net.minecraft.nbt.NbtTagSizeTracker;
import net.minecraft.nbt.NbtType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BooleanConfigOption extends AbstractConfigOption<Boolean>{
    public static final ConfigOptionType<BooleanConfigOption> TYPE = new ConfigOptionType<>() {
        public BooleanConfigOption read(DataInput dataInput, int i, ConfigSizeTracker nbtTagSizeTracker) throws IOException {
            nbtTagSizeTracker.add(2400L);
            String title = dataInput.readUTF();
            String tooltip = dataInput.readUTF();
            String category = dataInput.readUTF();
            boolean value = dataInput.readBoolean();
            Side side = getSide(dataInput.readUTF());
            nbtTagSizeTracker.add(224L + 16L * title.length());
            nbtTagSizeTracker.add(224L + 16L * tooltip.length());
            nbtTagSizeTracker.add(224L + 16L * category.length());
            nbtTagSizeTracker.add(64L);
            BooleanConfigOption booleanConfigOption = new BooleanConfigOption(new TranslatableText(title), new TranslatableText(tooltip), category, value, side);
            return booleanConfigOption;
        }

        public String getCrashReportName() {
            return "END";
        }

        public boolean isImmutable() {
            return true;
        }
    };
    private final Text title;
    private final Text tooltip;
    private final String category;
    private boolean value;
    private final boolean defaultValue;

    private final Side side;
    public BooleanConfigOption(Text title, Text tooltip, String category, boolean value, Side side) {
        this.title = title;
        this.category = category;
        this.tooltip = tooltip;
        this.value = value;
        this.defaultValue = value;
        this.side = side;
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

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public byte getConfigType() {
        return BOOL_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(((TranslatableText)title).getKey());
        output.writeUTF(((TranslatableText)tooltip).getKey());
        output.writeUTF(category);
        output.writeBoolean(value);
        output.writeUTF(side.asString());
    }
}
