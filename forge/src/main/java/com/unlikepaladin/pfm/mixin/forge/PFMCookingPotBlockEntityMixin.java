package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import vectorwing.farmersdelight.tile.CookingPotTileEntity;
import vectorwing.farmersdelight.tile.IHeatableTileEntity;
import vectorwing.farmersdelight.utils.tags.ModTags;

@Pseudo
@Mixin(CookingPotTileEntity.class)
public abstract class PFMCookingPotBlockEntityMixin implements IHeatableTileEntity {
    @Override
    public boolean isHeated(World world, BlockPos pos) {
        Block checkBlock = world.getBlockState(pos.down()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            return true;

        BlockState stateBelow = world.getBlockState(pos.down());
        if (ModTags.HEAT_SOURCES.contains(stateBelow.getBlock())) {
            return stateBelow.contains(Properties.LIT) ? stateBelow.get(Properties.LIT) : true;
        } else {
            if (!this.requiresDirectHeat() && ModTags.HEAT_CONDUCTORS.contains(stateBelow.getBlock())) {
                BlockState stateFurtherBelow = world.getBlockState(pos.down(2));
                if (ModTags.HEAT_SOURCES.contains(stateFurtherBelow.getBlock())) {
                    if (stateFurtherBelow.contains(Properties.LIT)) {
                        return stateFurtherBelow.get(Properties.LIT);
                    }
                    return true;
                }
            }

            return false;
        }
    }
}
