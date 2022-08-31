package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.menus.IronStoveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CounterOvenBlockEntity extends AbstractFurnaceBlockEntity {
    public CounterOvenBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, pos, state, RecipeType.SMOKING);
    }
     String blockname = this.getCachedState().getBlock().getTranslationKey();
    @Override
    protected Text getContainerName() {
        return Text.translatable("container.pfm.kitchen_counter_oven");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new IronStoveScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

}
