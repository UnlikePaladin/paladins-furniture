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
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
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
        Object current = model;
        int depth = 0;
        while (current != null && depth < 20) {
            depth++;
            if (current instanceof WrapperBlockStateModel wrapper) {
                current = ((PFMWrapperBlockstateModelAccessor) wrapper).pfm$getWrapped();
            } else if (current instanceof BlockStateModel bsm) {
                BlockStateModel unwrapped = super.unwrapBlockStateModel(bsm);
                if (unwrapped == bsm) {
                    break;
                }
                current = unwrapped;
            } else {
                break;
            }
        }
        return current instanceof BlockStateModel bsm ? bsm : model;
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
