package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.client.model.PFMItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class PFMFabricItemModel<T> extends PFMItemModel<T>  {
    public PFMFabricItemModel(Supplier<BlockStateModel> model, SpecialModelRenderer<T> specialModelType, List<TintSource> tints) {
        super(model, specialModelType, tints);
    }

    @Override
    protected void setProperties(ItemStack stack) {
        super.setProperties(stack);
        //TODO FIX ME
        /*
        BakedModel model1 = UnwrappableBakedModel.unwrap(this.model, m -> m instanceof PFMBakedModelSetPropertiesExtension);
            if (model1 != null && stack.getItem() instanceof BlockItem && model1 instanceof PFMBakedModelSetPropertiesExtension) {
                ((PFMBakedModelSetPropertiesExtension) model1).setBlockStateProperty(((BlockItem) stack.getItem()).getBlock().getDefaultState());
                if (stack.contains(PFMComponents.VARIANT_COMPONENT))
                    ((PFMBakedModelSetPropertiesExtension) model1).setVariant(VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)));
            }
        */
    }



}
