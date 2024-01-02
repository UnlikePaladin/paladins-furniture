package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.MicrowaveBlockEntityImpl;
import net.minecraft.screen.ArrayPropertyDelegate;

public class MicrowavePropertyDelegate extends ArrayPropertyDelegate {
    private final MicrowaveBlockEntityImpl microwaveBlockEntity;
    public MicrowavePropertyDelegate(MicrowaveBlockEntityImpl microwaveBlockEntity, int dataCount) {
        super(dataCount);
        this.microwaveBlockEntity = microwaveBlockEntity;
    }

    @Override
    public int get(int index) {
        return this.microwaveBlockEntity.getPropertyDelegate().get(index);
    }

    @Override
    public void set(int index, int value) {
        this.microwaveBlockEntity.getPropertyDelegate().set(index, value);
    }
}
