package com.unlikepaladin.pfm.client.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.client.ColorRegistry;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.mixin.*;
import com.unlikepaladin.pfm.registry.TriFunc;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PFMItemModel<T> implements ItemModel {
    private final SpecialModelRenderer<T> specialModelType;
    protected final BakedModel model;
    private final List<ItemTintSource> tints;
    private final List<ItemTintSource> pfm$parentTints = new ArrayList<>();

    public PFMItemModel(BakedModel model, SpecialModelRenderer<T> specialModelType, List<ItemTintSource> tints) {
        this.model = model;
        this.tints = tints;
        this.specialModelType = specialModelType;
    }

    @Override
    public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext transformationMode, @Nullable ClientLevel world, @Nullable LivingEntity user, int seed) {
        if (specialModelType != null) {
            ItemStackRenderState.LayerRenderState specialLayerRenderState = state.newLayer();
            specialLayerRenderState.setupSpecialModel(this.specialModelType, this.specialModelType.extractArgument(stack), this.model);
        }

        ItemStackRenderState.LayerRenderState layerRenderState = state.newLayer();
        if (stack.hasFoil()) {
            layerRenderState.setFoilType(ItemStackRenderState.FoilType.STANDARD);
        }

        RenderType renderLayer = ItemBlockRenderTypes.getRenderType(stack);
        layerRenderState.setupBlockModel(this.model, renderLayer);

        if (ColorRegistry.itemColorProviders.containsKey(stack.getItem()) && pfm$parentTints.isEmpty()) {
            Item item = ColorRegistry.itemColorProviders.get(stack.getItem()).asItem();

            ResourceLocation parentModelId = item.getDefaultInstance().get(DataComponents.ITEM_MODEL);

            ItemModel parentModel = Minecraft.getInstance().getModelManager().getItemModel(parentModelId);
            pfm$parentTints.addAll(exploreForTints(stack, parentModel, world, user, seed, transformationMode));
        }

        List<ItemTintSource> tintsToUse = pfm$parentTints.isEmpty() ? this.tints : pfm$parentTints;

        int tintCount = tintsToUse.size();
        if (tintCount == 0 && stack.has(PFMComponents.COLOR_COMPONENT)) {
            tintCount = 2;
            tintsToUse = new ArrayList<>(tintCount);

            tintsToUse.add(new Constant(0xffffff));
            tintsToUse.add(new Constant(0xffffff));
        }

        int[] tintArray = layerRenderState.prepareTintLayers(tintCount);

        for (int index = 0; index < tintCount; index++) {
            if (index == 1 && stack.has(PFMComponents.COLOR_COMPONENT)) {
                tintArray[index] =  stack.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE).getMapColor().col;
            } else {
                tintArray[index] = tintsToUse.get(index).calculate(stack, world, user);
            }
        }

        setProperties(stack);
    }

    protected void setProperties(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem && model instanceof PFMBakedModelSetPropertiesExtension) {
            ((PFMBakedModelSetPropertiesExtension) model).setBlockStateProperty(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            if (stack.has(PFMComponents.VARIANT_COMPONENT))
                ((PFMBakedModelSetPropertiesExtension) model).setVariant(VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT)));
        }
    }

    private List<ItemTintSource> exploreForTints(ItemStack itemStack, ItemModel model, ClientLevel world, LivingEntity user, int seed, ItemDisplayContext transformationMode) {
        switch (model) {
            case BasicItemModelAccessor accessor -> {
                return accessor.getTints();
            }
            case CompositeItemModelAccessor accessor -> {
                for (ItemModel itemModel : accessor.getItemModels()) {
                    List<ItemTintSource> src = exploreForTints(itemStack, itemModel, world, user, seed, transformationMode);
                    if (!src.isEmpty())
                        return src;
                }
            }
            case ConditionItemModelAccessor accessor -> {
                boolean property = accessor.getProperty().get(itemStack, world, user, seed, transformationMode);
                if (property)
                    return exploreForTints(itemStack, accessor.getOnTrue(), world, user, seed, transformationMode);
                else
                    return exploreForTints(itemStack, accessor.getOnFalse(), world, user, seed, transformationMode);
            }
            case SelectItemModelAccessor<?> accessor -> {
                ItemModel itemModel = accessor.getModels().get(accessor.getProperty().get(itemStack, world, user, seed, transformationMode));
                return exploreForTints(itemStack, itemModel, world, user, seed, transformationMode);
            }
            case RangeDispatchItemModelAccessor accessor -> {
                ItemModel itemModel;
                float select = accessor.getProperty().get(itemStack, world, user, seed) * accessor.getScale();
                if (Float.isNaN(select)) {
                    itemModel = accessor.getFallback();
                } else {
                    int i = RangeDispatchItemModelAccessor.getIndex(accessor.getThresholds(), select);
                    itemModel = i == -1 ? accessor.getFallback() : accessor.getModels()[i];
                }
                return exploreForTints(itemStack, itemModel, world, user, seed, transformationMode);
            }
            case null, default -> {
                return List.of();
            }
        }
        return List.of();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(ResourceLocation model, Optional<SpecialModelRenderer.Unbaked> specialModel, List<ItemTintSource> tints) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("model").forGetter(Unbaked::model),
                                SpecialModelRenderers.CODEC.optionalFieldOf("special_model").forGetter(Unbaked::specialModel),
                                ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.resolve(this.model);
        }

        @Override
        public ItemModel bake(BakingContext context) {
            BakedModel bakedModel = context.bake(this.model);
            if (specialModel.isPresent()) {
                SpecialModelRenderer<?> specialModelRenderer = this.specialModel.get().bake(context.entityModelSet());
                return getItemModelFunc().apply(bakedModel, specialModelRenderer, this.tints);
            }
            return getItemModelFunc().apply(bakedModel, null, this.tints);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }

    @ExpectPlatform
    public static TriFunc<BakedModel, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc(){
        throw new AssertionError();
    }
}
