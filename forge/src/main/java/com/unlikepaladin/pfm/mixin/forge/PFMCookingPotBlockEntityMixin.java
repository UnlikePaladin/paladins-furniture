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
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

@Pseudo
@Mixin(CookingPotBlockEntity.class)
public abstract class PFMCookingPotBlockEntityMixin implements HeatableBlockEntity {
    @Override
    public boolean isHeated(World world, BlockPos pos) {
        Block checkBlock = world.getBlockState(pos.down()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            return true;

        BlockState stateBelow = world.getBlockState(pos.down());
        if (stateBelow.isIn(ModTags.HEAT_SOURCES)) {
            return stateBelow.contains(Properties.LIT) ? stateBelow.get(Properties.LIT) : true;
        } else {
            if (!this.requiresDirectHeat() && stateBelow.isIn(ModTags.HEAT_CONDUCTORS)) {
                BlockState stateFurtherBelow = world.getBlockState(pos.down(2));
                if (stateFurtherBelow.isIn(ModTags.HEAT_SOURCES)) {
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
