package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.OvenBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.api.tag.BalmItemTags;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class OvenBlockEntityBalm extends OvenBlockEntityImpl implements KitchenItemProcessor, BalmContainerProvider, BalmProviderHolder, BlockEntityContract {
    private final KitchenItemProvider itemProvider;
    private final Map<Class<?>, BalmProvider<?>> providers = new HashMap<>();
    private final Map<Pair<Direction, Class<?>>, BalmProvider<?>> sidedProviders = new HashMap<>();
    private boolean providersInitialized;
    private final Container inputContainer;

    public OvenBlockEntityBalm(BlockPos pos, BlockState state) {
        super(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(new SubContainer(this, 12, 15));
        this.inputContainer = new SubContainer(this, 0, 3);
    }

    public OvenBlockEntityBalm(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(new SubContainer(this, 12, 15));
        this.inputContainer = new SubContainer(this, 0, 3);
    }

    public Container getInputContainer() {
        return inputContainer;
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(KitchenItemProvider.class, this.itemProvider), new BalmProvider<>(KitchenItemProcessor.class, this));
    }

    @Override
    public boolean canProcess(RecipeType<?> recipeType) {
        return recipeType == RecipeType.SMELTING;
    }

    @Override
    public KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens) {
        for(IngredientToken ingredientToken : ingredientTokens) {
            ItemStack itemStack = ingredientToken.consume();
            ItemStack restStack = ContainerUtils.insertItemStacked(getInputContainer(), itemStack, false);
            if (!restStack.isEmpty()) {
                ingredientToken.restore(restStack);
            }
        }
        return KitchenOperation.EMPTY;
    }

    @Override
    public int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil && itemStack.is(BalmItemTags.COOKING_OIL) ? 800 : level.fuelValues().burnDuration(itemStack);
        }
    }

    @Override
    public <T> T getProvider(Class<T> clazz) {
        if (!this.providersInitialized) {
            List<BalmProviderHolder> providers = new ArrayList<>();
            this.buildProviders(providers);

            for(BalmProviderHolder providerHolder : providers) {
                for(BalmProvider<?> provider : providerHolder.getProviders()) {
                    this.providers.put(provider.getProviderClass(), provider);
                }

                for(Pair<Direction, BalmProvider<?>> pair : providerHolder.getSidedProviders()) {
                    Direction direction = pair.getFirst();
                    BalmProvider<?> provider = pair.getSecond();
                    this.sidedProviders.put(Pair.of(direction, provider.getProviderClass()), provider);
                }
            }

            this.providersInitialized = true;
        }

        BalmProvider<?> found = this.providers.get(clazz);
        return (T)(found != null ? found.getInstance() : null);
    }
}