package com.unlikepaladin.pfm.config.option;

public class ConfigSizeTracker {
    public static final ConfigSizeTracker EMPTY = new ConfigSizeTracker(0L) {
        @Override
        public void add(long bits) {
        }
    };
    private final long maxBytes;
    private long allocatedBytes;

    public ConfigSizeTracker(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    public void add(long bits) {
        this.allocatedBytes += bits / 8L;
        if (this.allocatedBytes > this.maxBytes) {
            throw new RuntimeException("Tried to read Config Element that was too big; tried to allocate: " + this.allocatedBytes + "bytes where max allowed: " + this.maxBytes);
        }
    }
}
