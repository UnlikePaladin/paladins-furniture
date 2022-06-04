package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.StoveScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class StoveBlockEntity extends AbstractFurnaceBlockEntity {
    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(PaladinFurnitureMod.STOVE_BLOCK_ENTITY, pos, state, RecipeType.SMOKING);
    }
     String blockname = this.getCachedState().getBlock().getTranslationKey();

    @Override
    protected Text getContainerName() {
        blockname = blockname.replace("block.pfm", "");
        return new TranslatableText("container.pfm" + blockname);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new StoveScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

}
