package com.unlikepaladin.pfm.config.option;
import java.io.DataInput;
import java.io.IOException;

public interface ConfigOptionType<T extends AbstractConfigOption> {
    abstract T read(DataInput dataInput, int i, ConfigSizeTracker nbtTagSizeTracker) throws IOException;

    /**
     * Determines the immutability of this type.
     * <p>
     * The mutability of an NBT type means the held value can be modified
     * after the NBT element is instantiated.
     *
     * @return {@code true} if this NBT type is immutable, else {@code false}
     */
    default public boolean isImmutable() {
        return false;
    }

    public String getCrashReportName();

    public static ConfigOptionType<NullConfigOption> createInvalid(final int type) {
        return new ConfigOptionType<NullConfigOption>() {

            @Override
            public String getCrashReportName() {
                return "INVALID[" + type + "]";
            }

            @Override
            public /* synthetic */ NullConfigOption read(DataInput input, int depth, ConfigSizeTracker tracker) throws IOException {
                throw new IllegalArgumentException("Invalid Config id: " + type);
            }
        };
    }
}
