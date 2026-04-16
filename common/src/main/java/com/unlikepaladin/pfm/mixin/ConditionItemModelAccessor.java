package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConditionalItemModel.class)
public interface ConditionItemModelAccessor {
    @Accessor
    ItemModel getOnTrue();

    @Accessor
    ItemModel getOnFalse();

    @Accessor
    ConditionalItemModelProperty getProperty();
}
