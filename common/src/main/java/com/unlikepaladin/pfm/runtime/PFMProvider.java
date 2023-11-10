package com.unlikepaladin.pfm.runtime;

import net.minecraft.data.DataCache;

public abstract class PFMProvider {
    private final PFMGenerator parent;

    public PFMProvider(PFMGenerator parent) {
        this.parent = parent;
    }

    public PFMGenerator getParent() {
        return parent;
    }
}
