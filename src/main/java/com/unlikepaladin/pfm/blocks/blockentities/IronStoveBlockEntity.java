package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.menus.IronStoveScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class IronStoveBlockEntity extends AbstractFurnaceBlockEntity {
    public IronStoveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.IRON_STOVE_BLOCK_ENTITY, pos, state, RecipeType.SMOKING);
    }
     String blockname = this.getCachedState().getBlock().getTranslationKey();
    @Override
    protected Text getContainerName() {
        blockname = blockname.replace("block.pfm", "");
        return new TranslatableText("container.pfm" + blockname);
    }


    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new IronStoveScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

}
