package com.unlikepaladin.pfm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.unlikepaladin.pfm.client.ColorRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockModelWrapper.class)
public class BasicItemModelMixin {

    @Unique
    private ItemStack pfm$parentStack = ItemStack.EMPTY;

    @Unique
    private List<ItemTintSource> pfm$parentTints;

    @Inject(method = "update", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;prepareTintLayers(I)[I"))
    private void inject(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel world, ItemOwner heldItemContext, int seed, CallbackInfo ci) {
        if (ColorRegistry.itemColorProviders.containsKey(stack.getItem()) && pfm$parentTints == null) {
            Item item = ColorRegistry.itemColorProviders.get(stack.getItem()).asItem();

            ResourceLocation parentModelId = item.getDefaultInstance().get(DataComponents.ITEM_MODEL);

            ItemModel parentModel = Minecraft.getInstance().getModelManager().getItemModel(parentModelId);
            pfm$parentStack = item.getDefaultInstance();
            this.pfm$parentTints = exploreForTints(parentModel, world, heldItemContext.asLivingEntity(), seed, displayContext);
        }
        if (stack.get(PFMComponents.VARIANT_COMPONENT) != null) {
            Item item = WoodVariantRegistry.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)).getLogBlock().asItem();

            ResourceLocation parentModelId = item.getDefaultInstance().get(DataComponents.ITEM_MODEL);

            ItemModel parentModel = Minecraft.getInstance().getModelManager().getItemModel(parentModelId);
            pfm$parentStack = item.getDefaultInstance();
            this.pfm$parentTints = exploreForTints(parentModel, world, heldItemContext.asLivingEntity(), seed, displayContext);
        }
    }

    @Unique
    private List<ItemTintSource> exploreForTints(ItemModel model, ClientLevel world, LivingEntity user, int seed, ItemDisplayContext transformationMode) {
        switch (model) {
            case BasicItemModelAccessor accessor -> {
                return accessor.getTints();
            }
            case CompositeItemModelAccessor accessor -> {
                for (ItemModel itemModel : accessor.getItemModels()) {
                    List<ItemTintSource> src = exploreForTints(itemModel, world, user, seed, transformationMode);
                    if (!src.isEmpty())
                        return src;
                }
            }
            case ConditionItemModelAccessor accessor -> {
                boolean property = accessor.getProperty().get(pfm$parentStack, world, user, seed, transformationMode);
                if (property)
                    return exploreForTints(accessor.getOnTrue(), world, user, seed, transformationMode);
                else
                    return exploreForTints(accessor.getOnFalse(), world, user, seed, transformationMode);
            }
            case SelectItemModelAccessor accessor -> {
                Object object = accessor.getProperty().get(pfm$parentStack, world, user, seed, transformationMode);
                ItemModel itemModel = accessor.getModels().get(object, world);
                return exploreForTints(itemModel, world, user, seed, transformationMode);
            }
            case RangeDispatchItemModelAccessor accessor -> {
                ItemModel itemModel;
                float select = accessor.getProperty().get(pfm$parentStack, world, user, seed) * accessor.getScale();
                if (Float.isNaN(select)) {
                    itemModel = accessor.getFallback();
                } else {
                    int i = RangeDispatchItemModelAccessor.getIndex(accessor.getThresholds(), select);
                    itemModel = i == -1 ? accessor.getFallback() : accessor.getModels()[i];
                }
                return exploreForTints(itemModel, world, user, seed, transformationMode);
            }
            case null, default -> {
                return List.of();
            }
        }
        return List.of();
    }

    //optional because optifine no likey this
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), require = 0)
    private <E> E swapTintIndx(List<E> instance, int i, Operation<ItemTintSource> original, @Share("currentColorIndex")LocalIntRef currentColorIndex) {
        currentColorIndex.set(i);
        if (pfm$parentTints != null) {
            return (E) pfm$parentTints.get(i);
        }
        return (E) original.call(instance, i);
    }

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/item/ItemTintSource;calculate(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;)I"), require = 0)
    private int swapTintColor(ItemTintSource instance, ItemStack itemStack, ClientLevel clientWorld, LivingEntity livingEntity, Operation<Integer> original, @Share("currentColorIndex")LocalIntRef currentColorIndex) {
        if (currentColorIndex != null && currentColorIndex.get() == 1 && itemStack.has(PFMComponents.COLOR_COMPONENT)) {
            return itemStack.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE).getMapColor().col;
        }
        if (currentColorIndex != null && pfm$parentTints != null) {
            return pfm$parentTints.get(currentColorIndex.get()).calculate(pfm$parentStack, clientWorld, livingEntity);
        }
        return original.call(instance, itemStack, clientWorld, livingEntity);
    }
}
