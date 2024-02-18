package com.unlikepaladin.pfm.config.option;


import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class StringConfigOption implements AbstractConfigOption<String> {
    public static final ConfigOptionType<StringConfigOption> TYPE = new ConfigOptionType<>() {
        public StringConfigOption read(DataInput dataInput, int i, ConfigSizeTracker sizeTracker) throws IOException {
            sizeTracker.add(2400L);
            String title = dataInput.readUTF();
            String tooltip = dataInput.readUTF();
            String category = dataInput.readUTF();
            String value = dataInput.readUTF();
            Side side = AbstractConfigOption.getSide(dataInput.readUTF());
            sizeTracker.add(224L + 16L * title.length());
            sizeTracker.add(224L + 16L * tooltip.length());
            sizeTracker.add(224L + 16L * category.length());
            sizeTracker.add(224L + 16L * value.length());
            sizeTracker.add(64L);
            StringConfigOption booleanConfigOption = new StringConfigOption(new TranslatableText(title), new TranslatableText(tooltip), category, value, side);
            return booleanConfigOption;
        }

        public String getCrashReportName() {
            return "STRING-OPTION";
        }

        public boolean isImmutable() {
            return true;
        }
    };
    private final Text title;
    private final Text tooltip;
    private final String category;
    private String value;
    private final String defaultValue;

    private final Side side;
    public StringConfigOption(Text title, Text tooltip, String category, String value, Side side) {
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
    public String getValue() {
        return value;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Text getToolTip() {
        return tooltip;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public boolean isDefault() {
        return Objects.equals(value, defaultValue);
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public byte getConfigType() {
        return STRING_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(((TranslatableText)title).getKey());
        output.writeUTF(((TranslatableText)tooltip).getKey());
        output.writeUTF(category);
        output.writeUTF(value);
        output.writeUTF(side.asString());
    }
}
