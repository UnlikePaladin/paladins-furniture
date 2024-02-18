package com.unlikepaladin.pfm.config.option;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;

public class ListConfigOption extends AbstractList<AbstractConfigOption<?>> implements AbstractConfigOption {
    public static final ConfigOptionType<ListConfigOption> TYPE = new ConfigOptionType<ListConfigOption>(){

        @Override
        public ListConfigOption read(DataInput dataInput, int i, ConfigSizeTracker sizeTracker) throws IOException {
            String title = dataInput.readUTF();
            String tooltip = dataInput.readUTF();
            String category = dataInput.readUTF();
            sizeTracker.add(224L + 16L * title.length());
            sizeTracker.add(224L + 16L * tooltip.length());
            sizeTracker.add(224L + 16L * category.length());
            sizeTracker.add(64L);
            sizeTracker.add(296L);
            if (i > 512) {
                throw new RuntimeException("Tried to read option with too high complexity, depth > 512");
            }
            byte type = dataInput.readByte();
            int size = dataInput.readInt();
            if (type == 0 && size > 0) {
                throw new RuntimeException("Missing type on Option List");
            }
            sizeTracker.add(32L * (long)size);
            ConfigOptionType<?> optionType = ConfigOptionTypes.byId(type);
            ArrayList<AbstractConfigOption<?>> list = Lists.newArrayListWithCapacity(size);
            for (int index = 0; index < size; ++index) {
                list.add(optionType.read(dataInput, i + 1, sizeTracker));
            }
            Side side = AbstractConfigOption.getSide(dataInput.readUTF());
            return new ListConfigOption(new TranslatableText(title), new TranslatableText(tooltip), category, list, type, side);
        }

        @Override
        public String getCrashReportName() {
            return "LIST-OPTION";
        }
    };

    private final Text title;
    private final Text tooltip;
    private final String category;
    private ArrayList<AbstractConfigOption<?>> value;
    private byte type;
    private final Side side;
    public ListConfigOption(Text title, Text tooltip, String category, ArrayList<AbstractConfigOption<?>> value, byte type, Side side) {
        this.title = title;
        this.category = category;
        this.tooltip = tooltip;
        this.value = value;
        this.type = type;
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
    public Object getValue() {
        return value;
    }

    @Override
    public Object getDefaultValue() {
        return value;
    }

    @Override
    public Text getToolTip() {
        return tooltip;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof ArrayList<?> && !((ArrayList<?>) value).isEmpty() && ((ArrayList<?>) value).get(0) instanceof AbstractConfigOption)
            this.value = (ArrayList<AbstractConfigOption<?>>) value;
    }

    @Override
    public Class<ArrayList> getType() {
        return ArrayList.class;
    }

    @Override
    public boolean isDefault() {
        return value.stream().allMatch(AbstractConfigOption::isDefault);
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public byte getConfigType() {
        return LIST_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(((TranslatableText)title).getKey());
        output.writeUTF(((TranslatableText)tooltip).getKey());
        output.writeUTF(category);
        this.type = this.value.isEmpty() ? (byte)0 : this.value.get(0).getConfigType();
        output.writeByte(this.type);
        output.writeInt(this.value.size());
        for (AbstractConfigOption<?> configOption : this.value) {
            configOption.write(output);
        }
        output.writeUTF(side.asString());
    }

    @Override
    public AbstractConfigOption<?> get(int index) {
        return value.get(index);
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public AbstractConfigOption<?> set(int index, AbstractConfigOption<?> element) {
        if (index < value.size() && index >= 0 && value.get(index).getConfigType() == element.getConfigType()) {
            return value.set(index, element);
        }
        return null;
    }

    public void setElementValue(int index, Object v) {
        if (index < value.size() && index >= 0 && value.get(index).getClass() == v.getClass()) {
            ((AbstractConfigOption<Object>)value.get(index)).setValue(v);
        }
    }

    public Object getElementValue(int index) {
        if (index < value.size() && index >= 0) {
            return ((AbstractConfigOption<Object>)value.get(index)).getValue();
        }
        return NullConfigOption.INSTANCE.getValue();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return value.toArray(a);
    }

    @Override
    public AbstractConfigOption<?> remove(int index) {
        return value.remove(index);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof String)
            return compareTo((String) o);
        return 0;
    }
}
