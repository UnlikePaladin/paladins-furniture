package com.unlikepaladin.pfm.client.model.fabric;

import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.client.model.PFMItemModel;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.fabricmc.fabric.api.client.model.loading.v1.UnwrappableBakedModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PFMFabricItemModel<T> extends PFMItemModel<T> {
    public PFMFabricItemModel(BakedModel model, SpecialModelRenderer<T> specialModelType, List<ItemTintSource> tints) {
        super(model, specialModelType, tints);
    }

    @Override
    protected void setProperties(ItemStack stack) {
    BakedModel model1 = UnwrappableBakedModel.unwrap(this.model, m -> m instanceof PFMBakedModelSetPropertiesExtension);
        if (model1 != null && stack.getItem() instanceof BlockItem && model1 instanceof PFMBakedModelSetPropertiesExtension) {
            ((PFMBakedModelSetPropertiesExtension) model1).setBlockStateProperty(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            if (stack.has(PFMComponents.VARIANT_COMPONENT))
                ((PFMBakedModelSetPropertiesExtension) model1).setVariant(VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)));
        }
    }
}
