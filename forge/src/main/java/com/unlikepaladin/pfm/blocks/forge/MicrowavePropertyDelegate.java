package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;

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
