package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectItemModel.class)
public interface SelectItemModelAccessor<T>  {
    @Accessor
    SelectItemModelProperty<T> getProperty();

    @Accessor
    SelectItemModel.ModelSelector<T> getModels();
}
