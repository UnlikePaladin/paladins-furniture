package com.unlikepaladin.pfm.config.option;


import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class StringArrayConfigOption extends AbstractList<String> implements AbstractConfigOption<String[]> {
    public static final ConfigOptionType<StringArrayConfigOption> TYPE = new ConfigOptionType<>() {
        public StringArrayConfigOption read(DataInput dataInput, int i, ConfigSizeTracker sizeTracker) throws IOException {
            sizeTracker.add(2400L);
            String title = dataInput.readUTF();
            String tooltip = dataInput.readUTF();
            String category = dataInput.readUTF();
            int size = dataInput.readInt();
            sizeTracker.add(32L * (long)size);
            List<String> strings = new ArrayList<>(size);
            for (int index = 0; index < size; ++index) {
                strings.set(index, dataInput.readUTF());
            }
            Side side = AbstractConfigOption.getSide(dataInput.readUTF());
            sizeTracker.add(224L + 16L * title.length());
            sizeTracker.add(224L + 16L * tooltip.length());
            sizeTracker.add(224L + 16L * category.length());
            sizeTracker.add(64L);
            return new StringArrayConfigOption(new TranslatableText(title), new TranslatableText(tooltip), category, strings, side);
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
    private List<String> value;
    private final String[] defaultValue;

    private final Side side;
    private int size;
    public StringArrayConfigOption(Text title, Text tooltip, String category, List<String> value, Side side) {
        this.title = title;
        this.category = category;
        this.tooltip = tooltip;
        this.value = value;
        this.defaultValue = new String[1];
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
    public String[] getValue() {
        return value.toArray(new String[0]);
    }

    @Override
    public String[] getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Text getToolTip() {
        return tooltip;
    }

    @Override
    public void setValue(String[] value) {
        this.value = List.of(value);
    }

    @Override
    public Class<String[]> getType() {
        return String[].class;
    }

    @Override
    public boolean isDefault() {
        return value.equals(List.of(defaultValue));
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public byte getConfigType() {
        return STRING_ARRAY_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(((TranslatableText)title).getKey());
        output.writeUTF(((TranslatableText)tooltip).getKey());
        output.writeUTF(category);
        output.writeInt(this.value.size());
        for (String i : this.value) {
            output.writeUTF(i);
        }
        output.writeUTF(side.asString());
    }

    @Override
    public String get(int index) {
        if (index < value.size() && index >= 0)
            return value.get(index);
        return "";
    }

    @Override
    public String set(int index, String element) {
        if (index < value.size() && index >= 0) {
            return value.set(index, element);
        }
        return "";
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(String element) {
        return value.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return value.remove(o);
    }

    @Override
    public String remove(int index) {
        return value.remove(index);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return value.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super String> filter) {
        return value.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<String> operator) {
        value.replaceAll(operator);
    }

    @Override
    public Stream<String> stream() {
        return value.stream();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return value.toArray(a);
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        value.forEach(action);
    }

    @Override
    public void sort(Comparator<? super String> c) {
        value.sort(c);
    }

    @Override
    public Stream<String> parallelStream() {
        return value.parallelStream();
    }

    @Override
    public Iterator<String> iterator() {
        return value.iterator();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int lastIndexOf(Object o) {
        return value.lastIndexOf(o);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends String> c) {
        return value.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        return value.addAll(index, c);
    }

    @Override
    public int indexOf(Object o) {
        return value.indexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        return value.contains(o);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return value.toArray(generator);
    }
}
