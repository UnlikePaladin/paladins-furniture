package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RangeSelectItemModel.class)
public interface RangeDispatchItemModelAccessor {
    @Accessor
    RangeSelectItemModelProperty getProperty();

    @Accessor
    ItemModel[] getModels();

    @Accessor
    ItemModel getFallback();

    @Accessor
    float[] getThresholds();

    @Accessor
    float getScale();

    @Invoker("lastIndexLessOrEqual")
    static int getIndex(float[] thresholds, float value) {
        throw new AssertionError();
    }
}
