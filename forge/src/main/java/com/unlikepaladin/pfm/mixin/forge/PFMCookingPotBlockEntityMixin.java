package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

@Pseudo
@Mixin(CookingPotBlockEntity.class)
public abstract class PFMCookingPotBlockEntityMixin implements HeatableBlockEntity {
    @Override
    public boolean isHeated(Level world, BlockPos pos) {
        Block checkBlock = world.getBlockState(pos.below()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            return true;

        BlockState stateBelow = world.getBlockState(pos.below());
        if (ModTags.HEAT_SOURCES.contains(stateBelow.getBlock())) {
            return stateBelow.hasProperty(BlockStateProperties.LIT) ? stateBelow.getValue(BlockStateProperties.LIT) : true;
        } else {
            if (!this.requiresDirectHeat() && ModTags.HEAT_CONDUCTORS.contains(stateBelow.getBlock())) {
                BlockState stateFurtherBelow = world.getBlockState(pos.below(2));
                if (ModTags.HEAT_SOURCES.contains(stateFurtherBelow.getBlock())) {
                    if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT)) {
                        return stateFurtherBelow.getValue(BlockStateProperties.LIT);
                    }
                    return true;
                }
            }

            return false;
        }
    }
}
