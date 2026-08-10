package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.neoforge.OvenBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.tags.BalmItemTags;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.ContainerUtils;
import net.blay09.mods.balm.world.SubContainer;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProcessorHolder;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class OvenBlockEntityBalm extends OvenBlockEntityImpl implements KitchenItemProcessor, BalmContainerProvider, KitchenItemProviderHolder, KitchenItemProcessorHolder {
    protected final KitchenItemProvider itemProvider;
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
    public boolean canProcess(RecipeType<?> recipeType) {
        return recipeType == RecipeType.SMELTING || recipeType == RecipeType.SMOKING || recipeType == RecipeType.CAMPFIRE_COOKING || recipeType == ModRecipes.ovenRecipes.type();
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
    public KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens) {
        for (IngredientToken ingredientToken : ingredientTokens) {
            ItemStack itemStack = ingredientToken.consume();
            ItemStack restStack = ContainerUtils.insertItemStacked(getInputContainer(), itemStack, false);
            if (!restStack.isEmpty()) {
                ingredientToken.restore(restStack);
            }
        }
        return OvenOperation.INSTANCE;
    }

    @Override
    public KitchenItemProcessor getKitchenItemProcessor() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    private static class OvenOperation implements KitchenOperation {
        public static final KitchenOperation INSTANCE = new OvenOperation();

        public Optional<Component> getFeedback() {
            return Optional.of(Component.translatable("gui.cookingforblockheads.moved_to_oven").withStyle(ChatFormatting.YELLOW));
        }
    }
}