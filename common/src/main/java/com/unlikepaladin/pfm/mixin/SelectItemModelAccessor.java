package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.render.item.model.SelectItemModel;
import net.minecraft.client.render.item.property.select.SelectProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectItemModel.class)
public interface SelectItemModelAccessor<T>  {
    @Accessor
    SelectProperty<T> getProperty();

    @Accessor
    SelectItemModel.ModelSelector<T> getSelector();
}
