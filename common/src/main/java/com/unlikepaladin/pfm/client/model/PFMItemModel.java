package com.unlikepaladin.pfm.client.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.client.ColorRegistry;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.mixin.*;
import com.unlikepaladin.pfm.registry.TriFunc;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.client.render.item.tint.ConstantTintSource;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.client.render.model.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.HeldItemContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class PFMItemModel<T> implements ItemModel {
    private final SpecialModelRenderer<T> specialModelType;
    protected final Supplier<BlockStateModel> model;
    private final List<TintSource> tints;
    private final List<TintSource> pfm$parentTints = new ArrayList<>();

    private static final Function<ItemStack, RenderLayer> ITEMS_ATLAS_RENDER_LAYER_GETTER = stack -> TexturedRenderLayers.getItemTranslucentCull();
    private static final Function<ItemStack, RenderLayer> BLOCKS_ATLAS_RENDER_LAYER_GETTER = stack -> {
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockRenderLayer blockRenderLayer = BlockRenderLayers.getBlockLayer(blockItem.getBlock().getDefaultState());
            if (blockRenderLayer != BlockRenderLayer.TRANSLUCENT) {
                return TexturedRenderLayers.getEntityCutout();
            }
        }

        return TexturedRenderLayers.getBlockTranslucentCull();
    };

    public PFMItemModel(Supplier<BlockStateModel> model, SpecialModelRenderer<T> specialModelType, List<TintSource> tints) {
        this.model = model;
        this.tints = tints;
        this.specialModelType = specialModelType;
    }

    public BlockStateModel unwrapBlockStateModel(BlockStateModel model) {
        return model;
    }

    @Override
    public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable HeldItemContext heldItemContext, int seed) {
        state.addModelKey(this);
        BlockStateModel unwrapped = unwrapBlockStateModel(model.get());
        if (specialModelType != null) {
            ItemRenderState.LayerRenderState specialLayerRenderState = state.newLayer();
            if (unwrapped instanceof AbstractBakedModel) {
                ((AbstractBakedModel)unwrapped).itemDisplaySettings.addSettings(specialLayerRenderState, displayContext);
            }
            specialLayerRenderState.setSpecialModel(this.specialModelType, this.specialModelType.getData(stack));
            state.addModelKey(specialLayerRenderState);
        }

        state.markAnimated();
        ItemRenderState.LayerRenderState layerRenderState = state.newLayer();
        if (stack.hasGlint()) {
            layerRenderState.setGlint(ItemRenderState.Glint.STANDARD);
        }

        RenderLayer renderLayer = BLOCKS_ATLAS_RENDER_LAYER_GETTER.apply(stack);
        layerRenderState.setRenderLayer(renderLayer);

        if (ColorRegistry.itemColorProviders.containsKey(stack.getItem()) && pfm$parentTints.isEmpty()) {
            Item item = ColorRegistry.itemColorProviders.get(stack.getItem()).asItem();

            Identifier parentModelId = item.getDefaultStack().get(DataComponentTypes.ITEM_MODEL);

            ItemModel parentModel = MinecraftClient.getInstance().getBakedModelManager().getItemModel(parentModelId);
            pfm$parentTints.addAll(exploreForTints(stack, parentModel, world, heldItemContext, seed, displayContext));
        }

        List<TintSource> tintsToUse = pfm$parentTints.isEmpty() ? this.tints : pfm$parentTints;

        int tintCount = tintsToUse.size();
        if (tintCount == 0 && stack.get(PFMComponents.COLOR_COMPONENT) != null) {
            tintCount = 2;
            tintsToUse = new ArrayList<>(tintCount);

            tintsToUse.add(new ConstantTintSource(0xffffff));
            tintsToUse.add(new ConstantTintSource(0xffffff));
        }

        int[] tintArray = layerRenderState.initTints(tintCount);

        for (int index = 0; index < tintCount; index++) {
            if (index == 1 && stack.get(PFMComponents.COLOR_COMPONENT) != null) {
                tintArray[index] = PFMFileUtil.adjustColor(stack.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE).getMapColor().color);
            } else {
                tintArray[index] = PFMFileUtil.adjustColor(tintsToUse.get(index).getTint(stack, world, heldItemContext != null ? heldItemContext.getEntity() : null));
            }
            state.addModelKey(tintArray[index]);
        }

        setProperties(stack, state);

        Random random = Random.create(seed);
        // finally emit item quads
        emitItemModelQuads(layerRenderState, model.get(), displayContext, random);
    }

    @ExpectPlatform
    public static void emitItemModelQuads(ItemRenderState.LayerRenderState renderState, BlockStateModel model, ItemDisplayContext displayContext, Random random) {
        throw new AssertionError();
    }

    protected void setProperties(ItemStack stack, ItemRenderState state) {
        if (stack.getItem() instanceof BlockItem && model.get() instanceof PFMBakedModelSetPropertiesExtension) {
            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            ((PFMBakedModelSetPropertiesExtension) model.get()).setBlockStateProperty(blockState);
            state.addModelKey(blockState);
            if (stack.get(PFMComponents.VARIANT_COMPONENT) != null) {
                VariantBase<?> variantBase = VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT));
                ((PFMBakedModelSetPropertiesExtension) model.get()).setVariant(variantBase);
                state.addModelKey(variantBase);
            }
        }
    }

    private List<TintSource> exploreForTints(ItemStack itemStack, ItemModel model, ClientWorld world, HeldItemContext heldItemContext, int seed, ItemDisplayContext transformationMode) {
        switch (model) {
            case BasicItemModelAccessor accessor -> {
                return accessor.getTints();
            }
            case CompositeItemModelAccessor accessor -> {
                for (ItemModel itemModel : accessor.getItemModels()) {
                    List<TintSource> src = exploreForTints(itemStack, itemModel, world, heldItemContext, seed, transformationMode);
                    if (!src.isEmpty())
                        return src;
                }
            }
            case ConditionItemModelAccessor accessor -> {
                boolean property = accessor.getProperty().test(itemStack, world, heldItemContext != null ? heldItemContext.getEntity() : null, seed, transformationMode);
                if (property)
                    return exploreForTints(itemStack, accessor.getOnTrue(), world, heldItemContext, seed, transformationMode);
                else
                    return exploreForTints(itemStack, accessor.getOnFalse(), world, heldItemContext, seed, transformationMode);
            }
            case SelectItemModelAccessor accessor -> {
                ItemModel itemModel = accessor.getSelector().get(accessor.getProperty().getValue(itemStack, world, heldItemContext != null ? heldItemContext.getEntity() : null, seed, transformationMode), world);
                return exploreForTints(itemStack, itemModel, world, heldItemContext, seed, transformationMode);
            }
            case RangeDispatchItemModelAccessor accessor -> {
                ItemModel itemModel;
                float select = accessor.getProperty().getValue(itemStack, world, heldItemContext, seed) * accessor.getScale();
                if (Float.isNaN(select)) {
                    itemModel = accessor.getFallback();
                } else {
                    int i = RangeDispatchItemModelAccessor.getIndex(accessor.getThresholds(), select);
                    itemModel = i == -1 ? accessor.getFallback() : accessor.getModels()[i];
                }
                return exploreForTints(itemStack, itemModel, world, heldItemContext, seed, transformationMode);
            }
            case null, default -> {
                return List.of();
            }
        }
        return List.of();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(Block block, Optional<SpecialModelRenderer.Unbaked> specialModel, List<TintSource> tints, Optional<BlockState> blockState) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Registries.BLOCK.getCodec().fieldOf("block").forGetter(Unbaked::block),
                                SpecialModelTypes.CODEC.optionalFieldOf("special_model").forGetter(Unbaked::specialModel),
                                TintSourceTypes.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints),
                                BlockState.CODEC.optionalFieldOf("blockstate").forGetter(Unbaked::blockState)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public void resolve(Resolver resolver) {
        }

        @Override
        public ItemModel bake(BakeContext context) {
            Supplier<BlockStateModel> model = () -> MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(blockState.orElse(block.getDefaultState()));
            if (specialModel.isPresent()) {
                SpecialModelRenderer<?> specialModelRenderer = this.specialModel.get().bake(context);
                return getItemModelFunc().apply(model, specialModelRenderer, this.tints);
            }
            return getItemModelFunc().apply(model, null, this.tints);
        }

        @Override
        public MapCodec<Unbaked> getCodec() {
            return CODEC;
        }
    }

    @ExpectPlatform
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<TintSource>, ItemModel> getItemModelFunc(){
        throw new AssertionError();
    }
}
