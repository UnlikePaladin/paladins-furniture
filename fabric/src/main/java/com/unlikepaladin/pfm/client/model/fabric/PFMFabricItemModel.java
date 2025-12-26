package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class PFMFabricItemModel<T> extends PFMItemModel<T>  {
    public PFMFabricItemModel(Supplier<BlockStateModel> model, SpecialModelRenderer<T> specialModelType, List<TintSource> tints) {
        super(model, specialModelType, tints);
    }


    @Override
    public BlockStateModel unwrapBlockStateModel(BlockStateModel model) {
        BlockStateModel model1 = model;

        int ctr = 0;
        while (model1 instanceof WrapperBlockStateModel) {
            model1 = ((PFMWrapperBlockstateModelAccessor) model1).pfm$getWrapped();
            ctr++;
            if (ctr > 15)
                break;
        }

        return model1;
    }

    @Override
    protected void setProperties(ItemStack stack) {
        BlockStateModel model1 = unwrapBlockStateModel(model.get());

        if (model1 != null && stack.getItem() instanceof BlockItem && model1 instanceof PFMBakedModelSetPropertiesExtension) {
            ((PFMBakedModelSetPropertiesExtension) model1).setBlockStateProperty(((BlockItem) stack.getItem()).getBlock().getDefaultState());
            if (stack.contains(PFMComponents.VARIANT_COMPONENT))
                ((PFMBakedModelSetPropertiesExtension) model1).setVariant(VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)));
        }
    }



}
