package com.unlikepaladin.pfm.config.option;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.TagType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class BooleanConfigOption extends AbstractConfigOption<Boolean>{
    public static final ConfigOptionType<BooleanConfigOption> TYPE = new ConfigOptionType<BooleanConfigOption>() {
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
            BooleanConfigOption booleanConfigOption = new BooleanConfigOption(new TranslatableComponent(title), new TranslatableComponent(tooltip), category, value, side);
            return booleanConfigOption;
        }

        public String getCrashReportName() {
            return "END";
        }

        public boolean isImmutable() {
            return true;
        }
    };
    private final Component title;
    private final Component tooltip;
    private final String category;
    private boolean value;
    private final boolean defaultValue;

    private final Side side;
    public BooleanConfigOption(Component title, Component tooltip, String category, boolean value, Side side) {
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
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Component getToolTip() {
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
        output.writeUTF(((TranslatableComponent)title).getKey());
        output.writeUTF(((TranslatableComponent)tooltip).getKey());
        output.writeUTF(category);
        output.writeBoolean(value);
        output.writeUTF(side.getSerializedName());
    }
}
