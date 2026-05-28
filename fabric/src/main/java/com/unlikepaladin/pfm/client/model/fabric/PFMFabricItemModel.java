package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.mixin.fabric.PFMWrapperBlockstateModelAccessor;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class PFMFabricItemModel<T> extends PFMItemModel<T>  {
    public PFMFabricItemModel(Supplier<BlockStateModel> model, SpecialModelRenderer<T> specialModelType, List<ItemTintSource> tints) {
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
    protected void setProperties(ItemStack stack, ItemStackRenderState state) {
        BlockStateModel model1 = unwrapBlockStateModel(model.get());
        if (model1 != null && stack.getItem() instanceof BlockItem && model1 instanceof PFMBakedModelSetPropertiesExtension) {
            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            ((PFMBakedModelSetPropertiesExtension) model1).setBlockStateProperty(blockState);
            state.appendModelIdentityElement(blockState);
            if (stack.has(PFMComponents.VARIANT_COMPONENT)) {
                VariantBase<?> variantBase = VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT));
                ((PFMBakedModelSetPropertiesExtension) model1).setVariant(variantBase);
                state.appendModelIdentityElement(variantBase);
            }
        }
    }



}
