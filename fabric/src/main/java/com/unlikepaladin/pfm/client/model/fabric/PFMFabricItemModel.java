package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.fabricmc.fabric.api.client.model.loading.v1.UnwrappableBakedModel;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PFMFabricItemModel<T> extends PFMItemModel<T> {
    public PFMFabricItemModel(BakedModel model, SpecialModelRenderer<T> specialModelType, List<TintSource> tints) {
        super(model, specialModelType, tints);
    }

    @Override
    protected void setProperties(ItemStack stack) {
    BakedModel model1 = UnwrappableBakedModel.unwrap(this.model, m -> m instanceof PFMBakedModelSetPropertiesExtension);
        if (model1 != null && stack.getItem() instanceof BlockItem) {
            ((PFMBakedModelSetPropertiesExtension) model).setBlockStateProperty(((BlockItem) stack.getItem()).getBlock().getDefaultState());
            if (stack.contains(PFMComponents.VARIANT_COMPONENT))
                ((PFMBakedModelSetPropertiesExtension) model).setVariant(VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)));
        }
    }
}
