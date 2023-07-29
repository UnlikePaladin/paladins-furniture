package com.unlikepaladin.pfm.mixin.fabric;

import com.nhoryzon.mc.farmersdelight.entity.block.CookingPotBlockEntity;
import com.nhoryzon.mc.farmersdelight.entity.block.HeatableBlockEntity;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import com.unlikepaladin.pfm.blocks.KitchenStovetopBlock;
import com.unlikepaladin.pfm.blocks.StoveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(CookingPotBlockEntity.class)
public abstract class PFMCookingPotBlockEntityMixin implements HeatableBlockEntity {
    @Override
    public boolean isHeated(World world, BlockPos pos) {
        Block checkBlock = world.getBlockState(pos.down()).getBlock();
        if (checkBlock instanceof StoveBlock || checkBlock instanceof KitchenStovetopBlock)
            return true;

        BlockState stateBelow = world.getBlockState(pos.down());
        if (stateBelow.isIn(TagsRegistry.HEAT_SOURCES)) {
            return stateBelow.contains(Properties.LIT) ? stateBelow.get(Properties.LIT) : true;
        } else {
            if (!this.requiresDirectHeat() && stateBelow.isIn(TagsRegistry.HEAT_CONDUCTORS)) {
                BlockState stateFurtherBelow = world.getBlockState(pos.down(2));
                if (stateFurtherBelow.isIn(TagsRegistry.HEAT_SOURCES)) {
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
