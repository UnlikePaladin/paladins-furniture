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
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PFMItemModel<T> implements ItemModel {
    private final SpecialModelRenderer<T> specialModelType;
    protected final Supplier<BlockStateModel> model;
    private final List<ItemTintSource> tints;
    private final List<ItemTintSource> pfm$parentTints = new ArrayList<>();

    public PFMItemModel(Supplier<BlockStateModel> model, SpecialModelRenderer<T> specialModelType, List<ItemTintSource> tints) {
        this.model = model;
        this.tints = tints;
        this.specialModelType = specialModelType;
    }

    public BlockStateModel unwrapBlockStateModel(BlockStateModel model) {
        return model;
    }

    @Override
    public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel world, @Nullable ItemOwner heldItemContext, int seed) {
        state.appendModelIdentityElement(this);
        BlockStateModel unwrapped = unwrapBlockStateModel(model.get());
        if (specialModelType != null) {
            ItemStackRenderState.LayerRenderState specialLayerRenderState = state.newLayer();
            if (unwrapped instanceof AbstractBakedModel) {
                ((AbstractBakedModel)unwrapped).itemDisplaySettings.applyToLayer(specialLayerRenderState, displayContext);
            }
            specialLayerRenderState.setupSpecialModel(this.specialModelType, this.specialModelType.extractArgument(stack));
            state.appendModelIdentityElement(specialLayerRenderState);
        }

        state.setAnimated();
        ItemStackRenderState.LayerRenderState layerRenderState = state.newLayer();
        if (stack.hasFoil()) {
            layerRenderState.setFoilType(ItemStackRenderState.FoilType.STANDARD);
        }

        RenderType renderLayer = ItemBlockRenderTypes.getRenderType(stack);
        layerRenderState.setRenderType(renderLayer);

        if (ColorRegistry.itemColorProviders.containsKey(stack.getItem()) && pfm$parentTints.isEmpty()) {
            Item item = ColorRegistry.itemColorProviders.get(stack.getItem()).asItem();

            ResourceLocation parentModelId = item.getDefaultInstance().get(DataComponents.ITEM_MODEL);

            ItemModel parentModel = Minecraft.getInstance().getModelManager().getItemModel(parentModelId);
            pfm$parentTints.addAll(exploreForTints(stack, parentModel, world, heldItemContext, seed, displayContext));
        }

        List<ItemTintSource> tintsToUse = pfm$parentTints.isEmpty() ? this.tints : pfm$parentTints;

        int tintCount = tintsToUse.size();
        if (tintCount == 0 && stack.has(PFMComponents.COLOR_COMPONENT)) {
            tintCount = 2;
            tintsToUse = new ArrayList<>(tintCount);

            tintsToUse.add(new Constant(0xffffffff));
            tintsToUse.add(new Constant(0xffffffff));
        }

        int[] tintArray = layerRenderState.prepareTintLayers(tintCount);

        for (int index = 0; index < tintCount; index++) {
            if (index == 1 && stack.has(PFMComponents.COLOR_COMPONENT)) {
                tintArray[index] =  PFMFileUtil.adjustColor(stack.getOrDefault(PFMComponents.COLOR_COMPONENT, DyeColor.WHITE).getMapColor().col);
            } else {
                tintArray[index] = PFMFileUtil.adjustColor(tintsToUse.get(index).calculate(stack, world, heldItemContext != null ? heldItemContext.asLivingEntity() : null));
            }
            state.appendModelIdentityElement(tintArray[index]);
        }

        setProperties(stack, state);

        RandomSource random = RandomSource.create(seed);
        // finally emit item quads
        emitItemModelQuads(layerRenderState, unwrapped, displayContext, random);
    }

    @ExpectPlatform
    public static void emitItemModelQuads(ItemStackRenderState.LayerRenderState renderState, BlockStateModel model, ItemDisplayContext displayContext, RandomSource random) {
        throw new AssertionError();
    }

    protected void setProperties(ItemStack stack, ItemStackRenderState state) {
        BlockStateModel blockStateModel  = unwrapBlockStateModel(model.get());
        if (stack.getItem() instanceof BlockItem && blockStateModel instanceof PFMBakedModelSetPropertiesExtension) {
            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            ((PFMBakedModelSetPropertiesExtension) blockStateModel).setBlockStateProperty(blockState);
            state.appendModelIdentityElement(blockState);
            if (stack.has(PFMComponents.VARIANT_COMPONENT)) {
                VariantBase<?> variantBase = VariantHelper.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT));
                ((PFMBakedModelSetPropertiesExtension) blockStateModel).setVariant(variantBase);
                state.appendModelIdentityElement(variantBase);
            }
        }
    }

    private List<ItemTintSource> exploreForTints(ItemStack itemStack, ItemModel model, ClientLevel world, ItemOwner heldItemContext, int seed, ItemDisplayContext transformationMode) {
        switch (model) {
            case BasicItemModelAccessor accessor -> {
                return accessor.getTints();
            }
            case CompositeItemModelAccessor accessor -> {
                for (ItemModel itemModel : accessor.getItemModels()) {
                    List<ItemTintSource> src = exploreForTints(itemStack, itemModel, world, heldItemContext, seed, transformationMode);
                    if (!src.isEmpty())
                        return src;
                }
            }
            case ConditionItemModelAccessor accessor -> {
                boolean property = accessor.getProperty().get(itemStack, world, heldItemContext != null ? heldItemContext.asLivingEntity() : null, seed, transformationMode);
                if (property)
                    return exploreForTints(itemStack, accessor.getOnTrue(), world, heldItemContext, seed, transformationMode);
                else
                    return exploreForTints(itemStack, accessor.getOnFalse(), world, heldItemContext, seed, transformationMode);
            }
            case SelectItemModelAccessor accessor -> {
                ItemModel itemModel = accessor.getModels().get(accessor.getProperty().get(itemStack, world, heldItemContext != null ? heldItemContext.asLivingEntity() : null, seed, transformationMode), world);
                return exploreForTints(itemStack, itemModel, world, heldItemContext, seed, transformationMode);
            }
            case RangeDispatchItemModelAccessor accessor -> {
                ItemModel itemModel;
                float select = accessor.getProperty().get(itemStack, world, heldItemContext, seed) * accessor.getScale();
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
    public record Unbaked(Block block, Optional<SpecialModelRenderer.Unbaked> specialModel, List<ItemTintSource> tints, Optional<BlockState> blockState) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Unbaked::block),
                                SpecialModelRenderers.CODEC.optionalFieldOf("special_model").forGetter(Unbaked::specialModel),
                                ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints),
                                BlockState.CODEC.optionalFieldOf("blockstate").forGetter(Unbaked::blockState)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public void resolveDependencies(Resolver resolver) {
        }

        @Override
        public ItemModel bake(BakingContext context) {
            Supplier<BlockStateModel> model = () -> Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(blockState.orElse(block.defaultBlockState()));
            if (specialModel.isPresent()) {
                SpecialModelRenderer<?> specialModelRenderer = this.specialModel.get().bake(context);
                return getItemModelFunc().apply(model, specialModelRenderer, this.tints);
            }
            return getItemModelFunc().apply(model, null, this.tints);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }

    @ExpectPlatform
    public static TriFunc<Supplier<BlockStateModel>, SpecialModelRenderer<?>, List<ItemTintSource>, ItemModel> getItemModelFunc(){
        throw new AssertionError();
    }
}
