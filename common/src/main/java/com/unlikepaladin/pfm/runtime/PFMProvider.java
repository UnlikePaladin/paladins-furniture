package com.unlikepaladin.pfm.runtime;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataProvider;

public abstract class PFMProvider implements DataProvider {
    private final PFMGenerator parent;

    public PFMProvider(PFMGenerator parent) {
        this.parent = parent;
    }

    public PFMGenerator getParent() {
        return parent;
    }
}
