package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProcessorHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class StovetopBlockEntityBalm extends StovetopBlockEntityImpl implements Container, KitchenItemProcessor, BalmContainerProvider, KitchenItemProcessorHolder {

    public StovetopBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public boolean canProcess(RecipeType<?> recipeType) {
        return recipeType == RecipeType.SMELTING;
    }

    public ItemStack tryTakeItem(ItemStack itemStack) {
        int firstEmptyProcessing = -1;
        int processingStart = 0;
        for (int i = processingStart; i < this.itemsBeingCooked.size(); i++) {
            if (getItem(i).isEmpty()) {
                firstEmptyProcessing = i;
                break;
            }
        }

        if (firstEmptyProcessing != -1) {
            if (!itemStack.isEmpty()) {
                ItemStack moved = itemStack.split(1);
                setItem(firstEmptyProcessing, moved);
                return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
            }
        }

        return itemStack;
    }

    @Override
    public KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens) {
        for(IngredientToken ingredientToken : ingredientTokens) {
            ItemStack itemStack = ingredientToken.consume();
            ItemStack restStack = tryTakeItem(itemStack);
            if (!restStack.isEmpty()) {
                ingredientToken.restore(restStack);
            }
        }
        return StovetopOperation.INSTANCE;
    }

    @Override
    public KitchenItemProcessor getKitchenItemProcessor() {
        return this;
    }

    private static class StovetopOperation implements KitchenOperation {
        public static final KitchenOperation INSTANCE = new StovetopOperation();

        public Optional<Component> getFeedback() {
            return Optional.of(Component.translatable("pfm.moved_to_stovetop").withStyle(ChatFormatting.YELLOW));
        }
    }
}